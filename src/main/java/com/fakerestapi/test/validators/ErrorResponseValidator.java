package com.fakerestapi.test.validators;

import com.fakerestapi.test.models.ErrorResponse;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

import static com.fakerestapi.test.constants.ErrorMessagesConstants.ERROR_MESSAGE_NOT_VALID_VALUE;
import static com.fakerestapi.test.constants.ErrorMessagesConstants.ERROR_MESSAGE_VALIDATION_ERRORS;

public class ErrorResponseValidator {

    public static void validateErrorResponse(Response response, String jsonPath, String value) {
        // Deserialize the response into the ErrorResponse model
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // Validate the title
        Assert.assertEquals(errorResponse.getTitle(),ERROR_MESSAGE_VALIDATION_ERRORS,
                "Expected error title doesn't match.");

        // Validate the 'id' field error message
        List<String> idErrors = errorResponse.getErrors().get(value);
        Assert.assertNotNull(idErrors, String.format("Expected %s errors to be present.", value));
        Assert.assertTrue(idErrors.contains(String.format(ERROR_MESSAGE_NOT_VALID_VALUE, value)),
                String.format("Expected %s error message doesn't match.", jsonPath));
    }



}
