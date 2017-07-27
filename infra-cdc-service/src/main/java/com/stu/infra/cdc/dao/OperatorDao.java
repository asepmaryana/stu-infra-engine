package com.stu.infra.cdc.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Operator;

@Repository("operatorDao")
public class OperatorDao extends AbstractDao<Integer, Operator> {
	
	@SuppressWarnings("unchecked")
	public List<Operator> findByDay(int dayOfWeek) {
		Criteria crit = createEntityCriteria();
		Criterion enabled = Restrictions.eq("enabled", (short)1);
		Criterion day = null;
		if(dayOfWeek == Calendar.MONDAY) day = Restrictions.eq("mon", (short)1);
		else if(dayOfWeek == Calendar.TUESDAY) day = Restrictions.eq("tue", (short)1);
		else if(dayOfWeek == Calendar.WEDNESDAY) day = Restrictions.eq("wed", (short)1);
		else if(dayOfWeek == Calendar.THURSDAY) day = Restrictions.eq("thu", (short)1);
		else if(dayOfWeek == Calendar.FRIDAY) day = Restrictions.eq("fri", (short)1);
		else if(dayOfWeek == Calendar.SATURDAY) day = Restrictions.eq("sat", (short)1);
		else day = Restrictions.eq("sun", (short)1);
		crit.add(Restrictions.and(enabled, day));
		return crit.list();
	}
}
