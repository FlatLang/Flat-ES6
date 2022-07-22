package org.flatlang.es6.nodewriters;

import org.flatlang.tree.AccessorMethod;

public abstract class AccessorMethodWriter extends PropertyMethodWriter
{
	public abstract AccessorMethod node();

//	@Override
//	public StringBuilder writeSignature(StringBuilder builder) {
//		builder.append("get ");
//
//		return super.writeSignature(builder);
//	}

	@Override
	public StringBuilder writeName(StringBuilder builder)
	{
		builder.append("accessor_");
		
		return super.writeName(builder);
	}
}