/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

import java.util.stream.Stream;

class Util {

    static long powOf2(final Long value) {

        return Stream.iterate(2L, i -> i).limit(value).reduce(1L, (i1, i2) -> i1*i2);
    }

    static <T> Node<T> wrapNode(final Node<T> node, final long layers) {
        return Stream
                .iterate(node, i -> i)
                .limit(layers)
                .reduce(node, (n, _v) -> BranchyNode.init(n, null));
    }
}
