package org.flat.es6.nodewriters;

import org.flat.tree.match.Fallthrough;
import org.flat.tree.variables.Variable;

public abstract class FallthroughWriter extends MatchChildWriter
{
	public abstract Fallthrough node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		Variable fall = node().getParentMatch().getLocalFallthrough();
		
		if (fall != null)
		{
			getWriter(fall).writeExpression(builder).append(" = 1;\n");
		}
		
		return builder;
	}
}