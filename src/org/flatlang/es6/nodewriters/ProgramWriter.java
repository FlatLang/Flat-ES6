package org.flatlang.es6.nodewriters;

import org.flatlang.error.SyntaxMessage;
import org.flatlang.tree.*;
import org.flatlang.util.Location;
import org.flatlang.es6.engines.ES6CompileEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static org.flatlang.Flat.LIBRARY;

public abstract class ProgramWriter extends TypeListWriter
{
	public abstract Program node();
	
	public ClassDeclaration[] getClassesHierarchicalInOrder()
	{
		final ArrayList<ClassDeclaration> list = new ArrayList<>();
		
		node().forEachVisibleListChild(file -> {
			Arrays.stream(file.getClassDeclarations()).forEach(c -> {
				ClassDeclaration[] hierarchy = c.getClassHierarchy(true, true);
				
				for (int i = hierarchy.length - 1; i >= 0; i--)
				{
					if (!list.contains(hierarchy[i]))
					{
						list.add(hierarchy[i]);
					}
				}
			});
		});
		
		return list.toArray(new ClassDeclaration[0]);
	}
	
	@Override
	public StringBuilder write(final StringBuilder builder)
	{
		boolean localScope = ((ES6CompileEngine)node().getProgram().getController().compileEngine).localScope;
		
		if (localScope)
		{
			builder.append("(function () {\n");
		}
		
		ClassDeclaration[] classes = getClassesHierarchicalInOrder();

		builder
			.append("function __copyStaticFunctions() {\n")
			.append(  "[].slice.call(arguments).forEach(f =>\n")
			.append(  "Object.getOwnPropertyNames(f)\n")
			.append(  "  .filter(d => !f.prototype.hasOwnProperty(d))\n")
			.append(  "  .forEach(d => f.prototype[d] = f[d])\n")
			.append(  ");\n")
			.append("}\n\n");

		builder
			.append("function __copyPrototype(f, ...g) {\n")
			.append(  "let v = f.prototype;\n")
			.append(  "g.map(a => a.prototype)\n")
			.append(  "  .forEach(p => Object.getOwnPropertyNames(p)\n")
			.append(  "  .filter(d => !v.hasOwnProperty(d))\n")
			.append(  "  .forEach(d => v[d] = p[d])\n")
			.append(  ");\n")
			.append(  "return __copyPrototype;\n")
			.append("}\n\n");

		builder.append("var flat_null = undefined;\n\n");

		HashSet<String> printedClasses = new HashSet<>();

		int classIndex = 0;
		for (ClassDeclaration child : classes)
		{
			if (classIndex++ > 0) {
				builder.append("\n");
			}
			printClass(builder, printedClasses, child);
		}

		builder.append("\n");
		builder.append("__copyStaticFunctions(");
		classIndex = 0;
		for (ClassDeclaration child : classes) {
			if (classIndex++ > 0) {
				builder.append(", ");
			}
			builder.append(getWriter(child).writeName());
		}
		builder.append(");\n\n");
		builder.append("__copyPrototype");
		for (ClassDeclaration child : classes) {
			if (child.getImplementedInterfaces().length > 0) {
				builder.append("\n  (").append(getWriter(child).writeName());
				Arrays.stream(child.getImplementedInterfaces()).forEach(i -> {
					builder.append(", ").append(getWriter(i).writeName());
				});
				builder.append(")");
			}
		}
		builder.append(";\n\n");

		for (ClassDeclaration child : classes)
		{
			getWriter(child).writeEncapsulationAssignments(builder);
		}

		for (ClassDeclaration child : classes)
		{
			getWriter(child).writeStaticBlocks(builder);
		}

		Node parent = Arrays.stream(node().getChildrenOfType(FileDeclaration.class))
			.map(f -> (FileDeclaration)f)
			.map(FileDeclaration::getClassDeclaration)
			.filter(Objects::nonNull)
			.flatMap(c -> Arrays.stream(c.getMethods()))
			.filter(Node::isUserMade)
			.findFirst()
			.get();

		Value flatNullString = Instantiation.decodeStatement(parent, "new Null()", Location.INVALID, true);

		builder.append("\n");
		builder.append("flat_null = ").append(getWriter(flatNullString).writeUseExpression()).append(";\n\n");

		for (ClassDeclaration child : classes)
		{
			getWriter(child).writeStaticBlockCalls(builder);
		}

		Value emptyArgsArray = Instantiation.decodeStatement(parent, "new Array<String>()", Location.INVALID, true);
		Accessible argvArray = SyntaxTree.decodeIdentifierAccess(parent, "Array.jsStringArrayToFlatArray(null)", Location.INVALID, true);

		MethodCall jsStringArrayToFlatArrayCall = (MethodCall)argvArray.getAccessedNode();

		if (!node().getController().isFlagEnabled(LIBRARY)) {Value nullArg = jsStringArrayToFlatArrayCall.getArgumentList().getArgumentsInOrder()[0];
			Literal processArgv = new Literal(nullArg.getParent(), Location.INVALID);
			processArgv.value = "process.argv.slice(1)";

			nullArg.replaceWith(processArgv);

			builder.append("\n");
			builder.append("var flat_main_args = typeof process !== 'undefined' && process.argv ?\n  ");
			getWriter(argvArray.toValue()).writeExpression(builder);
			builder.append(" :\n  ");
			getWriter(emptyArgsArray).writeExpression(builder);
			builder.append(";\n\n");

			builder
				.append("if (typeof process !== 'undefined' && typeof process.on === 'function') {\n")
				.append(  "process.on('unhandledRejection', (reason, promise) => {\n")
				.append(    "console.error(reason);\n")
				.append(    "process.exit(1);\n")
				.append(  "});\n")
				.append("}\n\n");

			FlatMethodDeclaration method = node().getTree().getMainMethod(node().getController().codeGeneratorEngine.mainClass);

			if (method == null) {
				SyntaxMessage.error("Could not find main function", node().getController());

				return builder;
			}

			getWriter(method.getDeclaringClass()).writeName(builder).append('.').append(getWriter(method).writeName()).append("(flat_main_args);\n");
		}

		if (localScope)
		{
			builder.append("})();");
		}
		
		return builder;
	}

	private void printPrototypes(StringBuilder builder, HashSet<String> printedClasses, ClassDeclaration c) {
		if (!printedClasses.contains(c.getClassLocation())) {
			printedClasses.add(c.getClassLocation());

			ClassDeclaration extension = c.getExtendedClassDeclaration();

			if (extension != null) {
				printPrototypes(builder, printedClasses, extension);
			}

			getWriter(c).writeExtensionPrototypeAssignments(builder);

			builder.append("\n\n");
		}
	}

	private void printClass(StringBuilder builder, HashSet<String> printedClasses, ClassDeclaration c) {
		if (!printedClasses.contains(c.getClassLocation())) {
			printedClasses.add(c.getClassLocation());

			ClassDeclaration extension = c.getExtendedClassDeclaration();

			if (extension != null) {
				printClass(builder, printedClasses, extension);
			}

			getWriter(c).write(builder);
		}
	}
}