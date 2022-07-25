package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class StaticBlockWriter extends NodeWriter
{
	public abstract StaticBlock node();
	
	@Override
	public StringBuilder write(StringBuilder builder)
	{
		Scope scope = node().getScope();

		if (scope.getNumVisibleChildren() == 0)
		{
			return builder;
		}

		builder.append("var ");
		generateMethodName(builder, node().getParentClass(), node().getIndex()).append("_initialized").append(" = false;\n");

		builder.append("function ");
		generateMethodName(builder, node().getParentClass(), node().getIndex()).append("() {\n");

		builder.append("if (!");
		generateMethodName(builder, node().getParentClass(), node().getIndex()).append("_initialized");
		builder.append(") {\n");

		generateMethodName(builder, node().getParentClass(), node().getIndex()).append("_initialized = true;\n");

		for (ClassDeclaration c : scope.getDependencies())
		{
			TypeList<StaticBlock> blocks = c.getStaticBlockList();

			if (blocks.getNumVisibleChildren() > 0)
			{
				c.getStaticBlockList().forEachVisibleChild(block -> {
					if (block.getScope().getNumVisibleChildren() > 0) {
						generateMethodName(builder, c, block.getIndex()).append("();\n");
					}
				});
			}
		}

		getWriter(scope).write(builder, false, false);
		
		return builder.append("}\n}\n");
	}

	public static StringBuilder generateMethodName(StringBuilder builder, ClassDeclaration clazz, Integer index)
	{
		return builder.append(getWriter(clazz).writeName()).append(index).append(StaticBlock.C_PREFIX).append(StaticBlock.IDENTIFIER);
	}
}