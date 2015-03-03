package com.david.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.david.domain.ApiDownloadModel;
import com.david.domain.FtpFile;

public class ApiDownloadController extends BasicController
{
	private static final String HUPU_SOA_CLIENT_PHP = "HupuSOAClient.php";
	private final Logger logger = Logger.getLogger(ApiDownloadController.class);

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("Returning the apiDownload view.");

		String serviceName = getServiceName(request);
		ApiDownloadModel model = new ApiDownloadModel();
		model.setServiceName(serviceName);
		getFtpFiles(model);

		return new ModelAndView("apiDownload", "model", model);
	}

	/**
	 * 获取指定服务的sourcelib,binlib,phpclass
	 * 
	 * @param serviceName
	 *            需要过滤的服务名称，如果没有则获取全部服务 文件命名方式：服务名-jar包名
	 * @return 文件列表
	 */
	private ApiDownloadModel getFtpFiles(ApiDownloadModel model)
	{
		Map<String, List<FtpFile>> rsMap = new HashMap<String, List<FtpFile>>();
		rsMap.put(SOURCELIB, new ArrayList<FtpFile>());
		rsMap.put(BINLIB, new ArrayList<FtpFile>());
		rsMap.put(PHPLIB, new ArrayList<FtpFile>());

		try
		{
			ftpUtils.connectServer(FTP_SERVER, PORT, USERNAME, PASSWORD, "");
			logger.info("connect to ftp server...");

			addToMap(rsMap, model.getServiceName(), SOURCELIB);
			addToMap(rsMap, model.getServiceName(), BINLIB);
			addToMap(rsMap, model.getServiceName(), PHPLIB);

			logger.info("finish generate apiDownload model...");

			ftpUtils.closeServer();
			logger.info("close the ftp server...");

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.setSourceLib(rsMap.get(SOURCELIB));
		model.setBinLib(rsMap.get(BINLIB));
		model.setPhpLib(rsMap.get(PHPLIB));

		return model;
	}

	private void addToMap(Map<String, List<FtpFile>> rsMap, String serviceName, String type)
	{
		try
		{
			// 获取sourcelib
			List<String> fileList = null;
			String key = StringUtils.EMPTY;
			Map<String, List<String>> sourceLibMap = ftpUtils.getDirectoryAndFiles(type);
			for (Entry<String, List<String>> item : sourceLibMap.entrySet())
			{
				key = item.getKey();
				fileList = item.getValue();

				if (key.equalsIgnoreCase("defaultlib") || fileList == null || fileList.size() == 0)
				{
					continue;
				}

				if (serviceName.isEmpty() || serviceName.equalsIgnoreCase(TEST_SERVICE))
				{
					// 添加全部服务不过滤
					for (String file : fileList)
					{
						FtpFile ftpFile = new FtpFile();
						ftpFile.setServiceName(key);
						ftpFile.setFileName(String.format("%s: %s", key, file));
						ftpFile.setFtpFilepath(String.format("%s/%s/%s/%s", FTP_DOWNLOAD_PATH_PREFIX, type, key, file));

						rsMap.get(type).add(ftpFile);
					}

				} else
				{
					// 添加QueryString中指定的服务
					if (key.equalsIgnoreCase(serviceName))
					{
						for (String file : fileList)
						{
							FtpFile ftpFile = new FtpFile();
							ftpFile.setServiceName(key);
							ftpFile.setFileName(String.format("%s: %s", serviceName, file));
							ftpFile.setFtpFilepath(String.format("%s/%s/%s/%s", FTP_DOWNLOAD_PATH_PREFIX, type, key, file));

							rsMap.get(type).add(ftpFile);
						}
					}
				}
			}

		} catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}
}
