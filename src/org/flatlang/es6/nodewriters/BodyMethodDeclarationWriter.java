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
		// FIXME: This should be handled better. Static and non-static mixed overloads should be able to coexist easier
		if (node().isStatic()) {
			boolean nonStaticOverloadExists = Arrays.stream(node().getDeclaringClass().getMethodList().getMethods())
                    .filter(m -> m != node())
                    .filter(m -> m.getName().equals(node().getName()))
                    .anyMatch(m -> !m.isStatic());

            if (nonStaticOverloadExists) {
				if (node().containsAnnotationOfType(AsyncAnnotation.class)) {
					builder.append("async ");
				}

				writeName(builder);

                getWriter(node().getParameterList()).write(builder).append(" ");

                writeBody(builder);

                return builder.append("\n\n");
            }
		}

		if (node().containsAnnotationOfType(AsyncAnnotation.class)) {
			builder.append("async ");
		}

		writeName(builder);

		getWriter(node().getParameterList()).write(builder).append(" ");

		writeBody(builder);

		if (node().isStatic()) {
//			builder.append(";\n\n");
//			writeAssignedVariable(builder).append(" = ");
//			writeAssignedVariable(builder, true);
		}

		return builder.append("\n\n");
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
			builder.append("const self = this;\n\n");
		}

		getWriter(node().getScope()).write(builder, false);

		return builder.append('}');
	}
}