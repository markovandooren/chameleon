package be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.transform.list;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
