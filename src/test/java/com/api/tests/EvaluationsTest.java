package com.api.tests;

import com.report.config.ExtentManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.api.tests.LoginScenarioTest.accessToken;
import static io.restassured.RestAssured.given;

public class EvaluationsTest {

    @Test
    public void Evaluations(){

        if( accessToken == null){
            ExtentManager.getTest().warning("User is not authorized");
            return;
        }
        try{
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization","Bearer "+accessToken);
            headers.put("congress", "EHA");
            headers.put("company", "Regeneron");

            Response response =
                    given().headers(headers)
                            .when().get("evaluations/");

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
