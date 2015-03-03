package com.david.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
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
			uploadToFtp(jarApi, request, response);

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
	private void uploadToFtp(JarApi jarApi, HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("==========执行ftp上传=============");

		try
		{
			ftpUtils.connectServer(FTP_SERVER, PORT, USERNAME, PASSWORD, "");
			logger.info("connect to ftp server...");

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
			logger.info("is upload success：" + isSuccess);

			// 执行java转化php的任务，只有执行上传Jar Source的命令时候才进行上传
			if (jarApi.getType() == JarType.Source)
			{
				executeTranslateTask(jarApi, request, response);
			}

			// 关闭服务器
			/*
			 * ftpUtils.closeServer(); logger.info("close the server...");
			 */

		} catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 执行后台任务转化为php任务
	 * 
	 * @param jarApi
	 */
	private void executeTranslateTask(JarApi jarApi, HttpServletRequest request, HttpServletResponse response)
	{
		final JarApi runJarApi = jarApi;
		final HttpServletRequest runRequest = request;

		Runnable task = new Runnable() {

			@Override
			public void run()
			{
				try
				{
					// 尝试开启Ftp服务器连接如果开着则不必重新连接ftp工具类内部做了判断
					ftpUtils.connectServer(FTP_SERVER, PORT, USERNAME, PASSWORD, "");
					logger.info("php转化后台任务重新连接ftp服务器...");

					// 从source bin读取文件下载到项目下的临时目录tempfile
					downloadToTempFile(runJarApi, runRequest);

					// 解析source jar使用vm模板翻译成php class
					InputStream in = null;
					in = getTestVmFile();

					// 写入到ftp下的phplib下
					generatePhpClassFile(runJarApi, in);

					// 完成所有操作尝试关闭ftp服务器
					logger.info("执行完所有phpClass转化工作，尝试关闭ftp服务器连接...");
					ftpUtils.closeServer();
				} catch (IOException e)
				{
					logger.info("尝试关闭服务器失败..." + e.getMessage(), e);
				}
			}
		};

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(task);

	}

	/**
	 * 获取测试转化的vm留
	 * 
	 * @return
	 */
	private InputStream getTestVmFile()
	{
		VelocityEngine ve = new VelocityEngine();
		ve.init();

		Template t = ve.getTemplate("src/main/webapp/WEB-INF/layout/hello-velocity.vm");
		VelocityContext ctx = new VelocityContext();

		ctx.put("name", "velocity");
		ctx.put("date", (new Date()).toString());

		List<String> temp = new ArrayList<String>();
		temp.add("1");
		temp.add("2");
		ctx.put("list", temp);

		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);
		String result = sw.toString();
		logger.info(result);

		InputStream in = new ByteArrayInputStream(result.getBytes());
		return in;
	}

	/**
	 * 下载至临时文件夹
	 * 
	 * @param jarApi
	 */
	private void downloadToTempFile(JarApi jarApi, HttpServletRequest request)
	{
		String serviceName = jarApi.getServiceName();
		String filepath = "sourcelib" + "/" + serviceName;
		String temppath = "tempfile";

		try
		{
			List<String> files = ftpUtils.getFileList(filepath);

			String sourcepath = StringUtils.EMPTY;
			String combinePath = temppath;

			for (String file : files)
			{
				sourcepath = filepath + "/" + file;
				serviceName = serviceName.replaceAll("\\.", "-");
				combinePath = temppath + "/" + String.format("%s-%s", serviceName, file);

				ftpUtils.downloadFile(sourcepath, combinePath);
			}

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将Java Source Api翻译成php Class
	 */
	private InputStream translateJavaToPhp()
	{
		return null;
	}

	/**
	 * 写入phpclass到指定phplib
	 * 
	 * @param jarApi
	 *            拼接写入的php文件路径
	 * @param filepath
	 *            临时文件目录
	 */
	private void generatePhpClassFile(JarApi jarApi, InputStream in)
	{
		String serviceName = jarApi.getServiceName();
		String phpFilepath = "phplib" + "/" + serviceName;

		boolean isCreateDirectory;
		try
		{
			isCreateDirectory = ftpUtils.createDirectory(phpFilepath);
			String phpClassName = generatePhpClassName(jarApi);
			logger.info("isCreatePhpDirectory=> " + isCreateDirectory + " || phpClass=> " + phpClassName);
			boolean isSuccess = ftpUtils.uploadFile(in, phpFilepath + "/" + phpClassName);
			logger.info("上传php文件: " + phpClassName + "=>是否成功: " + isSuccess);

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 生成phpClass Name
	 * 
	 * @param jarApi
	 * @return phpClassName
	 */
	private String generatePhpClassName(JarApi jarApi)
	{

		String name = StringUtils.EMPTY;
		String version = StringUtils.EMPTY;
		String result = StringUtils.EMPTY;
		if (jarApi.getServiceName().contains("."))
		{
			name = StringUtils.substringAfterLast(jarApi.getServiceName(), ".");
		} else
		{
			name = jarApi.getServiceName();
		}

		// 如果没有填写版本默认设置为1.0.0
		version = (jarApi.getVersion() == null || jarApi.getVersion().isEmpty()) ? "1.0.0" : jarApi.getVersion();
		result = String.format("%s-%s.php", name, version);

		return result;
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
