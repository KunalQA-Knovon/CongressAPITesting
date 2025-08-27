package com.api.tests;

import com.api.utils.Configuration.testDataReader;
import com.api.utils.ResponseModels;
import com.report.config.ExtentManager;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginScenarioTest extends BaseTest {
    protected static String accessToken;
    SoftAssert softAssert = new SoftAssert();

    @Test(priority = 0)
    public void LoginTests() throws Exception {
        String filePath = "src/test/resources/loginData.json";
        String arrayName = "logins";
        JSONArray tests = testDataReader.extractJsonData(filePath, arrayName);
        String requestBody;

        for (int i = 0; i < tests.length(); i++) {
            JSONObject testCase = tests.getJSONObject(i);

            String scenario = testCase.getString("scenario");
            int expectedStatus = testCase.getInt("expectedStatus");

            System.out.println("\n==============================");
            System.out.println("Running Test: " + scenario);
            System.out.println("==============================");
            ExtentManager.setTest(
                    ExtentManager.getReporter().createTest("Scenario: " + scenario));


            // Handle raw or malformed test cases
            if (testCase.has("rawRequest")) {
                requestBody = testCase.getString("rawRequest");
            } else if (scenario.equals("Empty Request Body")) {
                requestBody = "{}";
            } else {
                JSONObject body = new JSONObject();
                if (testCase.has("email")) {
                    body.put("email", testCase.getString("email"));
                }
                if (testCase.has("password")) {
                    body.put("password", testCase.getString("password"));
                }
                requestBody = body.toString();
            }
            System.out.println("Request Body: " + requestBody);
            ExtentManager.getTest().info("Request Body: <pre>" + requestBody + "</pre>");
            try {
                Response response = ResponseModels.loginRequest(requestBody, "login/");
                int actualStatusCode = response.getStatusCode();
                String responseBody = response.getBody().asPrettyString();

                ExtentManager.getTest().info("Response Body: <pre>" + responseBody + "</pre>");
                ExtentManager.getTest().info("Actual Status: <pre>" + actualStatusCode+"</pre>");
                ExtentManager.getTest().info("Expected Status: <pre>" + expectedStatus+"</pre>");

                if (actualStatusCode == 200) {
                    response.then()
                            .body("access_token", notNullValue())
                            .body("refresh_token", notNullValue())
                            .body("is_active", equalTo(true));

                    accessToken = response.path("access_token");
                    ExtentManager.getTest().pass("Access Token: <pre>" +"Bearer "+ accessToken + "</pre>");
                    System.out.println("Access Token saved: " + accessToken);
                }
                if (actualStatusCode == expectedStatus) {
                    ExtentManager.getTest().pass("Test Passed: Expected = Actual (" + expectedStatus + ")");
                } else {
                    ExtentManager.getTest().fail("Test Failed: Expected " + expectedStatus + " but got " + actualStatusCode);
                }
                softAssert.assertEquals(actualStatusCode, expectedStatus, "Scenario Failed: " + scenario);

            } catch (Exception ex) {
                ExtentManager.getTest().fail("Exception: " + ex.getMessage());
                // mark failure but continue
                softAssert.fail("Scenario " + scenario + " threw exception: " + ex.getMessage());
            }
        }
        softAssert.assertAll();
    }
}
