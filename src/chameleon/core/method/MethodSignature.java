package chameleon.core.method;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.type.TypeReference;

public abstract class MethodSignature<E extends MethodSignature,P extends Method> extends Signature<E, P> {

	public String name() {
		return _name;
	}
	
	private String _name;
	
  /*********************
   * FORMAL PARAMETERS *
   *********************/

  public List<TypeReference> typeReferences() {
    return _parameters.getOtherEnds();
  }


  public void add(TypeReference arg) {
    _parameters.add(arg.parentLink());
  }

  public int getNbTypeReferences() {
    return _parameters.size();
  }

  private OrderedReferenceSet<E,TypeReference> _parameters = new OrderedReferenceSet<E,TypeReference>((E) this);

  @Override
	public List<String> identifiers() {
		List<String> result = new ArrayList<String>();
		result.add(name());
		return result;
	}

  public boolean sameParameterTypesAs(MethodSignature other) throws MetamodelException {
  	boolean result = false;
  	if (other != null) {
			List<TypeReference> mine = typeReferences();
			List<TypeReference> others = other.typeReferences();
			result = mine.size() == others.size();
			Iterator<TypeReference> iter1 = mine.iterator();
			Iterator<TypeReference> iter2 = others.iterator();
			while (result && iter1.hasNext()) {
        result = result && iter1.next().getType().equals(iter2.next().getType());
			}
		}
  	return result;
  }

	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		result.addAll(typeReferences());
		return result;
	}

}
