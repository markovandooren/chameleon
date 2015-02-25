package org.aikodi.chameleon.aspect.core.weave.transform.list;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.Util;

public class AroundStep<P extends Element,T extends Element> extends AdvisedListFactory<P,T> {

	@Override
	public T transform() throws LookupException {
		ListTransformer<P, ?, T> transformer = transformer();
		Advice<?> clonedAdvice = Util.clone(advice());
		
		T next = transformer.nextList();
		int count = 0;
		for(P proceed: transformer.proceeds(clonedAdvice)) {
			T nextClone = rewrite(next,count);
//			T nextClone = next.clone();
			transformer.replace(proceed, nextClone);
			count++;
		}
		return transformer.toList(clonedAdvice);
	}

	// next list moet uni parents gezet hebben? Maar dan resolven de labels niet meer naar steps die ook in de list zitten.
	private T rewrite(T beh, int count) throws LookupException {
		T clone = (T)beh.clone();
		clone.setUniParent(beh.parent());
		for(Declaration decl: clone.descendants(Declaration.class)) {
			Signature signature = decl.signature();
			if(signature != null) {
				String newName = prefix(signature.name(),count);
				for(CrossReference cref: clone.descendants(CrossReference.class)) {
					if(cref.getElement().sameAs(decl)) {
						transformer().changeName(cref, newName);
					}
				}
				signature.setName(newName);
			}
		}
		clone.setUniParent(null);
		return clone;
	}
	
	private String prefix(String value, int count) {
		return value+count;
	}

}
