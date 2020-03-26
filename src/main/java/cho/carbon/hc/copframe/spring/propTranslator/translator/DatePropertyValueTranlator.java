package cho.carbon.hc.copframe.spring.propTranslator.translator;


import java.util.Date;

import javax.annotation.Resource;

import cho.carbon.hc.copframe.spring.propTranslator.GetPropertyValueComposite;
import cho.carbon.hc.copframe.spring.propTranslator.SetPropertyValueComposite;
import cho.carbon.hc.copframe.utils.date.FrameDateFormat;

/**
 * 
 * <p>Title: DatePropertyValueTranlator</p>
 * <p>Description: </p><p>
 * 日期字段转换器
 * </p>
 * @author Copperfield Zhang
 * @date 2017年12月28日 下午4:41:35
 */
public class DatePropertyValueTranlator extends AbstractPropertyValueTranslator{

	@Resource
	FrameDateFormat dateFormat;
	
	@Override
	public boolean canGet(GetPropertyValueComposite composite) {
		return Date.class.isAssignableFrom(composite.getPropertyType());
	}

	@Override
	public boolean canSet(SetPropertyValueComposite composite) {
		return Date.class.isAssignableFrom(composite.getPropertyType()) 
				&& !composite.isPropertyNull() 
				&& composite.getToSetValue() instanceof String;
	}
	
	@Override
	public Object getValue(GetPropertyValueComposite composite) {
		return dateFormat.formatDate((Date) composite.getPropertyValue());
	}

	@Override
	public void setValue(SetPropertyValueComposite composite) {
		String value = (String) composite.getToSetValue();
		Date date = dateFormat.parse(value);
		composite.setValueByExpression(date);
	}

	protected void setDateFormat(FrameDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}
