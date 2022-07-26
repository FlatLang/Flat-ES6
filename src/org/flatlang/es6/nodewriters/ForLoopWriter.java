package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class ForLoopWriter extends LoopWriter
{
	public abstract ForLoop node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		Assignment initialization = node().getLoopInitialization();
		Node       condition      = node().getCondition();
		Node       update         = node().getLoopUpdate();

		if (!node().elementDeclaration.isPropertyTrue("fromAssignment")) {
			getWriter(node().elementDeclaration).write(builder);
		}

		if (initialization != null)
		{
			getWriter(initialization).write(builder);//.append('\n');
		}
		
		builder.append("for (; ");
		
		if (condition != null)
		{
			getWriter(condition).writeExpression(builder);
		}
		
		builder.append("; ");
		
		if (update != null)
		{
			getWriter(update).writeExpression(builder);
		}
		
		builder.append(") ");
		
		node().forEachChild(child -> {
			if (child != node().getArgumentList())
			{
				getWriter(child).write(builder);
			}
		});
		
		return builder;
	}
}