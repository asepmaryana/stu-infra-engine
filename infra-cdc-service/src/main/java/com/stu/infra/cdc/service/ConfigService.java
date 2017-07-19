package com.stu.infra.cdc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.ConfigDao;
import com.stu.infra.cdc.model.Config;

@Service("configService")
@Transactional
public class ConfigService {
	
	@Autowired
	private ConfigDao dao;
	
	public void save(Config config) {
		dao.save(config);
	}
	
	public Config getConfig() {
		return dao.getConfig();
	}
}
