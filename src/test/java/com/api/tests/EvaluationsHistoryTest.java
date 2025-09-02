package com.api.tests;

import com.report.config.ExtentManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.api.tests.LoginScenarioTest.accessToken;
import static io.restassured.RestAssured.given;
public class EvaluationsHistoryTest extends BaseTest{

    @Test
    public void History() {
        if (accessToken == null) {
            ExtentManager.getTest().warning("User is not authorised to access this data");
            return;
        }

        try {
            Response response = given()
                    .header("Authorization", "Bearer " + accessToken)
                    .when()
                    .get("evaluations/history");

            int statusCode = response.getStatusCode();
            String responseData = response.getBody().asPrettyString();

            ExtentManager.getTest().info("Status Code: <pre>" + statusCode + "</pre>");
            ExtentManager.getTest().info(
                    "<details>" +
                            "<summary><b>Show Response Body</b></summary>" +
                            "<pre>" + responseData + "</pre>" +
                            "</details>"
            );
            Assert.assertEquals(statusCode, 200, "Unexpected Status Code");
            ExtentManager.getTest().pass("History API returned status code " + statusCode);

        } catch (Exception e) {
            ExtentManager.getTest().fail("API call failed with error: " + e.getMessage());
            throw e;
        }
    }

}
