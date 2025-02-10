# Benjamin Raidman

## Java Challenge / Assessment

### Key Changes & Features

- **EmployeeController:**  
  - Acts as the entry point for API requests.
  - Delegates business logic to the service layer.

- **EmployeeService:**  
  - Handles most of the heavy lifting, including remote API calls using `RestTemplate`.
  - Implements rate-limiting workarounds with Spring Retry.

- **Custom Data Models:**  
  These models help structure the data for communication with the server:
  - **ApiResponse:** A generic wrapper for API responses.
  - **DeleteEmployeeInput:** Represents the data required to delete an employee.
  - **Employee:** Contains employee details such as name, salary, age, and title.
  - **EmployeeInput:** Represents the information required to create a new employee.

### Testing

- **Integration Tests:**  
Integration tests can be found here:
  < java-employee-challenge/api/src/test/java/com/reliaquest/api/integration/EmployeeControllerIntegrationTest.java >
  
  **Note:** Some methods require valid employee names and IDs. Tests may fail if invalid or placeholder values are used.

- **Test Execution Considerations:**  
- Running all tests at once might take longer because the web server enforces random rate limits. Spring Retry (`@Retryable`) is used to mitigate this, but it also increases test duration.
- For faster feedback, consider running tests individually.

- **Manual Testing:**  
You can use the following `curl` command to check the current list of employees on the server:
  < curl -X GET http://localhost:8112/api/v1/employee >
