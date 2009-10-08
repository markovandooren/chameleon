package chameleon.core.method;

import java.util.Iterator;
import java.util.List;

import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;

public abstract class MethodSignature<E extends MethodSignature,P extends Method> extends Signature<E, P> {

//	public String name() {
//		return _name;
//	}
//	
//	private String _name;
//	
//  /*********************
//   * FORMAL PARAMETERS *
//   *********************/
//
//  public List<TypeReference> typeReferences() {
//    return _parameters.getOtherEnds();
//  }
//
//
//  public void add(TypeReference arg) {
//    _parameters.add(arg.parentLink());
//  }
//
//  public int getNbTypeReferences() {
//    return _parameters.size();
//  }
//
//  private OrderedReferenceSet<E,TypeReference> _parameters = new OrderedReferenceSet<E,TypeReference>((E) this);

	public abstract String name();
	
	public abstract List<Type> parameterTypes() throws LookupException;
	
  public boolean sameParameterTypesAs(MethodSignature other) throws LookupException {
  	boolean result = false;
  	if (other != null) {
			List<Type> mine = parameterTypes();
			List<Type> others = other.parameterTypes();
			result = mine.size() == others.size();
			Iterator<Type> iter1 = mine.iterator();
			Iterator<Type> iter2 = others.iterator();
			while (result && iter1.hasNext()) {
        result = result && iter1.next().equals(iter2.next());
			}
		}
  	return result;
  }

}
