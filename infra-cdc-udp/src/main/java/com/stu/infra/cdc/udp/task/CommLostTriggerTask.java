package com.stu.infra.cdc.udp.task;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.stu.infra.cdc.dao.AppConstant;
import com.stu.infra.cdc.model.Config;
import com.stu.infra.cdc.model.Node;
import com.stu.infra.cdc.model.OprStatus;
import com.stu.infra.cdc.service.ConfigService;
import com.stu.infra.cdc.service.NodeService;

public class CommLostTriggerTask implements Runnable {
	
	private ConfigService configService;
	private NodeService nodeService;
	
	public CommLostTriggerTask(ConfigService configService, NodeService nodeService) {
		this.configService = configService;
		this.nodeService = nodeService;
	}
	
	@Override
	public void run() {
		Config config = configService.getConfig();
		LocalDateTime now = new LocalDateTime();
		now = now.minusHours(config.getCommLostTime().intValue());
		List<Node> nodes = nodeService.findComLost(now);
		LoggerFactory.getLogger(CommLostTriggerTask.class).debug("find comm lost where status = 1 and updatedAt < "+now + " --> " + nodes.size());
		if(nodes.isEmpty() || nodes == null) 
			LoggerFactory.getLogger(CommLostTriggerTask.class).debug("COMM LOST is empty.");
		else
		{
			for(Node node : nodes)
			{
				LoggerFactory.getLogger(CommLostTriggerTask.class).info(node.getName() + " was comm lost.");
				node.setOprStatus(new OprStatus(AppConstant.COMMLOST_STATUS));
				node.setUpdatedAt(node.getUpdatedAt());
				nodeService.updateNode(node);
			}
		}
		LoggerFactory.getLogger(CommLostTriggerTask.class).debug("Comm lost end.");
	}

}
