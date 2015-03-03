package com.david.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.Controller;

import com.david.common.CommonUtils;
import com.david.domain.FtpUtils;

public abstract class BasicController implements Controller
{
	protected static final String TEST_SERVICE = "com.david.TestService";
	protected static final String FTP_SERVER = CommonUtils.FTP_SERVER;
	protected static final int PORT = CommonUtils.PORT;
	protected static final String USERNAME = CommonUtils.USERNAME;
	protected static final String PASSWORD = CommonUtils.PASSWORD;
	protected static final String SOURCELIB = "sourcelib";
	protected static final String BINLIB = "binlib";
	protected static final String PHPLIB = "phplib";
	protected static final String FTP_DOWNLOAD_PATH_PREFIX = "ftp://192.168.8.116:48790";
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
