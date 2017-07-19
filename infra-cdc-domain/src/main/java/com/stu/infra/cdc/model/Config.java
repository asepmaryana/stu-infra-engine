package com.stu.infra.cdc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="config")
public class Config implements java.io.Serializable {
		
	private static final long serialVersionUID = 6670859333130766211L;

	@Id
	private String id;
	
	@Column(name = "server_port", nullable = false)
	private Integer serverPort			= 9876;
	
	@Column(name = "cron_scheduler", nullable = false, length=50)
	private String cronScheduler		= "0 */5 * * * *";
	
	// in minutes
	@Column(name = "alarm_tolerance", nullable = false)
	private Integer alarmTolerance		= 30;
	
	@Column(name = "node_limit", nullable = false)
	private Integer nodeLimit			= 100;
	
	@Column(name = "sms_limit", nullable = false)
	private Integer smsLimit			= 50;
	
	public Config() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public String getCronScheduler() {
		return cronScheduler;
	}

	public void setCronScheduler(String cronScheduler) {
		this.cronScheduler = cronScheduler;
	}

	public Integer getAlarmTolerance() {
		return alarmTolerance;
	}

	public void setAlarmTolerance(Integer alarmTolerance) {
		this.alarmTolerance = alarmTolerance;
	}

	public Integer getNodeLimit() {
		return nodeLimit;
	}

	public void setNodeLimit(Integer nodeLimit) {
		this.nodeLimit = nodeLimit;
	}

	public Integer getSmsLimit() {
		return smsLimit;
	}

	public void setSmsLimit(Integer smsLimit) {
		this.smsLimit = smsLimit;
	}
	
}
