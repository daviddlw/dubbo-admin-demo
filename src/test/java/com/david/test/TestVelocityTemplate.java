package com.david.test;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Before;
import org.junit.Test;

public class TestVelocityTemplate
{

	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testService()
	{
		String serviceName = "com.david.DavidService";
		serviceName = serviceName.replaceAll("\\.", "-");
		System.out.println(serviceName);
	}

	@Test
	public void testGenerateVelocityTemplate()
	{
		VelocityEngine ve = new VelocityEngine();		
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());

		ve.init();

		Template t = ve.getTemplate("HelloVelcity.vm");
		VelocityContext ctx = new VelocityContext();

		ctx.put("name", "velocity");
		ctx.put("date", (new Date()).toString());

		List<String> temp = new ArrayList<String>();
		temp.add("1");
		temp.add("2");
		ctx.put("list", temp);

		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);

		System.out.println(sw.toString());

	}

}
