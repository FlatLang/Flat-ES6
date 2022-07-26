package org.flatlang.es6.nodewriters;

import org.flatlang.tree.variables.VariableDeclarationList;

public abstract class VariableDeclarationListWriter extends ListWriter
{
	public abstract VariableDeclarationList node();

	@Override
	public StringBuilder write(StringBuilder builder)
	{
		node().forEachVisibleChild(child -> {
			if (!child.isPropertyTrue("fromAssignment") || child.isPropertyTrue("requiresPrecedingDeclaration")) {
				getWriter(child).write(builder);
			}
		});

		return builder;
	}
}