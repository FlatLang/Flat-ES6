package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class AnonymousCompilerMethodWriter extends BodyMethodDeclarationWriter
{
	public abstract AnonymousCompilerMethod node();

//	@Override
//	public StringBuilder writePrototypeAccess(StringBuilder builder) {
//		if (node().isExtension()) {
//			return builder;
//		}
//
//		return super.writePrototypeAccess(builder);
//	}
}