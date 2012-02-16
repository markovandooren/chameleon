package chameleon.aspect.core.weave.transform.list;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public class BeforeStep<P extends Element,T extends Element> extends AdvisedListFactory<P,T> {

	@Override
	public T transform() throws LookupException {
		ListTransformer<P, ?, T> transformer = transformer();
		T list = transformer.toList(advice());
		T befores = (T) list.clone();
		T result = transformer.nextList();
		transformer.addBefore(result,befores);
		return result;
	}

}
