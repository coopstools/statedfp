package com.coopstools.statedfp;

public class TestState {

    private final String json;
    private final String fieldName;
    private final String defaultName = "default";

    public static TestState getPrefilled() {

        return new TestState(
                ""      + "{\n"
                        + "    \"data\": \"meow, meow, I'm a cow\",\n"
                        + "    \"default\": \"bleep, bleep, I'm a sheep\"\n"
                        + "}",
                "data");
    }

    public TestState(String json, String fieldName) {
        this.json = json;
        this.fieldName = fieldName;
    }

    public String getJson() {
        return json;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDefaultName() {
        return defaultName;
    }
}
