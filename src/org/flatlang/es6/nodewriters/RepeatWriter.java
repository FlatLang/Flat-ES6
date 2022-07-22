package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class RepeatWriter extends LoopWriter
{
	public abstract Repeat node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		if (node().getValueNode() != null)
		{
			builder.append("for (").append(node().getName()).append(" = 0; ").append(node().getName()).append(" < ").append(getWriter(node().getValueNode()).writeExpression()).append("; ").append(node().getName()).append("++) ");
		}
		else
		{
			builder.append("for (;;) ");
		}
		
		return getWriter(node().getScope()).write(builder);
	}
}