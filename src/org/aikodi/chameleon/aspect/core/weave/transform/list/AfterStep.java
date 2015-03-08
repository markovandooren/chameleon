package org.aikodi.chameleon.aspect.core.weave.transform.list;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

public class AfterStep<P extends Element, T extends Element> extends AdvisedListFactory<P,T> {

	@Override
	public T transform() throws LookupException {
		ListTransformer<P, ?, T> transformer = transformer();
		T afters = (T)transformer.toList(advice()).clone();
		T result = transformer.nextList();
		transformer.addAfter(result,afters);
		return result;
	}

}
