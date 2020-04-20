package cho.carbon.hc.copframe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CPFSpringContextLoader implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	static Logger logger = LoggerFactory.getLogger(CPFSpringContextLoader.class);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(CPFSpringContextLoader.applicationContext == null) {
			CPFSpringContextLoader.applicationContext = applicationContext;
		}else {
			logger.debug("重复初始化。FilePublisher类已经初始化过applicationContext，将不再初始化新的容器对象");
		}
	}
	
	public static ApplicationContext getContext() {
		return applicationContext;
	}

}
