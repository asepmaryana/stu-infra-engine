package com.stu.infra.cdc.udp;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.stu.infra.cdc.dao.AppConstant;
import com.stu.infra.cdc.model.Node;
import com.stu.infra.cdc.model.OprStatus;
import com.stu.infra.cdc.service.NodeService;

public class StatusTriggerTask implements Runnable {
	
	private NodeService nodeService;
	
	public StatusTriggerTask(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	@Override
	public void run() {
		LocalDateTime now = new LocalDateTime();
		now.minusDays(1);
		List<Node> nodes = nodeService.findComLost(now);
		
		if(nodes == null || nodes.size() == 0) LoggerFactory.getLogger(StatusTriggerTask.class).info("------------- NO GENSET COMM LOST ---------------");
		else
		{
			for(Node node : nodes)
			{
				LoggerFactory.getLogger(StatusTriggerTask.class).warn(node.getName() + " - "+node.getPhone()+" COMM LOST.");
				node.setOprStatus(new OprStatus(AppConstant.COMMLOST_STATUS));
				nodeService.updateNode(node);
			}
		}
	}

}
