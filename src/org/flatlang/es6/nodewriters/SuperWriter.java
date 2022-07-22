package org.flatlang.es6.nodewriters;

import org.flatlang.tree.MethodCall;
import org.flatlang.tree.variables.Super;

public abstract class SuperWriter extends PriorityWriter
{
	public abstract Super node();
	
	@Override
	public StringBuilder writeExpression(StringBuilder builder)
	{
		builder.append("this.");
		
		if (node().getAccessedNode() instanceof MethodCall)
		{
			builder.append("super_");
		}
		
		return builder.append(writeAccessedExpression(false));
	}
}