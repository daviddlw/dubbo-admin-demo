package com.david.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Ftp������
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
	 * ��ʼ���ͻ��˲���ɶȷ���˵�����
	 * 
	 * @param ftpConfig
	 *            ftp������
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
	 * ��ʼ���ͻ��˲�����ɶԷ���˵�����
	 * 
	 * @param server
	 *            ����˵�ַ
	 * @param port
	 *            ����˶˿�
	 * @param username
	 *            ftp�û���
	 * @param password
	 *            ftp����
	 * @param path
	 *            Զ��·��������Ϊ��ֵ
	 * @throws IOException
	 */
	public void connectServer(String server, int port, String username, String password, String path) throws IOException
	{
		ftp.connect(server, port);
		ftp.setDataTimeout(DATA_TIME_OUT);
/*		if (!FTPReply.isPositiveCompletion(ftp.getReply()))
		{
			ftp.disconnect();
			System.out.println(server + "�ܾ����ӷ���...");
		}*/
		ftp.login(username, password);
		if (path.length() != 0)
		{
			ftp.changeWorkingDirectory(path);
		}
	}

	/**
	 * ����ftp���ļ�����
	 * 
	 * @param fileType
	 *            �ļ�����
	 * @throws IOException
	 */
	public void setFileType(int fileType) throws IOException
	{
		ftp.setFileType(fileType);
	}

	/**
	 * �ر�ftp����
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
	 * �ڷ������˴���һ��Ŀ¼
	 * 
	 * @param pathName
	 *            ���������Ŀ¼���߾��Ŀ¼
	 * @return �Ƿ񴴽��ɹ�
	 * @throws IOException
	 */
	public boolean createDirectory(String pathName) throws IOException
	{
		return ftp.makeDirectory(pathName);
	}

	/**
	 * �ڷ�������ɾ��һ��Ŀ¼�����Ϊ�գ�
	 * 
	 * @param pathName
	 * @return �Ƿ�ɾ���
	 * @throws IOException
	 */
	public boolean removeDirectory(String pathName) throws IOException
	{
		return ftp.removeDirectory(pathName);
	}

	/**
	 * �ڷ�������ɾ��һ��ftp�������ϵ�Ŀ¼
	 * 
	 * @param pathName
	 *            Ŀ¼·��
	 * @param isAll
	 *            �Ƿ�ɾ������Ŀ¼���ļ��������
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

	/**
	 * ���ظ�Ŀ¼�µ��ļ�
	 * 
	 * @param pathName
	 * @return FTPFile��ɵļ���
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
	 * �ڷ�������ɾ��һ���ļ�
	 * 
	 * @param pathName
	 *            �ļ���
	 * @return �Ƿ�ɾ��ɹ�
	 * @throws IOException
	 */
	public boolean deleteFile(String pathName) throws IOException
	{
		return ftp.deleteFile(pathName);
	}

	/**
	 * �ϴ��ļ�����������
	 * 
	 * @param fileName
	 *            �ϴ����ļ�����Ŀ¼���ļ���
	 * @param newName
	 *            �µ��ļ���
	 * @return �Ƿ��ϴ��ɹ�
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
	 * �ϴ��ļ�
	 * 
	 * @param fileName
	 *            �ϴ����ļ�����Ŀ¼���ļ���
	 * @return �Ƿ��ϴ��ɹ�
	 * @throws IOException
	 */
	public boolean uploadFile(String fileName) throws IOException
	{
		return uploadFile(fileName, fileName);
	}

	/**
	 * �ϴ��ļ�����InputStream
	 * 
	 * @param in
	 *            �ļ���
	 * @param newName
	 *            �µ��ļ���
	 * @return �Ƿ��ϴ��ɹ�
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
	 * �����ļ�
	 * 
	 * @param remoteFile
	 *            Զ���ļ���
	 * @param localFile
	 *            �����ļ�
	 * @return �Ƿ����سɹ�
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
	 * �����ļ������ļ���
	 * 
	 * @param sourceFile
	 *            Զ���ļ�
	 * @return �Ƿ����سɹ�
	 * @throws IOException
	 */
	public InputStream downloadFile(String sourceFile) throws IOException
	{
		return ftp.retrieveFileStream(sourceFile);
	}
}