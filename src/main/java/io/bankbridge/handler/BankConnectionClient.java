package io.bankbridge.handler;

import java.io.IOException;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.model.BankModel;
import io.bankbridge.utils.JsonUtils;

/**
 * @author shikshamalhotra
 *
 */
public class BankConnectionClient {

	private RestTemplate restTemplate;

	public BankConnectionClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * @param bankName
	 * @param bankUrl
	 * @return Banks details in form of String (Json)
	 * @throws IOException 
	 */
	public String getBankDetails(String bankName, String bankUrl) throws IOException {
		String response = null;
		try {
			String bankDetails = restTemplate.getForObject(bankUrl, String.class);
			BankModel bankModel = new ObjectMapper().readValue(bankDetails, BankModel.class);
			response=JsonUtils.convertBankObjToJsonString(bankModel,bankName);
		} catch (HttpStatusCodeException e) {
			response = handleExceptionForABank(bankName);
		} catch (RuntimeException ex) {
			response = handleExceptionForABank(bankName);
		}
		return response;

	}

	/**
	 * @param bank
	 * @return handle
	 * @throws JsonProcessingException handles exception if geBankDetails is unable
	 *                                 to fetch details for a bank.
	 */
	public String handleExceptionForABank(String bankName) throws JsonProcessingException {
		return JsonUtils.convertErrorToJsonString(bankName);
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
