package com.david.web;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class TestController implements Controller
{
	private Logger logger = Logger.getLogger(TestController.class);

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("Returning the test view.");
		Date now = new Date();
		String nowVal = now.toString();
		logger.info("now: " + nowVal);
		// logger.info("request: " + request);
		// logger.info("response: " + response);
		String queryString = request.getQueryString();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String type = request.getParameter("type");

		logger.info(String.format("id: %s, name: %s", id, name));
		logger.info("query string: " + queryString);

		if (type.equalsIgnoreCase("redirect"))
		{
			response.sendRedirect("redirect.do");
		} else if (type.equalsIgnoreCase("forward"))
		{
			RequestDispatcher dispatcher = request.getRequestDispatcher("forward.do");
			dispatcher.forward(request, response);
		}

		return new ModelAndView("test");
	}

}
