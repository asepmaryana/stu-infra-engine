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
import com.stu.infra.cdc.service.OperatorService;
import com.stu.infra.cdc.service.OutboxService;

public class ShiftNotificationTask implements Runnable {
	
	private Config config;
	private OperatorService operatorService;
	private OutboxService outboxService;
	
	public ShiftNotificationTask(OperatorService operatorService, OutboxService outboxService, Config config)
	{
		this.config = config;
		this.operatorService = operatorService;
		this.outboxService = outboxService;
	}

	@Override
	public void run() {
		DateFormat df = new SimpleDateFormat("HH:mm");
		String now = df.format(new Date());
		if(now.equals(config.getShiftTime()))
		{
			List<Operator> operators = operatorService.findByDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
			if(operators.isEmpty() || operators == null)
				LoggerFactory.getLogger(ShiftNotificationTask.class).debug("No operator found.");
			else
			{
				for(Operator op : operators)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("Hi, ").append(op.getName()).append("\r");
					sb.append("Today is your schedule !");
					Outbox sms = new Outbox();
					sms.setRecipient(op.getPhone());
					sms.setText(sb.toString());
					sms.setStatus('U');
					sms.setCreateDate(new LocalDateTime());
					sms.setGatewayId("*");
					
					outboxService.save(sms);				
				}
			}
		}
		else LoggerFactory.getLogger(ShiftNotificationTask.class).info(now+" ignored.");
	}

}
