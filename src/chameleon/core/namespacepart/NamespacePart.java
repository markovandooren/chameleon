package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.Reference;
import org.rejuse.association.Relation;

import chameleon.core.Config;
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LexicalLookupStrategy;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.lookup.LookupStrategySelector;
import chameleon.core.namespace.Namespace;
import chameleon.core.type.Type;
/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class NamespacePart extends NamespaceElementImpl<NamespacePart,NamespacePartContainer> implements DeclarationContainer<NamespacePart,NamespacePartContainer>, NamespacePartContainer<NamespacePart,NamespacePartContainer> {

	private final class DefaultNamespaceSelector implements LookupStrategySelector {
		public LookupStrategy strategy() throws LookupException {
			// 5 SEARCH IN DEFAULT NAMESPACE
			return NamespacePart.this.getDefaultNamespace().targetContext();
		}
	}

	protected class ImportLocalDemandContext extends LocalLookupStrategy<NamespacePart> {
	  public ImportLocalDemandContext(NamespacePart element) {
			super(element);
		}

	  @Override
	  public <D extends Declaration> List<D> demandDeclarations(DeclarationSelector<D> selector) throws LookupException {
	    List<D> result = new ArrayList<D>();
			List<Import> imports = imports();
			ListIterator<Import> iter = imports.listIterator(imports.size());
			// If the selector found a match, we stop.
			// We must iterate in reverse.
			while(result.isEmpty() && iter.hasPrevious()) {
				Import imporT = iter.previous();
		    result.addAll(imporT.demandImports(selector));
	    }
	    return result;
	  }
	}
	
	protected class ImportLocalDirectContext extends LocalLookupStrategy<NamespacePart> {
		private ImportLocalDirectContext(NamespacePart element) {
			super(element);
		}

		@Override
		public <D extends Declaration> List<D> directDeclarations(DeclarationSelector<D> selector) throws LookupException {
			List<D> result = new ArrayList<D>();
			List<Import> imports = imports();
			ListIterator<Import> iter = imports.listIterator(imports.size());
			// If the selector found a match, we stop.
			// We must iterate in reverse.
			while(result.isEmpty() && iter.hasPrevious()) {
				Import imporT = iter.previous();
				result.addAll(imporT.directImports(selector));
			}
		  return result;
		}
	}

	protected class DemandImportStrategySelector implements LookupStrategySelector {
		public LookupStrategy strategy() {
			// 4 SEARCH DEMAND IMPORTS
			return _importDemandContext;
		}
	}

	protected class CurrentNamespaceStrategySelector implements LookupStrategySelector {
		public LookupStrategy strategy() {
			// 3 SEARCH IN CURRENT NAMESPACE
			LookupStrategy currentNamespaceStrategy = getDeclaredNamespace().localStrategy();
			return language().lookupFactory().createLexicalLookupStrategy(
					 currentNamespaceStrategy, NamespacePart.this, _demandImportStrategySelector)
			;
		}
	}

	protected class DirectImportStrategySelector implements LookupStrategySelector {
		public LookupStrategy strategy() {
			// 2 SEARCH IN DIRECT IMPORTS
			return _importDirectContext;
		}
	}

	/**
	 * Create a new namespace part the adds elements to the given namespace.
	 * @param namespace
	 */
 /*@
   @ public behavior
   @
   @ pre namespace != null;
   @
   @ post getDeclaredNamespace() == namespace
   @*/ 
	public NamespacePart(Namespace namespace) {
    setNamespace(namespace);
	}
	
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		if(imports().contains(child)) {
			return getDefaultNamespace().targetContext();
		} else {
			return getLexicalContext();
		}
	}
	
	private LookupStrategy getLexicalContext() {
		if(_lexicalContext == null) {
			initContexts();
		}
		return _lexicalContext;
	}
	
	private void initContexts() {
    // This must be executed after the namespace is set, so it cannot be in the initialization.
    _typeLocalContext = language().lookupFactory().createTargetLookupStrategy(this);
    _importLocalDirectContext = new ImportLocalDirectContext(this);
//    _importLocalDemandContext = language().lookupFactory().wrapLocalStrategy(new ImportLocalDemandContext(this),this);
    _importLocalDemandContext = new ImportLocalDemandContext(this);
		_importDirectContext = language().lookupFactory().createLexicalLookupStrategy(_importLocalDirectContext, this, _currentNamespaceStrategySelector);
		_importDemandContext = language().lookupFactory().createLexicalLookupStrategy(_importLocalDemandContext, this, _defaultNamespaceSelector);
    // 1 SEARCH IN NAMESPACEPART
		_lexicalContext = language().lookupFactory().createLexicalLookupStrategy(localContext(), this, _directImportStrategySelector); 
	}

  private DirectImportStrategySelector _directImportStrategySelector = new DirectImportStrategySelector();

	private CurrentNamespaceStrategySelector _currentNamespaceStrategySelector = new CurrentNamespaceStrategySelector();

	private DemandImportStrategySelector _demandImportStrategySelector = new DemandImportStrategySelector();

	private DefaultNamespaceSelector _defaultNamespaceSelector = new DefaultNamespaceSelector();
	
	private LookupStrategy _importLocalDirectContext;
	
	private LookupStrategy _importDirectContext;
	
	private LookupStrategy _importLocalDemandContext;
	
	private LookupStrategy _importDemandContext;
	
	public LookupStrategy localContext() {
		if(_typeLocalContext == null) {
			initContexts();
		}
		return _typeLocalContext;
	}
	
	private LookupStrategy _lexicalContext;
	
	public String getName(){
		return getDeclaredNamespace().getName();
	}

	public String getFullyQualifiedName() {
		return getDeclaredNamespace().getFullyQualifiedName();
	}

	public NamespacePart getNearestNamespacePart() {
		return this;
	}
	
	public CompilationUnit getCompilationUnit() {
		return parent().getCompilationUnit();
	}

	/**
	 * NAMESPACEPARTS
	 */

	public List<NamespacePart> getNamespaceParts() {
		return _subNamespaceParts.getOtherEnds();
	}

	public void addNamespacePart(NamespacePart pp) {
		_subNamespaceParts.add(pp.parentLink());
	}

	public void removeNamespacePart(NamespacePart pp) {
		_subNamespaceParts.remove(pp.parentLink());
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

	public List<? extends Element> children() {
		List result = types(); // can't specify type parameter without having to clone types(). don't like it.
		result.addAll(getNamespaceParts());
		result.addAll(imports());
		return result;
	}
	
	public List<? extends Declaration> declarations() {
      return types();
	}
	
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
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
			pack.addNamespacePart(this);
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
		_imports.add(newImport.parentLink());
	}

	public void removeImport(Import removedImport) {
		_imports.remove(removedImport.parentLink());
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
		_types.add(type.parentLink());
	}

	public void removeType(Type type) {
		_types.remove(type.parentLink());
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
	public LookupStrategyFactory getContextFactory() {
		return language().lookupFactory();
	}

 /*@
   @ public behavior
   @
   @ post \result = getDeclaredNamespace().getDefaultNamespace(); 
   @*/
	public Namespace getDefaultNamespace() {
		return getDeclaredNamespace().defaultNamespace();
	}

  @Override
  public NamespacePart clone() {
  	NamespacePart result = new NamespacePart(null);
  	for(NamespacePart part: getNamespaceParts()) {
  		result.addNamespacePart(part.clone());
  	}
  	for(Type type:types()) {
  		result.addType(type.clone());
  	}
  	for(Import<Import> importt:imports()) {
  		result.addImport(importt.clone());
  	}
  	return result;
  }

	private LookupStrategy _typeLocalContext;

}
