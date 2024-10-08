package com.fakerestapi.test.clients;

import com.fakerestapi.test.config.ApiConfig;
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
    public Response post(String endpoint, Object body) {
        return request.body(body).when().post(endpoint);
    }

    // PUT method with path parameter, like /Books/{id}
    public Response putWithPathParam(String endpoint, String paramName, Object paramValue, Object body) {
        return request.pathParam(paramName, paramValue).body(body).when().put(endpoint);
    }

    // DELETE method with path parameter, like /Books/{id}
    public Response deleteWithPathParam(String endpoint, String paramName, Object paramValue) {
        return given().pathParam(paramName, paramValue).when().delete(endpoint);

    }
}
