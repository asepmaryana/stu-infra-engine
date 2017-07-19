package com.stu.infra.cdc.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.IQueueSendingNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.stu.infra.cdc.config.SmsGwConfig;
import com.stu.infra.cdc.model.CdcMsg;
import com.stu.infra.cdc.model.Inbox;
import com.stu.infra.cdc.model.Modem;
import com.stu.infra.cdc.model.Outbox;
import com.stu.infra.cdc.service.InboxService;
import com.stu.infra.cdc.util.HttpClientUtil;
import com.stu.infra.cdc.util.SmsUtil;
import com.stu.infra.cdc.util.UdpPacketUtil;

public class Main {
	
	boolean shutdown = false;
	InboundNotification inboundNotification;
	OutboundNotification outboundNotification;
	CallNotification callNotification;
	QueueSendingNotification queueSendingNotification;
	InboundPollingThread inboundPollingThread;
	OutboundPollingThread outboundPollingThread;	
	List<Modem> modems;
	
	// configuration
	SmsGwConfig config;
	
	// services
	InboxService inboxService;
	
	class Shutdown extends Thread
	{
		@Override
        public void run()
        {
			LoggerFactory.getLogger(Shutdown.class).info("Shutting down SMS Server..");
			Main.this.shutdown = true;
			try
			{
				if (Service.getInstance().getQueueManager() != null) Service.getInstance().getQueueManager().removeAllPendingMessages();
				if (Service.getInstance().getQueueManager() != null) Service.getInstance().getQueueManager().removeAllDelayedMessages();
				Service.getInstance().stopService();
				if (Main.this.inboundPollingThread != null)
				{
					Main.this.inboundPollingThread.interrupt();
					Main.this.inboundPollingThread.join();
				}
				
				if (Main.this.outboundPollingThread != null)
				{
					Main.this.outboundPollingThread.interrupt();
					Main.this.outboundPollingThread.join();
				}
				
				SpringManager.getInstance().getApplicationContext().close();
			}
			catch (Exception e)
			{
				LoggerFactory.getLogger(Shutdown.class).error("Shutdown hook error.", e);				
			}
			LoggerFactory.getLogger(Shutdown.class).info("SMS Server stopped.");
        }
	}
	
	void readMessages()
	{
		List<InboundMessage> msgList = new ArrayList<InboundMessage>();
		try {
			Service.getInstance().readMessages(msgList, MessageClasses.ALL);
			LoggerFactory.getLogger(Main.class).info(msgList.size()+ " messages to be processed.");
            if (msgList.size() > 0)
            {
                for(InboundMessage msg : msgList)
                {
                	LoggerFactory.getLogger(Main.class).info("SMS RECEIVED ON " + msg.getGatewayId() + ":\n" +
                    msg.getOriginator() + "\n"+
                    msg.getText() + "\n" +
                    "-----------------------------------------------\n");
                	
                	Inbox sms = SmsUtil.translate(msg);
    				inboxService.save(sms);
    				
        			//process
    				(new SmsHandler(sms)).start();
    				Service.getInstance().deleteMessage(msg);
                    //if(config.isSmsDeleted()) Service.getInstance().deleteMessage(msg);
                }
            }
        }
        catch (Exception e)
        {
        	LoggerFactory.getLogger(Main.class).debug("SMSServer: reading messages exception! ", e);
        }
	}
	
	void sendMessages()
	{
		boolean foundOutboundGateway = false;
		for (org.smslib.AGateway gtw : Service.getInstance().getGateways())
		{
			if (gtw.isOutbound())
			{
				foundOutboundGateway = true;
				break;
			}
		}
		if (foundOutboundGateway)
		{
			List<Outbox> lists = null;
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JodaModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        	try {
				String request = mapper.writeValueAsString(new CdcMsg<Outbox>("GETOUT", null));
				String smsJson = UdpPacketUtil.sendPacket(config.getUdpHost(), config.getUdpPort(), config.getUdpTimeOut(), request);
				
				lists = mapper.readValue(smsJson, new TypeReference<List<Outbox>>(){});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LoggerFactory.getLogger(SmsHandler.class).error("Read SMS Outbox from UDP server error : " + e.getMessage());
			}
        	
        	if(lists != null)
        	{
        		if (config.getSendMode().equalsIgnoreCase(("sync"))) 
    			{
    				for(Outbox list : lists)
    				{
    					OutboundMessage msg = new OutboundMessage();
    					msg.setDate(new Date());
    					msg.setRecipient(list.getRecipient());
    					msg.setText(list.getText());
    					msg.setStatusReport(true);
    					try
    					{
    						Service.getInstance().sendMessage(msg);
    						list.setStatus('S');
    						list.setSentDate(new LocalDateTime(System.currentTimeMillis()));
    						
    						String request = mapper.writeValueAsString(new CdcMsg<Outbox>("SETOUT", list));
    						String respons = UdpPacketUtil.sendPacket(config.getUdpHost(), config.getUdpPort(), config.getUdpTimeOut(), request);    						
    					}
    					catch (Exception e)
    					{
    						LoggerFactory.getLogger(Main.class).error("Sending messages exception !", e, null);
    					}
    				}
    			}
        	}
        	else LoggerFactory.getLogger(Main.class).info("list of SMS outbox is null.");
		}
	}
	
