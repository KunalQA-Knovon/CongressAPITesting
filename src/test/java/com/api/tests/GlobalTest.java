package com.api.tests;

import com.aventstack.extentreports.gherkin.model.Given;
import com.report.config.ExtentManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.api.tests.LoginScenarioTest.accessToken;
import static io.restassured.RestAssured.given;

public class GlobalTest {

    @Test
    public void global(){

        if( accessToken == null){
            ExtentManager.getTest().warning("User is not authorized");
            return;
        }
        try{
            Response response =
                    given().header("authorization", "Bearer "+ accessToken)
                            .when().get("evaluations/global/");

            String responseData = response.getBody().asPrettyString();
            int statusCode = response.getStatusCode();
            System.out.println(" Api Response: " + responseData);
            ExtentManager.getTest().info(" Api Response: <pre>" + responseData + "</pre>");
            ExtentManager.getTest().info("Status Code: <pre>" + statusCode + "</pre>");

            Assert.assertEquals(statusCode, 200, "Status code did not match!");
            ExtentManager.getTest().pass("Expected code = Actual Code("+statusCode+")");

        } catch (Exception e) {
            ExtentManager.getTest().fail("Error message: "+ e.getMessage());
        }

    }
}
