package com.reliaquest.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    // Setters
    // Getters
    private T data;
    private String status;
    private String errorMessage;

}