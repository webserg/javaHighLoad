package com.gmail.webserg.travelaws;

import java.io.Serializable;

public class CustomErrorType implements Serializable{
    private final int value;
    private final String message;

    public CustomErrorType(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }


}
