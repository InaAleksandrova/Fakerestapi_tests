# Fakerestapi_tests

## Overview
This project is designed for automating API tests for an online bookstore using the FakeRestAPI. The project uses TestNG for structuring the test cases, RestAssured for making HTTP calls to the API, and Allure for generating comprehensive reports. Additionally, the project is configured with GitHub Actions for continuous integration and deployment (CI/CD).

***

**Project Structure**

Here is a brief overview of the folder structure in the project:
```
Fakerestapi_tests/
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.fakerestapi.test
│   │   │   │   ├── fakerestapi
│   │   │   │   │   ├── clients
|   |   |       
│   │   │   │   │   │   └── BookClient.java
│   │   │   │   │   ├── config
│   │   │   │   │   │   └── ApiConfig.java
│   │   │   │   │   ├── models
│   │   │   │   │   │   └── Book.java
│   │   │   │   │   └── utils
│   │   │   │   │       └── ResponseValidator.java
│   ├── test
│       ├── java
│       │   ├── com
│       │   │   ├── fakerestapi
│       │   │   │   ├── test
│       │   │   │   │   └── BooksTests.java
│       │   │   │   └── config
│       │   │   │       └── BaseTests.java
├── pom.xml
├── README.md

```

***

**Project Features:**
 * API Test automation using TestNG and RestAssured
 * Allure reports integration for visualizing test results
 * CI/CD pipeline using GitHub Actions
 * Validations for both success and error scenarios
 * Usage of Java Lombok for model simplification

***

**Technologies Used**
 * Java: Programming language used for the test implementation
 * TestNG: Testing framework for organizing the test cases
 * RestAssured: Library for HTTP calls and API automation
 * Allure: Reporting tool for generating test execution reports
 * Maven: Build tool for managing dependencies and running tests
 * GitHub Actions: CI/CD for continuous integration and test execution
 * Java Faker: Used for generating random test data 

***

**Prerequisites**
 * Java 11+: Ensure that JDK is installed on your machine.
 * Maven: Make sure you have Maven installed.
 * Allure: Install Allure for report generation if you plan to view reports locally.

***
**Setup**
 * Step 1: Clone the repository
```
git clone https://github.com/InaAleksandrova/Fakerestapi_tests.git
cd Fakerestapi_tests
```
 * Step 2: Install Dependencies 
   * Run the following Maven command to install all dependencies:
 ```
mvn clean install
```
This will also download the necessary libraries like TestNG, RestAssured, Lombok, and Allure.




