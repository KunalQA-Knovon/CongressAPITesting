package com.api.tests;

import com.api.utils.Configuration.testDataReader;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class SubmitEvaluationTest extends LoginScenarioTest {
    @Test
    public static void Submit_Evaluation(){
        String fileName = "src/test/resources/evaluationData.json";
        String arrayName = "evaluationData";
        JSONArray jsonObject = testDataReader.extractJsonData(fileName, arrayName);
        for(int i=0; i<jsonObject.length(); i++) {
            JSONObject data = jsonObject.getJSONObject(i);
            String scenario = data.getString("scenario");
            int expectedStatus = data.getInt("expectedStatus");

            System.out.println("\n==============================");
            System.out.println("Running Test: " + scenario);
            System.out.println("==============================");

            JSONObject jsonData = new JSONObject();

            String randomText = UUID.randomUUID().toString().substring(0, 5);

            if (scenario.equals("Empty Congress Name")) {
                jsonData.put("congress_name", JSONObject.NULL);
            } else if (!scenario.equals("Missing Congress Name")) {
                String congressName = data.getString("congress_name").trim();
                jsonData.put("congress_name", congressName + " " + randomText);
            }


            if (scenario.equals("Empty Company Name")) {
                jsonData.put("company_name", JSONObject.NULL);
            } else if (!scenario.equals("Missing Company Name")) {
                String companyName = data.getString("company_name").trim();
                jsonData.put("company_name", companyName + " " + randomText);
            }

            if (scenario.equals("Invalid Date Format")) {
                String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                jsonData.put("date", formattedDate);

            }else if (scenario.equals("Empty Date Field")) {
                jsonData.put("date", JSONObject.NULL);
            }else if (!scenario.equals("Missing Date Field")) {
                String safeDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE);
                jsonData.put("date", safeDate);
            }

            LocalTime now = LocalTime.now();
            DateTimeFormatter correctFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String correctFormattedTime = now.format(correctFormatter);

            DateTimeFormatter wrongFormatter = DateTimeFormatter.ofPattern("h:mm a");
            String wrongFormattedTime = now.format(wrongFormatter);

            if (scenario.equals("Invalid Time Format")) {
                jsonData.put("time", wrongFormattedTime);

            }else if (scenario.equals("Empty Time Field")) {
                jsonData.put("time", JSONObject.NULL);
            } else if (!scenario.equals("Missing Time Field")) {
                // only add if NOT missing
                jsonData.put("time", correctFormattedTime);
            }


            if (data.has("welcoming_score")) {
                try {
                    jsonData.put("welcoming_score", data.getInt("welcoming_score"));
                } catch (Exception e) {
                    jsonData.put("welcoming_score", data.getString("welcoming_score")); // keep raw string
                }
            }
            if(data.has("greeting_score")){
                jsonData.put("greeting_score" , data.getInt("greeting_score"));
            }
            if (data.has("welcoming_evidence")) {
                jsonData.put("welcoming_evidence", data.getString("welcoming_evidence"));
            }

            if(data.has("materials_score")){
                jsonData.put("materials_score" , data.getInt("materials_score"));
            }
            if(data.has("stand_use_score")){
                jsonData.put("stand_use_score" , data.getInt("stand_use_score"));
            }
            if(data.has("wiffm_score")){
                jsonData.put("wiffm_score" , data.getInt("wiffm_score"));
            }
            if(data.has("dialogue_score")){
                jsonData.put("dialogue_score" , data.getInt("dialogue_score"));
            }
            if(data.has("appearance_score")){
                jsonData.put("appearance_score" , data.getInt("appearance_score"));
            }
            if (scenario.equals("Extra Field Provided")){
                jsonData.put("extra_field", data.getString("extra_field"));
            }

            System.out.println("Request Body: "+ jsonData);
            if (accessToken != null) {
                System.out.println("accessToken: " + accessToken);
                try {
                    given()
                            .contentType(ContentType.JSON)
                            .header("Authorization", "Bearer " + accessToken)
                            .body(jsonData.toString())
                            .when()
                            .post("submit-evaluation/");
                } catch (AssertionError e) {
                    System.out.println("❌ Test Failed: " + scenario);
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("⚠️ No accessToken available. Run login test first!");
            }
        }
    }
}