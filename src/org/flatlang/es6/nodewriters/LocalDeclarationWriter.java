package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class LocalDeclarationWriter extends VariableDeclarationWriter
{
	public abstract LocalDeclaration node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		builder.append("var ");
		
		writeName(builder);
		
		return builder.append(" = null;\n");
	}
}