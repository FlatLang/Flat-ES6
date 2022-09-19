package org.flat.es6.nodewriters;

import org.flat.tree.ExternalCodeBlock;

public abstract class ExternalCodeBlockWriter extends NodeWriter
{
	public abstract ExternalCodeBlock node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		return builder.append(node().joinContents((x, expression) -> (expression ? getWriter(x).writeExpression() : getWriter(x).write()).toString())).append('\n');
	}
}