package com.stu.infra.cdc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Inbox;

@Repository("inboxDao")
public class InboxDao extends AbstractDao<Long, Inbox> {
	
	public Inbox findById(long id) {
		return getByKey(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Inbox> findAllInbox() {
		return getSession().createQuery("from Inbox order by messageDate asc").list();
	}
}
