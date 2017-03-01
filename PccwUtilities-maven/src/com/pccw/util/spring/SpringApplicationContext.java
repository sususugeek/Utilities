package com.pccw.util.spring;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringApplicationContext {
	static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String pBeanId) {
		return (T) applicationContext.getBean(pBeanId);
	}

	public static <T> T getBean(Class<T> pDaoClass) {
		return getBean(pDaoClass, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> pDaoClass, String pSuffix) {
		String beanName = pDaoClass.getName() + StringUtils.defaultString(pSuffix);
		try {
			return (T) applicationContext.getBean(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			try {
				beanName = pDaoClass.getSimpleName() + StringUtils.defaultString(pSuffix);
				return (T) applicationContext.getBean(beanName);
			} catch (NoSuchBeanDefinitionException e1) {
				beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1) + StringUtils.defaultString(pSuffix);
				return (T) applicationContext.getBean(beanName);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String pBeanId, Class<T> pBeanClass,
			boolean pIsPrototype,
			Map<String, Object> pPropertyValueMap,
			Map<String, String> pPropertyReferenceMap) {
		try {
			return (T) applicationContext.getBean(pBeanId);
		} catch (NoSuchBeanDefinitionException e) {
			
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(pBeanClass);
			builder.setScope(pIsPrototype ? BeanDefinition.SCOPE_PROTOTYPE : BeanDefinition.SCOPE_SINGLETON);
			if (pPropertyValueMap != null) {
				for (Map.Entry<String, Object> entry : pPropertyValueMap.entrySet()) {
					builder.addPropertyValue(entry.getKey(), entry.getValue()); 
				}
			}
			if (pPropertyReferenceMap != null) {
				for (Map.Entry<String, String> entry : pPropertyReferenceMap.entrySet()) {
					builder.addPropertyReference(entry.getKey(), entry.getValue()); 
				}
			}

			if (applicationContext instanceof ConfigurableApplicationContext) {
				((DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory()).registerBeanDefinition(pBeanId, builder.getBeanDefinition());
			}
		}

		return getBean(pBeanId);
	}
	
	public static String getMessage(String pCode, Object[] pParameter, Locale pLocale) {
		return applicationContext.getMessage(pCode, pParameter, pLocale);
	}

	public static String getMessage(String pCode, Object[] pParameter, String pDefaultString, Locale pLocale) {
		return applicationContext.getMessage(pCode, pParameter, pDefaultString, pLocale);
	}
}