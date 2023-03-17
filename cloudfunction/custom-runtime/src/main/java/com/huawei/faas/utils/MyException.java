package com.huawei.faas.utils;

public class MyException extends Exception{
    private static final long serialVersionUID = -2325033366102299353L;

    public MyException(String message) {
        super(message);
    }
}