	class InboundNotification implements IInboundMessageNotification
	{
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
			
			if( msgType == MessageTypes.STATUSREPORT ) {
				LoggerFactory.getLogger(InboundNotification.class).info("SMS STATUSREPORT: " + msg.toString());
			}
			else if( msgType == MessageTypes.INBOUND ) {
				
				LoggerFactory.getLogger(InboundNotification.class).info("SMS RECEIVED ON " + msg.getGatewayId() + ":\n" +
	                    msg.getOriginator() + "\n"+
	                    msg.getText() + "\n" +
	                    "-----------------------------------------------\n");
				
				Inbox sms = SmsUtil.translate(msg);
				inboxService.save(sms);
				
				//process
				(new SmsHandler(sms)).start();				
			}
			
			try { if(Main.this.config.isSmsDeleted()) Service.getInstance().deleteMessage(msg); }
			catch (Exception e) { LoggerFactory.getLogger(InboundNotification.class).error("Error deleting message:", e); }
		}
	}
	
	class OutboundNotification implements IOutboundMessageNotification 
	{
		public void process(AGateway gateway, OutboundMessage msg) {
			// TODO Auto-generated method stub
		}
	}
	
	class QueueSendingNotification implements IQueueSendingNotification
	{
		public void process(AGateway gateway, OutboundMessage msg) {
			// TODO Auto-generated method stub
		}
	}
	
	class CallNotification implements ICallNotification 
	{
		public void process(AGateway gateway, String callerId) {
			// TODO Auto-generated method stub
			LoggerFactory.getLogger(CallNotification.class).debug(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}
	
	class GatewayStatusHandler implements IGatewayStatusNotification
	{
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
			// TODO Auto-generated method stub
			LoggerFactory.getLogger(GatewayStatusHandler.class).debug(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}
	
	class InboundPollingThread extends Thread
	{
		@Override
        public void run()
        {
			try
            {
                while(!Main.this.shutdown)
                {
                	LoggerFactory.getLogger(InboundPollingThread.class).debug("Running");
                    readMessages();
                    Thread.sleep(Main.this.config.getInboundInterval() * 1000);
                }
            }
            catch (Exception e)
            {
            	LoggerFactory.getLogger(InboundPollingThread.class).error("Error", e);
            }
        }
	}
	
	class OutboundPollingThread extends Thread
	{
		@Override
        public void run()
        {
			int sendSmsCycle = 0;
			while(!Main.this.shutdown)
			{
				sendSmsCycle++;				
				LoggerFactory.getLogger(OutboundPollingThread.class).debug("Send SMS Cycle #" + sendSmsCycle);
				sendMessages();
				LoggerFactory.getLogger(OutboundPollingThread.class).debug("SMS Cycle #" + sendSmsCycle+" finished.");
				try { Thread.sleep(Main.this.config.getOutboundInterval() * 1000);} catch (Exception e) {}
			}
        }
	}
	
	class SmsHandler extends Thread
	{
		Inbox msg;
		
		public SmsHandler(Inbox msg)
		{
			this.msg = msg;
		}
		
		@Override
        public void run()
        {
			String smsMessage = msg.getText().trim();
            String sender = msg.getSender();
            if(sender.startsWith("62")) sender = "0" + sender.substring(2, sender.length());
            
            if(config.getSmsProtocolGw().equalsIgnoreCase("http")) {
            	String postUrl = config.getHttpUrl();
                Map<String, String> params = new HashMap<String, String>();
                params.put("sender", sender);
                params.put("message_date", SmsUtil.formatDate(msg.getMessageDate().toDate()));
                params.put("receive_date", SmsUtil.formatDate(msg.getReceiveDate().toDate()));            
                params.put("text", smsMessage);
                params.put("gateway_id", msg.getGatewayId());
                
                String resp = "";
                try { 
                	resp = HttpClientUtil.post(postUrl, params, null);
                	//if(!resp.toUpperCase().equals("OK")) smsDao.saveInbox(msg, null, null);
                }
                catch (Exception e) { 
                	resp = e.getMessage();
                }
                LoggerFactory.getLogger(SmsHandler.class).info("Request "+postUrl+" --> "+resp);
            }
            else if(config.getSmsProtocolGw().equalsIgnoreCase("udp")) {
            	ObjectMapper mapper = new ObjectMapper();
            	mapper.registerModule(new JodaModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            	try {
					String smsJson = mapper.writeValueAsString(new CdcMsg<Inbox>("SMSINB", msg));
					UdpPacketUtil.sendPacket(config.getUdpHost(), config.getUdpPort(), config.getUdpTimeOut(), smsJson);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LoggerFactory.getLogger(SmsHandler.class).error("Send JSON via UDP error.", e);
				}
            }
        }
	}
	
	public Main()
	{
		Runtime.getRuntime().addShutdownHook(new Shutdown());
		
		this.inboundNotification 	= new InboundNotification();
		this.outboundNotification 	= new OutboundNotification();
		this.callNotification 		= new CallNotification();
		this.queueSendingNotification = new QueueSendingNotification();
		this.inboundPollingThread 	= null;
		this.outboundPollingThread 	= null;		
	}
	
	private void run() throws Exception {
		
		config  = SpringManager.getInstance().getBean(SmsGwConfig.class);
		inboxService = SpringManager.getInstance().getBean(InboxService.class);
		
		Service.getInstance().setInboundMessageNotification(this.inboundNotification);
		Service.getInstance().setOutboundMessageNotification(this.outboundNotification);
		Service.getInstance().setCallNotification(this.callNotification);
		Service.getInstance().setQueueSendingNotification(this.queueSendingNotification);
		
		try {
			modems = config.getModems();
			if(modems != null && modems.size() > 0)
			{
				for(Modem modem : modems)
				{
					SerialModemGateway gateway = new SerialModemGateway (modem.getName(), modem.getPort(), modem.getBaudRate(), modem.getBrand(), modem.getModel());
					gateway.setProtocol(Protocols.PDU);
					gateway.setInbound(true);
					gateway.setOutbound(true);
					gateway.setSimPin(modem.getPin());
					Service.getInstance().addGateway(gateway);
					LoggerFactory.getLogger(Main.class).info("Adding gateway - " + modem.getName()+"@"+modem.getPort()+":"+modem.getBaudRate());
				}
				LoggerFactory.getLogger(Main.class).info("SMSServer starting...");
				Service.getInstance().startService();
				LoggerFactory.getLogger(Main.class).info("SMSServer started.");
				process();
			}
			else LoggerFactory.getLogger(Main.class).info("No modem found.");
		}
		catch (Exception e) {
			Service.getInstance().stopService();
			if (this.inboundPollingThread != null)
			{
				this.inboundPollingThread.interrupt();
				this.inboundPollingThread.join();
			}
			
			if (this.outboundPollingThread != null)
			{
				this.outboundPollingThread.interrupt();
				this.outboundPollingThread.join();
			}
			
		}
	}
	
	private void process() throws Exception
	{
		this.inboundPollingThread = new InboundPollingThread();
		this.inboundPollingThread.setName("SMSServer - InboundPollingThread");
		this.inboundPollingThread.start();
		this.outboundPollingThread = new OutboundPollingThread();
		this.outboundPollingThread.setName("SMSServer - OutboundPollingThread");
		this.outboundPollingThread.start();
		while (!this.shutdown)
			Thread.sleep(1000);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main app = new Main();
		try
		{
			app.run();
		}
		catch (Exception e)
		{
			LoggerFactory.getLogger(Main.class).error("SMSServer Error: ", e);
			try
			{
				Service.getInstance().stopService();
			}
			catch (Exception e1)
			{
				LoggerFactory.getLogger(Main.class).error("SMSServer error while shutting down: ", e1);
			}
		}
	}

}
