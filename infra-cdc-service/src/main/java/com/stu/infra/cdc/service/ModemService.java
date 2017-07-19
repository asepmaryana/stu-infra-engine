package com.stu.infra.cdc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.ModemDao;
import com.stu.infra.cdc.model.Modem;

@Service("modemService")
@Transactional
public class ModemService {
	
	@Autowired
	private ModemDao modemDao;
	
	public void saveModem(Modem modem) {
		modemDao.persist(modem);
	}     	
	
	public Modem findById(int id) {
		return modemDao.findById(id);
	}
	
	public Modem findByPort(String port) {
		return modemDao.findByPort(port);
	}
	
	public List<Modem> findAllModems() {
		return modemDao.findAllModems();
	}
	
	public boolean isModemUnique(String port) {
		Modem modem = findByPort(port);
		return modem == null;
	}
}
