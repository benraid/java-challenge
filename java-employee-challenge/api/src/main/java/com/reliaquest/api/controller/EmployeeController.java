package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RestController
public class EmployeeController implements IEmployeeController<Employee, EmployeeInput> {

    @Autowired
    private EmployeeService employeeService;

    // Used @Retryable because of the web server randomly choosing when to rate limit requests
    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return (employees != null) ? ResponseEntity.ok(employees) : ResponseEntity.notFound().build();
    }

    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
        return (employees != null) ? ResponseEntity.ok(employees) : ResponseEntity.notFound().build();
    }

    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<Employee> getEmployeeById(String id) {
        Employee employee = employeeService.getEmployeeById(id);
        return (employee != null) ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        return (highestSalary != null) ? ResponseEntity.ok(highestSalary) : ResponseEntity.notFound().build();
    }

    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
        return (topTenNames != null) ? ResponseEntity.ok(topTenNames) : ResponseEntity.notFound().build();
    }

    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<Employee> createEmployee(EmployeeInput employeeInput) {
        Employee employee = employeeService.createEmployee(employeeInput);
        return (employee != null) ? ResponseEntity.ok(employee) : ResponseEntity.badRequest().build();
    }

    @Override
    @Retryable(
            value = {HttpClientErrorException.class, HttpServerErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 30000, multiplier = 2, maxDelay = 180000)
    )
    public ResponseEntity<String> deleteEmployeeById(String id) {
        String result = employeeService.deleteEmployeeById(id);
        return (result != null) ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
}