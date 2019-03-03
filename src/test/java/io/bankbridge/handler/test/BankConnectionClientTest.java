package io.bankbridge.handler.test;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import io.bankbridge.handler.BankConnectionClient;
import io.bankbridge.utils.JsonUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

/**
 * @author shikshamalhotra
 *
 */
public class BankConnectionClientTest {

	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;
	private BankConnectionClient bankServiceClient;

	@Before
	public void setUp() {
		restTemplate = new RestTemplate();
		bankServiceClient = new BankConnectionClient(restTemplate);
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void testGetMessage() throws IOException {
        String success="{\"bic\":\"1234\",\"name\":\"Royal Bank of Boredom\",\"countryCode\":\"GB\",\"auth\":\"OAUTH\"}";
		mockServer.expect(requestTo("http://google.com")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(success, MediaType.TEXT_PLAIN));
		String result = bankServiceClient.getBankDetails("Royal Bank of Boredom", "http://google.com");
		String expected="{\n" + 
				"  \"Royal Bank of Boredom\" : {\n" + 
				"    \"bic\" : \"1234\",\n" + 
				"    \"name\" : \"Royal Bank of Boredom\",\n" + 
				"    \"countryCode\" : \"GB\",\n" + 
				"    \"auth\" : \"OAUTH\"\n" + 
				"  }\n" + 
				"}";
		mockServer.verify();
		assertEquals(expected, result);
	}

	@Test
	public void testGetMessage_500() throws IOException {
		mockServer.expect(requestTo("http://google.com")).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());
		String result = bankServiceClient.getBankDetails("Royal Bank of Boredom", "http://google.com");
		String expected = JsonUtils.convertErrorToJsonString("Royal Bank of Boredom");
		mockServer.verify();
		assertEquals(expected, result);
	}

	@Test
	public void testGetMessage_404() throws IOException {
		mockServer.expect(requestTo("http://google.com")).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		String result = bankServiceClient.getBankDetails("Royal Bank of Boredom", "http://google.com");
		mockServer.verify();
		String expected = JsonUtils.convertErrorToJsonString("Royal Bank of Boredom");
		assertEquals(expected, result);
	}
}
