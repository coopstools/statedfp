package com.coopstools.statedfp;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <S> A state that is passed from container to container. Should be effectively immutable
 * @param <T> The type that is stored in a single container and acted upon
 */
public class ProcedureContainer<S, T> {

    protected final Procedure<S, T> procedure;

    protected ProcedureContainer(final Procedure<S, T> procedure) {
        this.procedure = procedure;
    }

    public static <S, R> ProcedureContainer<S, R> initializeContainer(
            final Function<S, R> initialDataRetriever) {

        Procedure<S, R> initializer = (paramContainer) -> {
            R initialValue = initialDataRetriever.apply(paramContainer);
            return ValueContainer.of(initialValue);
        };
        return new ProcedureContainer<>(initializer);
    }

    public <R> ProcedureContainer<S, R> map(final BiFunction<S, T, R> mapper) {
        return new ProcedureContainer<>(procedure.andThen(mapper));
    }

    public <R> ProcedureContainer<S, R> map(final Function<T, R> mapper) {
        BiFunction<S, T, R> subMapper =
                (ignored, stagedData) -> mapper.apply(stagedData);
        return new ProcedureContainer<>(procedure.andThen(subMapper));
    }

    public BranchProcedureContainer<S, T, T> branch(final BiPredicate<S, T> predicate) {

        return BranchProcedureContainer.branch(procedure, predicate);
    }

    public DivergentProcedureContainer<S, T> test(final BiPredicate<S, T> predicate) {
        return DivergentProcedureContainer.branch(procedure, predicate);
    }

    public Consumer<S> terminate(final BiConsumer<S, T> consumer) {
        return (param) -> {
            ValueContainer<T> finalValue = procedure.procede(param);
            if (finalValue.isTerminated())
                return;

            consumer.accept(param, finalValue.getValue());
        };
    }

    public Consumer<S> terminate(final Consumer<T> consumer) {
        return (param) -> {
            ValueContainer<T> finalValue = procedure.procede(param);
            if (finalValue.isTerminated())
                return;

            consumer.accept(finalValue.getValue());
        };
    }

    public Function<S, Optional<T>> get() {

        return s -> {
            ValueContainer<T> containedValue = procedure.procede(s);
            if (containedValue.isTerminated())
                return Optional.empty();
            return Optional.ofNullable(containedValue.getValue());
        };
    }
}
