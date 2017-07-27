package com.stu.infra.cdc.udp.task;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.stu.infra.cdc.model.Config;
import com.stu.infra.cdc.model.Node;
import com.stu.infra.cdc.service.ConfigService;
import com.stu.infra.cdc.service.NodeService;

public class AlarmTriggerTask implements Runnable {
	
	private ConfigService configService;
    private NodeService nodeService;
    
    public AlarmTriggerTask(ConfigService configService, NodeService nodeService) {
    	this.configService = configService;
        this.nodeService = nodeService;
    }
    
	@Override
	public void run() {
		Config config = configService.getConfig();
		LocalDateTime now = new LocalDateTime();
		List<Node> nodes = nodeService.findFailed(now, config.getNodeLimit());
		if(nodes == null || nodes.size() == 0) LoggerFactory.getLogger(AlarmTriggerTask.class).debug("------------- NO GENSET FAILED ---------------");
		else
		{
			for(Node node : nodes)
			{				
				// genset is on and now > nextOn and gensetOnFail = 0
				if(node.getGensetStatus().intValue() == 0 && 
						now.toDate().getTime() > node.getNextOn().toDate().getTime() &&
						node.getGensetOnFail() == 0) {
					node.setGensetOnFail((short)1);
					node.setGensetOffFail((short)0);
					node.setTrapUpdated(now);
					nodeService.updateNode(node);
					LoggerFactory.getLogger(AlarmTriggerTask.class).info(node.getName() + " - "+node.getPhone()+" Genset On Fail. Now: "+now+", nextOn: "+node.getNextOn());
				}
				// genset is off and now > nextOff and gensetOffFail = 0
				else if(node.getGensetStatus().intValue() == 1 && 
						now.toDate().getTime() > node.getNextOff().toDate().getTime() &&
						node.getGensetOffFail() == 0) {
					node.setGensetOnFail((short)0);
					node.setGensetOffFail((short)1);
					node.setTrapUpdated(now);
					nodeService.updateNode(node);
					LoggerFactory.getLogger(AlarmTriggerTask.class).info(node.getName() + " - "+node.getPhone()+" Genset Off Fail. Now: "+now+", nextOff: "+node.getNextOff());
				}
			}			
		}
	}

}
