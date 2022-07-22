package org.flatlang.es6.nodewriters;

import org.flatlang.tree.List;

public abstract class ListWriter extends NodeWriter
{
	public abstract List node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		node().forEachVisibleChild(child -> {
			getWriter(child).write(builder);
		});
		
		return builder;
	}
}