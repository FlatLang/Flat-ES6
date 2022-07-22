package org.flatlang.es6.nodewriters;

import org.flatlang.tree.Scope;
import org.flatlang.tree.Value;
import org.flatlang.tree.match.Match;

public abstract class MatchWriter extends ControlStatementWriter
{
	public abstract Match node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		Scope scope = node().getScope();
		
		if (node().isConventionalSwitch())
		{
			Value control = node().getControlValue();
			
			builder.append("switch (").append(getWriter(control).writeExpression()).append(") ");
			
			getWriter(scope).write(builder);
		}
		else
		{
			boolean requiresFacade = node().requiresLoopFacade();
			
			if (requiresFacade)
			{
				builder.append("do {\n");
			}
			
			getWriter(scope).write(builder, false);
			
			if (requiresFacade)
			{
				builder.append("} while (false);\n");
			}
		}
		
		return builder;
	}
}