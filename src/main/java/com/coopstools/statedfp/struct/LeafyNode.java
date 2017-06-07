/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

class LeafyNode<T> implements Node<T> {

    private T leftValue;
    private T rightValue;

    static <T> LeafyNode<T> init(final T leftValue, final T rightValue) {
        return new LeafyNode<>(leftValue, rightValue);
    }

    LeafyNode(final T leftValue, final T rightValue) {

        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public T get(final long path) {
        return (path & 1L) == 0 ? leftValue : rightValue;
    }

    @Override
    public long getDepth(final long previous) {
        return 1L + previous;
    }

    @Override
    public long count() {
        return (rightValue == null) ? 1L : 2L;
    }

    @Override
    public Node<T> set(long path, T value) {

        if ((path & 1L) == 0)
            return new LeafyNode<>(value, rightValue);
        return new LeafyNode<>(leftValue, value);
    }
}
