package com.fakerestapi.test.clients;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;

public class BaseClient {

    protected RequestSpecification request = given().contentType(ContentType.JSON);

    // GET method to retrieve data from the specified endpoint
    public Response getAll(String endpoint) {
        return given().when().get(endpoint);
    }

    // GET method with path parameter, like /Books/{id}
    public Response getWithPathParam(String endpoint, String paramName, Object paramValue) {
        return given().pathParam(paramName, paramValue).when().get(endpoint).then().extract().response();
    }

    // POST method to send data to the specified endpoint
    public Response post(String endpoint, String bodyJson) {
        return request.body(bodyJson).when().post(endpoint);
    }

    // PUT method with path parameter, like /Books/{id}
    public Response putWithPathParam(String endpoint, String paramName, Object paramValue, String bodyJson) {
        return request.pathParam(paramName, paramValue).body(bodyJson).when().put(endpoint);
    }

    // DELETE method with path parameter, like /Books/{id}
    public Response deleteWithPathParam(String endpoint, String paramName, Object paramValue) {
        return given().pathParam(paramName, paramValue).when().delete(endpoint);

    }
}
