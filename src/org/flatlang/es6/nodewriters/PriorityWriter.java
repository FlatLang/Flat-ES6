package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class PriorityWriter extends ValueWriter implements AccessibleWriter
{
	public abstract Priority node();
	
	@Override
	public StringBuilder writeUseExpression(StringBuilder builder)
	{
		return builder.append('(').append(getWriter(node().getContents()).writeExpression()).append(')').append(writeArrayAccess());
	}
	
	@Override
	public StringBuilder writeExpression(StringBuilder builder)
	{
		if (isInstanceClosure())
		{
			return writeInstanceClosure(builder);
		}

		writeNullFallbackPrefix(builder);
		
		writeUseExpression(builder);

		writeNullFallbackPostfix(builder);

		writeAccessedExpression(builder);

		return builder;
	}
}