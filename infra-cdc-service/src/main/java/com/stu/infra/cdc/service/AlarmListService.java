package com.stu.infra.cdc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.AlarmListDao;
import com.stu.infra.cdc.model.AlarmList;

@Service("alarmListService")
@Transactional
public class AlarmListService {
	
	@Autowired
	private AlarmListDao dao;
	
	@Transactional(readOnly=true)
	public AlarmList findById(int alarmListId) {
		return dao.getByKey(new Integer(alarmListId));
	}
}
