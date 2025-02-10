package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Employee {
    // Setters
    // Getters
    private String id;
    @JsonProperty("employee_name")
    private String name;
    @JsonProperty("employee_salary")
    private int salary;
    @JsonProperty("employee_age")
    private int age;
    @JsonProperty("employee_title")
    private String title;
    @JsonProperty("employee_email")
    private String email;

}