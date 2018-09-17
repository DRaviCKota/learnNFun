package com.learnnfun.activemq;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {

	private ConnectionFactory connectionFactory;
	private Connection conn;
	private Session session;
	private Queue queue;
	private Topic topic;
	
	
	public static void main( String[] args ) throws JMSException{
    	
		Producer p = null;
		try{
			if(args.length != 0){
				
				switch(args[0]){
		        
			        case "REQUEST_REPLY_QUEUE" :{
			        	p = new Producer();
			        	p.sendMessagesToQueue("SAMPLE.QUEUE");
			        	break;
			        }
			        default:{
			        	
			        	System.out.println("DO NOTHING");
			        }
				}
			}
		}catch(JMSException jmx){
			
			jmx.printStackTrace();
		}finally{
			
			try{
				if(p != null){
					
					p.close();
				}
			}catch(JMSException ex){ ex.printStackTrace();}
			
		}
		System.out.println("DO NOTHING");
	}
	
	private void sendMessagesToQueue(String queueName) throws JMSException {
		
		connectionFactory = new ActiveMQConnectionFactory();
		conn = connectionFactory.createConnection();
		session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
		queue = session.createQueue(queueName);
		
		MessageProducer messageProducer = session.createProducer(queue);
		System.out.println("Enter messages");
		try(Scanner scanner = new Scanner(System.in)){
		
			while(scanner.hasNextLine()){
				
				String txt = scanner.nextLine();
				//System.out.println("Sending -> "+txt);
				messageProducer.send(session.createTextMessage(txt));
				if("END".equals(txt)){
					
					break;
				}
			}
		}
	}
	
	private void close() throws JMSException{
		
		if(session != null) session.close();
		if(conn != null) conn.close();
	}
}