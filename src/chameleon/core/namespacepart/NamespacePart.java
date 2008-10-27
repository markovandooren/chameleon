package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.Reference;
import org.rejuse.association.Relation;
import org.rejuse.predicate.PrimitiveTotalPredicate;

import chameleon.core.Config;
import chameleon.core.MetamodelException;
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.context.Context;
import chameleon.core.context.ContextFactory;
import chameleon.core.context.DeclarationCollector;
import chameleon.core.context.LexicalContext;
import chameleon.core.context.TargetContext;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.type.Type;
import chameleon.core.type.TypeContainer;
/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 */
public class NamespacePart extends NamespacePartElementImpl<NamespacePart,NamespacePartContainer> implements TypeContainer<NamespacePart,NamespacePartContainer>, NamespacePartContainer<NamespacePart,NamespacePartContainer> {

	public NamespacePart(Namespace pack) {
    setNamespace(pack);
	}
	
	public Context lexicalContext(Element child) {
		if(imports().contains(child)) {
			return _importContext;
		} else {
			return _lexicalContext;
		}
	}
	
	private Context _importLocalContext = new TargetContext(new DeclarationCollector(this) {

		@Override
		public Set<Declaration> demandDeclarations() throws MetamodelException {
		  Set<Declaration> result = new HashSet<Declaration>();
		  for(Import imporT: imports()) {
			  result.addAll(imporT.demandImports());
		  }
		  return result;
		}

		@Override
		public Set<Declaration> directDeclarations() throws MetamodelException {
		  Set<Declaration> result = new HashSet<Declaration>();
		  for(Import imporT: imports()) {
			  result.addAll(imporT.directImports());
		  }
		  return result;
		}
		
	}
	);
	
	private Context _importContext = new LexicalContext(_importLocalContext, this);
	
	private Context _typeLocalContext = new TargetContext(new DeclarationCollector(this));
	
	private Context _lexicalContext = new LexicalContext(_typeLocalContext,this) {
		@Override
		public Context parentContext() {
			return _importContext;
		}
	};
	
	public String getName(){
		return getDeclaredNamespace().getName();
	}


	public NamespacePart getNearestNamespacePart() {
		return this;
	}
	
	public CompilationUnit getCompilationUnit() {
		return getParent().getCompilationUnit();
	}

	/**
	 * NAMESPACEPARTS
	 */

	public List<NamespacePart> getNamespaceParts() {
		return _subNamespaceParts.getOtherEnds();
	}

	public void addNamespacePart(NamespacePart pp) {
		_subNamespaceParts.add(pp.getParentLink());
	}

	public void removeNamespacePart(NamespacePart pp) {
		_subNamespaceParts.remove(pp.getParentLink());
	}

	public OrderedReferenceSet<NamespacePart, NamespacePart> getNamespacePartsLink() {
		return _subNamespaceParts;
	}
	
	/**
	 * Recursively disconnect this namespace declaration and all descendant namespace declarations
	 * from their namespaces. 
	 */
	public void disconnect() {
		if(Config.DEBUG) {
			if(getDeclaredNamespace() != null) {
			  System.out.println("Disconnecting from "+getDeclaredNamespace().getFullyQualifiedName());
			}
		}
		for(NamespacePart nsp: getNamespaceParts()) {
			nsp.disconnect();
		}
		setNamespace(null);
	}

	private OrderedReferenceSet<NamespacePart, NamespacePart> _subNamespaceParts = new OrderedReferenceSet<NamespacePart, NamespacePart>(
			this);

	public List<? extends Element> getChildren() {
		List result = types(); // can't specify type parameter without having to clone types(). don't like it.
		result.addAll(getNamespaceParts());
		result.addAll(imports());
		return result;
	}
	
	public Set<Declaration> declarations() {
	  Set<Declaration> result = new HashSet<Declaration>(types());
      return result;
	}

	/*************
	 * NAMESPACE *
	 *************/
	private Reference<NamespacePart, Namespace> _namespaceLink = new Reference<NamespacePart, Namespace>(this);

	public Reference<NamespacePart, Namespace> getNamespaceLink() {
		return _namespaceLink;
	}

	public Namespace getDeclaredNamespace() {
		return (Namespace) _namespaceLink.getOtherEnd();
	}

	public void setNamespace(Namespace pack) {
		if (pack != null) {
			_namespaceLink.connectTo(pack.getNamespacePartsLink());
		} else {
			_namespaceLink.connectTo(null);
		}
	}


	/******************
	 * DEMAND IMPORTS *
	 ******************/

	private OrderedReferenceSet<NamespacePart,Import> _imports = new OrderedReferenceSet<NamespacePart,Import>(this);

	public List<Import> imports() {
		return _imports.getOtherEnds();
	}

	public void addImport(Import newImport) {
		_imports.add(newImport.getParentLink());
	}

