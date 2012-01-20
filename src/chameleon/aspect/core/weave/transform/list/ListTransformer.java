package chameleon.aspect.core.weave.transform.list;

import java.util.List;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.weave.transform.JoinPointTransformer;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

public interface ListTransformer<P extends Element, E extends Element, T extends Element<?>> extends JoinPointTransformer<E, T>{

	public T toList(Advice advice);
	
	public T nextList() throws LookupException;
	
	public void replace(P proceed, T clones);
	
	public List<P> proceeds(Advice advice);
	
	public Advice advice();

	public void addAfter(T first, T after);
	
	public void addBefore(T first, T before);

	public void changeName(CrossReference cref, String newName);
}
