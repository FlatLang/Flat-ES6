package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class InstantiationWriter extends IIdentifierWriter
{
	public abstract Instantiation node();
	
	@Override
	public StringBuilder writeUseExpression(StringBuilder builder)
	{
		getWriter(node().getIdentifier()).writeUseExpression(builder);

		writeNullFallbackPostfix(builder);

		return builder;
	}

	@Override
	public void writeNullFallbackPrefix(StringBuilder builder, int skipCount) {
		super.writeNullFallbackPrefix(builder, skipCount + 1);
	}

	@Override
	public void writeNullFallbackPostfix(StringBuilder builder) {
		if (node().doesAccess()) {
//			builder.append(")");
		}
	}
}