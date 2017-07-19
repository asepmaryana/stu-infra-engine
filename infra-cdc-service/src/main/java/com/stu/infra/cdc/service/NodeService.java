package com.stu.infra.cdc.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stu.infra.cdc.dao.NodeDao;
import com.stu.infra.cdc.model.Node;

@Service("nodeService")
@Transactional
public class NodeService {
	
	@Autowired
	private NodeDao dao;
	
	@Transactional(readOnly = true)
	public Node findByPhone(String phone) {
		return dao.findByPhone(phone);
	}
	
	@Transactional(readOnly = false)
	public void updateNode(Node node) {
		dao.update(node);
	}
	
	@Transactional(readOnly = true)
	public List<Node> findFailed(LocalDateTime now, int maxNode) {
		return dao.findFailed(now, maxNode);
	}
	
	@Transactional(readOnly = true)
	public List<Node> findComLost(LocalDateTime now) {
		return dao.findComLost(now);
	}
}
