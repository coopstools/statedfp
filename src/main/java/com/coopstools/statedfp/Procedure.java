/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp;

import java.util.Objects;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Procedure<S, T> {

    ValueContainer<T> procede(S stateStructure);

    default <R> Procedure<S, R> andThen(BiFunction<S, T, R> after) {
        Objects.requireNonNull(after);
        return (param) -> {
            ValueContainer<T> intermediateValue = procede(param);
            if (intermediateValue.isTerminated())
                return ValueContainer.<R>terminated();
            R returnValue = after.apply(param, intermediateValue.getValue());
            return ValueContainer.of(returnValue);
        };
    }
}
