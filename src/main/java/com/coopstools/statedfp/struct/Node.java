/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

interface Node<T> {

    T get(final long path);
    long getDepth(final long previous);
    long count();
    Node<T> set(final long path, final T value);
}
