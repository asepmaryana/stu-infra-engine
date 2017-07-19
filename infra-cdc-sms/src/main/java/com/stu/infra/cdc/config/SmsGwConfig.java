package com.stu.infra.cdc.config;

import java.util.ArrayList;
import java.util.List;

import com.stu.infra.cdc.model.Modem;

public class SmsGwConfig {
	
	private int inboundInterval  	= 10;
	
	private int outboundInterval 	= 10;
	
	private boolean smsDeleted		= true;
	
	private String smsProtocolGw	= "http";
	
	private String httpUrl			= "http://infra.sinergiteknologi.com/api/sms/trap";
	
	private String udpHost			= "212.1.213.133";
	
	private int udpPort				= 9871;
	
	private int udpTimeOut			= 30;
	
	private List<Modem> modems		= new ArrayList<Modem>(0);
	
	private String sendMode			= "async";
	
	public SmsGwConfig() {}

	public int getInboundInterval() {
		return inboundInterval;
	}

	public void setInboundInterval(int inboundInterval) {
		this.inboundInterval = inboundInterval;
	}

	public int getOutboundInterval() {
		return outboundInterval;
	}

	public void setOutboundInterval(int outboundInterval) {
		this.outboundInterval = outboundInterval;
	}

	public boolean isSmsDeleted() {
		return smsDeleted;
	}

	public void setSmsDeleted(boolean smsDeleted) {
		this.smsDeleted = smsDeleted;
	}

	public String getSmsProtocolGw() {
		return smsProtocolGw;
	}

	public void setSmsProtocolGw(String smsProtocolGw) {
		this.smsProtocolGw = smsProtocolGw;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getUdpHost() {
		return udpHost;
	}

	public void setUdpHost(String udpHost) {
		this.udpHost = udpHost;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public int getUdpTimeOut() {
		return udpTimeOut;
	}

	public void setUdpTimeOut(int udpTimeOut) {
		this.udpTimeOut = udpTimeOut;
	}

	public List<Modem> getModems() {
		return modems;
	}

	public void setModems(List<Modem> modems) {
		this.modems = modems;
	}

	public String getSendMode() {
		return sendMode;
	}

	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	
}
