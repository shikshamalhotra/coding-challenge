package io.bankbridge.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.bankbridge.model.BankModel;

/**
 * @author shikshamalhotra 
 * Utility for Json conversion
 */
public class JsonUtils {

	public static Map<String, BankModel> convertJsonStringToBankMap(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, BankModel> resultMap = new HashMap<>();
		if (jsonString != null) {
			resultMap = new ObjectMapper().readValue(jsonString, new TypeReference<HashMap<String, BankModel>>() {
			});
		}
		return resultMap;
	}

	public static String convertBankObjToJsonString(BankModel bankModel,String bankName) throws JsonProcessingException
	{
		String response = null;
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode rootNode = mapper.createObjectNode();

		JsonNode childNode1 = mapper.createObjectNode();
		((ObjectNode) childNode1).put("bic", bankModel.getBic());
		((ObjectNode) childNode1).put("name", bankModel.getName());
		((ObjectNode) childNode1).put("countryCode", bankModel.getCountryCode());
		((ObjectNode) childNode1).put("auth", bankModel.getAuth());

		((ObjectNode) rootNode).set(bankName, childNode1);
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		return response;
	}
	
	public static String convertErrorToJsonString(String key) throws JsonProcessingException {
		String response = null;
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode rootNode = mapper.createObjectNode();

		JsonNode childNode1 = mapper.createObjectNode();
		((ObjectNode) childNode1).put("bic", "Unable to fetch Details");
		((ObjectNode) childNode1).put("name", "Unable to fetch Details");
		((ObjectNode) childNode1).put("countryCode", "Unable to fetch Details");
		((ObjectNode) childNode1).put("auth", "Unable to fetch Details");

		((ObjectNode) rootNode).set(key, childNode1);
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		return response;
	}

}
