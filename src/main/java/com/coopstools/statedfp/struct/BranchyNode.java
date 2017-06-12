/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

import static com.coopstools.statedfp.struct.Util.wrapNode;

import java.util.function.Consumer;

class BranchyNode<T> implements Node<T> {

    private Node<T> leftNode;
    private Node<T> rightNode;

    static <T> BranchyNode<T> init(final Node<T> leftNode, final Node<T> rightNode) {
        return new BranchyNode<>(leftNode, rightNode);
    }

    BranchyNode(Node<T> leftNode, Node<T> rightNode) {

        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    @Override
    public T get(long path) {
        return (path & 1L) == 0 ? leftNode.get(path >> 1) : rightNode.get(path >> 1);
    }

    @Override
    public long getDepth(final long previous) {
        return leftNode.getDepth(previous + 1);
    }

    @Override
    public long count() {

        long leftNodeCount = leftNode.count();
        return (rightNode == null) ? leftNodeCount : leftNodeCount + rightNode.count();
    }

    @Override
    public Node<T> set(long path, T value) {

        if ((path & 1L) == 0)
            return new BranchyNode<>(leftNode.set(path >> 1, value), rightNode);
        return new BranchyNode<>(leftNode, rightNode.set(path >> 1, value));
    }

    @Override
    public Node<T> add(T value, long newIndex) {

        if ((newIndex & 1L) == 0)
            return new BranchyNode<>(leftNode.add(value, newIndex >> 1), rightNode);
        if (rightNode != null)
            return new BranchyNode<>(leftNode, rightNode.add(value, newIndex >> 1));

        long numberOfLayersLeft = leftNode.getDepth(0);
        Node<T> leafNode = LeafyNode.init(value, null);
        Node<T> rightNode = wrapNode(leafNode, numberOfLayersLeft - 1);
        return new BranchyNode<>(leftNode, rightNode);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        leftNode.forEach(consumer);
        if (rightNode != null)
            rightNode.forEach(consumer);
    }
}
