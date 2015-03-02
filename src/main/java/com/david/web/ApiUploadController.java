package com.david.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.david.domain.JarApi;

public class ApiUploadController extends BasicController
{
	private final Logger logger = Logger.getLogger(ApiUploadController.class);

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("Returning the apiUpload view.");
		
		String serviceName = getServiceName(request);
		
		JarApi jarApi = new JarApi();
		jarApi.setServiceName(serviceName);

		return new ModelAndView("apiUpload", "model", jarApi);
	}
}
