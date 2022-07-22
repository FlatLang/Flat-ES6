package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class AssignmentMethodWriter extends BodyMethodDeclarationWriter
{
	public abstract AssignmentMethod node();

	@Override
	public StringBuilder writeName(StringBuilder builder) {
		return builder.append("__assignments");
	}

	@Override
	public StringBuilder writeSignature(StringBuilder builder) {
		builder.append("static ");

		return super.writeSignature(builder);
	}
}