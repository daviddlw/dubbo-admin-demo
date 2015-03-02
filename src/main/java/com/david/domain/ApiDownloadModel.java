package com.david.domain;

import java.util.List;

public class ApiDownloadModel
{
	private String serviceName;
	private List<FtpFile> sourceLib;
	private List<FtpFile> binLib;

	public ApiDownloadModel()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiDownloadModel(String serviceName, List<FtpFile> sourceLib, List<FtpFile> binLib)
	{
		super();
		this.serviceName = serviceName;
		this.sourceLib = sourceLib;
		this.binLib = binLib;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public List<FtpFile> getSourceLib()
	{
		return sourceLib;
	}

	public void setSourceLib(List<FtpFile> sourceLib)
	{
		this.sourceLib = sourceLib;
	}

	public List<FtpFile> getBinLib()
	{
		return binLib;
	}

	public void setBinLib(List<FtpFile> binLib)
	{
		this.binLib = binLib;
	}

	@Override
	public String toString()
	{
		return "ApiDownloadModel [serviceName=" + serviceName + ", sourceLib=" + sourceLib + ", binLib=" + binLib + "]";
	}

}
