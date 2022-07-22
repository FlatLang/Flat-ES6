package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class LiteralWriter extends IValueWriter implements AccessibleWriter
{
	public abstract Literal node();
	
	public StringBuilder writeUseExpression(StringBuilder builder)
	{
		if (node().isStringInstantiation())
		{
			getWriter(node().getStringInstantiation()).writeExpression(builder);
		}
		else
		{
			builder.append(node().value);
		}
		
		return writeArrayAccess(builder);
	}
	
	@Override
	public StringBuilder writeExpression(final StringBuilder builder)
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

	@Override
	public void writeNullFallbackPrefix(StringBuilder builder, int skipCount) {
		AccessibleWriter.super.writeNullFallbackPrefix(builder, skipCount + 1);
	}

	@Override
	public void writeNullFallbackPostfix(StringBuilder builder) {

	}
}