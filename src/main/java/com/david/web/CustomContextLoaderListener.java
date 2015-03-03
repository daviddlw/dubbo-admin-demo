package com.david.web;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * 自定义ContextClassLoader类
 * 容器启动时候执行扫描ftp source jar包下的内容
 * 并且解析成php class的任务
 * @author dailiwei
 *
 */
public class CustomContextLoaderListener implements ServletContextListener
{
	private Logger logger = Logger.getLogger(CustomContextLoaderListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		// TODO Auto-generated method stub
		logger.info("contextInitialized...");
//		scheduledTaskRun();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		// TODO Auto-generated method stub
		logger.info("contextDestroyed...");
	}
	
	public void scheduledTaskRun()
	{
		Runnable scheduledTask = new Runnable() {

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				taskToRun();
			}
		};

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(scheduledTask, 0, 3, TimeUnit.SECONDS);
	}

	private void taskToRun()
	{
		System.out.println("执行扫描ftp服务器上的source jar包");
	}

}
