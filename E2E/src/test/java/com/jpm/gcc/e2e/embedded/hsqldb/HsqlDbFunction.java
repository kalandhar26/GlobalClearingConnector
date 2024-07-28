package com.jpm.gcc.e2e.embedded.hsqldb;

public class HsqlDbFunction {
    public static byte[] caseToRaw(String text){
        return null == text || "null".equalsIgnoreCase(text) ? null : text.getBytes();
    }
}
