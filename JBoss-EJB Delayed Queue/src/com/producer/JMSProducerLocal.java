package com.producer;

import javax.ejb.Local;

@Local
public interface JMSProducerLocal {
	
	void sendMessage(String message);

}
