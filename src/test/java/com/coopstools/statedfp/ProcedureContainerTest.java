package com.coopstools.statedfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.function.Consumer;
import java.util.function.Function;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class ProcedureContainerTest {

    @Test
    public void testSimple() throws Exception {

        ResultContainer result = new ResultContainer();

        Function<TestState, String> intializer = TestState::getJson;
        Consumer<TestState> consumer = ProcedureContainer
                .initializeContainer(intializer)
                .map(this::parseObjFromJson)
                .map(this::yankStringFromJsonObj)
                .terminate(result::setResult);

        assertNull(result.getResult());

        consumer.accept(TestState.getPrefilled());
        Assert.assertEquals("meow, meow, I'm a cow", result.getResult());
    }

    @Test
    public void testUsedBranch() throws Exception {

        ResultContainer result = new ResultContainer();

        Function<TestState, String> intializer = TestState::getJson;
        Consumer<TestState> consumer = ProcedureContainer
                .initializeContainer(intializer)
                .map(this::parseObjFromJson)

                .branch((s, v) -> v.has(s.getFieldName()))
                .map(this::yankStringFromJsonObj)
                .endBranch((s, v) -> result.setResult(v))

                .map(this::yankDefaultFromJsonObj)
                .terminate((s, v) -> result.setResult(v));

        assertNull(result.getResult());
        consumer.accept(TestState.getPrefilled());

        assertEquals("meow, meow, I'm a cow", result.getResult());
    }

    @Test
    public void testUnusedBranch() throws Exception {

        ResultContainer result = new ResultContainer();

        Function<TestState, String> intializer = TestState::getJson;
        Consumer<TestState> consumer = ProcedureContainer
                .initializeContainer(intializer)
                .map(this::parseObjFromJson)

                .branch((s, v) -> !v.has(s.getFieldName()))
                .map(this::yankStringFromJsonObj)
                .endBranch((s, v) -> result.setResult(v))

                .map(this::yankDefaultFromJsonObj)
                .terminate((s, v) -> result.setResult(v));

        assertNull(result.getResult());
        consumer.accept(TestState.getPrefilled());

        assertEquals("bleep, bleep, I'm a sheep", result.getResult());
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

    private String yankDefaultFromJsonObj(final TestState state, final JSONObject obj) {

        try {
            return obj.getString(state.getDefaultName());
        } catch (JSONException jsonEx) {
            throw new RuntimeException(jsonEx);
        }
    }
}
