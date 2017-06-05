/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class DivergentProcedureContainer<S, T> {

    private final Procedure<S, T> baseProcedure;
    private final BiPredicate<S, T> predicate;

    private DivergentProcedureContainer(
            final Procedure<S, T> baseProcedure,
            final BiPredicate<S, T> predicate) {

        this.baseProcedure = baseProcedure;
        this.predicate = predicate;
    }

    public static <S, T> DivergentProcedureContainer<S, T> branch(
            final Procedure<S, T> baseProcedure,
            final BiPredicate<S, T> predicate) {

        return new DivergentProcedureContainer<>(baseProcedure, predicate);
    }

    public ProcedureContainer<S, T> then(final BiFunction<S, T, T> function) {

        BiFunction<S, T, T> branchPath = (s, v) ->
                predicate.test(s, v) ? function.apply(s, v) : v;
        Procedure<S, T> branchedProcedure = baseProcedure.andThen(branchPath);
        return new ProcedureContainer<>(branchedProcedure);
    }

    public <R> ProcedureContainer<S, R> thenOrElse(
            final BiFunction<S, T, R> truePath,
            final BiFunction<S, T, R> falsePath) {

        BiFunction<S, T, R> branchPath = (s, v) ->
                predicate.test(s, v) ? truePath.apply(s, v) : falsePath.apply(s, v);
        Procedure<S, R> branchedProcedure = baseProcedure.andThen(branchPath);
        return new ProcedureContainer<>(branchedProcedure);
    }
}
