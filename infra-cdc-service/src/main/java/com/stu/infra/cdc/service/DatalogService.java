package com.stu.infra.cdc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.DatalogDao;
import com.stu.infra.cdc.model.Datalog;

@Service("datalogService")
@Transactional
public class DatalogService {
	
	@Autowired
	private DatalogDao dao;
	
	public void saveDatalog(Datalog data) {
		if(data != null) dao.save(data);
	}
}
