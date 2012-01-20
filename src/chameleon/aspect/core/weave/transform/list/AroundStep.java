package chameleon.aspect.core.weave.transform.list;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

public class AroundStep<P extends Element,T extends Element<T>> extends AdvisedListFactory<P,T> {

	@Override
	public T transform() throws LookupException {
		ListTransformer<P, ?, T> transformer = transformer();
		Advice<?,?> clonedAdvice = advice().clone();
		
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
		T clone = beh.clone();
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
