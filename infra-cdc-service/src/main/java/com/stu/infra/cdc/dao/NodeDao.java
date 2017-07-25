package com.stu.infra.cdc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Node;

@Repository("nodeDao")
public class NodeDao extends AbstractDao<Integer, Node> {
	
	
	public Node findByPhone(String phone) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("phone", phone));
		return (Node) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Node> findFailed(LocalDateTime now, int limit) {
		
		Criteria criteria = createEntityCriteria();
		
		/// where operationalStatus = 1 
		Criterion mainOff = Restrictions.eq("oprStatus.id", AppConstant.OPERATIONAL_STATUS);
		
		/// where maintainStatus = 0 and gensetStatus = 0
		Criterion genOn  = Restrictions.and(mainOff, Restrictions.eq("gensetStatus", (short)0));
		
		/// where maintainStatus = 0 and gensetStatus = 0 and nextOn < now
		Criterion expOn   = Restrictions.and(genOn, Restrictions.lt("nextOn", now));
		
		Criterion genOff  = Restrictions.and(mainOff, Restrictions.eq("gensetStatus", (short)1));
		Criterion expOff   = Restrictions.and(genOff, Restrictions.lt("nextOff", now));
		
		criteria.add(Restrictions.or(expOn, expOff));
		criteria.addOrder(Order.asc("id"));		
		if(limit > 0)
		{
			criteria.setFirstResult(0);
			criteria.setMaxResults(limit);
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Node> findFailedOn(LocalDateTime now) {
		
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("maintainStatus", 0));
		criteria.add(Restrictions.eq("gensetStatus", 0));
		criteria.add(Restrictions.lt("nextOn", now));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Node> findFailedOff(LocalDateTime now) {
			
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("maintainStatus", 0));
		criteria.add(Restrictions.eq("gensetStatus", 1));
		criteria.add(Restrictions.lt("nextOff", now));
			
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Node> findComLost(LocalDateTime now) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.gt("updatedAt", now));
		criteria.add(Restrictions.eq("oprStatus.id", AppConstant.OPERATIONAL_STATUS));
		
		return criteria.list();
	}
}
