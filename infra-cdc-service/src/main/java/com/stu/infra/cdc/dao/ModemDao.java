package com.stu.infra.cdc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Modem;

@Repository("modemDao")
public class ModemDao extends AbstractDao<Integer, Modem> {
	
	public Modem findById(int id) {
		return getByKey(id);
	}
	
	public Modem findByPort(String port) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("port", port));
		return (Modem) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Modem> findAllModems() {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("enabled", true));
		return criteria.list();
	}
}
