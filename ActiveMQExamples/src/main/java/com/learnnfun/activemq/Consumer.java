package com.learnnfun.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.Message;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Consumer {
	
	private static int counter=0;
	private static boolean terminate;
	private ConnectionFactory connectionFactory;
	private Connection conn;
	private Session session;
	private Queue queue;
	private Topic topic;
	//private MessageConsumer messageConsumer;
	
	private void close() throws JMSException{
		
		if(session != null) session.close();
		if(conn != null) conn.close();
	}
	
	public static void main( String[] args ) throws JMSException{
    	
		final Consumer c = new Consumer();
		try{
			if(args.length != 0){
				
				switch(args[0]){
		        
			        case "REQUEST_REPLY_QUEUE" :{
			        	c.receiveMessagesFromQueue("SAMPLE.QUEUE");
			        	break;
			        }
			        default:{
			        	
			        	System.out.println("DO NOTHING");
			        }
				}
				while(!terminate){
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}catch(JMSException jmx){
			
			jmx.printStackTrace();
		}finally{
			
			try{
				if(c != null){
					
					c.close();
				}
			}catch(JMSException ex){ ex.printStackTrace();}
			
		}
		//Adding shutdown hook
		/*Runtime.getRuntime().addShutdownHook(new Thread(){
			
			public void run(){
				
				try{
					if(c != null){
						System.out.println("Calling shutdown");
						c.close();
					}
				}catch(JMSException ex){ ex.printStackTrace();}
			}
		});*/
		System.out.println("DO NOTHING");
	}
	
	private void receiveMessagesFromQueue(String queueName) throws JMSException {
		
		connectionFactory = new ActiveMQConnectionFactory();
		conn = connectionFactory.createConnection();
		session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
		queue = session.createQueue(queueName);
		MessageConsumer messageConsumer = session.createConsumer(queue);
		messageConsumer.setMessageListener(new CustomMessageListener());
		conn.start();
		//Message message = messageConsumer.receive(); // This is a blocked call.
		//Message message = messageConsumer.receive(5000); // Call blocks for 5000 millisec
		//Message message = messageConsumer.receiveNoWait(); // consumes message if immediately available, else does not wait
		
		System.out.println("Hello");
	}
	
	private class CustomMessageListener implements MessageListener{
		private int counter=0;
		@Override
		public void onMessage(Message message) {
			
			try {
				System.out.println("Counter ->"+(++counter));
				System.out.println("Received -> "+((TextMessage)message).getText());
				int i=1/0;
				
			} catch (JMSException e) {
				
				e.printStackTrace();
			}
		}
	}
}
