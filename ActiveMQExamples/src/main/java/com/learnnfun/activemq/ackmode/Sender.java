package com.learnnfun.activemq.ackmode;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	private static ConnectionFactory factory;
	private static Connection connection;
	private Session session;
	private String name;
	
	static{
		
		factory=new ActiveMQConnectionFactory();
		try {
			connection=factory.createConnection();
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
	}
	
	public Sender() throws JMSException{
		this("Default");
		session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	}
	
	public Sender(String name) throws JMSException{
		
		this.name=name;
		session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	}
	
	public Sender(String name,boolean transactionMode,int ackMode) throws JMSException{
		
		this.name=name;
		session=connection.createSession(transactionMode,Session.AUTO_ACKNOWLEDGE);
	}
	
	public void sendMessages(String destinationType,String destinationName) throws JMSException{
	
		if(session == null) throw new JMSException("Session not found");
		Destination destination = "QUEUE".equals(destinationType)?session.createQueue(destinationName) : session.createTopic(destinationName);
		MessageProducer producer = session.createProducer(destination);
		for(int i=1;i<=10;i++){
		
			System.out.println(name+" sending message#"+i);
			Message message = session.createTextMessage(name+" sending message#"+i);
			message.setJMSMessageID("MessageID->"+i);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(message);
		}
	}

	public void close(){
		try{
			if(session != null) session.close();
			if(connection != null) connection.close();
		}catch(JMSException ex){
			
			ex.printStackTrace();
		}
	}
	public static void main(String...args) throws JMSException{
		
		Sender s=null;
		try{

			s = new Sender();
			s.sendMessages("QUEUE","SAMPLE.QUEUE");
		}catch(JMSException ex){
			ex.printStackTrace();
		}finally{
			
			if(s != null) s.close();
		}
	}
}
