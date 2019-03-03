package io.bankbridge.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.utils.JsonUtils;
import spark.Request;
import spark.Response;

public class BanksRemoteCalls {

	private static Map<String, String> config;
	private static BankConnectionClient bankConnectionClient;

	public static void setConfig(Map<String, String> config) {
		BanksRemoteCalls.config = config;
	}

	public static void setBankServiceClient(BankConnectionClient bankServiceClient) {
		BanksRemoteCalls.bankConnectionClient = bankServiceClient;
	}

	public static void init() throws Exception {
		config = new ObjectMapper().readValue(
				Thread.currentThread().getContextClassLoader().getResource("banks-v2.json"),
				new TypeReference<Map<String, String>>() {
				});

		bankConnectionClient = new BankConnectionClient(new RestTemplate());
	}

	/**
	 * Connects to every bank with help of bankConnectionClient and fetches bank
	 * details and returns List of Banks in the form of Json String
	 */
	public static String handle(Request request, Response response) {
		List<Map<String, String>> result = new ArrayList<>();
		config.entrySet().stream().forEach(entry -> {
			try {
				String bankDetails = bankConnectionClient.getBankDetails(entry.getKey(), entry.getValue());
				result.add(JsonUtils.convertJsonStringToMap(bankDetails));

			} catch (IOException e) {
				throw new RuntimeException("Error while processing request");
			}
		});

		try {
			return new ObjectMapper().writeValueAsString(result);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while processing request");
		}
	}

}
