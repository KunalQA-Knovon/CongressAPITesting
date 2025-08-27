package com.api.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class ResponseModels {

    public static Response loginRequest(String jsonObject, String endPoint){
        Response response = given().contentType(ContentType.JSON)
                .body(jsonObject)
                .when()
                .post(endPoint);
        return response;
    }
}
