package io.bankbridge.handler.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.HashMap;
import io.bankbridge.handler.BankConnectionClient;
import io.bankbridge.handler.BanksRemoteCalls;
import spark.Request;
import spark.Response;

/**
 * @author shikshamalhotra
 *
 */
public class BankRemoteCallsTest {

	@Mock
	private BankConnectionClient bankConnectionClient;// =mock(BankConnectionClient.class);
	private String rbbBankDetails;

	@InjectMocks
	private BanksRemoteCalls banksRemoteCalls;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		rbbBankDetails = "{\n" + "\"bic\":\"1234\",\n" + "\"countryCode\":\"GB\",\n" + "\"auth\":\"OAUTH\"\n" + "}";
	}

	@Test
	public void testHandleRemoteCalls() throws IOException {
		bankConnectionClient.setRestTemplate(new RestTemplate());
		assertNotNull(bankConnectionClient);
		String bankName = "Royal Bank of Boredom";
		String bankUrl = "http://localhost:1234/rbb";
		// String bankUrl="http://google.com";
		HashMap<String, String> map = new HashMap<>();
		map.put(bankName, bankUrl);
		when(bankConnectionClient.getBankDetails(bankName, bankUrl)).thenReturn(rbbBankDetails);
		String expected = "[{\"auth\":\"OAUTH\",\"countryCode\":\"GB\",\"bic\":\"1234\"}]";
		Request request = null;
		Response response = null;
		BanksRemoteCalls.setConfig(map);
		BanksRemoteCalls.setBankServiceClient(bankConnectionClient);
		String result = BanksRemoteCalls.handle(request, response);
		assertEquals(expected, result);
	}

}
