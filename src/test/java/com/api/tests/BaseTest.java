package com.api.tests;

import com.api.utils.Configuration.ConfigManager;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    @BeforeClass
    public void setup(){
        RestAssured.baseURI = ConfigManager.get("baseURI");
    }
}
