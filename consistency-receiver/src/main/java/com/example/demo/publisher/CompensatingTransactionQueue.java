package com.example.demo.publisher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.*;

//cmd :rabbitmq-plugins enable rabbitmq_management --online
@Component
public class CompensatingTransactionQueue {
	private static Connection connection;
	private static Channel channel;
	
	public static void init() {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("rabbit1");	
	    factory.setPort(5672);
	    factory.setUsername("guest");
	    factory.setPassword("guest");
		try {
			connection =  factory.newConnection();
		    channel =  connection.createChannel();
		    channel.exchangeDeclare("CompensatingTransaction.exchange", "fanout",true);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void produceMsg(String msg){
	    try {
			channel.basicPublish("CompensatingTransaction.exchange", "", null, msg.getBytes("UTF-8"));
			System.out.println("Reciver Service request a compensating transaction: " + msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		    	   			 
	}	
	
	
}