/*
 * Copyright (C) 2016 by Amobee Inc.
 * All Rights Reserved.
 */
package com.coopstools.statedfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.function.Consumer;
import java.util.function.Function;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class DivergeMergeTest {

    @Test
    public void testCreateDiverge() throws Exception {

        DivergentProcedureContainer<TestState, JSONObject> branch =
                ProcedureContainer
                .initializeContainer(TestState::getJson)
                .map(this::parseObjFromJson)
                .test((s, v) -> !v.has(s.getFieldName()));

        assertNotNull(branch);
    }

    @Test
    public void testTruthMergeBack() throws Exception {

        ResultContainer result = new ResultContainer();

        Consumer<TestState> consumer = ProcedureContainer
                .initializeContainer(TestState::getJson)
                .map(this::parseObjFromJson)
                .test((s, v) -> true)
                .then((s, v) -> v)
                .map(this::yankStringFromJsonObj)
                .terminate((s, f) -> result.setResult(f));

        consumer.accept(TestState.getPrefilled());
        assertEquals("meow, meow, I'm a cow", result.getResult());
    }

    @Test
    public void testTruePath() throws Exception {

        ResultContainer result = new ResultContainer();

        ProcedureContainer
                .initializeContainer(TestState::getFieldName)
                .test((s, v) -> true)
                .then((s, v) -> v + v)
                .terminate(result::setResult)
                .accept(TestState.getPrefilled());

        assertEquals("datadata", result.getResult());
    }

    @Test
    public void testFalsePath() throws Exception {

        ResultContainer result = new ResultContainer();
        Consumer<String> consumer = ProcedureContainer
                .initializeContainer(Function.<String>identity())
                .test((s, v) -> "truth".equals(v))
                .thenOrElse((s, v) -> v.length(), (s, v) -> 2 * v.length())
                .map(v -> String.valueOf(v))
                .terminate(result::setResult);

        consumer.accept("truth");
        assertEquals("5", result.getResult());

        consumer.accept("false");
        assertEquals("10", result.getResult());
    }

    private JSONObject parseObjFromJson(final String json) {

        try {
            return new JSONObject(json);
        } catch (JSONException jsonEx) {
            throw new RuntimeException(jsonEx);
        }
    }

    private String yankStringFromJsonObj(final TestState state, final JSONObject obj) {

        try {
            return obj.getString(state.getFieldName());
        } catch (JSONException jsonEx) {
            throw new RuntimeException(jsonEx);
        }
    }
}
