package com.stu.infra.cdc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.InboxDao;
import com.stu.infra.cdc.model.Inbox;

@Service("inboxService")
@Transactional
public class InboxService {

	@Autowired
	private InboxDao inboxDao;
	
	public Long save(Inbox msg) {
		return inboxDao.save(msg);
	}
	
	public List<Inbox> findUnsent(int limit) {
		return inboxDao.findUnsent(limit);
	}
	
	public void update(Inbox msg) {
		inboxDao.update(msg);
	}
}
