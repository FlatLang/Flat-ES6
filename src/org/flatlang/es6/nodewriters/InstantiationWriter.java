package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class InstantiationWriter extends IIdentifierWriter
{
	public abstract Instantiation node();
	
	@Override
	public StringBuilder writeUseExpression(StringBuilder builder)
	{
		getWriter(node().getIdentifier()).writeUseExpression(builder);

		return builder;
	}
}