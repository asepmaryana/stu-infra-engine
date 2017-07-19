package com.stu.infra.cdc.dao;

import org.springframework.stereotype.Repository;

import com.stu.infra.cdc.model.Datalog;

@Repository("datalogDao")
public class DatalogDao extends AbstractDao<Long, Datalog> {
	
}
