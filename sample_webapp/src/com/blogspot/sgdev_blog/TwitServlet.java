package com.blogspot.sgdev_blog;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blogspot.sgdev_blog.exception.RecordNotFoundException;
import com.blogspot.sgdev_blog.service.TwitService;
import com.blogspot.sgdev_blog.service.impl.TwitServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class TwitServlet
 */
public class TwitServlet extends HttpServlet {
	
	private TwitService twitService;
	
	private Gson gson = new Gson();
       
    public TwitServlet() {
        super();
        twitService = new TwitServiceImpl();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseBody = "";
		String path = (request.getPathInfo()==null) ? "" : request.getPathInfo().replaceFirst("/", "");
		
		if (path.length()==0){
			Collection<String> twits = twitService.getAllTwits();
			responseBody = gson.toJson(twits);
		}
		else {
			try {
				String userId = path;
				responseBody = twitService.getTwit(userId);
			} catch (RecordNotFoundException e) {
				responseBody = e.getMessage();
				response.setStatus(404);
			}
		}
		
		response.getWriter().write(responseBody);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseBody = "";
		String path = (request.getPathInfo()==null) ? "" : request.getPathInfo().replaceFirst("/", "");
		
		if (path.length()==0){
			responseBody = "Invalid usage";
			response.setStatus(401);
		}
		else {
			String userId = path;
			String twit = convertStreamToString(request.getInputStream());
			
			twitService.insertTwit(userId, twit);
			responseBody = "Twit inserted";
		}
		
		response.getWriter().write(responseBody);
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
