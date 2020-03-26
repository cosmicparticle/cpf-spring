package cho.carbon.hc.copframe.spring.propTranslator.translator;

import cho.carbon.hc.copframe.spring.propTranslator.GetPropertyValueComposite;
import cho.carbon.hc.copframe.spring.propTranslator.SetPropertyValueComposite;

public class DefaultPropertyValueTranslator extends AbstractPropertyValueTranslator{

	@Override
	public boolean canGet(GetPropertyValueComposite composite) {
		return true;
	}

	@Override
	public boolean canSet(SetPropertyValueComposite composite) {
		return true;
	}

	@Override
	public Object getValue(GetPropertyValueComposite composite) {
		return composite.getPropertyValue();
	}

	@Override
	public void setValue(SetPropertyValueComposite composite) {
		composite.setValueByExpression(composite.getToSetValue());
	}

	
	
	

}
