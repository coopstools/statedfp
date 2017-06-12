/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp.struct;

import java.util.function.Consumer;

interface Node<T> {

    T get(final long path);
    long getDepth(final long previous);
    long count();
    Node<T> set(final long path, final T value);
    Node<T> add(final T value, final long newIndex);
    void forEach(final Consumer<T> consumer);
}
