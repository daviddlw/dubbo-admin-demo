package com.david.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.david.domain.JarApi;
import com.david.domain.JarType;

public class ApiUploadFormController extends BasicController
{
	private static final String DL_API_TYPE = "dlApiType";
	private static final String TXT_FILEPATH = "txtFilepath";
	private static final String TXT_VERSION = "txtVersion";
	private static final String HD_SERVICE_NAME = "hdServiceName";
	private static final String FILE = "file";

	private final Logger logger = Logger.getLogger(ApiUploadFormController.class);
	private final String UTF_8 = "UTF-8";


	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("Returning the apiUploadForm action.");
		request.setCharacterEncoding(UTF_8);		
		fileUpload(request, response);		

		return null;
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @param response
	 */
	private void fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String serviceName = StringUtils.EMPTY;
		boolean isMutipart = ServletFileUpload.isMultipartContent(request);

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);

		logger.info("isMutipart: " + isMutipart);
		try
		{
			List<FileItem> items = fileUpload.parseRequest(request);
			String fieldName = StringUtils.EMPTY;
			String value = StringUtils.EMPTY;

			for (FileItem item : items)
			{
				if (item.isFormField())
				{
					fieldName = item.getFieldName();
					value = item.getString();
					request.setAttribute(fieldName, value);

				} else
				{
					request.setAttribute(TXT_FILEPATH, item.getName());
					request.setAttribute(FILE, item.getInputStream());
				}
			}

			JarApi jarApi = new JarApi();
			serviceName = (String) request.getAttribute(HD_SERVICE_NAME);
			jarApi.setServiceName(serviceName);			
			jarApi.setVersion((String) request.getAttribute(TXT_VERSION));
			jarApi.setFileName((String) request.getAttribute(TXT_FILEPATH));
			String type = (String) request.getAttribute(DL_API_TYPE);
			jarApi.setType(EnumUtils.getEnum(JarType.class, type));
			jarApi.setFile((InputStream) request.getAttribute(FILE));

			logger.info("JarApi: " + jarApi);

			// 上传ftp服务器
			uploadToFtp(jarApi);
			
			

		} catch (FileUploadException e)
		{
			logger.error(e.getMessage(), e);
		}
		
		response.sendRedirect("apiDownload.do?serviceName=" + serviceName);
	}

	/**
	 * 上传到ftp服务器
	 * 
	 * @param jarApi
	 * @return
	 */
	private void uploadToFtp(JarApi jarApi)
	{
		logger.info("==========执行ftp上传=============");

		try
		{
			ftpUtils.connectServer(FTP_SERVER, PORT, USERNAME, PASSWORD, "");
			logger.info("连接服务器...");

			/*
			 * 文件上传规则 根目录文件夹维servicelib 1.
			 * 根据jar包类型分别上传到sourcelib（source源文件）与binlib（二进制源文件）中 2.
			 * 然后根据服务名创建文件夹名称，已经存在则不创建 3.
			 * 重新上传的新jar包名称（服务名-版本号-（sources）,二进制文件默认不添加这个后缀）
			 */

			String filepath = StringUtils.EMPTY;
			String newJarName = StringUtils.EMPTY;

			if (jarApi.getType() == JarType.Source)
			{
				filepath = "sourcelib" + "/" + jarApi.getServiceName();

			} else
			{
				filepath = "binlib" + "/" + jarApi.getServiceName();
			}

			// 在对应的lib中创建报名
			boolean isCreateDirectory = ftpUtils.createDirectory(filepath);
			newJarName = generateJarName(jarApi);

			logger.info("isCreateDirectory=> " + isCreateDirectory + " || newJarName=> " + newJarName);

			// 上传到ftp服务器
			boolean isSuccess = ftpUtils.uploadFile(jarApi.getFile(), filepath + "/" + newJarName);
			logger.info("是否上传成功：" + isSuccess);

			// 关闭服务器
			ftpUtils.closeServer();
			logger.info("关闭服务器...");

		} catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 生成新jar包名称
	 * 
	 * @param jarApi
	 * @return
	 */
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
}
