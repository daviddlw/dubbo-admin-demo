package com.david.domain;

/**
 * Ftp工具类
 * 
 * @author dailiwei
 * 
 */
public class FtpConfig
{
	private String username;
	private String password;
	private String server;
	private int port;
	private String path;

	public FtpConfig()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public FtpConfig(String username, String password, String server, int port, String path)
	{
		super();
		this.username = username;
		this.password = password;
		this.server = server;
		this.port = port;
		this.path = path;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	@Override
	public String toString()
	{
		return "FtpConfig [username=" + username + ", password=" + password + ", server=" + server + ", port=" + port
				+ ", path=" + path + "]";
	}

}