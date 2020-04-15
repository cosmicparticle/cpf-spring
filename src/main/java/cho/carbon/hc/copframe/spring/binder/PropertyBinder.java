package cho.carbon.hc.copframe.spring.binder;

import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;

import cho.carbon.hc.copframe.utils.Assert;

/**
 * 
 * <p>Title: HttpRequestBinder</p>
 * <p>Description: </p><p>
 * 用于将数据绑定到对象的属性当�?
 * </p>
 * @author Copperfield Zhang
 * @date 2017�?2�?6�? 下午4:09:28
 */
public class PropertyBinder {
	private Object target;
	
	@SuppressWarnings("rawtypes")
	private List<PropertyValueConverter> pvConverterList = new ArrayList<PropertyValueConverter>(); 
	
	static Logger logger = LoggerFactory.getLogger(HttpServletRequest.class);
	
	/**
	 * 构�?�一个绑定器
	 * @param target
	 */
	public PropertyBinder(Object target) {
		Assert.notNull(target);
		this.target = target;
		//默认添加�?个属性反射转换器
		addPvConverter(ReflectSetValueConverter.getInstance());
	}
	
	/**
	 * 根据字段-值对象，通过反射获得字段的setter方法，并绑定对象的属�?
	 * @param pvs 字段-值对象�?�类似于Map<String, Object>
	 * @throws BindException 当绑定失败时，会抛出异常
	 */
	public void bindIgnoreException(PropertyValues pvs){
		for (PropertyValue pv : pvs.getPropertyValues()) {
			PropertyValueConvertContext context = new DefaultPropertyValueConvertContext(this.target, pv);
			setPropertyValue(context);
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setPropertyValue(PropertyValueConvertContext context){
		PropertyValue pv = context.getPropertyValue();
		if(pv != null){
			//遍历�?有转换器，注意该序列添加转化器元素时时放到前面，因此越后面添加的转换器越先遍历到
			for (PropertyValueConverter pvConverter : this.pvConverterList) {
				if(pvConverter.check(context)){
					try {
						//转换�?
						Object convertValue = pvConverter.convert(pv.getValue(), context);
						//设置属�??
						pvConverter.setPropertyValue(this.target, convertValue, context);
					} catch (PropertyValueConvertException e) {
						//只记录除了反射set属�?��?�的转换器抛出的异常
						if(!(pvConverter instanceof ReflectSetValueConverter)){
							logger.error(e.toString());
						}
					}
					//只要check成功，就会停止遍�?
					break;
				}
			}
		}
	}
	
	/**
	 * 添加属�?��?�转换器。注意添加方式是插入到序列的头部，越后面添加的转换器优先级越�?
	 * @param pvConverter
	 */
	@SuppressWarnings("rawtypes")
	public void addPvConverter(PropertyValueConverter pvConverter){
		this.pvConverterList.add(0, pvConverter);
	}
	/**
	 * 添加属�?��?�转换器数组。注意添加方式是插入到序列的头部，越后面添加的转换器优先级越�?
	 * @param propertyValueConverters
	 */
	@SuppressWarnings("rawtypes")
	public void addPvConverter(PropertyValueConverter[] propertyValueConverters) {
		for (PropertyValueConverter propertyValueConverter : propertyValueConverters) {
			addPvConverter(propertyValueConverter);
		}
		
	}
	
}
