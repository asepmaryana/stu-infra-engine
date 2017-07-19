package com.stu.infra.cdc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan(basePackages = "com.stu.infra.cdc")
@PropertySource(value = "classpath:server.properties")
public class SmsUdpConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public UdpServerConfig udpServerConfig() {
		UdpServerConfig usc = new UdpServerConfig();
		usc.setServerPort(Integer.parseInt(env.getProperty("server.port")));
		usc.setSmsLimit(Integer.parseInt(env.getProperty("outbox.limit")));
		usc.setCronScheduler(env.getProperty("scheduler.cron"));
		usc.setAlarmTolerance(Integer.parseInt(env.getProperty("alarm.tolerance")));
		usc.setNodeLimit(Integer.parseInt(env.getProperty("node.limit")));
		return usc;
	}
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(2);
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		return threadPoolTaskScheduler;
	}
}
