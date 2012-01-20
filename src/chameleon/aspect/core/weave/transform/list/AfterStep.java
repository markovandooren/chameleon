package chameleon.aspect.core.weave.transform.list;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.util.Util;

public class AfterStep<P extends Element, T extends Element<T>> extends AdvisedListFactory<P,T> {

	@Override
	public T transform() throws LookupException {
		ListTransformer<P, ?, T> transformer = transformer();
		T afters = transformer.toList(advice()).clone();
		T result = transformer.nextList();
		transformer.addAfter(result,afters);
		return result;
	}

}
