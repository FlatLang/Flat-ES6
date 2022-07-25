package org.flatlang.es6.nodewriters;

import org.flatlang.tree.*;

public abstract class MethodListWriter extends TypeListWriter {
    public abstract MethodList node();

    @Override
    public StringBuilder write(final StringBuilder builder) {
        int[] i = new int[]{0};

        node().forEachVisibleChild(method -> {
            StringBuilder b = getWriter(method).write();

            if (b.length() > 0) {
                if (i[0]++ > 0) {
                    builder.append("\n");
                }

                builder.append(b);
            }
        });

        return builder;
    }
}