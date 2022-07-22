package org.flatlang.es6.nodewriters;

import org.flatlang.tree.variables.VariableDeclarationList;

public abstract class VariableDeclarationListWriter extends ListWriter
{
	public abstract VariableDeclarationList node();
}