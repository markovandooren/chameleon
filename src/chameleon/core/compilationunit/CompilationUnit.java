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
package chameleon.core.compilationunit;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.Context;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.language.Language;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.namespacepart.NamespacePartContainer;
import chameleon.util.Util;


/**
 * A compilation unit basically represents a file, which is usually the smallest element that can be compiled.
 * 
 * @author marko
 */
public class CompilationUnit extends ElementImpl<CompilationUnit,Element> implements NamespacePartContainer<CompilationUnit,Element> {

  /**
   * Create a new compilation unit with the given namespacepart as default namespace part.
   * @param defaultNamespacePart
   */
	public CompilationUnit(NamespacePart defaultNamespacePart) {
    setDefaultNamespacePart(defaultNamespacePart);
	}
	
	
	/************
	 * Children *
	 ************/

	public List<? extends Element> children() {
		return getNamespaceParts();
	}

	/***********
	 * CONTEXT *
	 ***********/

//	public Context getContext(Element element) throws MetamodelException {
//		if(getDemandImports().contains(element) || getTypeImports().contains(element)) {
//			return getNamespace().getDefaultNamespace().getContext();
//		}
//		return (Context)_context.getOtherEnd();
//	}
	

//	/******************
//	 * DEMAND IMPORTS *
//	 ******************/
//
//	public List getDemandImportElements() throws MetamodelException {
//		try {
//			final ArrayList result = new ArrayList();
//			new RobustVisitor() {
//				public Object visit(Object element) throws MetamodelException {
//					NamespaceOrType pot = ((DemandImport)element).getElement();
//					if(pot != null) {
//						result.add(pot);
//					} else {
//						throw new MetamodelException();
//					}
//					return null;
//				}
//				public void unvisit(Object element, Object undo) {
//					//NOPs
//				}
//			}
//			.applyTo(getDemandImports());
//			return result;
//		}
//		catch (MetamodelException e) {
//			throw e;
//		}
//		catch (Exception e) {
//			throw new Error();
//		}
//	}

//
//	/******************
//	 * IMPORTED TYPES *
//	 ******************/
//
//	public List getImportedTypes() throws MetamodelException {
//		try {
//			final ArrayList result = new ArrayList();
//			new RobustVisitor() {
//				public Object visit(Object element) throws MetamodelException {
//					try {
//						result.add(((TypeImport)element).getType());
//					}
//					catch(MetamodelException exc) {
//						((TypeImport)element).getType();
//						throw exc;
//					}
//					return null;
//				}
//				public void unvisit(Object element, Object undo) {
//					//NOP
//				}
//			}
//			.applyTo(getTypeImports());
//			return result;
//		}
//		catch (MetamodelException e) {
//			throw e;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			throw new Error();
//		}
//	}

	/********
	 * MISC *
	 ********/

	public CompilationUnit getCompilationUnit() {
		return this;
	}

//	public Type getVisibleType() {
//		List types = getTypes();
//		new PrimitiveTotalPredicate() {
//			public boolean eval(Object o) {
//				return !((Type)o).is(new Private()) &&
//				!((Type)o).is(new Protected());
//			}
//		}.filter(types);
//		if(types.size() > 0) {
//			return (Type)types.iterator().next();
//		}
//		else {
//			return null;
//		}
//	}

//	/**
//	 * @param name
//	 * @return
//	 */
//	public Type getImportedType(final String name) throws MetamodelException {
//		List allTypes = getImportedTypes();
//		new PrimitiveTotalPredicate() {
//			public boolean eval(Object o) {
//				return ((Type)o).getName().equals(name);
//			}
//		}.filter(allTypes);
//		if(allTypes.isEmpty()) {
//			return null;
//		}
//		else {
//			return (Type)allTypes.iterator().next();
//		}
//	}

//	public AccessibilityDomain getTypeAccessibilityDomain() {
//		return new All();
//	}

	private Reference<CompilationUnit,NamespacePart> _defaultNamespacePart = new Reference(this);

	public NamespacePart getDefaultNamespacePart(){
		return _defaultNamespacePart.getOtherEnd();
	}
	
	/**
	 * Disconnect this compilation unit from the model by recursively
	 * disconnecting all namespace declarations.
	 */
	public void disconnect() {
		getDefaultNamespacePart().disconnect();
	}
	
	public void setDefaultNamespacePart(NamespacePart nsp) {
		if(nsp == null) {
			_defaultNamespacePart.connectTo(null);
		}
		else {
			_defaultNamespacePart.connectTo(nsp.parentLink());
		}
	}

  /**
   * NAMESPACEPARTS
   */
	public List<NamespacePart> getNamespaceParts() {
		return Util.createNonNullList(getDefaultNamespacePart());
	}

	public Context lexicalContext(Element child) throws MetamodelException {
		return getDefaultNamespacePart().getDeclaredNamespace().lexicalContext();
	}


	public Language language() {
	  NamespacePart defaultNamespace = getDefaultNamespacePart();
	  if(defaultNamespace != null) {
		  return defaultNamespace.language();
	  } else {
	    return null;
	  }
	}


  @Override
  public CompilationUnit clone() {
    CompilationUnit result = new CompilationUnit(getDefaultNamespacePart().clone());
    return result;
  }

 }
