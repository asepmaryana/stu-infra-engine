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
		Criterion operational = Restrictions.eq("oprStatus.id", AppConstant.OPERATIONAL_STATUS);
		
		// failed off, if genset is on and now > next off
		Criterion genOn  	= Restrictions.and(operational, Restrictions.eq("gensetStatus", (short)1));
		Criterion nextOff 	= Restrictions.and(genOn, Restrictions.lt("nextOff", now));
		
		// failed on, if genset is off and next on time < now time	
		Criterion genOff  	= Restrictions.and(operational, Restrictions.eq("gensetStatus", (short)0));
		Criterion nextOn   	= Restrictions.and(genOff, Restrictions.lt("nextOn", now));
		
		criteria.add(Restrictions.or(nextOff, nextOn));
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
		criteria.add(Restrictions.eq("oprStatus.id", AppConstant.OPERATIONAL_STATUS));
		criteria.add(Restrictions.gt("updatedAt", now));
		
		return criteria.list();
	}
}
