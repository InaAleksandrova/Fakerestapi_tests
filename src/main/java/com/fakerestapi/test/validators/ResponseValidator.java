package com.fakerestapi.test.validators;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

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

    public static void validateField(Response response, String jsonPath, Object expectedValue) {
        Response actualValue = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actualValue, expectedValue,
                "Expected value for '" + jsonPath + "': " + expectedValue + ", but got: " + actualValue);
    }

    public static void validateFields(Response response, Map<String, Object> expectedFields) {
        for (Map.Entry<String, Object> entry : expectedFields.entrySet()) {
            String jsonPath = entry.getKey();
            Object expectedValue = entry.getValue();
            validateField(response, jsonPath, expectedValue);
        }
    }

    public static void validateFieldExists(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(value, "Expected field '" + jsonPath + "' to exist in the response, but it is missing.");
    }

    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        Assert.assertEquals(actualContentType, expectedContentType,
                "Expected content type: " + expectedContentType + ", but got: " + actualContentType);
    }
}