package org.flatlang.es6.nodewriters;

import org.flatlang.tree.variables.FieldDeclaration;

public abstract class FieldDeclarationWriter extends InstanceDeclarationWriter
{
	public abstract FieldDeclaration node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
//		if (node().isStatic()) {
//			getWriter(node().getDeclaringClass()).writeUseExpression(builder);
//		} else {
//			builder.append("this");
//		}
//
//		builder.append(".");

		if (node().isStatic()) {
			builder.append("static ");
		}

		return super.write(builder);
	}
}