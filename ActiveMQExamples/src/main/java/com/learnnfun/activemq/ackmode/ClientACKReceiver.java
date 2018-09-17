package com.learnnfun.activemq.ackmode;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ClientACKReceiver extends Receiver{

	public ClientACKReceiver() throws JMSException{
		
		super();
	}
	
	public ClientACKReceiver(String name) throws JMSException{
		
		super(name);
	}
	public ClientACKReceiver(String name, boolean transactionMode, int ackMode) throws JMSException {
		super(name, transactionMode, ackMode);
	}

	@Override
	public void recvMessages(String destinationType,String destinationName) throws JMSException, IOException{
		
		if(session == null) throw new JMSException("Session not found");
		Destination destination = "QUEUE".equals(destinationType)?session.createQueue(destinationName) : session.createTopic(destinationName);
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(m->{
				try {
					if(ai.get() == 3){
						
						m.acknowledge();
					}
					/*if(ai.get() == 5){
						
						session.recover();
					}*/
					System.out.println(name+" received message. "+ai.getAndIncrement()+" ->"+((TextMessage)m).getText());
				} catch (JMSException e) {
					
					e.printStackTrace();
				}});
		connection.start();
		InputStreamReader aISR = new InputStreamReader(System.in);
        char aAnswer = ' ';
        do {
            aAnswer = (char) aISR.read();
            if ((aAnswer == 'r') || (aAnswer == 'R')) {
                session.recover();
            }
        } while ((aAnswer != 'q') && (aAnswer != 'Q'));
	}
	public static void main(String[] args) {
        System.out.println("Starting...");
        ClientACKReceiver clientACKReceiver = null;
        try{
        	clientACKReceiver = new ClientACKReceiver("clientACKReceiver",false,Session.CLIENT_ACKNOWLEDGE);
        	clientACKReceiver.recvMessages("QUEUE", "SAMPLE.QUEUE");
        }catch(JMSException | IOException ex){
        	
        	ex.printStackTrace();
		}finally{
        	if(clientACKReceiver != null) clientACKReceiver.close();
        }
        System.out.println("Ending...");
    }
}
