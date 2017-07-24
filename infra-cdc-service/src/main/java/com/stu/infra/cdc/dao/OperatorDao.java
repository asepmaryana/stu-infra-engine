package com.stu.infra.cdc.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Operator;

@Repository("operatorDao")
public class OperatorDao extends AbstractDao<Integer, Operator> {
	
	@SuppressWarnings("unchecked")
	public List<Operator> findByDay(int dayOfWeek) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("enabled", (short)1));
		if(dayOfWeek == Calendar.MONDAY) crit.add(Restrictions.eq("mon", (short)1));
		else if(dayOfWeek == Calendar.TUESDAY) crit.add(Restrictions.eq("tue", (short)1));
		else if(dayOfWeek == Calendar.WEDNESDAY) crit.add(Restrictions.eq("wed", (short)1));
		else if(dayOfWeek == Calendar.THURSDAY) crit.add(Restrictions.eq("thu", (short)1));
		else if(dayOfWeek == Calendar.FRIDAY) crit.add(Restrictions.eq("fri", (short)1));
		else if(dayOfWeek == Calendar.SATURDAY) crit.add(Restrictions.eq("sat", (short)1));
		else if(dayOfWeek == Calendar.SUNDAY) crit.add(Restrictions.eq("sun", (short)1));
		return crit.list();
	}
}
