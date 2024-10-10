package com.fakerestapi.test.validators;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

public class ResponseValidator {


    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode,
                "Expected status code: " + expectedStatusCode + ", but got: " + actualStatusCode);
    }

    public static void validateListOfIdsNotEmpty(Response response) {
        List<Integer> ids = response.jsonPath().getList("id", Integer.class);
        Assert.assertFalse(ids.isEmpty(), "List of items is empty");
    }

    public static void validateFieldValue(Object actualValue, String jsonPath, Object expectedValue) {
        Assert.assertEquals(actualValue, expectedValue,
                "Expected value for '" + jsonPath + "': " + expectedValue + ", but got: " + actualValue);
    }

    public static void validateFieldNotNull(Object actualValue, String jsonPath) {
        Assert.assertNotNull(actualValue, "The book " + jsonPath + " should not be null.");
    }

    public static void  validateFieldValueIsNotEmpty(Object actualValue, String jsonPath) {
        Assert.assertFalse(actualValue.toString().isEmpty(), "Expected field '" + jsonPath + "' is empty.");
    }

}