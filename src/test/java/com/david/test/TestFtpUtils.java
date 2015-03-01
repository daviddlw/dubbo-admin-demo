package com.david.test;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestFtpUtils
{
	private static String ftpServer = "192.168.8.116";
	private static int port = 48790;
	private static FtpUtils ftpUtils = new FtpUtils();
	private static String testFileName = "xom-1.2.10.jar";

	@Before
	public void setUp() throws Exception
	{
		ftpUtils.connectServer(ftpServer, port, "daviddai", "123456", "");

	}

	@Test
	public void testGetFtpClient() throws IOException
	{
		System.out.println(ftpUtils.getFtp().toString());
	}

	private void showMessage(String message, boolean flag)
	{
		System.out.println(message + "=>" + flag);
	}

	@Test
	public void testCreateDirectory() throws IOException
	{
		boolean flag = ftpUtils.createDirectory("newdir");
		showMessage("�����ļ���", flag);
	}

	@Test
	public void testRemoveDirectory() throws IOException
	{
		boolean flag = ftpUtils.removeDirectory("newdir");
		showMessage("ɾ���ļ���", flag);
	}

	@Test
	public void testGetFileList() throws IOException
	{
		List<String> files = ftpUtils.getFileList("");
		System.out.println("file.size()=>" + files.size());
		for (String item : files)
		{
			System.out.println(item);
		}
	}

	@Test
	public void testUploadFile() throws IOException
	{
		String path = "D:\\Java\\" + testFileName + "";
		boolean flag = ftpUtils.uploadFile(path, testFileName);
		showMessage("�ϴ��ɹ�", flag);
	}

	@Test
	public void testDeleteFile() throws IOException
	{
		String filename = testFileName;
		boolean flag = ftpUtils.deleteFile(filename);
		showMessage("ɾ��ɹ�", flag);
	}

	@Test
	public void testDownloadFile() throws IOException
	{
		// String localFileName = "F:\\ftpdownload\\"+testFileName;
		String localFileName = "D:\\FtpDownload\\" + testFileName;
		boolean flag = ftpUtils.downloadFile(testFileName, localFileName);
		showMessage("���سɹ�", flag);
	}

}