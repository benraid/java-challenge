package com.reliaquest.api.service;

import com.reliaquest.api.model.ApiResponse;
import com.reliaquest.api.model.DeleteEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final String BASE_URL = "http://localhost:8112/api/v1/employee";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves all employees from the mock employee API.
     *
     * @return A list of employees or null if the API response is null.
     */
    public List<Employee> getAllEmployees() {
        ResponseEntity<ApiResponse<List<Employee>>> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<List<Employee>>>() {}
        );

        ApiResponse<List<Employee>> apiResponse = response.getBody();
        return (apiResponse != null) ? apiResponse.getData() : null;
    }

    /**
     * Searches for employees whose names contain the given search string.
     *
     * @param searchString The search string to be used for filtering.
     * @return A list of employees whose names contain the search string or null if the API response is null.
     */
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = getAllEmployees();
        if (employees != null) {
            return employees.stream()
                    .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Retrieves an employee by their unique identifier.
     *
     * @param id The unique identifier of the employee.
     * @return The employee with the given identifier or null if the employee is not found or the API response is null.
     */
    public Employee getEmployeeById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            ResponseEntity<ApiResponse<Employee>> response = restTemplate.exchange(
                    BASE_URL + "/" + uuid,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Employee>>() {}
            );

            ApiResponse<Employee> apiResponse = response.getBody();
            return (apiResponse != null) ? apiResponse.getData() : null;
        } catch (IllegalArgumentException | HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    /**
     * Retrieves the highest salary among all employees.
     *
     * @return The highest salary among all employees or 0 if the API response is null.
     */
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployees();
        if (employees != null) {
            return employees.stream()
                    .mapToInt(Employee::getSalary)
                    .max()
                    .orElse(0);
        }
        return null;
    }

    /**
     * Retrieves the names of the top 10 highest-earning employees.
     *
     * @return A list of the names of the top 10 highest-earning employees or null if the API response is null.
     */
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees();
        if (employees != null) {
            return employees.stream()
                    .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                    .limit(10)
                    .map(Employee::getName)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Creates a new employee using the provided employee input.
     *
     * @param employeeInput The input data for creating a new employee.
     * @return The created employee or null if the API response is null.
     */
    public Employee createEmployee(EmployeeInput employeeInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmployeeInput> request = new HttpEntity<>(employeeInput, headers);

        ResponseEntity<ApiResponse<Employee>> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ApiResponse<Employee>>() {}
        );

        ApiResponse<Employee> apiResponse = response.getBody();
        return (apiResponse != null) ? apiResponse.getData() : null;
    }

    /**
     * Deletes an employee by their unique identifier using the provided delete input.
     *
     * @param id The unique identifier of the employee to be deleted.
     * @return A success message if the employee is deleted successfully or null if the employee is not found or the API response is null.
     */
    public String deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            return null;
        }

        String employeeName = employee.getName();
        DeleteEmployeeInput deleteInput = new DeleteEmployeeInput();
        deleteInput.setName(employeeName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DeleteEmployeeInput> request = new HttpEntity<>(deleteInput, headers);

        ResponseEntity<ApiResponse<Boolean>> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
        );

        ApiResponse<Boolean> apiResponse = response.getBody();
        return (apiResponse != null && Boolean.TRUE.equals(apiResponse.getData()))
                ? "Employee deleted successfully"
                : null;
    }
}