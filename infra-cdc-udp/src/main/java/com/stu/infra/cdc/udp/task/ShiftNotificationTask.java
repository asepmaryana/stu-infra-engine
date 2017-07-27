package com.stu.infra.cdc.udp.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.stu.infra.cdc.model.Config;
import com.stu.infra.cdc.model.Operator;
import com.stu.infra.cdc.model.Outbox;
import com.stu.infra.cdc.service.ConfigService;
import com.stu.infra.cdc.service.OperatorService;
import com.stu.infra.cdc.service.OutboxService;

public class ShiftNotificationTask implements Runnable {
	
	private ConfigService configService;
	private OperatorService operatorService;
	private OutboxService outboxService;
	
	public ShiftNotificationTask(ConfigService configService, OperatorService operatorService, OutboxService outboxService)
	{
		this.configService = configService;
		this.operatorService = operatorService;
		this.outboxService = outboxService;
	}

	@Override
	public void run() {
		Config config = configService.getConfig();
		DateFormat df = new SimpleDateFormat("HH:mm");
		String now = df.format(new Date());
		if(now.equals(config.getShiftTime()))
		{
			List<Operator> operators = operatorService.findByDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
			if(operators.isEmpty() || operators == null)
				LoggerFactory.getLogger(ShiftNotificationTask.class).info("No operator found.");
			else
			{
				for(Operator op : operators)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("CDC SYSTEM REMINDER\n");
					sb.append("-------------------\n\n");
					sb.append("Hello, ").append(op.getName()).append("\n");
					sb.append("Today is your CDC monitoring schedule !\n\n");
					sb.append("Thank You.");
					
					Outbox sms = new Outbox();
					sms.setRecipient(op.getPhone());
					sms.setText(sb.toString());
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
		else LoggerFactory.getLogger(ShiftNotificationTask.class).debug("Schedule: "+config.getShiftTime()+", now: "+now+" --> ignored.");
	}

}
