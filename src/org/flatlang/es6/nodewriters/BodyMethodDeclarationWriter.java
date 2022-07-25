package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;
import org.flatlang.tree.annotations.AsyncAnnotation;
import org.flatlang.tree.lambda.LambdaMethodDeclaration;

import java.util.Arrays;

public abstract class BodyMethodDeclarationWriter extends FlatMethodDeclarationWriter
{
	public abstract BodyMethodDeclaration node();

	@Override
	public StringBuilder write(StringBuilder builder)
	{
		writeSignature(builder, true, true).append(" ");
		writeBody(builder);

		return builder.append("\n");
	}

	public boolean shouldUseStatic() {
		return node().isStatic() || node() instanceof InitializationMethod || node() instanceof AssignmentMethod || node() instanceof AnonymousCompilerMethod || node() instanceof ExtensionMethodDeclaration;
	}

	public StringBuilder writeSignature(StringBuilder builder, boolean checkStatic, boolean checkAsync) {
		if (checkStatic && shouldUseStatic()) {
			builder.append("static ");
		}

		if (checkAsync && node().containsAnnotationOfType(AsyncAnnotation.class)) {
			builder.append("async ");
		}

		writeName(builder);

		getWriter(node().getParameterList()).write(builder);

		return builder;
	}

	public StringBuilder writeBody()
	{
		return writeBody(new StringBuilder());
	}

	private static boolean isLambda(Node n)
	{
		return ((Closure)n).isLambda();
	}

	public StringBuilder writeBody(StringBuilder builder)
	{
		builder.append("{\n");

		if (node() instanceof InitializationMethod || node() instanceof LambdaMethodDeclaration == false && node().whereChildOfType(Closure.class, BodyMethodDeclarationWriter::isLambda))
		{
			builder.append("let self = this;\n\n");
		}

		getWriter(node().getScope()).write(builder, false, false);

		return builder.append('}');
	}
}