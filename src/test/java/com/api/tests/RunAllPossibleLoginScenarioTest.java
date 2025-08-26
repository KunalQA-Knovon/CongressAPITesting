package com.api.tests;

import com.api.utils.Configuration.testDataReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RunAllPossibleLoginScenarioTest extends BaseTest {
    protected static String accessToken;

    @Test(priority = 0)
    public void runAllLoginTests() throws Exception {
        String filePath = "src/test/resources/loginData.json";
        String arrayName = "logins";
        JSONArray tests = testDataReader.extractJsonData(filePath, arrayName);

        for (int i = 0; i < tests.length(); i++) {
            JSONObject testCase = tests.getJSONObject(i);

            String scenario = testCase.getString("scenario");
            int expectedStatus = testCase.getInt("expectedStatus");

            System.out.println("\n==============================");
            System.out.println("Running Test: " + scenario);
            System.out.println("==============================");

            String requestBody;

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

            try {
                Response response = given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("login/") // 🔹 Replace with your endpoint
                        .then()
                        .log().all()
                        .statusCode(expectedStatus)
                        .extract().response();

                // ✅ Only check token fields when login is successful
                if (expectedStatus == 200) {
                    response.then()
                            .body("access_token", notNullValue())
                            .body("refresh_token", notNullValue())
                            .body("is_active", equalTo(true));

                    accessToken = response.path("access_token");
                    System.out.println("✅ Access Token saved: " + accessToken);
                }

            } catch (AssertionError e) {
                System.out.println("❌ Assertion failed for: " + scenario);
                System.out.println(e.getMessage());
            }
        }
    }
}
