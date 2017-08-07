package com.stu.infra.cdc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
	
	@SuppressWarnings("unchecked")
	public List<Inbox> findUnsent(int limit) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("status", 'U'));
		crit.addOrder(Order.asc("messageDate"));
		if(limit > 0)
		{
			crit.setFirstResult(0);
			crit.setMaxResults(limit);
		}
		return crit.list();
	}
}
