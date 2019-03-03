package io.bankbridge.handler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.bankbridge.handler.BanksCacheBased;
import io.bankbridge.model.BankModel;
import spark.Request;
import spark.Response;

/**
 * @author shikshamalhotra
 *
 */
public class BanksCacheBasedTest {

	private CacheManager cacheManager;

	@Before
	public void setUp() {
		cacheManager = CacheManagerBuilder
				.newCacheManagerBuilder().withCache("banks", CacheConfigurationBuilder
						.newCacheConfigurationBuilder(String.class, BankModel.class, ResourcePoolsBuilder.heap(10)))
				.build();
		cacheManager.init();

		Cache<String, BankModel> cache = cacheManager.getCache("banks", String.class, BankModel.class);
		BankModel bankModel=new BankModel();
		bankModel.setAuth("OAUTH");
		bankModel.setBic("1234");
		bankModel.setCountryCode("GB");
		bankModel.setName("Royal Bank of Boredom");
		cache.put("Royal Bank of Boredom", bankModel);

	}

	@Test
	public void testCacheBasedCalls() {
		assertNotNull(cacheManager);
		BanksCacheBased.setCacheManager(cacheManager);
		Request request = null;
		Response response = null;
		String result = BanksCacheBased.handle(request, response);
		String expected="[{\"Royal Bank of Boredom\":{\"bic\":\"1234\",\"name\":\"Royal Bank of Boredom\",\"countryCode\":\"GB\",\"auth\":\"OAUTH\"}}]";
		assertEquals(expected, result);
	}

	@After
	public void shutDown() {
		cacheManager.close();
	}

}
