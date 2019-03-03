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

/**
 * @author shikshamalhotra 
 * Utility for Json conversion
 */
public class JsonUtils {

	public static Map<String, String> convertJsonStringToMap(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, String> resultMap = new HashMap<>();
		if (jsonString != null) {
			resultMap = new ObjectMapper().readValue(jsonString, new TypeReference<HashMap<String, String>>() {
			});
		}
		return resultMap;
	}

	public static String convertToJsonString(String key, String value) throws JsonProcessingException {
		String response = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put(key, value);
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		return response;
	}

}
