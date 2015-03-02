package com.david.domain;

public class FtpFile
{
	private String serviceName;
	private String fileName;
	private String ftpFilepath;

	public FtpFile()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public FtpFile(String serviceName, String fileName, String ftpFilepath)
	{
		super();
		this.serviceName = serviceName;
		this.fileName = fileName;
		this.ftpFilepath = ftpFilepath;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFtpFilepath()
	{
		return ftpFilepath;
	}

	public void setFtpFilepath(String ftpFilepath)
	{
		this.ftpFilepath = ftpFilepath;
	}

	@Override
	public String toString()
	{
		return "FtpFile [serviceName=" + serviceName + ", fileName=" + fileName + ", ftpFilepath=" + ftpFilepath + "]";
	}

}
