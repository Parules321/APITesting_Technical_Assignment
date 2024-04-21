package com.UniversityApiTesting.Utilities;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class JsonReader {

	public static JsonNode readJson(String filePath) {
		
		JsonNode  jsonNode =null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			jsonNode = objectMapper.readTree(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
				
		return jsonNode;
	}
}