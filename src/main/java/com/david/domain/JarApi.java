package com.david.domain;

/**
 * Jar API所用实体
 * @author pc
 *
 */
public class JarApi
{
	private String serviceName;
	private String version;
	private ApiType type;
	private String filepath;

	public JarApi()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public JarApi(String serviceName, String version, ApiType type, String filepath)
	{
		super();
		this.serviceName = serviceName;
		this.version = version;
		this.type = type;
		this.filepath = filepath;
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

	public ApiType getType()
	{
		return type;
	}

	public void setType(ApiType type)
	{
		this.type = type;
	}

	public String getFilepath()
	{
		return filepath;
	}

	public void setFilepath(String filepath)
	{
		this.filepath = filepath;
	}

	@Override
	public String toString()
	{
		return "JarApi [serviceName=" + serviceName + ", version=" + version + ", type=" + type + ", filepath="
				+ filepath + "]";
	}

}
