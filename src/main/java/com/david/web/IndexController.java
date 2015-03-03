package com.david.web;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class IndexController implements Controller
{
	private final Logger logger = Logger.getLogger(IndexController.class);

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("Returning the index view.");
//		testGenerateVelocityTemplate();
		
		return new ModelAndView("hello");
	}
	
	
	public void testGenerateVelocityTemplate()
	{
		VelocityEngine ve = new VelocityEngine();		
		ve.init();

		Template t = ve.getTemplate("src/main/webapp/WEB-INF/layout/hello-velocity.vm");
		VelocityContext ctx = new VelocityContext();

		ctx.put("name", "velocity");
		ctx.put("date", (new Date()).toString());

		List<String> temp = new ArrayList<String>();
		temp.add("1");
		temp.add("2");
		ctx.put("list", temp);

		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);

		logger.info(sw.toString());

	}

}
