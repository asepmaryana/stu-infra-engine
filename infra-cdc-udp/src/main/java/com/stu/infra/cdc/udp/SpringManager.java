package com.stu.infra.cdc.udp;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.stu.infra.cdc.config.SmsUdpConfig;

public class SpringManager {
	
	private AbstractApplicationContext applicationContext;
	
	public SpringManager() {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(SmsUdpConfig.class);
		context.registerShutdownHook();
		
		applicationContext = context;
	}

	public AbstractApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    private static SpringManager INSTANCE;
    
    public static SpringManager getInstance() {
    	if (SpringManager.INSTANCE == null) SpringManager.INSTANCE = new SpringManager();
    	return SpringManager.INSTANCE;
    }
}
