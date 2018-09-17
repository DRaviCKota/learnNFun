package com.learnnfun.activemq.ackmode;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

public class AutoReceiver extends Receiver{

	public AutoReceiver() throws JMSException{
		
		super();
	}
	
	public AutoReceiver(String name) throws JMSException{
		
		super(name);
	}
	public AutoReceiver(String name, boolean transactionMode, int ackMode) throws JMSException {
		super(name, transactionMode, ackMode);
	}

	public static void main(String[] args) {
        System.out.println("Starting...");
        AutoReceiver autoReceiver = null;
        try{
        	autoReceiver = new AutoReceiver("AutoReceiver",false,Session.AUTO_ACKNOWLEDGE);
        	autoReceiver.recvMessages("QUEUE", "SAMPLE.QUEUE");
        }catch(JMSException | IOException ex){
        	
        	ex.printStackTrace();
		}finally{
        	if(autoReceiver != null) autoReceiver.close();
        }
        System.out.println("Ending...");
    }
}
