package com.stu.infra.cdc.udp;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.LoggerFactory;

import com.stu.infra.cdc.model.Node;
import com.stu.infra.cdc.service.NodeService;

public class AlarmTriggerTask implements Runnable {
	
    private NodeService nodeService;
    
    private int maxNode;
    
    private int maxMinute;
    
    public AlarmTriggerTask(NodeService nodeService, int maxNode, int maxMinute) {
        this.nodeService = nodeService;
        this.maxNode = maxNode;
        this.maxMinute = maxMinute;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println(new Date()+" Runnable Task with "+message
		//          +" on thread "+Thread.currentThread().getName());
		
		LocalDateTime now = new LocalDateTime();
		List<Node> nodes = nodeService.findFailed(now, maxNode);
		if(nodes == null || nodes.size() == 0) LoggerFactory.getLogger(AlarmTriggerTask.class).info("------------- NO GENSET FAILED ---------------");
		else
		{
			for(Node node : nodes)
			{				
				// genset on failed
				if(node.getGensetStatus().intValue() == 0 && 
						node.getNextOn().toDate().getTime() < now.toDate().getTime()) {
					node.setGensetOnFail((short)1);
					LoggerFactory.getLogger(AlarmTriggerTask.class).info(node.getName() + " - "+node.getPhone()+" Genset On Fail");
				}
				else if(node.getGensetStatus().intValue() == 1 && 
						node.getNextOff().toDate().getTime() < now.toDate().getTime()) {
					node.setGensetOffFail((short)1);
					LoggerFactory.getLogger(AlarmTriggerTask.class).info(node.getName() + " - "+node.getPhone()+" Genset Off Fail");
				}
				node.setTrapUpdated(now);
				nodeService.updateNode(node);				
			}			
		}
	}

}
