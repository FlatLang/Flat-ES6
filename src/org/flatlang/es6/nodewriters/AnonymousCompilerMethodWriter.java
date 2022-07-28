package org.flatlang.es6.nodewriters;

import org.flatlang.es6.engines.ES6CompileEngine;
import org.flatlang.tree.*;

public abstract class AnonymousCompilerMethodWriter extends BodyMethodDeclarationWriter
{
	public abstract AnonymousCompilerMethod node();

	@Override
	public StringBuilder write(StringBuilder builder) {
		if (ES6CompileEngine.INLINE_ARRAY_INITIALIZERS && node().getProperty("array") != null) {
			return builder;
		}

		return super.write(builder);
	}

	//	@Override
//	public StringBuilder writePrototypeAccess(StringBuilder builder) {
//		if (node().isExtension()) {
//			return builder;
//		}
//
//		return super.writePrototypeAccess(builder);
//	}
}