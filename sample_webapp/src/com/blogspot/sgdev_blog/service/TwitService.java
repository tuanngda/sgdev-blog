package com.blogspot.sgdev_blog.service;

import java.util.Collection;

import com.blogspot.sgdev_blog.exception.RecordNotFoundException;

public interface TwitService {
	
	public static int MAX_LENGTH = 160;
	
	public String getTwit(String userId) throws RecordNotFoundException;
	
	public void insertTwit(String userId, String text);
	
	public Collection<String> getAllTwits();

}
