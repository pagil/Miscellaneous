package com;

public enum SingletoneEnum {
    INSTANCE;

    private String field = "private fieled in enum";

    public void sayHello() {
        System.out.println("Hello!");
    }

    public String getField() {
        return field;
    }
}
