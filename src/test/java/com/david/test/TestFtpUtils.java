package com.david.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import sun.tools.jar.resources.jar;

import com.david.domain.FtpUtils;
import com.david.domain.JarApi;
import com.david.domain.JarType;

public class TestFtpUtils
{
	private static String ftpServer = "192.168.8.116";
	private static int port = 48790;
	private static FtpUtils ftpUtils = new FtpUtils();
	private static String testFileName = "xom-1.2.10.jar";
	private static String testSourceFileName = "xom-1.2.10-sources.jar";

	private static String DEFAULT_JAR_NAME = "xom-1.0.0.jar";
	private static String DEFAULT_SOURCE_JAR_NAME = "xom-1.0.0-sources.jar";
	private static String VALID_JAR_NAME = "xom-1.0.6.jar";
	private static String VALID_SOURCES_JAR_NAME = "xom-1.0.6-sources.jar";

	@Before
	public void setUp() throws Exception
	{
		ftpUtils.connectServer(ftpServer, port, "daviddai", "123456", "");

	}

	/**
	 * 生成新的用户名
	 */
	@Test
	public void testGenerateNewName()
	{
		JarApi validSourceJar = new JarApi();
		validSourceJar.setFileName(testSourceFileName);
		validSourceJar.setType(JarType.Source);
		validSourceJar.setVersion("1.0.6");
		String validSourceJarName = generateJarName(validSourceJar);
		assertEquals(VALID_SOURCES_JAR_NAME, validSourceJarName);
		System.err.println(validSourceJarName);

		JarApi validJar = new JarApi();
		validJar.setFileName(testFileName);
		validJar.setType(JarType.Bin);
		validJar.setVersion("1.0.6");
		String validJarName = generateJarName(validJar);
		assertEquals(VALID_JAR_NAME, validJarName);
		System.err.println(validJarName);

		JarApi defaultSourceJar = new JarApi();
		defaultSourceJar.setFileName(testSourceFileName);
		defaultSourceJar.setType(JarType.Source);
		String defaultSourceJarName = generateJarName(defaultSourceJar);
		assertEquals(DEFAULT_SOURCE_JAR_NAME, defaultSourceJarName);
		System.err.println(defaultSourceJarName);

		JarApi defaultJar = new JarApi();
		defaultJar.setFileName(testFileName);
		defaultJar.setType(JarType.Bin);
		String defaultJarName = generateJarName(defaultJar);
		assertEquals(DEFAULT_JAR_NAME, defaultJarName);
		System.err.println(defaultJarName);
	}

	private String generateJarName(JarApi jarApi)
	{
		String name = StringUtils.EMPTY;
		String version = StringUtils.EMPTY;
		String result = StringUtils.EMPTY;
		if (jarApi.getFileName().contains("-"))
		{
			name = StringUtils.substringBefore(jarApi.getFileName(), "-");
		} else
		{
			name = jarApi.getFileName();
		}

		// 如果没有填写版本默认设置为1.0.0
		version = (jarApi.getVersion() == null || jarApi.getVersion().isEmpty()) ? "1.0.0" : jarApi.getVersion();

		// 如果是source类型则需要添加sources后缀
		if (jarApi.getType() == JarType.Source)
		{
			result = String.format("%s-%s-sources.jar", name, version);
		} else
		{
			result = String.format("%s-%s.jar", name, version);
		}

		return result;
	}

	@Test
	public void testJarType()
	{
		JarType type = EnumUtils.getEnum(JarType.class, "bin");
		System.out.println(type);
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
		boolean flag = ftpUtils.createDirectory("sourcelib/com.david.Test1");
		showMessage("创建文件夹", flag);
	}

	@Test
	public void testRemoveDirectory() throws IOException
	{
		boolean flag = ftpUtils.removeDirectory("newdir");
		showMessage("移除文件夹", flag);
	}

	@Test
	public void testGetFileList() throws IOException
	{
		List<String> files = ftpUtils.getFileList("sourcelib");
		System.out.println("file.size()=> " + files.size());
		for (String item : files)
		{
			System.out.println(item);
		}
	}

	@Test
	public void testGetDirectoryAndFiles() throws IOException
	{
		Map<String, List<String>> rsMap = ftpUtils.getDirectoryAndFiles("binlib");
		System.out.println("rsMap.size()=> " + rsMap.size());
		for (Entry<String, List<String>> item : rsMap.entrySet())
		{
			System.out.println(item);
		}
	}

	@Test
	public void testUploadFile() throws IOException
	{
		String path = "D:\\Java\\" + testFileName + "";
		boolean flag = ftpUtils.uploadFile(path, testFileName);
		showMessage("上传文件", flag);
	}

	@Test
	public void testDeleteFile() throws IOException
	{
		String filename = testFileName;
		boolean flag = ftpUtils.deleteFile(filename);
		showMessage("删除文件", flag);
	}

	@Test
	public void testDownloadFile() throws IOException
	{
		// String localFileName = "F:\\ftpdownload\\"+testFileName;
		String localFileName = "D:\\FtpDownload\\" + testFileName;
		boolean flag = ftpUtils.downloadFile(testFileName, localFileName);
		showMessage("下载文件", flag);
	}

}