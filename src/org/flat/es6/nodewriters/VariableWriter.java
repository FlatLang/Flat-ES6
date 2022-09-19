package org.flat.es6.nodewriters;

import org.flat.tree.Constructor;
import org.flat.tree.InitializationMethod;
import org.flat.tree.InstanceDeclaration;
import org.flat.tree.ReferenceParameter;
import org.flat.tree.lambda.LambdaMethodDeclaration;
import org.flat.tree.variables.Variable;
import org.flat.tree.variables.VariableDeclaration;

public abstract class VariableWriter extends IdentifierWriter
{
	public abstract Variable node();

	public StringBuilder writeUsePrefix(StringBuilder builder) {
		if (!node().isAccessed())
		{
			VariableDeclaration declaration = node().getDeclaration();

			if (!declaration.isExternal())
			{
				// static field
				if (!declaration.isInstance())
				{
					getWriter(declaration.getDeclaringClass()).writeName(builder).append('.');
				}
				else if (declaration instanceof InitializationMethod)
				{
					getWriter(declaration.getDeclaringClass()).writeName(builder).append('.');
				}
				// field access
				else if (!(declaration instanceof Constructor) && !declaration.isLocal())
				{
					if (!(node().getDeclaration() instanceof ReferenceParameter))
					{
						if (node().getParentMethod() instanceof LambdaMethodDeclaration) {
							builder.append("self");
						} else if (declaration instanceof InstanceDeclaration && ((InstanceDeclaration) declaration).isStatic()) {
							getWriter(declaration.getDeclaringClass()).writeUseExpression(builder);
						} else {
							builder.append("this");
						}

						builder.append('.');
					}
				}
			}
		}

		return builder;
	}

	@Override
	public StringBuilder writeUseExpression(StringBuilder builder) {
		writeUsePrefix(builder);

		return super.writeUseExpression(builder);
	}

	@Override
	public StringBuilder writeExpression(StringBuilder builder) {
		if (isInstanceClosure())
		{
			return writeInstanceClosure(builder);
		}

		return super.writeExpression(builder);
	}
}