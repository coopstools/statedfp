/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class PersistantArrayTest {

    @Test
    public void testCreation() throws Exception {

        PersistantArray<String> strings = PersistantArray.create();

        assertNotNull(strings);
    }

    @Test
    public void testLeafyNodeCreation() throws Exception {

        Node<String> node = LeafyNode.init(null, null);

        assertNotNull(node);
    }

    @Test
    public void testLeafyNodeGetLeft() throws Exception {

        Node<String> node = LeafyNode.init("left", "right");

        assertEquals("left", node.get(0L));
    }

    @Test
    public void testLeafyNodeGetRight() throws Exception {

        Node<String> node = LeafyNode.init("left", "right");

        assertEquals("right", node.get(1L));
    }

    @Test
    public void testBranchyNodeCreation() throws Exception {

        Node<String> node = BranchyNode.init(null, null);

        assertNotNull(node);
    }

    @Test
    public void testBranchyNodeGetLeftA() throws Exception {

        Node<String> leftNode = LeafyNode.init("leftA", "leftB");
        Node<String> rightNode = LeafyNode.init("rightA", "rightB");

        Node<String> parentNode = BranchyNode.init(leftNode, rightNode);

        assertEquals("leftA", parentNode.get(0L));
    }

    @Test
    public void testBranchyNodeGetLeftB() throws Exception {

        Node<String> leftNode = LeafyNode.init("leftA", "leftB");
        Node<String> rightNode = LeafyNode.init("rightA", "rightB");

        Node<String> parentNode = BranchyNode.init(leftNode, rightNode);

        assertEquals("leftB", parentNode.get(10L));
    }

    @Test
    public void testBranchyNodeGetRightA() throws Exception {

        Node<String> leftNode = LeafyNode.init("leftA", "leftB");
        Node<String> rightNode = LeafyNode.init("rightA", "rightB");

        Node<String> parentNode = BranchyNode.init(leftNode, rightNode);

        assertEquals("rightA", parentNode.get(2L));
    }

    @Test
    public void testPersistantArrayDimensions() throws Exception {

        Node<String> leftNode = LeafyNode.init("leftA", "leftB");
        Node<String> rightNode = LeafyNode.init("rightA", "rightB");

        Node<String> parentNode = BranchyNode.init(leftNode, rightNode);

        PersistantArray<String> pa = new PersistantArray<>(parentNode);

        assertNotNull(pa);
        assertEquals(2L, pa.getNumberOfLayers());
        assertEquals(4L, pa.size());

        //assertEquals("leftA", pa.get(0L));
        //assertEquals("leftB", pa.get(1L));
        //assertEquals("rightA", pa.get(2L));
        //assertEquals("rightB", pa.get(3L));
    }

    @Test
    public void testPersistantArrayGetTooBig() throws Exception {

        try {
            PersistantArray<Void> persistantArray = PersistantArray.create();
            persistantArray.get(0L);
            fail("Index out of bounds error not thrown");
        } catch (IndexOutOfBoundsException indOOBEx) {

        }
    }

    @Test
    public void testPAGetWithOneNode() throws Exception {

        Node<String> node = LeafyNode.init("left", "right");
        PersistantArray<String> pa = new PersistantArray<>(node);

        assertEquals("right", pa.get(1L));
    }

    @Test
    public void testPAGetWithMultiNode() throws Exception {

        Node<String> leftNode = LeafyNode.init("leftA", "leftB");
        Node<String> rightNode = LeafyNode.init("rightA", "rightB");

        Node<String> parentNode = BranchyNode.init(leftNode, rightNode);

        PersistantArray<String> pa = new PersistantArray<>(parentNode);

        assertEquals("leftA", pa.get(0L));
        assertEquals("leftB", pa.get(1L));
        assertEquals("rightA", pa.get(2L));
        assertEquals("rightB", pa.get(3L));
    }

    @Test
    public void testPABuildFromCollection() throws Exception {

        List<String> collection = Arrays.<String>asList("a", "b", "c");

        PersistantArray<String> pa = PersistantArray.init(collection);

        assertEquals("b", pa.get(1L));
    }

    @Test
    public void testPABuildFromLargerCollection() throws Exception {

        List<String> collection = Stream
                .iterate(0, i -> i + 1)
                .limit(10000)
                .map(String::valueOf)
                .collect(Collectors.toList());

        PersistantArray<String> pa = PersistantArray.init(collection);

        assertEquals("5323", pa.get(5323L));
    }
}
