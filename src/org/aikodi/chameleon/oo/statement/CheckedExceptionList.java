package org.aikodi.chameleon.oo.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.rejuse.java.collections.Visitor;
import org.aikodi.rejuse.predicate.Predicate;

/**
 * A checked exception list is a list that contains tuples of the form (checked exception type, exception declaration, cause).
 * 
 * @author Marko van Dooren
 */
public class CheckedExceptionList {

	/**
	 * Initialize a new empty checked exception list.
	 */
  public CheckedExceptionList() {
    _pairs = Lists.create();
  }

  
  public void add(ExceptionTuple pair) throws LookupException {
    Type exception = pair.getException();
		if(exception.language(ObjectOrientedLanguage.class).isCheckedException(exception)) {
      _pairs.add(pair);
    }
  }
  
  public List getPairs() {
    return new ArrayList(_pairs);
  }
  
  public void absorb(CheckedExceptionList other) {
    _pairs.addAll(other.getPairs());
  }
  
  public void handleType(final Type type) throws LookupException {
      new Predicate<ExceptionTuple,LookupException>() {
        @Override
      public boolean eval(ExceptionTuple ep) throws LookupException {
          return ! ep.getException().assignableTo(type);
        }
      }.filter(_pairs);
  }

	/**
	 * 
	 * @uml.property name="_pairs"
	 * @uml.associationEnd 
	 * @uml.property name="_pairs" multiplicity="(0 -1)" elementType="chameleon.core.statement.ExceptionPair"
	 */
	private List _pairs;

  /**
   * @return
   */
  public Collection getDeclarations() {
    final List result = Lists.create();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.add(((ExceptionTuple)element).getDeclaration());
      }
    }.applyTo(getPairs());
    return result;
  }

  /**
   * @return
   */
  public Set getExceptions() throws LookupException {
    final Set result = new HashSet();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.add(((ExceptionTuple)element).getException());
      }
    }.applyTo(getPairs());
    return result;
  }
}
