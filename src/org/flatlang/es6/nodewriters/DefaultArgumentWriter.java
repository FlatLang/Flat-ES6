package org.flatlang.es6.nodewriters;

import org.flatlang.tree.DefaultArgument;

public abstract class DefaultArgumentWriter extends IValueWriter
{
	public abstract DefaultArgument node();
	
	@Override
	public StringBuilder writeExpression(StringBuilder builder)
	{
		return builder.append("undefined");
	}
}