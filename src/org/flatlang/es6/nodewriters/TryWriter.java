package org.flatlang.es6.nodewriters;

import org.flatlang.tree.exceptionhandling.ExceptionHandler;
import org.flatlang.tree.exceptionhandling.Try;

public abstract class TryWriter extends ExceptionHandlerWriter
{
	public abstract Try node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		builder.append("try ").append(getWriter(node().getScope()).write(true, false));
		
		if (node().getNextNode() instanceof ExceptionHandler)
		{
			builder.append(' ');
		}
		else
		{
			builder.append('\n');
		}
		
		return builder;
	}
}