package com.learnnfun.activemq.ackmode;

import javax.jms.JMSException;

public class SenderFactory {
	
	public Sender createSender(String name) throws JMSException{
		
		return new Sender(name);
	}
	
	public Sender createSender() throws JMSException{
		
		return new Sender();
	}
}
