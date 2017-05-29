package com.coopstools.statedfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.junit.Test;

public class BranchProcedureContainerTest {

    @Test
    public void testConstructionTest() throws Exception {

        BranchProcedureContainer<TestState, String, String> branch =
                BranchProcedureContainer.branch(
                        (s) -> ValueContainer.of("test"),
                        (s, v) -> true);

        assertNotNull(branch);
    }

    @Test
    public void testBranchMapping() throws Exception {

        BiFunction<TestState, String, String> mapper = ((testState, s) -> s + s);
        Procedure<TestState, String> procedure = state -> ValueContainer.of("test");

        BranchProcedureContainer<TestState, String, String> branch =
                BranchProcedureContainer.branch(
                        procedure,
                        (s, v) -> true)
                        .map(mapper);

        assertNotNull(branch);
    }

    @Test
    public void testBranchTerminationTrue() throws Exception {

        ResultContainer result = new ResultContainer();

        BiFunction<TestState, String, String> mapper = ((testState, s) -> s + s);
        Procedure<TestState, String> procedure = state -> ValueContainer.of("true");

        Consumer<TestState> branchedProcedure =
                BranchProcedureContainer.branch(procedure, (s, v) -> true)
                        .map(mapper)
                        .endBranch((s, v) -> result.setResult(v))
                        .terminate(v -> result.setResult("false"));

        assertNull(result.getResult());

        branchedProcedure.accept(new TestState("", ""));
        assertEquals("truetrue", result.getResult());
    }

    @Test
    public void testBranchTerminationFalse() throws Exception {

        ResultContainer result = new ResultContainer();

        BiFunction<TestState, String, String> mapper = ((testState, s) -> s + s);
        Procedure<TestState, String> procedure = state -> ValueContainer.of("true");

        Consumer<TestState> branchedProcedure =
                BranchProcedureContainer.branch(procedure, (s, v) -> false)
                        .map(mapper)
                        .endBranch((s, v) -> result.setResult(v))
                        .terminate(v -> result.setResult("false"));

        assertNull(result.getResult());

        branchedProcedure.accept(new TestState("", ""));
        assertEquals("false", result.getResult());
    }
}
