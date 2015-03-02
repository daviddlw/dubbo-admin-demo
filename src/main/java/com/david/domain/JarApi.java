package com.david.domain;

import java.io.InputStream;

/**
 * Jar API所用实体
 * 
 * @author pc
 * 
 */
public class JarApi
{
	private String serviceName;
	private String version;
	private JarType type;
	private String fileName;
	private InputStream file;

	public JarApi()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public JarApi(String serviceName, String version, JarType type, String fileName, InputStream file)
	{
		super();
		this.serviceName = serviceName;
		this.version = version;
		this.type = type;
		this.fileName = fileName;
		this.file = file;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public JarType getType()
	{
		return type;
	}

	public void setType(JarType type)
	{
		this.type = type;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public InputStream getFile()
	{
		return file;
	}

	public void setFile(InputStream file)
	{
		this.file = file;
	}

	@Override
	public String toString()
	{
		return "JarApi [serviceName=" + serviceName + ", version=" + version + ", type=" + type + ", fileName=" + fileName + "]";
	}

}
