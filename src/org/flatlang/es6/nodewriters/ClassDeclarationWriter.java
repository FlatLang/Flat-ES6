package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class ClassDeclarationWriter extends InstanceDeclarationWriter
{
	public abstract ClassDeclaration node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		builder.append("class ").append(writeName()).append(" {\n");
		
		getWriter(node().getFieldList().getPrivateFieldList()).write(builder);
		getWriter(node().getFieldList().getPublicFieldList()).write(builder);
		
		builder.append('\n');
		
		node().forEachVisibleChild(child -> {
			builder.append('\n').append(getWriter(child).write());
		});
		

//		if (node().doesExtendClass()) {
//			writeName(builder).append(".prototype = Object.create(").append(getWriter(node().getExtendedClassDeclaration()).writeName()).append(".prototype);\n");
//		}

//		writeName(builder).append(".prototype.constructor = ").append(writeName()).append(";\n\n");
		builder.append("\n");
		
		getWriter(node().getDestructorList()).write(builder);
		getWriter(node().getMethodList()).write(builder);
		getWriter(node().getPropertyMethodList()).write(builder);
		getWriter(node().getHiddenMethodList()).write(builder);
		getWriter(node().getConstructorList()).write(builder);

		if (node().encapsulatingClass != null) {
			writeUseExpression(builder).append(" = ");
			writeName(builder).append(";\n\n");
		}

		builder.append("\n}\n\n");

		return builder;
	}

	public StringBuilder writeUseExpression(StringBuilder builder) {
		ClassDeclaration encapsulating = node().encapsulatingClass;
		Stack<ClassDeclaration> classes = new Stack<>();

		while (encapsulating != null) {
			classes.push(encapsulating);

			encapsulating = encapsulating.encapsulatingClass;
		}

		while (!classes.isEmpty()) {
			getWriter(classes.pop()).writeName(builder).append('.');
		}

		return writeName(builder);
	}
	
	public StringBuilder writeExtensionPrototypeAssignments(StringBuilder builder) {
		if (node().doesExtendClass())
		{
//			node().getExtendedClassDeclaration().getMethodList().forEachFlatMethod(method -> {
//				getWriter(method).writePrototypeAssignment(builder, node(), true);
//			});
			
//			builder.append("\n");
		}
		
//		node().getInterfacesImplementationList().forEachVisibleListChild(i -> {
//			Arrays.stream(i.getTypeClass().getMethods()).forEach(interfaceMethod -> {
//				if (Arrays.stream(interfaceMethod.getOverridingMethods()).noneMatch(m -> m.getDeclaringClass() == node())) {
//					getWriter(interfaceMethod).writePrototypeAssignment(builder, node(), true);
//					if (Arrays.stream(node().getMethodList().getMethods()).noneMatch(a -> Objects.equals(interfaceMethod.getName(), a.getName()))) {
//						getWriter(interfaceMethod).writePrototypeAssignment(builder, node(), false);
//					}
//				}
//			});
//		});

		return builder;
	}
	public StringBuilder writeStaticBlocks(final StringBuilder builder)
	{
		if (node().getStaticBlockList().getNumVisibleChildren() > 0)
		{
			node().getStaticBlockList().forEachVisibleChild(block -> {
				if (block.getScope().getNumVisibleChildren() > 0)
				{
					builder.append('\n');
				}
				
				getWriter(block).write(builder);
			});
		}
		
		return builder;
	}

	public StringBuilder writeStaticBlockCalls(final StringBuilder builder)
	{
		if (node().getStaticBlockList().getNumVisibleChildren() > 0)
		{
			node().getStaticBlockList().forEachVisibleChild(block -> {
				if (block.getScope().getNumVisibleChildren() > 0)
				{
					StaticBlockWriter.generateMethodName(builder, node(), block.getIndex()).append("();\n");
				}
			});
		}

		return builder;
	}

	public List<ClassDeclaration> getClassesWithSameName() {
		return node().getProgram()
			.getVisibleListChildren()
			.stream()
			.filter(f -> f != node().getFileDeclaration())
			.map(f -> f.getClassDeclaration(node().getName()))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
	
	@Override
	public StringBuilder writeName(StringBuilder builder)
	{
		for (String c : new String[] { "flatlang/Object", "flatlang/String", "flatlang/io/Console", "flatlang/datastruct/list/Array",
			"flatlang/time/Date", "flatlang/Math", "flatlang/datastruct/Node", "flatlang/primitive/number/Int", "flatlang/primitive/number/Double",
			"flatlang/primitive/number/Byte", "flatlang/primitive/number/Short", "flatlang/primitive/number/Long", "flatlang/primitive/number/Float",
			"flatlang/time/DateTime", "flatlang/async/Promise" })
		{
			if (node().getClassLocation().equals(c))
			{
				return builder.append("Flat").append(node().getName());
			}
		}

		List<ClassDeclaration> dupes = getClassesWithSameName();

		if (dupes.size() > 0) {
			return builder.append(node().getClassLocation().replaceAll("[^\\w\\d_]", "_"));
		}

		return super.writeName(builder);
	}
}