package org.flatlang.es6.nodewriters;

import org.flatlang.tree.ExtensionMethodDeclaration;

public abstract class ExtensionMethodDeclarationWriter extends BodyMethodDeclarationWriter
{
	public abstract ExtensionMethodDeclaration node();
	
	public StringBuilder writePrototypeAccess(StringBuilder builder)
	{
		return builder;
	}
}