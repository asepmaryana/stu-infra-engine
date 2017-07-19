package com.stu.infra.cdc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDateTime;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageEncodings;
import org.smslib.Message.MessageTypes;

import com.stu.infra.cdc.model.Inbox;

public class SmsUtil {
	
	public static String[] split(String msg)
    {
        String pesan[] = null;
        if( (msg.contains("\n")) && (msg.contains("=")) ) pesan = msg.split("\n");
        else if( (msg.contains("\r")) && (msg.contains("=")) ) pesan = msg.split("\r");        
        else pesan = new String[] { msg };
        return pesan;
    }
    
    // input  : 030214232123
    // output : 2014-02-03 23:21:23
    public static String parseTime(String time) {
        StringBuilder sb = new StringBuilder();
        if(time.length() == 12) {
            String dd = time.substring(0, 2);
            String mm = time.substring(2, 4);
            String yy = time.substring(4, 6);
            String hh = time.substring(6, 8);
            String ii = time.substring(8, 10);
            String ss = time.substring(10, 12);
            sb.append("20").append(yy).append("-").append(mm).append("-").append(dd).append(" ");
            sb.append(hh).append(":").append(ii).append(":").append(ss);
        }
        return sb.toString();
    }  
    
    public static String getTrapOID(String trapSpec)
    {
        if(trapSpec.equals("601")) return "digital-Input-Active";
        else if(trapSpec.equals("611")) return "digital-Input-Inactive";        
        else if(trapSpec.equals("603")) return "high-Temperature-Active";
        else if(trapSpec.equals("623")) return "high-temperature-Inactive";        
        else return "";
    }
    
    public static Date parseDate(String dateInString)
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	Date result = null;
    	try { result = formatter.parse(dateInString); }
    	catch (Exception ex) { ex.printStackTrace(); }
    	return result;
    }
    
    public static String formatDate(Date date)
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String result = "";
    	try { result = formatter.format(date); }
    	catch (Exception ex) { ex.printStackTrace(); }
    	return result;
    }
    
    public static Inbox translate(InboundMessage msg)
    {
    	Inbox sms = new Inbox();
    	String sender = msg.getOriginator();
        if(sender.startsWith("62")) sender = "0" + sender.substring(2, sender.length());
    	sms.setOriginator(sender);
		sms.setText(msg.getText());
		
		if(msg.getEncoding() == MessageEncodings.ENC7BIT) sms.setEncoding('7');
		else if(msg.getEncoding() == MessageEncodings.ENC8BIT) sms.setEncoding('8');
		
		if(msg.getType() == MessageTypes.STATUSREPORT) sms.setMessageType('S');
		else if(msg.getType() == MessageTypes.INBOUND) sms.setMessageType('I');
		else if(msg.getType() == MessageTypes.OUTBOUND) sms.setMessageType('O');
		else if(msg.getType() == MessageTypes.UNKNOWN) sms.setMessageType('U');
		else sms.setMessageType('W');
		
		sms.setMessageDate(new LocalDateTime(msg.getDate().getTime()));
		sms.setReceiveDate(new LocalDateTime(System.currentTimeMillis()));				
		sms.setGatewayId(msg.getGatewayId());
    	return sms;
    }
}
