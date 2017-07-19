package com.stu.infra.cdc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.OutboxDao;
import com.stu.infra.cdc.model.Outbox;

@Service("outboxService")
@Transactional
public class OutboxService {
	
	@Autowired
	private OutboxDao outboxDao;
	
	@Transactional(readOnly=true)
	public List<Outbox> getUnsent(int limit) {
		return outboxDao.findUnprocessed(limit);
	}
	
	public void update(Outbox msg) {
		outboxDao.update(msg);		
	}
}
