/*
 * Copyright 2000-2004 the Jnome development team.
 *
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 * This file is part of Jnome.
 *
 * Jnome is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.java.collections.Visitor;
import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;

/**
 * @author marko
 */
public class CheckedExceptionList {

  public CheckedExceptionList(Language language) {
    _pairs = new ArrayList();
    _language = language;
  }

	/**
	 * 
	 * @uml.property name="_language"
	 * @uml.associationEnd 
	 * @uml.property name="_language" multiplicity="(1 1)"
	 */
	private Language _language;

  
  public Language getLanguage() {
    return _language;
  }
  
  public void add(ExceptionPair pair) throws LookupException {
    if(getLanguage().isCheckedException(pair.getException())) {
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
    try {
      new PrimitivePredicate() {
        public boolean eval(Object o) throws LookupException {
          ExceptionPair ep = (ExceptionPair)o;
          return ! ep.getException().assignableTo(type);
        }
      }.filter(_pairs);
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
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
    final List result = new ArrayList();
    new Visitor() {
      public void visit(Object element) {
        result.add(((ExceptionPair)element).getDeclaration());
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
      public void visit(Object element) {
        result.add(((ExceptionPair)element).getException());
      }
    }.applyTo(getPairs());
    return result;
  }
}
