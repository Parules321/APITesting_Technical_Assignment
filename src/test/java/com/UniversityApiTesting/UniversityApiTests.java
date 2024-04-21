package com.UniversityApiTesting;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.UniversityApiTesting.Utilities.JsonReader;
import com.UniversityApiTesting.Utilities.PropertyUtil;
import com.fasterxml.jackson.databind.JsonNode;

import io.restassured.module.jsv.JsonSchemaValidator;

public class UniversityApiTests {

	String filePath = null;
	String apiKey = PropertyUtil.getProperty("src/test/resources/config.properties", "api_key");
	FileReader fileReader = null;
	JsonNode jsonNode = null;

	@BeforeClass
	public void setup() {
		// Set the base url
		baseURI = PropertyUtil.getProperty("src/test/resources/config.properties", "baseUrl");
	}

	@Test
	public void testAddNewUniversity() {
		filePath = "src/test/resources/schemas/universitySchema.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("university").header("api_key", apiKey).header("Content-Type", "application/json")
				.body(jsonNode).log().all().when().post().then().statusCode(201).log().all();

	}

	@Test
	public void testGetUniversityDetailsUsingName() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);
		try {
			fileReader = new FileReader(new File("src/test/resources/schemas/universitySchema.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		given().basePath("university").header("api_key", apiKey)
				.queryParam("universityName", jsonNode.get("universityName").asText()).log().all().when().get().then()
				.statusCode(200).body("UniversityName", equalTo(jsonNode.get("unencodedUniversityName").asText()))
				.body("UniversityFounded", equalTo(jsonNode.get("universityFounded").asInt()))
				.body("UniversityLocation", equalTo(jsonNode.get("universityLocation").asText()))
				.body(JsonSchemaValidator.matchesJsonSchema(fileReader)).log().all();

	}

	@Test
	public void testGetUniversityDetailsWithUnecodedParam() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);
		try {
			fileReader = new FileReader(new File("src/test/resources/schemas/universitySchema.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		given().basePath("university").header("api_key", apiKey)
				.queryParam("universityName", jsonNode.get("unencodedUniversityName").asText()).log().all().when().get()
				.then().statusCode(200)
				.body("UniversityName", equalTo(jsonNode.get("unencodedUniversityName").asText()))
				.body("UniversityFounded", equalTo(jsonNode.get("universityFounded").asInt()))
				.body("UniversityLocation", equalTo(jsonNode.get("universityLocation").asText()))
				.body(JsonSchemaValidator.matchesJsonSchema(fileReader)).log().all();

	}

	@Test
	public void testGetUniversityDetailsUsingId() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);
		try {
			fileReader = new FileReader(new File("src/test/resources/schemas/universitySchema.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		given().basePath("university{UniversityID}").pathParam("UniversityID", jsonNode.get("universityID").asInt())
				.header("api_key", apiKey).log().all().when().get().then().statusCode(200)
				.body("UniversityName", equalTo(jsonNode.get("unencodedUniversityName").asText()))
				.body("UniversityFounded", equalTo(jsonNode.get("universityFounded").asInt()))
				.body("UniversityLocation", equalTo(jsonNode.get("universityLocation").asText()))
				.body(JsonSchemaValidator.matchesJsonSchema(fileReader)).log().all();

	}

	@Test
	public void testDeleteUniversity() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("university").header("api_key", apiKey)
				.queryParam("universityName", jsonNode.get("universityName").asText()).log().all().when().delete()
				.then().statusCode(204).log().all();
	}

	@Test
	public void testDeleteUniversityWithUnencodedParam() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("university").header("api_key", apiKey)
				.queryParam("universityName", jsonNode.get("unencodedUniversityName").asText()).log().all().when()
				.delete().then().statusCode(204).log().all();
	}

	@Test
	public void testUpdateUniversityInfo() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("university{UniversityID}").pathParam("UniversityID", jsonNode.get("universityID").asInt())
				.header("api_key", apiKey).log().all().when().put().then().statusCode(201).log().all();

	}

	@Test
	public void testGetUniversities() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);
		try {
			fileReader = new FileReader(new File("src/test/resources/schemas/universitiesSchema.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		given().basePath("universities").header("api_key", apiKey).log().all().when().get().then().statusCode(200)
				.body("[0].UniversityName", equalTo(jsonNode.get("unencodedUniversityName").asText()))
				.body("[0].UniversityFounded", equalTo(jsonNode.get("universityFounded").asInt()))
				.body("[0].UniversityLocation", equalTo(jsonNode.get("universityLocation").asText()))
				.body(JsonSchemaValidator.matchesJsonSchema(fileReader)).log().all();
	}

	@Test
	public void verifyMissingAuth401() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("university").queryParam("universityName", jsonNode.get("universityName").asText()).log().all()
				.when().get().then().statusCode(401).log().all();

	}

	@Test
	public void verifyInvalidAuth401() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("university{UniversityID}").pathParam("UniversityID", jsonNode.get("universityID").asInt())
				.header("apiKey", generateIncorrectToken()).log().all().when().put().then().statusCode(401).log().all();

	}

	@Test
	public void verifyUnprocessableEntity422ForInvalidPayload() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);
		given().basePath("university").header("api_key", apiKey).header("Content-Type", "application/json")
				.body(jsonNode.get("universityID").asInt()).log().all().when().post().then().statusCode(422).log()
				.all();

	}

	@Test
	public void verifyUnprocessableEntity422ForMissingParam() {
		given().basePath("university").header("api_key", apiKey).log().all().when().put().then().statusCode(422).log()
				.all();

	}

	@Test
	public void verify404ForIncorrectURI() {
		filePath = "src/test/resources/data/requestsData.json";
		jsonNode = JsonReader.readJson(filePath);

		given().basePath("http://").header("api_key", apiKey)
				.queryParam("universityName", jsonNode.get("universityName").asText()).log().all().when().delete()
				.then().statusCode(404).log().all();
	}

	public String generateIncorrectToken() {
		Random random = new Random();
		String[] incorrectTokens = { "J123YHGFD", "DER4567", "SA987654", "MI09876543", "1234SOP", "WIL7653" };
		String incorrectToken = incorrectTokens[random.nextInt(incorrectTokens.length)];
		return incorrectToken;
	}

}
