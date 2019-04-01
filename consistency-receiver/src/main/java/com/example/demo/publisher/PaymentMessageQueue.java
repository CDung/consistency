package com.example.demo.publisher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;
// import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.*;

@Component
public class PaymentMessageQueue {
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
		    boolean autoAck = false;
		    channel.basicConsume("Payment.queue", autoAck, "myConsumerTag",
		         new DefaultConsumer(channel) {
		             @Override
		             public void handleDelivery(String consumerTag,
		                                        Envelope envelope,
		                                        AMQP.BasicProperties properties,
		                                        byte[] body)
		                 throws IOException
		             {
		                 long deliveryTag = envelope.getDeliveryTag();
		                 String message = new String(body, "UTF-8");

		                 //begin process		                

		                 //closed	
		                 int faildCounter=0;	                 
		                 while (faildCounter<CircuitBreaker.MAX_FAILD){		                
		                 	try {
			                	// call transaction ,end if success
			                	channel.basicAck(deliveryTag, false);
			                	return;												
							} catch (Exception e) {
								++faildCounter;
							}
						}	

						// opened
						Thread.sleep(CircuitBreaker.TIME_OUT); 

						// half-opend 
						// in this case : MAX_SUCCESS=1
						int probeCounter=0;
						while (probeCounter<CircuitBreaker.MAX_PROBE){		                
		                 	try {
		                 		Thread.sleep(CircuitBreaker.DELAY_TIME);
			                	// call transaction ,end if success	
			                	channel.basicAck(deliveryTag, false);
			                	return;												
							} catch (Exception e) {
								++faildCounter;
							}
						}

						// recovery by compensating transaction
						CompensatingTransactionQueue.produceMsg(message);
						channel.basicAck(deliveryTag, false);
		              
		             }
		         });  
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
}