package org.flatlang.es6.nodewriters;

import org.flatlang.tree.AssignmentMethod;
import org.flatlang.tree.ClassDeclaration;
import org.flatlang.tree.Constructor;

public abstract class ConstructorWriter extends BodyMethodDeclarationWriter
{
	public abstract Constructor node();
	
	@Override
	public StringBuilder writeBody(StringBuilder builder)
	{
		builder.append("{\n");
		
		ClassDeclaration extended = node().getDeclaringClass().getExtendedClassDeclaration();

		builder.append("let __value = new ");
		getWriter(node().getDeclaringClass()).writeName(builder).append("();\n");

		if (extended != null)
		{
			AssignmentMethod assignmentMethod = extended.getAssignmentMethodNode();
			getWriter(assignmentMethod.getDeclaringClass()).writeName(builder).append('.');
			getWriter(assignmentMethod).writeName(builder).append(".apply(__value, [].slice.call(arguments));\n");
		}

//		builder.append("this.__proto__ = ").append(getWriter(node().getDeclaringClass()).writeName()).append(".prototype;\n\n");

		AssignmentMethod assignmentMethod = node().getParentClass().getAssignmentMethodNode();

		getWriter(assignmentMethod.getDeclaringClass()).writeName(builder).append('.');
		getWriter(assignmentMethod).writeName(builder).append(".apply(__value, [].slice.call(arguments));\n");
		
		getWriter(node().getScope()).write(builder, false, true);

		builder.append("return __value;\n");
		
		return builder.append('}');
	}

	@Override
	public StringBuilder write(StringBuilder builder)
	{
//		writeAssignedVariable(builder).append(" = function ");
//		builder.append("construct");
//		writeOverload(builder);

		builder.append("static ");
		writeName(builder);

		getWriter(node().getParameterList()).write(builder).append(" ");

		writeBody(builder);

		return builder.append("\n");
	}

//	@Override
//	public StringBuilder writePrototypeAccess(StringBuilder builder) {
//		return builder;
//	}
//
//	@Override
//	public StringBuilder writeAssignedVariable(StringBuilder builder)
//	{
//		return writeName(builder);//builder.append("flatConstructors.new").append(writeName());
//	}
	
	public StringBuilder writeConstructorListName()
	{
		return writeConstructorListName(new StringBuilder());
	}
	
	public StringBuilder writeConstructorListName(StringBuilder builder)
	{
		java.util.List<ClassDeclaration> dupes = getWriter(node().getParentClass()).getClassesWithSameName();

		builder.append("new");

		if (dupes.size() > 0) {
			return builder.append(node().getParentClass().getClassLocation().replaceAll("[^\\w\\d_]", "_"));
		} else {
			return super.writeName(builder);
		}
	}
	
	@Override
	public StringBuilder writeName(StringBuilder builder)
	{
		builder.append("construct");
		return writeOverload(builder);
//		return builder.append("flatConstructors.").append(writeConstructorListName());
	}
}