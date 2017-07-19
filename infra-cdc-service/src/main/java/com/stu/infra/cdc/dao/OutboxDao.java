package com.stu.infra.cdc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Outbox;

@Repository("outboxDao")
public class OutboxDao extends AbstractDao<Long, Outbox> {
	
	@SuppressWarnings("unchecked")
	public List<Outbox> findUnprocessed(int limit) {
		Criteria crit = getSession().createCriteria(Outbox.class);
		crit.add(Restrictions.eq("status", 'U'));
		crit.addOrder(Order.asc("id"));
		crit.setFirstResult(0);
		crit.setMaxResults(limit);
		return crit.list();
	}
}
