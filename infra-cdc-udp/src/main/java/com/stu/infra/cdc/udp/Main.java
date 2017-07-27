package com.stu.infra.cdc.udp;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.stu.infra.cdc.model.Config;
import com.stu.infra.cdc.service.ConfigService;
import com.stu.infra.cdc.service.NodeService;
import com.stu.infra.cdc.service.OperatorService;
import com.stu.infra.cdc.service.OutboxService;
import com.stu.infra.cdc.udp.task.AlarmTriggerTask;
import com.stu.infra.cdc.udp.task.CommLostTriggerTask;
import com.stu.infra.cdc.udp.task.ShiftNotificationTask;

public class Main {
	
	Config config	  			= null;
	boolean shutdown 		  	= false;
	ThreadPooledServer server 	= null;
	ThreadPoolTaskScheduler scheduler = null;
	
	NodeService nodeService 	= null;
	ConfigService confService 	= null;
	OperatorService operatorService 	= null;
	OutboxService outboxService = null;
	
	class Shutdown extends Thread
	{
		@Override
        public void run()
        {
			LoggerFactory.getLogger(Shutdown.class).info("Shutting down UDP Server..");
			
			if(scheduler != null) scheduler.shutdown();
			if(server != null) server.stop();
			Main.this.shutdown = true;
			SpringManager.getInstance().getApplicationContext().close();
			LoggerFactory.getLogger(Shutdown.class).info("UDP Server stopped.");
        }
	}
	
	public Main()
	{
		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}
	
	private void run() throws Exception
	{		
		confService = SpringManager.getInstance().getBean(ConfigService.class);
		nodeService = SpringManager.getInstance().getBean(NodeService.class);
		operatorService = SpringManager.getInstance().getBean(OperatorService.class);
		outboxService = SpringManager.getInstance().getBean(OutboxService.class);
		
		config 		= confService.getConfig();
		scheduler 	= SpringManager.getInstance().getBean(ThreadPoolTaskScheduler.class);
		
		CronTrigger cron = new CronTrigger(config.getCronScheduler());
		scheduler.schedule(new AlarmTriggerTask(confService, nodeService), cron);
		scheduler.scheduleAtFixedRate(new CommLostTriggerTask(confService, nodeService), new Date(), 60000);
		scheduler.scheduleAtFixedRate(new ShiftNotificationTask(confService, operatorService, outboxService), new Date(), 60000);
		
		server = new ThreadPooledServer(config.getServerPort());
		new Thread(server).start();
		
		LoggerFactory.getLogger(Main.class).info("UDP Server started on port "+config.getServerPort());
		
		while(!shutdown)
		{
			try {
			    Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				LoggerFactory.getLogger(Main.class).error("Error Running UDP Server.", e);
			}
		}
	}
	
	public static void main(String[] args) {
		
		try {
			//
			//String smsJson = "{\"id\":14,\"originator\":\"6287825411059\",\"encoding\":\"7\",\"messageDate\":\"2016-10-19 13:21:15\",\"receiveDate\":\"2016-10-19 13:21:22\",\"text\":\"\",\"gatewayId\":\"Modem1\"}";
			//System.out.println(smsJson);
			/*
			ObjectMapper mapper = new ObjectMapper();
			Inbox msg = mapper.readValue(smsJson, Inbox.class);
			
			System.out.println("from: "+msg.getOriginator());
			System.out.println("text: "+msg.getText());
			System.out.println("receive: "+msg.getReceiveDate().toString());
			*/
			Main app = new Main();
			app.run();
			
		}
		catch (Exception e) {
			LoggerFactory.getLogger(Shutdown.class).error("SMS UDP Server error.", e);
		}
	}

}
