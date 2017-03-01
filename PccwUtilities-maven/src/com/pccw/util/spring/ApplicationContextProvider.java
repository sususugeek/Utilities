package com.pccw.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext pApplicationContext)
			throws BeansException {
		SpringApplicationContext.applicationContext = pApplicationContext;
	}
}