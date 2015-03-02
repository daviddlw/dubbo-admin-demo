package com.david.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.Controller;

import com.david.domain.FtpUtils;

public abstract class BasicController implements Controller
{
	protected static final String TEST_SERVICE = "com.david.TestService";
	protected static String FTP_SERVER = "192.168.8.116";
	protected static int PORT = 48790;
	protected static String USERNAME = "daviddai";
	protected static String PASSWORD = "123456";
	protected static String SOURCELIB = "sourcelib";
	protected static String BINLIB = "binlib";
	protected static String FTP_DOWNLOAD_PATH_PREFIX = "ftp://192.168.8.116:48790";
	protected FtpUtils ftpUtils = new FtpUtils();

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
			serviceName = TEST_SERVICE;
		}

		return serviceName;
	}
}
