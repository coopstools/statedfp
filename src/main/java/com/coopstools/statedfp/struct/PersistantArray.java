/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

import static com.coopstools.statedfp.struct.Util.powOf2;
import static com.coopstools.statedfp.struct.Util.wrapNode;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class PersistantArray<T> {

    private final Node<T> topeNode;
    private final long size;
    private final long numberOfLayers;

    public static <T> PersistantArray<T> create() {
        return new PersistantArray<>(null);
    }

    public static <T> PersistantArray<T> init(final Collection<T> collection) {

        return new PersistantArray<>(NodeBuilder.buildNode(collection));
    }

    protected PersistantArray(final Node<T> topNode) {

        if (topNode == null) {
            this.topeNode = null;
            this.numberOfLayers = 0;
            this.size = 0L;
            return;
        }
        this.topeNode = topNode;
        this.numberOfLayers = topNode.getDepth(0L);
        this.size = topNode.count();
    }

    public long size() {
        return size;
    }

    public long getNumberOfLayers() {
        return numberOfLayers;
    }

    public T get(final long index) {

        checkIndex(index);
        long reversedIndex = reverseIndex(index);
        return topeNode.get(reversedIndex);
    }

    public PersistantArray<T> set(final long index, final T value) {

        checkIndex(index);
        long reversedIndex = reverseIndex(index);
        Node<T> newTopNode = topeNode.set(reversedIndex, value);
        return new PersistantArray<>(newTopNode);
    }

    public PersistantArray<T> add(final T value) {

        Node<T> newTopNode = allLeavesFilled() ?
                createLayerWithNewValue(value) :
                topeNode.add(value, reverseIndex(size));
        return new PersistantArray<>(newTopNode);
    }

    public void forEach(final Consumer<T> consumer) {
        topeNode.forEach(consumer);
    }

    private long reverseIndex(final long index) {

        long reversedIndex = 0;
        for (int i = 0; i < numberOfLayers; i++) {
            reversedIndex <<= 1;
            long shiftedIndex = index >> i;
            reversedIndex |= (shiftedIndex & 1L);
        }
        return reversedIndex;
    }

    private void checkIndex(final long index) {
        if (index + 1 > size)
            throw new IndexOutOfBoundsException(String.format("Array only has %d elements", size));
    }

    private boolean allLeavesFilled() {
        return size == powOf2(numberOfLayers);
    }

    private Node<T> createLayerWithNewValue(final T value) {

        Node<T> leafNode = LeafyNode.init(value, null);
        Node<T> rightNode = wrapNode(leafNode, numberOfLayers - 1);
        return BranchyNode.init(topeNode, rightNode);
    }
}
