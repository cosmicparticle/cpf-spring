package cho.carbon.hc.copframe.spring.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {

	private static final Map<String, String> propertyMap = new HashMap<String, String>();

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			PropertyPlaceholder.propertyMap.put(keyStr, value);
		}
	}

	public static String getProperty(String name) {
		String value = PropertyPlaceholder.propertyMap.get(name);
		if(value == null){
			throw new PropertyUndefinedException(name);
		}
		return value;
	}
}