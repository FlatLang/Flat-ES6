package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class BreakWriter extends NodeWriter
{
	public abstract Break node();
	
	@Override
	public StringBuilder writeExpression(StringBuilder builder)
	{
		return builder.append("break");
	}
}