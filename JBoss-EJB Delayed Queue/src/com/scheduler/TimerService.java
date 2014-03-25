package com.scheduler;

import java.util.Date;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;

import org.jboss.ejb3.annotation.ResourceAdapter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.producer.JMSProducerLocal;

// Annotations taken from:
// http://www.len.ro/work/creating-a-quartz-job/
// Useful link:
// http://mauricedechateau.blogspot.com/2011/07/external-quartz-on-jboss-51.html
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "cronTrigger", propertyValue = "0/30 * * * * ?") })
@ResourceAdapter("quartz-ra.rar")
public class TimerService implements Job {
	@EJB
	JMSProducerLocal messageProducer;
	static int i = 0;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("\n>>>>>> TimerService >>>>>>\n Executing Job: " + new Date()+ " \n<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		messageProducer.sendMessage("Message #"+i++);
	}
}
