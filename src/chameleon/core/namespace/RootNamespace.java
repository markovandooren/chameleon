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


//import org.jnome.mm.type.NullType;
import org.rejuse.association.Reference;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.language.Language;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.type.Type;

public class RootNamespace extends Namespace {

  /**
   * @param name
   */
  public RootNamespace(NamespaceSignature sig, Language language) {
    super(sig);
   // NamespacePart pp = new NamespacePart(this, nspLocalContext);
    //_nullType = new NullType(pp);
    //pp.addType(language.getNullType());
    _language.connectTo(language.defaultNamespaceLink());
    
    //FIXME: Marko : is this for primitive types, or what?
    NamespacePart primitiveNP = new NamespacePart(this);
    _primitiveNamespacePart.connectTo(primitiveNP.getNamespaceLink());
  }

  public void setNullType(){
	  NamespacePart pp = new NamespacePart(this);
	  pp.addType(language().getNullType());
	  new CompilationUnit(pp);
  }
  
  private Type _nullType;
  private Reference _language = new Reference(this);

  public Type getNullType() {
	  return this.language().getNullType();
  }
  
  public Language language() {
    return (Language)_language.getOtherEnd();
  }
	  
	  /**
	   * PRIMITIVE NAMESPACE PART
	   * 
	   * In this namespace part a UsingAlias will be present which
	   * connects the shortcuts for all primitive types (except void) and
	   * their class in the System namespace
	   * for example:
	   * using int = System.int32
	   * 
	   * Normally usings are only ment for the type declared in the NamespacePart
	   * but these usings should be there for every type.
	   * Therefor the RootNamespace overrides the getType() method.
	   * 
	   * !!! This is a logical expantion to fit everything in the metamodel.
	   * !!! The _primitiveNamespacePart should never be written down with for
	   * !!! example the CSharpCodeWriter or an editor.
	   */
	  private Reference _primitiveNamespacePart = new Reference(this);
	  
	  public NamespacePart getPrimitiveNamespacePart(){
		  return (NamespacePart)_primitiveNamespacePart.getOtherEnd();
	  }
	  
//	  public Type getType(final String name) throws MetamodelException {
//		  NamespacePartContext npc = (NamespacePartContext)getPrimitiveNamespacePart().lexicalContext(); 
//		  Type t =  npc.getAliasImport(name);
//		  if(t == null)
//			  t = super.getType(name);  
//		  
//		  return t;
//	  }
}
