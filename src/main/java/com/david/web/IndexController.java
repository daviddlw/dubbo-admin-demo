package com.david.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class IndexController implements Controller
{
	private final Logger logger = Logger.getLogger(IndexController.class);

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("Returning the index view.");
		Date now = new Date();
		String nowVal = now.toString();
		logger.info("now: " + nowVal);
		logger.info("request: "+ request);
		logger.info("response: "+ response);
		return new ModelAndView("hello");
	}

}
