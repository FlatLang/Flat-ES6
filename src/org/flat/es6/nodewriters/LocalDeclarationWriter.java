package org.flat.es6.nodewriters;

import org.flat.tree.*;

public abstract class LocalDeclarationWriter extends VariableDeclarationWriter
{
	public abstract LocalDeclaration node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		builder.append("let ");
		
		return writeExpression(builder).append(";\n");
	}
}