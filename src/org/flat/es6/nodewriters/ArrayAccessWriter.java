package org.flat.es6.nodewriters;

import org.flat.tree.variables.ArrayAccess;

public abstract class ArrayAccessWriter extends NodeWriter
{
	public abstract ArrayAccess node();
	
	@Override
	public StringBuilder writeExpression(StringBuilder builder)
	{
		return getWriter(node().getDimensions()).writeExpression(builder);
	}
}