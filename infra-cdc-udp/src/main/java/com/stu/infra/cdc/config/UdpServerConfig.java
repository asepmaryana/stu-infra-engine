package com.stu.infra.cdc.config;

public class UdpServerConfig {
	
	private int serverPort;
	
	private int smsLimit;
	
	private String cronScheduler;
	
	private int alarmTolerance;
	
	private int nodeLimit;
	
	public UdpServerConfig() {}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getSmsLimit() {
		return smsLimit;
	}

	public void setSmsLimit(int smsLimit) {
		this.smsLimit = smsLimit;
	}

	public String getCronScheduler() {
		return cronScheduler;
	}

	public void setCronScheduler(String cronScheduler) {
		this.cronScheduler = cronScheduler;
	}

	public int getAlarmTolerance() {
		return alarmTolerance;
	}

	public void setAlarmTolerance(int alarmTolerance) {
		this.alarmTolerance = alarmTolerance;
	}

	public int getNodeLimit() {
		return nodeLimit;
	}

	public void setNodeLimit(int nodeLimit) {
		this.nodeLimit = nodeLimit;
	}
	
}
