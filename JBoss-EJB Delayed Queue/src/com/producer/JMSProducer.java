package com.producer;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
/**
 * Do not forget to add MBean name="jboss.messaging.destination:service=Queue,name=DelayedQueue"
 * into JBoss configuration. See destinations-service.xml for more details.
 * 
 * @author Victor
 *
 */
@Stateless
public class JMSProducer implements JMSProducerLocal {
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "queue/DelayedQueue")
	private Queue delayedQueue;


	public void sendMessage(String message) {
		QueueConnection connection  = null;
		QueueSession session = null;
		QueueSender sender = null;
		try {
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)connectionFactory;
			connection = queueConnectionFactory.createQueueConnection();
			session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			sender = session.createSender(delayedQueue);

			TemporaryQueue replyQueue = session.createTemporaryQueue();
			MessageConsumer replyConsumer = session.createConsumer(replyQueue);

			TextMessage msg = session.createTextMessage(message);
			msg.setJMSReplyTo(replyQueue);
			long now = System.currentTimeMillis();
			msg.setLongProperty("JMS_JBOSS_SCHEDULED_DELIVERY", now + 1000 * 12);

			sender.send(msg);
	        System.out.println("\n>>>>>> JMSProducer >>>>>>\n \"" + message + "\" sent " + new Date()+ " \n<<<<<<<<<<<<<<<<<<<<<<<<<\n");

            TextMessage replyMsg = (TextMessage)replyConsumer.receive();
            System.out.println("\n>>>>>> JMSProducer >>>>>>\n Received reply: \"" + replyMsg.getText()+ "\"   "+ new Date()+"\n<<<<<<<<<<<<<<<<<<<<<<<<<\n");

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (Throwable e) {
		    e.printStackTrace();
		} finally {
			try {
				sender.close();
				session.close();
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