	public void removeImport(Import removedImport) {
		_imports.remove(removedImport.getParentLink());
	}

//	public List getDemandImportElements() throws MetamodelException {
//		final ArrayList result = new ArrayList();
//		for(DemandImport element: getDemandImports()) {
//				NamespaceOrType pot = ((DemandImport)element).getElement();
//				if(pot != null) {
//					result.add(pot);
//				} else {
//					throw new MetamodelException();
//				}
//			}
//		return result;
//  }
 
//	public List getAliasImports() {
//		List imports = getDemandImports();
//		ArrayList result = new ArrayList();
//		for(int i=0; i < imports.size(); i++){
//			Object o = imports.get(i);
//			if (o instanceof UsingAlias){
//				result.add(o);
//			}
//		}
//		return result;
//	}

//	/******************
//	 * IMPORTED TYPES *
//	 ******************/
//
//	/**
//	 * The result is a list with as elements Types
//	 */
//	public List getImportedTypes() throws MetamodelException {
//		ArrayList result = new ArrayList();
//		for(TypeImport typeImport: getTypeImports()) {
//			result.add(typeImport.getType());
//		}
//		return result;
//	}
//
//	/**
//	 * DIRECT IMPORTS
//	 */
//	
//	public List<TypeImport> getTypeImports() {
//		return _importedTypes.getOtherEnds();
//	}
//
//	private OrderedReferenceSet<NamespacePart,TypeImport> _importedTypes = new OrderedReferenceSet<NamespacePart,TypeImport>(this);
//
//
//	public void addImportedType(TypeImport type) {
//		_importedTypes.add(type.getParentLink());
//	}
//	
//	public void removeImportedType(TypeImport type) {
//		_importedTypes.remove(type.getParentLink());
//	}
//
//	/**
//	 * Get the Imported type with the given name.
//	 * If this type is not imported as an ImportedType null is returned
//	 */
//	public Type getImportedType(final String name) throws MetamodelException {
//	    List allTypes = getImportedTypes();
//	    new PrimitiveTotalPredicate() {
//	      public boolean eval(Object o) {
//	      	try{
//	          return ((Type)o).getName().equals(name);
//	      	}
//	      	catch(NullPointerException exc) {
//	      		throw exc;
//	      	}
//	      }
//	    }.filter(allTypes);
//	    if(allTypes.isEmpty()) {
//	      return null;
//	    }
//	    else {
//	      return (Type)allTypes.iterator().next();
//	    }
//	  }

//	/*****************
//	 * USING ALIASES *
//	 *****************/
//
//	private OrderedReferenceSet<NamespacePart,UsingAlias> _usingAliases = new OrderedReferenceSet<NamespacePart,UsingAlias>(this);
//
//	public List<UsingAlias> getAliases() {
//		return _usingAliases.getOtherEnds();
//	}
//
//	public void addAlias(UsingAlias alias) {
//		_usingAliases.add(alias.getParentLink());
//	}
//
//	public void removeAlias(UsingAlias alias) {
//		_usingAliases.remove(alias.getParentLink());
//	}

	
	/*********
	 * TYPES *
	 *********/
	protected OrderedReferenceSet<NamespacePart, Type> _types = new OrderedReferenceSet<NamespacePart, Type>(this);

	public Relation<NamespacePart, Type> getTypesLink() {
		return _types;
	}

	public List<Type> types() {
		return _types.getOtherEnds();
	}

	public void addType(Type type) {
		_types.add(type.getParentLink());
	}

	public void removeType(Type type) {
		_types.remove(type.getParentLink());
	}

//	public Type getType(final String name) throws MetamodelException {
//		List types = types();
//		new PrimitiveTotalPredicate() {
//			public boolean eval(Object o) {
//				return ((Type)o).getName().equals(name);
//			}
//		}.filter(types);
//		if(types.isEmpty()) {
//			return null;
//		} else if(types.size() == 1) {
//			return (Type)types.get(0);
//		} else {
//			throw new MetamodelException("Multiple types with same name in this package");
//		}
//	}

//	/**
//	 * Get all the visisble types.
//	 * These types are NOT private or protected
//	 */
//	public List getVisibleTypes() {
//		List types = getTypes();
//		new PrimitiveTotalPredicate() {
//			public boolean eval(Object o) {
//				return !((Type)o).hasModifier(new Private()) &&
//				!((Type)o).hasModifier(new Protected());
//			}
//		}.filter(types);
//		if(types.size() > 0) {
//			return types;
//		}
//		else {
//			return null;
//		}
//	}

	/**
	 * The parents are possibly other PackageParts and the CompilationUnit
	 */



//	/**
//	 * CONTEXT
//	 */
//
//	//TODO is dit correct? (copied from old cu)
//	//Waarom is hetgeen er teruggeven wordt afhankelijk van het feit of het element in een
//	//import voorkomt
//	  public Context getContext(Element element) throws MetamodelException {
//	    if(getDemandImports().contains(element) || getTypeImports().contains(element)) {
//	      return getDeclaredNamespace().getDefaultNamespace().lexicalContext();
//	    }
//	    return _context.getOtherEnd();
//	  }


	/**
	 * ACCESSIBILITY
	 */

//	public AccessibilityDomain getTypeAccessibilityDomain() {
//		return new All();
//	}

//	public Type getTopLevelType() {
//		return null;
//	}

	public Language language(){
		if(getDeclaredNamespace() != null) {
		  return getDeclaredNamespace().language();
		} else {
			return null;
		}
	}
	public ContextFactory getContextFactory() {
		return language().contextFactory();
	}

 /*@
   @ public behavior
   @
   @ post \result = getDeclaredNamespace().getDefaultNamespace(); 
   @*/
	public Namespace getDefaultNamespace() {
		return getDeclaredNamespace().getDefaultNamespace();
	}

  @Override
  public NamespacePart clone() {
    return new NamespacePart(null);
  }
}
