package com.api.tests;

import com.report.config.ExtentManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.api.tests.LoginScenarioTest.accessToken;
import static io.restassured.RestAssured.given;

public class ByHeaderTest {

    @Test
    public void by_header(){

        if(accessToken == null){
            ExtentManager.getTest().warning("User is not authorised to access this data");
            return;
        }

        try{
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization","Bearer "+accessToken);
            headers.put("congress", "EHA");
            headers.put("company", "Regeneron");

            Response response = given()
                            .headers(headers)
                            .when()
                            .get("evaluations/by-header/");
            int statusCode = response.getStatusCode();
            String responseBody = response.getBody().asPrettyString();
            System.out.println(responseBody);
            ExtentManager.getTest().info("Response Data: <pre>" +responseBody+ "</pre>");
            ExtentManager.getTest().info("Status Code: <pre>"+ statusCode +"</pre>");

            Assert.assertEquals(statusCode , 200);
            ExtentManager.getTest().pass("Expected Code = Actual Code ("+statusCode+")");
        }catch (Exception ex){
            ExtentManager.getTest().fail("Error Message: "+ex.getMessage());
        }
    }
}
