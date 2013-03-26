package be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.transform.list;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.Advice;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public abstract class AdvisedListFactory<P extends Element, T extends Element> {

	public AdvisedListFactory() {
	}

	public void setTransformer(ListTransformer<P,?,T> transformer) {
		_transformer = transformer;
	}
	
	public ListTransformer<P,?,T> transformer() {
		return _transformer;
	}
	private ListTransformer<P,?,T> _transformer;
	
	public abstract T transform() throws LookupException;

	public Advice<?> advice() {
		return transformer().advice();
	}

}
