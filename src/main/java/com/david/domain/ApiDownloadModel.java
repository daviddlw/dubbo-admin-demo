package com.david.domain;

import java.util.List;

public class ApiDownloadModel
{
	private String serviceName;
	private List<FtpFile> sourceLib;
	private List<FtpFile> binLib;
	private List<FtpFile> phpLib;

	public ApiDownloadModel()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiDownloadModel(String serviceName, List<FtpFile> sourceLib, List<FtpFile> binLib, List<FtpFile> phpLib)
	{
		super();
		this.serviceName = serviceName;
		this.sourceLib = sourceLib;
		this.binLib = binLib;
		this.phpLib = phpLib;
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

	public List<FtpFile> getPhpLib()
	{
		return phpLib;
	}

	public void setPhpLib(List<FtpFile> phpLib)
	{
		this.phpLib = phpLib;
	}

	@Override
	public String toString()
	{
		return "ApiDownloadModel [serviceName=" + serviceName + ", sourceLib=" + sourceLib + ", binLib=" + binLib + ", phpLib=" + phpLib
				+ "]";
	}

}
