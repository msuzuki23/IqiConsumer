/**
* This Application will pull Messages from a Queue
* And Return the Message pulled
*
* @author: msuzuki
* @version: 1.0.0-SNAPSHOT
* @since: 2019-09-13
*/

package iqiConsumer;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessagePuller {
	
	String text;
	
	public String pull() {
	
		try {
			// URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is
			// on localhost
			String url = ActiveMQConnection.DEFAULT_BROKER_URL;

			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

			// Create a Connection
			javax.jms.Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create the Destination (Topic or Queue)
			Destination destination = session.createQueue("Iqi.mine");

			// Create a MessageConsumer from the Session to the Topic or Queue
			MessageConsumer consumer = session.createConsumer(destination);

			// Wait for a Message
			Message message = consumer.receive(1000);
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				text = textMessage.getText();
				System.out.println(Thread.currentThread().getName() + "Received: " + text);
			} else {
				System.out.println(Thread.currentThread().getName() + "Received: " + message);
			}
			consumer.close();
			session.close();
			connection.close();	
			// System.out.println("JOB PULLED from Queue: " + text);
		} catch (Exception e) {
			System.err.println("Error in Pulling Message from Queue: " + text);
//			e.printStackTrace();
			e.toString();
		} 
		return text;
	}
}
