/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class NodeBuilder {

    public static <T> Node<T> buildNode(final Collection<T> collection) {

        if (collection == null)
            return null;

        int collectionSize = collection.size();
        if (collectionSize == 0)
            return null;

        Iterator<T> iterator = collection.iterator();
        if (collectionSize == 1)
            return LeafyNode.init(iterator.next(), null);
        if (collectionSize == 2)
            return LeafyNode.init(iterator.next(), iterator.next());

        int nodeCount = (collectionSize / 2) + 1;
        List<Node<T>> nodes = new ArrayList<>();
        while(iterator.hasNext()) {
            T leftValue = iterator.next();
            T rightValue = iterator.hasNext() ? iterator.next() : null;
            Node<T> newNode = LeafyNode.init(leftValue, rightValue);
            nodes.add(newNode);
        }

        return condenseNodes(nodes, nodeCount);
    }

    static <T> Node<T> condenseNodes(final List<Node<T>> nodes, final int nodeCount) {

        if (nodes.size() < 2)
            return nodes.get(0);

        Iterator<Node<T>> nodeIter = nodes.iterator();
        int parentNodeCount = (nodeCount / 2) + 1;
        List<Node<T>> parentNodes = new ArrayList<>(parentNodeCount);
        while (nodeIter.hasNext()) {
            Node<T> leftNode = nodeIter.next();
            Node<T> rightNode = nodeIter.hasNext() ? nodeIter.next() : null;
            Node<T> newNode = BranchyNode.init(leftNode, rightNode);
            parentNodes.add(newNode);
        }
        return condenseNodes(parentNodes, parentNodeCount);
    }
}
