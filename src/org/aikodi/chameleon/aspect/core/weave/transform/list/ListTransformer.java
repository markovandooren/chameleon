package org.aikodi.chameleon.aspect.core.weave.transform.list;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.aspect.core.weave.transform.JoinPointTransformer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <P> The type of the proceed element.
 * @param <E>
 * @param <T>
 */
public interface ListTransformer<P extends Element, E extends Element, T extends Element> extends JoinPointTransformer<E, T>{

	public T toList(Advice<?> advice);
	
	public T nextList() throws LookupException;
	
	public void replace(P proceed, T clones);
	
	public List<P> proceeds(Advice advice);
	
	public Advice<?> advice();

	public void addAfter(T first, T after);
	
	public void addBefore(T first, T before);

	public void changeName(CrossReference cref, String newName);
}
