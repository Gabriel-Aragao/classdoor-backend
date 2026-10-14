package com.classdoor.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ClassRoomRequest {

    @NotBlank(message = "Semester is required (e.g. 2026.1)")
    private String semester;

    private String policy = "ANONYMOUS_ONLY";

    public ClassRoomRequest() {}

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }
}
