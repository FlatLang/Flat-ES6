package org.flat.es6.nodewriters;

import org.flat.tree.*;

public abstract class ImportListWriter extends TypeListWriter
{
	public abstract ImportList node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		return builder;
	}
}