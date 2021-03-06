package com.stu.infra.cdc.udp;

import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.stu.infra.cdc.config.UdpServerConfig;
import com.stu.infra.cdc.dao.AppConstant;
import com.stu.infra.cdc.model.AlarmList;
import com.stu.infra.cdc.model.CdcMsg;
import com.stu.infra.cdc.model.Config;
import com.stu.infra.cdc.model.Datalog;
import com.stu.infra.cdc.model.Inbox;
import com.stu.infra.cdc.model.Node;
import com.stu.infra.cdc.model.Operator;
import com.stu.infra.cdc.model.OprStatus;
import com.stu.infra.cdc.model.Outbox;
import com.stu.infra.cdc.service.AlarmListService;
import com.stu.infra.cdc.service.ConfigService;
import com.stu.infra.cdc.service.DatalogService;
import com.stu.infra.cdc.service.InboxService;
import com.stu.infra.cdc.service.NodeService;
import com.stu.infra.cdc.service.OperatorService;
import com.stu.infra.cdc.service.OutboxService;

public class SmsUdpResponder implements Runnable, AppConstant {
	
	private ConfigService configService;
	
	private NodeService nodeService;
	
	private DatalogService datalogService;
	
	private InboxService inboxService;
	
	private OutboxService outboxService;
	
	private OperatorService operatorService;
	
	private AlarmListService alarmListService;
	
	private byte[] sendData = new byte[1024];
	
	private DatagramSocket socket;
	
	private DatagramPacket receivePacket;
	
	private Config config;
	
	public SmsUdpResponder(DatagramSocket socket, DatagramPacket packet) {
		this.socket = socket;
		this.receivePacket = packet;
		this.configService = SpringManager.getInstance().getBean(ConfigService.class);
		this.nodeService = SpringManager.getInstance().getBean(NodeService.class);
		this.datalogService = SpringManager.getInstance().getBean(DatalogService.class);
		this.inboxService = SpringManager.getInstance().getBean(InboxService.class);
		this.outboxService = SpringManager.getInstance().getBean(OutboxService.class);
		this.alarmListService = SpringManager.getInstance().getBean(AlarmListService.class);
		this.operatorService = SpringManager.getInstance().getBean(OperatorService.class);
	}

	public byte[] getSendData() {
		return sendData;
	}

