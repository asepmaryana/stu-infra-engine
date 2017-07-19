package com.stu.infra.cdc.service;

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
	
	public void save(Inbox msg) {
		inboxDao.save(msg);
	}
}
