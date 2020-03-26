package cho.carbon.hc.copframe.spring.binder;

import cho.carbon.hc.copframe.utils.Assert;
import cho.carbon.hc.copframe.utils.xml.XMLTag;
import cho.carbon.hc.copframe.utils.xml.XmlNode;

public class XMLNodeConverter {
	/**
	 * 简单地将节点转换成对象
	 * 对象属性和子节点的名称对应
	 * @param node
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseObject(XmlNode node, T source){
		Assert.notNull(node);
		Assert.notNull(source);
		FieldRefectUtils<T> utils = new FieldRefectUtils<T>((Class<T>) source.getClass(), (composite)->{
			XMLTag tag = composite.getFieldAnno(XMLTag.class);
			return tag.tagName();
		});
		utils.iterateField((propName, composite) -> {
			try {
				composite.setValue(source, node.getFirstElementText(propName));
			} catch (Exception e) {
			}
		});
		return source;
	}
}
