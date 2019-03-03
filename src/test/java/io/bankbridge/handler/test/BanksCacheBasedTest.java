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
						.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10)))
				.build();
		cacheManager.init();

		Cache<String, String> cache = cacheManager.getCache("banks", String.class, String.class);
		cache.put("1234", "Royal Bank of Boredom");
		cache.put("5678", "Credit Sweets");

	}

	@Test
	public void testCacheBasedCalls() {
		assertNotNull(cacheManager);
		BanksCacheBased.setCacheManager(cacheManager);
		Request request = null;
		Response response = null;
		String result = BanksCacheBased.handle(request, response);
		String expected = "[{\"name\":\"Credit Sweets\",\"id\":\"5678\"},{\"name\":\"Royal Bank of Boredom\",\"id\":\"1234\"}]";
		assertEquals(expected, result);
	}

	@After
	public void shutDown() {
		cacheManager.close();
	}

}
