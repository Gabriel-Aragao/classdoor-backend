package com.classdoor.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_STUDENT,
    ROLE_PROFESSOR;

    @JsonCreator
    public static Role fromValue(String value) {
        if (value == null) return ROLE_STUDENT;
        String val = value.trim().toUpperCase();
        if (val.equals("STUDENT") || val.equals("ROLE_STUDENT")) {
            return ROLE_STUDENT;
        }
        if (val.equals("PROFESSOR") || val.equals("ROLE_PROFESSOR")) {
            return ROLE_PROFESSOR;
        }
        try {
            return Role.valueOf(val);
        } catch (IllegalArgumentException e) {
            return ROLE_STUDENT;
        }
    }
}
