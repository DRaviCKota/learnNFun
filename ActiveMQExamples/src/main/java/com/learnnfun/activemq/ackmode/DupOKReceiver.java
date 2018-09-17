package com.learnnfun.activemq.ackmode;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

public class DupOKReceiver  extends Receiver{

	public DupOKReceiver() throws JMSException{
		
		super();
	}
	
	public DupOKReceiver(String name) throws JMSException{
		
		super(name);
	}
	public DupOKReceiver(String name, boolean transactionMode, int ackMode) throws JMSException {
		super(name, transactionMode, ackMode);
	}

	public static void main(String[] args) {
        System.out.println("Starting...");
        DupOKReceiver dupOKReceiver = null;
        try{
        	dupOKReceiver = new DupOKReceiver("dupOkReceiver",false,Session.DUPS_OK_ACKNOWLEDGE);
        	dupOKReceiver.recvMessages("QUEUE", "SAMPLE.QUEUE");
        }catch(JMSException | IOException ex){
        	
        	ex.printStackTrace();
		}finally{
        	if(dupOKReceiver != null) dupOKReceiver.close();
        }
        System.out.println("Ending...");
    }
}