	public void setSendData(byte[] sendData) {
		this.sendData = sendData;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public DatagramPacket getReceivePacket() {
		return receivePacket;
	}

	public void setReceivePacket(DatagramPacket receivePacket) {
		this.receivePacket = receivePacket;
	}

	public void run() {
		config = configService.getConfig();
		
		String smsJson = new String(receivePacket.getData());
		
		InetAddress ipAddress = receivePacket.getAddress();
		int port = receivePacket.getPort();
		LoggerFactory.getLogger(SmsUdpResponder.class).info("Receive: " + smsJson+"\nFrom: "+ipAddress.getHostAddress()+"\nPort: "+port);
		
		if(smsJson.contains("SMSINB")) smsInb(smsJson, ipAddress, port);
		else if(smsJson.contains("GETOUT")) getOut(smsJson, ipAddress, port);
		else if(smsJson.contains("SETOUT")) setOut(smsJson, ipAddress, port);
	}
	
	/// SMS Inbox handler
	private void smsInb(String smsJson, InetAddress ipAddress, int port) {
		ObjectMapper mapper = new ObjectMapper();
		String response = "FAILED";
		try {
			CdcMsg<Inbox> inb = mapper.readValue(smsJson, new TypeReference<CdcMsg<Inbox>>(){});			
			Inbox msg = inb.getData();
			//Inbox msg = mapper.readValue(smsJson, Inbox.class);
			String smsMessage = msg.getText().trim();
            String sender = msg.getSender();
            if(sender.startsWith("62")) sender = "0" + sender.substring(2, sender.length());
            msg.setOriginator(sender);
            
            LoggerFactory.getLogger(SmsUdpResponder.class).debug("JSON to Java OK --> " + msg.getSender());
            
            Node node = nodeService.findByPhone(sender);
            //response  = "Unregister phone " + sender;
            response  = "UNREGISTER";
            
            if(node == null) LoggerFactory.getLogger(SmsUdpResponder.class).warn(response);
            else 
            {
            	node.setOprStatus(new OprStatus(AppConstant.OPERATIONAL_STATUS));
            	
            	LoggerFactory.getLogger(SmsUdpResponder.class).debug("Registered phone " + sender);            	
            	String messages[] = {};
        		if(smsMessage.contains("\r")) messages = smsMessage.split("\r");
            	else messages = smsMessage.split("\n");
        		
        		if(messages.length > 18)
            	{
            		LoggerFactory.getLogger(SmsUdpResponder.class).debug("Valid SMS");
            		
            		// set node last update from timestamp of operator
            		Date updated = msg.getMessageDate().toDate();
            		Datalog data = new Datalog();
                	data.setNode(node);
                	data.setdTime(new LocalDateTime(updated.getTime()));
                	
            		for(String param : messages)
            		{
            			param = param.trim();
            			if(param.isEmpty()) continue;
            			
            			String key = param.substring(0, param.indexOf("="));
            			String val = param.substring(param.indexOf("=")+1, param.trim().length());
            			
            			LoggerFactory.getLogger(SmsUdpResponder.class).debug(key + " --> " + val);
            			
            			if(key.toUpperCase().equals("2")) {
            				String gvs [] = val.split(",");
            				if(gvs.length == 3) {
            					node.setGensetVr(new BigDecimal(gvs[0]));
            					node.setGensetVs(new BigDecimal(gvs[1]));
            					node.setGensetVt(new BigDecimal(gvs[2]));
            					
            					data.setGensetVr(node.getGensetVr());
            					data.setGensetVs(node.getGensetVs());
            					data.setGensetVt(node.getGensetVt());                					
            				}
            			}
            			else if(key.toUpperCase().equals("3")) {
            				node.setBattVolt(new BigDecimal(val));
            				data.setBattVolt(node.getBattVolt());                				
            			}
            			else if(key.toUpperCase().equals("4")) {
            				node.setGensetBattVolt(new BigDecimal(val));
            				data.setGensetBattVolt(node.getGensetBattVolt());                				
            			}
            			else if(key.toUpperCase().equals("5")) {
            				node.setTimerGensetOn(new Integer(val));
            				data.setTimerGensetOn(node.getTimerGensetOn());
            			}
            			else if(key.toUpperCase().equals("6")) {
            				node.setTimerGensetOff(new Integer(val));
            				data.setTimerGensetOff(node.getTimerGensetOff());
            			}
            			else if(key.toUpperCase().equals("7")) {
            				node.setRunHour(new BigDecimal(val));
            				data.setRunHour(new BigDecimal(val));
            			}
            			else if(key.toUpperCase().equals("8")) {
            				node.setRunHourTresh(new BigDecimal(val));
            				data.setRunHourTresh(new BigDecimal(val));
            			}
            			else if(key.toUpperCase().equals("9")) {
            				node.setGensetStatus(new Short(val));
            				data.setGensetStatus(node.getGensetStatus());
            			}
            			// genset on fail
            			else if(key.toUpperCase().equals("10")) {
            				if(val.equals("1") && node.getGensetOnFail() == 0) createNotifSms(GENSET_ON_FAIL, node, updated);
            				node.setGensetOnFail(new Short(val));
            				data.setGensetOnFail(node.getGensetOnFail());
            			}
            			// genset off fail
            			else if(key.toUpperCase().equals("11")) {
            				if(val.equals("1") && node.getGensetOffFail() == 0) createNotifSms(GENSET_OFF_FAIL, node, updated);
            				node.setGensetOffFail(new Short(val));
            				data.setGensetOffFail(node.getGensetOffFail());
            			}
            			else if(key.toUpperCase().equals("12")) {
            				node.setLowFuel(new Short(val));
            				data.setLowFuel(node.getLowFuel());                				
            			}
            			else if(key.toUpperCase().equals("13")) {
            				node.setRectiFail(new Short(val));
            				data.setRectiFail(node.getRectiFail());
            			}
            			else if(key.toUpperCase().equals("14")) {
            				node.setBattLow(new Short(val));
            				data.setBattLow(node.getBattLow());
            			}
            			else if(key.toUpperCase().equals("15")) {
            				node.setSinHighTemp(new Short(val));
            				data.setSinHighTemp(node.getSinHighTemp());
            			}
            			else if(key.toUpperCase().equals("16")) {
            				node.setEngHighTemp(new Short(val));
            				data.setEngHighTemp(node.getEngHighTemp());
            			}
            			else if(key.toUpperCase().equals("17")) {
            				node.setOilPressure(new Short(val));
            				data.setOilPressure(node.getOilPressure());
            			}
            			else if(key.toUpperCase().equals("18")) {
            				node.setMaintainStatus(new Short(val));
            				data.setMaintainStatus(node.getMaintainStatus());
            			}
            			else if(key.toUpperCase().equals("19")) {
            				node.setRectiStatus(new Short(val));
            				data.setRectiStatus(node.getRectiStatus());
            			}
            		}
            		
            		if(updated != null)
            		{
            			if(node.getUpdatedAt() != null)
            			{
            				if(updated.getTime() > node.getUpdatedAt().toDate().getTime()) {
                				node.setUpdatedAt(new LocalDateTime(updated.getTime()));
                				nodeService.updateNode(node);
                				response = "NODE_UPDATED";
                			}
                			else {
                				data.setdTime(new LocalDateTime(updated.getTime()));
                				datalogService.saveDatalog(data);
                				response = "DATALOG_SAVED";
                			}
            			}
            			else
            			{
            				node.setUpdatedAt(new LocalDateTime(updated.getTime()));
            				nodeService.updateNode(node);
            				response = "NODE_UPDATED";
            			}
            			//msg.setMessageDate(new LocalDateTime(updated.getTime()));
            			inboxService.save(msg);
            		}
            	}
            	else {
            		response = "INVALID_SMS";
            		LoggerFactory.getLogger(SmsUdpResponder.class).info(response);
            	}
            }
            sendData = response.getBytes();
    		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
    		socket.send(sendPacket);
		}
		catch (Exception e) {
			response = e.getMessage();
			LoggerFactory.getLogger(SmsUdpResponder.class).error("Error SMSINB Process.", e);
		}
	}	
	
	private void getOut(String smsJson, InetAddress ipAddress, int port) {
		ObjectMapper mapper = new ObjectMapper();
		String response = "OK";
		try
		{
			UdpServerConfig config = SpringManager.getInstance().getBean(UdpServerConfig.class);
			List<Outbox> msgs = outboxService.getUnsent(config.getSmsLimit());
			mapper.registerModule(new JodaModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			response = mapper.writeValueAsString(msgs);
			sendData = response.getBytes();
    		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
    		socket.send(sendPacket);
		}
		catch (Exception e) {
			response = e.getMessage();
			LoggerFactory.getLogger(SmsUdpResponder.class).error("Error GETOUT Process.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setOut(String smsJson, InetAddress ipAddress, int port) {
		ObjectMapper mapper = new ObjectMapper();
		String response = "OK";
		try
		{
			CdcMsg<Outbox> inb = mapper.readValue(smsJson, new TypeReference<CdcMsg<Outbox>>(){});
			Outbox outbox = inb.getData();
			outboxService.update(outbox);
			
			sendData = response.getBytes();
    		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
    		socket.send(sendPacket);
		}
		catch (Exception e) {
			response = e.getMessage();
			LoggerFactory.getLogger(SmsUdpResponder.class).error("Error SETOUT Process.", e);
		}
	}
	
	private void createNotifSms(int alarmListId, Node node, Date date)
	{
		AlarmList alarm = alarmListService.findById(alarmListId);
		
		if(alarm != null) {
			// read shift time
			LocalDateTime local = new LocalDateTime(date.getTime());			
			int hour	= new Integer(config.getShiftTime().substring(0, 2)).intValue();
			if(local.getHourOfDay() < hour) local = local.minusDays(1);
			
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			StringBuilder sb = new StringBuilder();
			sb.append("CDC NOTIFICATION SYSTEM\n");
			sb.append("-------------------\n\n");
			sb.append(alarm.getName().toUpperCase()).append("\n");
			sb.append(node.getName()).append("\n");
			sb.append(df.format(date));
			String msg = sb.toString();
			
			// get name of day
			List<Operator> operators	= operatorService.findByDay(local.getDayOfWeek());
			for(Operator op : operators)
			{
				Outbox sms = new Outbox();
				sms.setRecipient(op.getPhone());
				sms.setText(msg);
				sms.setCreateDate(new LocalDateTime());
				sms.setSentDate(null);
				sms.setReplyDate(null);
				sms.setReplyText(null);
				sms.setRequestId(null);
				sms.setStatus('U');
				sms.setGatewayId("*");
				sms.setMessageType('N');
				
				outboxService.save(sms);
			}
		}
	}
}
