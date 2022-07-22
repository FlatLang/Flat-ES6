package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class ElseStatementWriter extends ControlStatementWriter
{
	public abstract ElseStatement node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		builder.append("else ");
		
		if (node().getNumChildren() == 2)
		{
			Node child = node().getChild(1);
			
			if (child instanceof IfStatement)
			{
				getWriter(child).writeExpression(builder).append(' ');

				return getWriter(child.getScope()).write(builder);
			}
		}
		
		return getWriter(node().getScope()).write(builder);
	}
}