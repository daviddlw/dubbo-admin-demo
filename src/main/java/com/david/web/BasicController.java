package com.david.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.Controller;

public abstract class BasicController implements Controller
{
	/**
	 * 获取服务名称
	 * 
	 * @param request
	 *            Servlet请求
	 * @return QueryString中的服务名称
	 */
	protected String getServiceName(HttpServletRequest request)
	{
		String serviceName = request.getParameter("serviceName");
		if (serviceName == null)
		{
			serviceName = "com.david.TestService";
		}

		return serviceName;
	}
}
