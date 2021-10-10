/**
 * 
 */
package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.relation.WeakPartialOrder;
import org.aikodi.chameleon.oo.type.Type;

import java.util.Iterator;
import java.util.List;

public class MoreSpecificTypesOrder implements WeakPartialOrder<List<Type>> {
	
	public static MoreSpecificTypesOrder create() {
		return _protoType; 
	}
	
	private static MoreSpecificTypesOrder _protoType = new MoreSpecificTypesOrder();
	
	private MoreSpecificTypesOrder() {
		
	}
	
  @Override
  public boolean contains(List<Type> first, List<Type> second)
      throws LookupException {
    boolean result = first.size() == second.size();
    Iterator<Type> firstIter = first.iterator();
    Iterator<Type> secondIter = second.iterator();
    while(result && firstIter.hasNext()) {
      Type firstType = firstIter.next();
      Type secondType = secondIter.next();
      result = result && firstType.subtypeOf(secondType);
    }
    return result;
  }
}
