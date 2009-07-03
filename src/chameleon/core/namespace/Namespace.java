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
package chameleon.core.namespace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.java.collections.Visitor;
import org.rejuse.predicate.PrimitiveTotalPredicate;
import org.rejuse.predicate.TypePredicate;

import chameleon.core.IMetaModel;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.type.Type;

/**
 * @author Marko van Dooren
 */

public abstract class Namespace extends ElementImpl<Namespace,Namespace> implements NamespaceOrType<Namespace,Namespace,SimpleNameSignature>, IMetaModel, DeclarationContainer<Namespace, Namespace>, TargetDeclaration<Namespace, Namespace,SimpleNameSignature> {
  //FIXME
	//SPEED : use hashmap to store the subnamespaces and forbid
	//        adding multiple namespaces with the same name. That is
	//        never useful anyway.
	
	/**
	 * Initialize a new Namespace with the given name.
	 *
	 * @param name
	 *        The name of the new package.
	 */
	/*@
	 @ public behavior
	 @
	 @ pre name != null;
	 @
	 @ post \result.getName().equals(name);
	 @*/
	public Namespace(SimpleNameSignature sig) {
      setSignature(sig);
	}


	public void setSignature(Signature signature) {
	  if(signature != null) {
	    _signature.connectTo(signature.parentLink());
	  } else {
	    _signature.connectTo(null);
	  }
	}
		  
	  /**
	   * Return the signature of this member.
	   */
	  public SimpleNameSignature signature() {
	    return _signature.getOtherEnd();
	  }
		  
	  private Reference<Namespace, SimpleNameSignature> _signature = new Reference<Namespace, SimpleNameSignature>(this);

	  public String getName() {
		  return signature().getName();
	  }
	  
	/**
	 * Return the fully qualified name of this package/
	 */
	/*@
	 @ public behavior
	 @
	 @ post (getParent() == null) || getParent().getName().equals("") ==>
	 @        \result == getName();
	 @ post (getParent() != null) && (! getParent().getName().equals("")) ==>
	 @        \result == getParent().getFullyQualifiedName() + "." + getName();
	 @*/
	public String getFullyQualifiedName() {
		return (parent() == null || parent().getName().equals("") ? "" : parent().getFullyQualifiedName() + ".") + getName();
	}

	private final class DummyNamespaceOrTypeReference extends NamespaceOrTypeReference {
		private DummyNamespaceOrTypeReference(String name) {
			super(name);
			setUniParent(defaultNamespace());
		}
	}

	public class NamePredicate extends PrimitiveTotalPredicate {
		public boolean eval(Object pack) {
			return (pack instanceof Namespace) && ((Namespace)pack).getFullyQualifiedName().equals(getFullyQualifiedName());
		}

	}

	/**************
	 * PACKAGEPART
	 **************/

	public abstract void addNamespacePart(NamespacePart namespacePart);

	public abstract List<NamespacePart> getNamespaceParts();

	/**
	 * Return the root namespace of this metamodel instance.
	 */
	/*@
	 @ public behavior
	 @
	 @ post getParent() != null ==> \result == getParent().getDefaultPackage();
	 @ post getParent() == null ==> \result == this;
	 @*/
	public Namespace defaultNamespace() {
		if (parent() == null) {
			return this;
		}
		else {
			return parent().defaultNamespace();
		}
	}

	/**
	 * See superclass
	 */


	public abstract List<Namespace> getSubNamespaces();

	/**
	 * <p>Return the package with the fullyqualified name that
	 * equals the fqn of this package concatenated with the
	 * given name.</p>
	 *
	 * <p>If the package does not exist yet, it will be created.</p>
	 *
	 * @param name
	 *        The qualified name relative to this package
	 */
	public abstract Namespace getOrCreateNamespace(final String name) throws LookupException;

