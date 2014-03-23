package com.blogspot.sgdev_blog.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.blogspot.sgdev_blog.exception.RecordNotFoundException;
import com.blogspot.sgdev_blog.service.TwitService;

public class TwitServiceImpl implements TwitService {
	
	Map<String, String> twitMap = new ConcurrentHashMap<String, String>();

	@Override
	public String getTwit(String userId) throws RecordNotFoundException {
		
		String twit = twitMap.get(userId); 
		
		if (twit == null){
			throw new RecordNotFoundException("No twit found for user "+userId);
		}
		
		return twit;
	}

	@Override
	public void insertTwit(String userId, String text) {
		
		if (userId==null || userId.length()==0 || text==null || text.length()==0 || text.length()>MAX_LENGTH){
			throw new RuntimeException("Invalid twit!");
		}
		
		twitMap.put(userId, text);
	}

	@Override
	public Collection<String> getAllTwits() {
		
		return twitMap.values();
	}
}
