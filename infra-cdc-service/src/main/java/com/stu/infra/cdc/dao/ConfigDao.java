package com.stu.infra.cdc.dao;

import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Config;

@Repository("configDao")
public class ConfigDao extends AbstractDao<String, Config> {
	
	public Config getConfig() {
		return (Config) getSession().createQuery("from Config").uniqueResult();
	}
}
