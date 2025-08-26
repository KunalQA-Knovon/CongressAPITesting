package com.api.utils.Configuration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class testDataReader {
    public static JSONArray extractJsonData(String filePath , String arrayName){
        File file = new File(filePath);
        try{
            FileReader fileReader = new FileReader(file);
            JSONTokener jt = new JSONTokener(fileReader);
            JSONObject jsonObject = new JSONObject(jt);
            return jsonObject.getJSONArray(arrayName);
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
         return new JSONArray();
    }
}
