package org.aikodi.chameleon.util.association;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.rejuse.association.OrderedMultiAssociation;

/**
 * An n-ary ordered association end. Use objects of this class for n-ary relations between model elements.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the elements whose 'parent' association end can be connected to this association end.
 */
public class Multi<T extends Element> extends OrderedMultiAssociation<Element, T> implements ChameleonAssociation<T> {

	public Multi(Element element) {
		this(element,3);
		_max = -1;
	}
	
	private Multi(Element element, int initialSize) {
		super(element,initialSize);
	}
	
	public Multi(Element element, int min, int max) {
		this(element);
		if(min > max && max >= 0) {
			throw new IllegalArgumentException();
		}
		_min=min;
		_max=max;
	}
	
	public Multi(Element element, String role) {
		this(element);
		setRole(role);
	}
	
	public Multi(Element element, int min, int max, String role) {
		this(element,min,max);
		setRole(role);
	}
	
	public int min() {
		return _min;
	}
	
	public int max() {
		return _max > 0 ? _max : Integer.MAX_VALUE;
	}
	
	private int _min;
	private int _max;
	
	public boolean hasFiniteMaximumArity() {
		return _max > 0;
	}
	
	@Override
   public Verification verify() {
		Verification result = Valid.create();
      int size = size();
      if(size < min()) {
         result = result.and(new BasicProblem(getObject(), "At least " + min()+" " + role() + " were expected, but only "+size+ " are defined."));
      }
		if(hasFiniteMaximumArity()) {
			if(size > max()) {
				result = result.and(new BasicProblem(getObject(), "At most " + max()+" " + role() +" were expected, but "+size+ " are defined."));
			}
		}
		return result;
	}
	
	@Override
   public String role() {
		return _role;
	}
	
	public void setRole(String role) {
		_role = role;
	}
	
	private String _role = "elements";

	@Override
	public void pairWise(ChameleonAssociation<?> other, BiConsumer<Element, Element> consumer) {
	   if(other instanceof Multi) {
	      Multi<?> otherMulti = (Multi<?>) other;
	      int size = size();
         for(int i=0; i<size;i++) {
	         Element first = elementAt(i);
	         Element second = otherMulti.elementAt(i);
	         consumer.accept(first, second);
	      }
	   } else {
         if(other == null) {
            throw new IllegalArgumentException("The given association is null.");
         } else {
            throw new IllegalArgumentException("The given association is of the incorrect type. Expected: "+getClass().getName()+", but received: "+other.getClass().getName());
         }
      }
	}
	
	@Override
   public void mapTo(ChameleonAssociation<?> other, Function<Element, Element> mapper) {
      if(other instanceof Multi) {
         Multi<?> otherMulti = (Multi<?>) other;
         int size = size();
         for(int i=0; i<size;i++) {
            Element first = elementAt(i);
            mapper.apply(first).parentLink().connectTo((Multi)other);
         }
      } else {
         if(other == null) {
            throw new IllegalArgumentException("The given association is null.");
         } else {
            throw new IllegalArgumentException("The given association is of the incorrect type. Expected: "+getClass().getName()+", but received: "+other.getClass().getName());
         }
      }
   }
	
	
//	protected List<T> explicitElements() {
//		return super.getOtherEnds();
//	}
//	
//	protected List<T> implicitElements() {
//		return Collections.EMPTY_LIST;
//	}
	
//	@Override
//	public List<T> getOtherEnds() {
//		List<T> result = explicitElements();
//		result.addAll(implicitElements());
//		return result;
//	}
	
//	@Override
//	public void cloneTo(ChameleonAssociation<T> o) {
//		Multi<T> other = (Multi<T>) o;
//		for(T t: getOtherEnds()) {
//			other.add((Association)t.clone().parentLink());
//		}
//	}

}
