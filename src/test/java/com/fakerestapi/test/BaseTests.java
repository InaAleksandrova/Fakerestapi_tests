package com.fakerestapi.test;

import com.fakerestapi.test.config.ApiConfig;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import java.util.Properties;

public class BaseTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
    }
}
