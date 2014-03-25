package com.consumer;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Do not forget to add MBean name="jboss.messaging.destination:service=Queue,name=DelayedQueue"
 * into JBoss configuration. See destinations-service.xml for more details.
 * 
 * @author Victor
 *
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/DelayedQueue"),
		@ActivationConfigProperty(propertyName = "messagingType",propertyValue = "javax.jms.MessageListener"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")
})
public class JMSListener implements MessageListener {

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

	public void onMessage(Message message) {
        Connection conn = null;
        Session session = null;
        MessageProducer replyProducer = null;
		TextMessage txtMsg = (TextMessage)message;
		try {
			System.out.println("\n>>>>> JMSListener >>>>>>\n \"" + txtMsg.getText()
					+ "\"  received "+ new Date() + "\n<<<<<<<<<<<<<<<<<<<<<\n");

            conn = connectionFactory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            replyProducer = session.createProducer(null);
            // Extract the ReplyTo destination
            Destination replyDestination = message.getJMSReplyTo();

            TextMessage replyMessage = session.createTextMessage("Hello my friend! This is my Reply!");

            replyProducer.send(replyDestination, replyMessage);

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
