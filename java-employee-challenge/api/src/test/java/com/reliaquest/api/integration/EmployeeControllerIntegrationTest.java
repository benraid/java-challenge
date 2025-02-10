package com.reliaquest.api.integration;

import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    // IMPORTANT NOTE: Unit tests when ran all at once will take time because of the web server randomly choosing
    // when to rate limit requests and having to use retryable to avoid issues with this.
    // If you would like faster results you can run the tests individually.

    // I also used "curl -X GET http://localhost:8112/api/v1/employee" to help monitor the list of employees on the server.

    @Autowired
    private EmployeeController employeeController;

    @Test
    public void testGetAllEmployees() {
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code 200 OK");
        List<Employee> employees = response.getBody();
        assertNotNull(employees, "Employee list should not be null");
        assertFalse(employees.isEmpty(), "Employee list should not be empty");
    }

    // IMPORTANT! You should put your own legitimate name to test this method or it will fail.
    @Test
    public void testGetEmployeesByNameSearch() {
        String searchString = "Elvira Fahey";
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(searchString);
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code 200 OK");
        List<Employee> employees = response.getBody();
        assertNotNull(employees, "Employee list should not be null");
        assertFalse(employees.isEmpty(), "Employee list should contain at least one match");
        // Verify that each returned employee's name contains the search string (case-insensitive)
        employees.forEach(employee ->
                assertTrue(employee.getName().toLowerCase().contains(searchString.toLowerCase()),
                        "Employee name should contain '" + searchString + "'"));
    }

    // IMPORTANT! You must use a valid ID or the test will fail.
    @Test
    public void testGetEmployeeById() {
        String testId = "de7ae676-083c-435b-acd3-a5a204675083";
        ResponseEntity<Employee> response = employeeController.getEmployeeById(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code 200 OK");
        Employee employee = response.getBody();
        assertNotNull(employee, "Employee should not be null");
        assertEquals(testId, employee.getId(), "Employee ID should match the searched ID");
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code 200 OK");
        Integer highestSalary = response.getBody();
        assertNotNull(highestSalary, "Highest salary should not be null");
        assertTrue(highestSalary > 0, "Highest salary should be greater than 0");
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code 200 OK");
        List<String> topTenNames = response.getBody();
        assertNotNull(topTenNames, "Top ten names list should not be null");
        // It should return at most 10 names
        assertTrue(topTenNames.size() <= 10, "List should contain 10 or fewer names");
    }

    @Test
    public void testCreateEmployee() {
        // Create a sample EmployeeInput
        EmployeeInput input = new EmployeeInput();
        input.setName("John Doe");
        input.setSalary(1000000);
        input.setAge(30);
        input.setTitle("Software Engineer");

        // Call the createEmployee method
        ResponseEntity<Employee> response = employeeController.createEmployee(input);
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code 200 OK on create");
        Employee createdEmployee = response.getBody();
        assertNotNull(createdEmployee, "Created employee should not be null");
        assertNotNull(createdEmployee.getId(), "Created employee ID should not be null");
        assertEquals("John Doe", createdEmployee.getName(), "Employee name should match the input");

        // Cleanup: Remove the created employee after testing
        ResponseEntity<String> deleteResponse = employeeController.deleteEmployeeById(createdEmployee.getId());
    }

    @Test
    public void testDeleteEmployee() {
        // Create an employee to delete
        EmployeeInput input = new EmployeeInput();
        input.setName("Joe Doe");
        input.setSalary(90000);
        input.setAge(28);
        input.setTitle("Product Manager");

        ResponseEntity<Employee> createResponse = employeeController.createEmployee(input);
        Employee createdEmployee = createResponse.getBody();
        String employeeId = createdEmployee.getId();

        // Now delete the employee
        ResponseEntity<String> deleteResponse = employeeController.deleteEmployeeById(employeeId);
        assertNotNull(deleteResponse, "Delete response should not be null");
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode(), "Expected status code 200 OK on delete");
        assertEquals("Employee deleted successfully", deleteResponse.getBody(), "Expected deletion success message");

        // Attempt to delete a non-existing employee (using an unlikely UUID)
        ResponseEntity<String> deleteResponse2 = employeeController.deleteEmployeeById("1");
        assertNotNull(deleteResponse2, "Delete response for non-existing employee should not be null");
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse2.getStatusCode(), "Expected status code 404 NOT FOUND for non-existing employee");
    }
}