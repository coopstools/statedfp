package com.coopstools.statedfp;

public class ResultContainer {

    private String result;
    private long counter;

    public ResultContainer() {
        counter = 0L;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void tick() {
        counter++;
    }

    public long getCount() {
        return counter;
    }
}
