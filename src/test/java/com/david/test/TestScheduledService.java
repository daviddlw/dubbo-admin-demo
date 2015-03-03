package com.david.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduledService
{
	public static void main(String[] args)
	{
		scheduledTaskRun();
	}

	public static void scheduledTaskRun()
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

	private static void taskToRun()
	{
		System.out.println("执行扫描ftp服务器上的source jar包");
	}
}
