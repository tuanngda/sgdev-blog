package com.sgdevblog.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class GlobalControllerAdvice {

	private static Logger logger = Logger.getLogger(GlobalControllerAdvice.class);

	@ExceptionHandler(Throwable.class)
	public void handleError(HttpServletRequest req, HttpServletResponse response, Throwable th) {
		if(th.toString() != null) {
			logger.error("Request: " + req.getRequestURL() + " raised unexpected exception - " + th.toString(), th);
		}
		HttpStatus httpResponseCode = HttpStatus.INTERNAL_SERVER_ERROR;
		String errorMessage = "";

		if (th instanceof HttpStatusCodeException){
			HttpStatusCodeException exception = (HttpStatusCodeException) th;
			httpResponseCode = exception.getStatusCode();
			errorMessage = exception.getStatusText();
			logger.error("Request: " + req.getRequestURL() + " raised unexpected exception - " + th.toString(), th);
		}
		response.setContentType("application/json; charset=utf-8");
		try {
			response.setStatus(httpResponseCode.value());
			response.getWriter().write(errorMessage);
			response.getWriter().flush();
		} catch (IOException e) {
		}
	}

}
