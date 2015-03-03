package com.david.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Ftp工具类
 * 
 * @author dailiwei
 * 
 */
public class FtpUtils
{
	private FTPClient ftp;
	public static final int BINARY_FTP_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
	private static final int DATA_TIME_OUT = 120000;

	public FtpUtils()
	{
		ftp = new FTPClient();
	}

	public FTPClient getFtp()
	{
		return ftp;
	}

	public void setFtp(FTPClient ftp)
	{
		this.ftp = ftp;
	}

	/**
	 * 连接服务器
	 * 
	 * @param ftpConfig
	 *            ftp配置类
	 * @throws IOException
	 */
	public void connectServer(FtpConfig ftpConfig) throws IOException
	{
		String server = ftpConfig.getServer();
		int port = ftpConfig.getPort();
		String username = ftpConfig.getUsername();
		String password = ftpConfig.getPassword();
		String path = ftpConfig.getPath();
		connectServer(server, port, username, password, path);
	}

	/**
	 * 连接服务器
	 * 
	 * @param server
	 *            服务器IP
	 * @param port
	 *            服务器端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param path
	 *            上传文件路径
	 * @throws IOException
	 */
	public void connectServer(String server, int port, String username, String password, String path) throws IOException
	{
		ftp.connect(server, port);
		ftp.setDataTimeout(DATA_TIME_OUT);
		/*
		 * if (!FTPReply.isPositiveCompletion(ftp.getReply())) {
		 * ftp.disconnect(); System.out.println(server + "cannot connect..."); }
		 */
		ftp.login(username, password);
		if (path.length() != 0)
		{
			ftp.changeWorkingDirectory(path);
		}
	}

	/**
	 * 设置文件类型
	 * 
	 * @param fileType
	 *            文件类型
	 * @throws IOException
	 */
	public void setFileType(int fileType) throws IOException
	{
		ftp.setFileType(fileType);		
	}

	/**
	 * 关闭服务器
	 * 
	 * @throws IOException
	 */
	public void closeServer() throws IOException
	{
		if (ftp != null && ftp.isConnected())
		{
			ftp.logout();
			ftp.disconnect();
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param pathName
	 *            文件路径名
	 * @return 是否创建成功
	 * @throws IOException
	 */
	public boolean createDirectory(String pathName) throws IOException
	{
		return ftp.makeDirectory(pathName);
	}

	/**
	 * 移除目录
	 * 
	 * @param pathName
	 *            文件路径名
	 * @return 是否移除成功
	 * @throws IOException
	 */
	public boolean removeDirectory(String pathName) throws IOException
	{
		return ftp.removeDirectory(pathName);
	}

	/**
	 * 移除目录
	 * 
	 * @param pathName
	 *            文件路径名
	 * @param isAll
	 *            是否级联删除
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String pathName, boolean isAll) throws IOException
	{
		if (!isAll)
		{
			return removeDirectory(pathName);
		}

		FTPFile[] ftpFiles = ftp.listFiles(pathName);
		for (FTPFile file : ftpFiles)
		{
			String name = file.getName();
			String subFileName = pathName + "/" + name;
			if (file.isDirectory())
			{
				removeDirectory(subFileName, true);
			} else if (file.isFile())
			{
				deleteFile(subFileName);
			}
		}

		return removeDirectory(pathName);
	}

	public Map<String, List<String>> getDirectoryAndFiles(String pathName) throws IOException
	{
		Map<String, List<String>> rsMap = new HashMap<String, List<String>>();

		FTPFile[] ftpFiles = ftp.listFiles(pathName);

		if (ftpFiles == null || ftpFiles.length == 0)
		{
			return rsMap;
		}

		List<String> subFiles = null;
		for (FTPFile file : ftpFiles)
		{
			if (file.isDirectory())
			{
				if (!rsMap.containsKey(file.getName()))
				{
					rsMap.put(file.getName(), new ArrayList<String>());
				}

			} else
			{
				rsMap.put("defaultlib", new ArrayList<String>());
			}

			subFiles = rsMap.get(file.getName());
			FTPFile[] subFtpFiles = ftp.listFiles(pathName + "/" + file.getName());
			for (FTPFile subFile : subFtpFiles)
			{
				if (subFile.isFile())
				{
					subFiles.add(subFile.getName());
				}
			}
		}

		return rsMap;
	}

	/**
	 * 获取文件列表
	 * 
	 * @param pathName
	 * @return FTPFile文件列表
	 * @throws IOException
	 */
	public List<String> getFileList(String pathName) throws IOException
	{
		FTPFile[] ftpFiles = ftp.listFiles(pathName);
		List<String> fileLs = new ArrayList<String>();

		if (ftpFiles == null || ftpFiles.length == 0)
		{
			return fileLs;
		}

		for (FTPFile file : ftpFiles)
		{
			if (file.isFile())
			{
				fileLs.add(file.getName());
			}
		}

		return fileLs;
	}

	/**
	 * 删除服务器上的文件
	 * 
	 * @param pathName
	 *            文件名
	 * @return 是否删除成功
	 * @throws IOException
	 */
	public boolean deleteFile(String pathName) throws IOException
	{
		return ftp.deleteFile(pathName);
	}

	/**
	 * 上传文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param newName
	 *            重命名后的文件
	 * @return 是否上传成功
	 * @throws IOException
	 */
	public boolean uploadFile(String fileName, String newName) throws IOException
	{
		boolean flag = false;
		FileInputStream bin = null;

		try
		{
			bin = new FileInputStream(fileName);
			flag = ftp.storeFile(newName, bin);

		} catch (IOException e)
		{
			flag = false;
			return flag;
		} finally
		{
			if (bin != null)
			{
				bin.close();
			}
		}

		return flag;
	}

	/**
	 * 上传文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 是否上传成功
	 * @throws IOException
	 */
	public boolean uploadFile(String fileName) throws IOException
	{
		return uploadFile(fileName, fileName);
	}

	/**
	 * 上传文件
	 * 
	 * @param in
	 *            文件流
	 * @param newName
	 *            新的文件名
	 * @return 是否上传成功
	 * @throws IOException
	 */
	public boolean uploadFile(InputStream in, String newName) throws IOException
	{
		boolean flag = false;
		try
		{
			flag = ftp.storeFile(newName, in);
		} catch (IOException e)
		{
			flag = false;
			return flag;
		} finally
		{
			if (in != null)
			{
				in.close();
			}
		}

		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param remoteFile
	 *            远程服务器路径
	 * @param localFile
	 *            本地路径
	 * @return 是否下载成功
	 * @throws IOException
	 */
	public boolean downloadFile(String remoteFile, String localFile) throws IOException
	{
		boolean flag = false;
		File outFile = new File(localFile);
		FileOutputStream out = null;

		try
		{
			out = new FileOutputStream(outFile);
			out.flush();
			flag = ftp.retrieveFile(remoteFile, out);

		} catch (IOException e)
		{
			flag = false;
			return flag;
		} finally
		{
			if (out != null)
			{
				out.close();
			}
		}

		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param sourceFile
	 *            原文件路径
	 * @return 是否下载成功
	 * @throws IOException
	 */
	public InputStream downloadFile(String sourceFile) throws IOException
	{
		return ftp.retrieveFileStream(sourceFile);
	}
}