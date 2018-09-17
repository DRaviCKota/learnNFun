package com.learnnfun.activemq.ackmode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class Receiver {
	
	protected static ConnectionFactory factory;
	protected static Connection connection;
	protected Session session;
	protected String name;
	protected AtomicInteger ai = new AtomicInteger(0);
	
	static{
		
		factory=new ActiveMQConnectionFactory();
		try {
			connection=factory.createConnection();
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
	}
	
	public Receiver() throws JMSException{
		this("Default");
		session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	}
	
	public Receiver(String name) throws JMSException{
		
		this.name=name;
		session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	}
	
	public Receiver(String name,boolean transactionMode,int ackMode) throws JMSException{
		
		this.name=name;
		session=connection.createSession(transactionMode,ackMode);
	}
	
	public void recvMessages(String destinationType,String destinationName) throws JMSException, IOException{
	
		if(session == null) throw new JMSException("Session not found");
		Destination destination = "QUEUE".equals(destinationType)?session.createQueue(destinationName) : session.createTopic(destinationName);
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(m->{
				try {
					if(ai.get() == 2){
						
						int x=1/0;
					}
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

	protected void close(){
		try{
			if(session != null) session.close();
			if(connection != null) connection.close();
		}catch(JMSException ex){
			
			ex.printStackTrace();
		}
	}
}
