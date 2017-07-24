package com.stu.infra.cdc.dao;

import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.AlarmList;

@Repository("alarmListDao")
public class AlarmListDao extends AbstractDao<Integer, AlarmList> {

}
