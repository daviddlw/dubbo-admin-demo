package com.david.common;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class CommonUtils
{
	private static Logger logger = Logger.getLogger(CommonUtils.class);
	public static String FTP_SERVER = "192.168.8.116";
	public static int PORT = 48790;
	public static String USERNAME = "daviddai";
	public static String PASSWORD = "123456";

	static
	{
		try
		{
			Configuration propConfig = new PropertiesConfiguration("config.properties");
			FTP_SERVER = propConfig.getString("ftpserver", FTP_SERVER);
			PORT = propConfig.getInt("port", PORT);
			USERNAME = propConfig.getString("username", USERNAME);
			PASSWORD = propConfig.getString("password", PASSWORD);

		} catch (ConfigurationException e)
		{
			logger.error(e.getMessage(), e);
		}
	}
}
