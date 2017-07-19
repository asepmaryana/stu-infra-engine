package com.stu.infra.cdc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.stu.infra.cdc.model.Modem;

@Configuration
@ComponentScan(basePackages = "com.stu.infra.cdc")
@PropertySource(value = "classpath:sms-gw.properties")
public class SmsAppConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public SmsGwConfig smsGwConfig() {
		
		SmsGwConfig conf = new SmsGwConfig();
		conf.getModems().clear();
		
		int count = Integer.parseInt(env.getProperty("modem.count", "1"));
		for(int i=1; i<=count; i++)
		{
			if(env.getProperty("modem.enabled."+i).equals("1"))
			{
				Modem modem = new Modem();
				modem.setName(env.getProperty("modem.name."+i));
				modem.setPort(env.getProperty("modem.port."+i));
				modem.setBaudRate(Integer.parseInt(env.getProperty("modem.baud."+i)));
				modem.setBrand(env.getProperty("modem.brand."+i));
				modem.setModel(env.getProperty("modem.model."+i));
				modem.setPin(env.getProperty("modem.pin."+i));
				
				conf.getModems().add(modem);
			}
		}
		
		conf.setInboundInterval(Integer.parseInt(env.getProperty("inbound.interval")));
		conf.setOutboundInterval(Integer.parseInt(env.getProperty("outbound.interval")));
		if(env.getProperty("sms.delete").equalsIgnoreCase("yes")) conf.setSmsDeleted(true);
		else conf.setSmsDeleted(false);
		conf.setSendMode(env.getProperty("sms.send_mode"));
		conf.setSmsProtocolGw(env.getProperty("sms.protocol"));
		conf.setHttpUrl(env.getProperty("http.url"));
		conf.setUdpHost(env.getProperty("udp.host"));
		conf.setUdpPort(Integer.parseInt(env.getProperty("udp.port")));
		conf.setUdpTimeOut(Integer.parseInt(env.getProperty("udp.timeout")));
		
		return conf;
	}
	
}
