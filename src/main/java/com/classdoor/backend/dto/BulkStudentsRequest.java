package com.classdoor.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class BulkStudentsRequest {

    @NotEmpty(message = "Student emails list cannot be empty")
    private List<String> emails;

    public BulkStudentsRequest() {}

    public List<String> getEmails() { return emails; }
    public void setEmails(List<String> emails) { this.emails = emails; }
}
