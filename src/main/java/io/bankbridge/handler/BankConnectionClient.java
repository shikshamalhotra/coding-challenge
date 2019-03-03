package io.bankbridge.handler;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	 * @throws JsonProcessingException Make connection with different banks using
	 *                                 Rest Client and returns bank details in form
	 *                                 of Json String
	 */
	public String getBankDetails(String bankName, String bankUrl) throws JsonProcessingException {
		String response = null;
		try {
			response = restTemplate.getForObject(bankUrl, String.class);
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
	public String handleExceptionForABank(String bank) throws JsonProcessingException {
		return JsonUtils.convertToJsonString(bank, "Unable to fetch details");
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
