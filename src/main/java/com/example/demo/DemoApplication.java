package com.example.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class DemoApplication implements RequestHandler<SQSEvent, Void>{
	Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().enableComplexMapKeySerialization()
            .serializeNulls().setVersion(1.0).create();
	public static Object getDetails(Map<String,Object> jsonData) throws IOException {
		Gson gson = new Gson(); 
		String json = gson.toJson(jsonData);
		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, json);
		Request request = new Request.Builder()
				.url("https://api.gridlines.io/pan-api/verify")
				.method("POST", body)
				.addHeader("X-Auth-Type", "API-Key")
				.addHeader("X-API-Key", "c7v9msDAUKaHJMZ3s4acJwzl3KA8i2mq")
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		if(response!=null){
			System.out.println(response.body().string());
		}
		return null;

	}

//	public static void main(String[] args) throws IOException {
//		Map<String,Object> map=new HashMap<>();
//		map.put("pan_id", "BVJPG6268H");
//		map.put("name", "Neeraj Gaur");
//		map.put("date_of_birth", "1989-12-25");
//		map.put("consent", "Y");
//		getDetails(map);
//	}
	

	@Override
	public Void handleRequest(SQSEvent event, Context context) {
		for(SQSEvent.SQSMessage msg : event.getRecords()){
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            System.out.println(msg.getBody());
            Map<String, Object> myMap = gson.fromJson(msg.getBody(), type);
		    try {
				getDetails(myMap);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
}
