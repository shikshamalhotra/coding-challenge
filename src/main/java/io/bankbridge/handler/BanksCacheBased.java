package io.bankbridge.handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import spark.Request;
import spark.Response;

public class BanksCacheBased {


	public static CacheManager cacheManager;
	
	public static void setCacheManager(CacheManager cacheManager) {
		BanksCacheBased.cacheManager = cacheManager;
	}

	public static void init() throws Exception {
		cacheManager = CacheManagerBuilder
				.newCacheManagerBuilder().withCache("banks", CacheConfigurationBuilder
						.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10)))
				.build();
		cacheManager.init();
		Cache<String, String> cache = cacheManager.getCache("banks", String.class, String.class);
		try {
			BankModelList models = new ObjectMapper().readValue(
					Thread.currentThread().getContextClassLoader().getResource("banks-v1.json"), BankModelList.class);
			for (BankModel model : models.banks) {
				cache.put(model.bic, model.name);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static String handle(Request request, Response response) {

		List<Map<String,String>> result = new ArrayList<>();
		cacheManager.getCache("banks", String.class, String.class).forEach(entry -> {
			Map<String,String> map = new HashMap<>();
			map.put("id", entry.getKey());
			map.put("name", entry.getValue());
			result.add(map);
		});
		try {
			String resultAsString = new ObjectMapper().writeValueAsString(result);
			return resultAsString;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while processing request");
		}

	}

}