	/**
	 * Return the direct subpackage with the given short name.
	 *
	 * @param name
	 *        The short name of the package to be returned.
	 */
	/*@
	 @ public behavior
	 @
	 @ post getSubNamespaces().contains(\result);
	 @ post \result.getName().equals(name);
	 @
	 @ signals (MetamodelException) (* The subpackage could not be found*);
	 @*/
	public Namespace getSubNamespace(final String name) throws LookupException {
		List<Namespace> packages = getSubNamespaces();

		new PrimitiveTotalPredicate<Namespace>() {
			public boolean eval(Namespace o) {
				return o.getName().equals(name);
			}
		}.filter(packages);
		if (packages.isEmpty()) {
			return null;
		}
		else if(packages.size() == 1){
			return (Namespace)packages.iterator().next();
		}
		else {
			throw new LookupException("Namespace "+getFullyQualifiedName()+ " contains "+packages.size()+" sub namespaces with name "+name);
		}

	}

//	/**
//	 * Find the package or type with the given qualified name.
//	 *
//	 * @param name
//	 * 		The qualified name relative to this package.
//	 */
//	/*@
//	 @ public behavior
//	 @
//	 @ post \result != null;
//	 @ post name == null ==> \result == this;
//	 @ post name != null ==> \result.getFullyQualifiedName().equals(getFullyQualifiedName() + "." + name);
//	 @
//	 @ signals (MetamodelException) (* The type/package could not be found*);
//	 @*/
//	public NamespaceOrType findNamespaceOrType(String qualifiedName) throws MetamodelException {
//		NamespaceOrTypeReference ref = new DummyNamespaceOrTypeReference(qualifiedName);
//		return ref.getNamespaceOrType();
//	}

//	/**
//	 * Return the type with the given fully qualified name.
//	 *
//	 * @param fqn
//	 *        The fully qualified name of the requested type.
//	 */
//	/*@
//	 @ public behavior
//	 @
//	 @ post \result == getDefaultPackage().findPackageOrType(fqn);
//	 @
//	 @ signals (MetamodelException) (* The type could not be found*);
//	 @*/
//	public Type findType(String fqn) throws MetamodelException {
//		try {
//			return (Type)findNamespaceOrType(fqn);
//		}
//		catch (ClassCastException exc) {
//			throw new MetamodelException("Can not find type " + fqn);
//		}
//	}


	/**
	 * Return the types directly contained in this package.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @*/
	  public Set getTypes() {
	  	//FIXME: filter out non-exported types such as private types.
	    final Set result = new HashSet();
	    new Visitor() {
	      public void visit(Object o) {
	        List l = ((NamespacePart)o).types();
	        //if(l != null) TODO Who wrote this and why?
	        result.addAll(l);
	      }
	    }.applyTo(getNamespaceParts());
	    return result;
	  }

	/**
	 * Get the type with the specified names
	 */
	public Type getType(final String name) throws LookupException {
		Set types = getTypes();
		new PrimitiveTotalPredicate() {
			public boolean eval(Object o) {
				return ((Type)o).getName().equals(name);
			}
		}.filter(types);
		if(types.isEmpty()) {
			return null;
		} else if(types.size() == 1) {
			return (Type)types.iterator().next();
		} else {
			throw new LookupException("Namespace "+getFullyQualifiedName()+" contains "+types.size() +" classes named "+name);
		}
	}


	/**
	 * Return the set of all types in this package and all of its subpackages.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @ post \result.containsAll(getTypes());
	 @ post (\forall Namespace sub; getSubNamespaces().contains(sub);
	 @         \result.containsAll(sub.getAllTypes()));
	 @*/
	public Set getAllTypes() {
		final Set result = getTypes();
		new Visitor() {
			public void visit(Object element) {
				result.addAll(((Namespace)element).getAllTypes());
			}
		}.applyTo(getSubNamespaces());
		return result;
	}

	/**
	 * Return the set of all expressions in all types of this package.
	 */
	public Set<Expression> getDirectlyContainedExpressions() {
        Set<Expression> result = new HashSet<Expression>(); 
        //final Set result = new HashSet();
        for (Object t : getTypes()) {
            result.addAll(((Type)t).descendants(Expression.class));
        }
		return result;
	}


		/***********
		 * CONTEXT *
		 ***********/
		
//	public AccessibilityDomain getAccessibilityDomain() {
//		return new All();
//	}

	 /*@
	   @ also public behavior
	   @
	   @ post \result.containsAll(getSubNamespaces());
	   @ post \result.containsAll(getCompilationUnits());
	   @*/
	  public List<? extends Element> children() {
	    List<Element> result = new ArrayList<Element>();
      result.addAll(getSubNamespaces());
	    result.addAll( getNamespaceParts());
	    return result;
	  }


//    @Override
//    public Namespace clone() {
//      return new Namespace(signature().clone());
//    }

	public LookupStrategy targetContext() {
		return language().lookupFactory().createTargetContext(this);
	}

	public Set<Declaration> declarations() {
		Set<Declaration> result = new HashSet<Declaration>();
		result.addAll(getSubNamespaces());
		result.addAll(getTypes());
		return result;
	}
	
	public <T extends Declaration> Set<T> declarations(Class<T> cls) {
  	List<Element> tmp = (List<Element>) children();
  	if(tmp == null) {
  		throw new ChameleonProgrammerException("children() returns null for " + getClass().getName());
  	}
  	new TypePredicate<Element,T>(cls).filter(tmp);
  	return (Set<T>) tmp;
	}

	public Declaration alias(SimpleNameSignature sig) {
		return new NamespaceAlias(sig,this);
	}

	public Namespace resolve() {
		return this;
	}
}