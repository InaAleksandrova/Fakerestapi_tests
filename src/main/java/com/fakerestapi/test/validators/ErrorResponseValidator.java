package com.fakerestapi.test.validators;

import com.fakerestapi.test.constants.ErrorMessagesConstants;
import com.fakerestapi.test.models.ErrorResponse;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static com.fakerestapi.test.constants.ErrorMessagesConstants.ERROR_MESSAGE_COULD_NOT_CONVERT_TO_SYSTEM;

public class ErrorResponseValidator {


    public static void validateErrorMessageTitle(Response response, String errorMessage) {
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getTitle(), errorMessage, "The expected error message is not found");
    }

    public static void validateErrorResponse(Response response, String value) {
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        Map<String, List<String>> errors = errorResponse.getErrors();
        if (errors == null) {
            Assert.fail("The 'errors' map is null.");
        } else if (!errors.containsKey(value)) {
            Assert.fail(String.format("The %s field is missing from the errors map.", value));
        } else {
            List<String> idErrors = errorResponse.getErrors().get(value);
            Assert.assertNotNull(idErrors, String.format("Expected %s errors to be present.", value));
            Assert.assertTrue(idErrors.get(0).contains(ERROR_MESSAGE_COULD_NOT_CONVERT_TO_SYSTEM),
                        String.format("The %s error message is not displayed", value));
        }
    }

}
