package com.stu.infra.cdc.udp.task;

import java.util.Calendar;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.stu.infra.cdc.model.Operator;
import com.stu.infra.cdc.model.Outbox;
import com.stu.infra.cdc.service.OperatorService;
import com.stu.infra.cdc.service.OutboxService;

public class ShiftNotificationTask implements Runnable {
	
	private OperatorService operatorService;
	private OutboxService outboxService;
	
	public ShiftNotificationTask(OperatorService operatorService, OutboxService outboxService) {
		this.operatorService = operatorService;
		this.outboxService = outboxService;
	}



	@Override
	public void run() {
		
		List<Operator> operators = operatorService.findByDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		if(operators.isEmpty() || operators == null)
			LoggerFactory.getLogger(ShiftNotificationTask.class).debug("No operator found.");
		else
		{
			for(Operator op : operators)
			{
				Outbox sms = new Outbox();
				sms.setRecipient(op.getPhone());
				sms.setText("CDC SHIFT SCHEDULE\rYour turn is on duty now.");
				sms.setStatus('U');
				sms.setCreateDate(new LocalDateTime());
				sms.setGatewayId("*");
				
				outboxService.save(sms);				
			}
		}
	}

}
