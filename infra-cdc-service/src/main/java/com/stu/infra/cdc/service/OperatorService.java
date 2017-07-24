package com.stu.infra.cdc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.OperatorDao;
import com.stu.infra.cdc.model.Operator;

@Service("operatorService")
@Transactional
public class OperatorService {
	
	@Autowired
	private OperatorDao dao;
	
	@Transactional(readOnly = true)
	public List<Operator> findByDay(int dayOfWeek) {
		return dao.findByDay(dayOfWeek);
	}
}
