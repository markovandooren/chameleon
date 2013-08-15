package be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContextFactory;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContextSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.core.reference.SimpleReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
/**
 * A namespace part adds its declarations to a namespace. Different namespace parts in different compilation units
 * can contribute to the same namespace.
 * 
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class NamespaceDeclaration extends ElementImpl implements DeclarationContainer {

  static {
    excludeFieldName(NamespaceDeclaration.class,"_namespaceLink");
  }
  
	private final class DefaultNamespaceSelector implements LookupContextSelector {
		public LookupContext strategy() throws LookupException {
			// 5 SEARCH IN DEFAULT NAMESPACE
			return NamespaceDeclaration.this.getDefaultNamespace().targetContext();
		}
	}

	protected class ImportLocalDemandContext extends LocalLookupContext<NamespaceDeclaration> {
	  public ImportLocalDemandContext(NamespaceDeclaration element) {
			super(element);
		}

	  @Override
	  public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
	    List<SelectionResult> result = new ArrayList<SelectionResult>();
			List<? extends Import> imports = imports();
			ListIterator<? extends Import> iter = imports.listIterator(imports.size());
			// If the selector found a match, we stop.
			// We must iterate in reverse.
			while(result.isEmpty() && iter.hasPrevious()) {
				Import imporT = iter.previous();
		    result.addAll(imporT.demandImports(selector));
	    }
	    return result;
	  }
	}
	
	protected class ImportLocalDirectContext extends LocalLookupContext<NamespaceDeclaration> {
		private ImportLocalDirectContext(NamespaceDeclaration element) {
			super(element);
		}

		@Override
		public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
			List<SelectionResult> result = new ArrayList<SelectionResult>();
			List<? extends Import> imports = imports();
			ListIterator<? extends Import> iter = imports.listIterator(imports.size());
			// If the selector found a match, we stop.
			// We must iterate in reverse.
			while(result.isEmpty() && iter.hasPrevious()) {
				Import imporT = iter.previous();
				result.addAll(imporT.directImports(selector));
			}
		  return result;
		}
	}

	protected class DemandImportStrategySelector implements LookupContextSelector {
		public LookupContext strategy() {
			// 4 SEARCH DEMAND IMPORTS
			return _importDemandContext;
		}
	}

	protected class CurrentNamespaceStrategySelector implements LookupContextSelector {
		public LookupContext strategy() throws LookupException {
			// 3 SEARCH IN CURRENT NAMESPACE
			LookupContext currentNamespaceStrategy = namespace().localContext();
			return language().lookupFactory().createLexicalLookupStrategy(
					 currentNamespaceStrategy, NamespaceDeclaration.this, _demandImportStrategySelector)
			;
		}
	}

	protected class DirectImportStrategySelector implements LookupContextSelector {
		public LookupContext strategy() {
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
	public NamespaceDeclaration(CrossReference<Namespace> ref) {
    setNamespaceReference(ref);
	}
	
	public NamespaceDeclaration(String fqn) {
		this(new SimpleReference<Namespace>(fqn,Namespace.class));
	}
	
	public void setNamespaceReference(CrossReference<Namespace> ref) {
		set(_ref, ref);
	}
	
	private Single<CrossReference<Namespace>> _ref = new Single<CrossReference<Namespace>>(this,true,"namespace reference");
	
	public LookupContext lookupContext(Element child) throws LookupException {
		if(containsImport(child) || child == namespaceReference()) {
			return getDefaultNamespace().targetContext();
		} else {
			return getLexicalContext();
		}
	}
	
	private LookupContext getLexicalContext() {
		if(_lexicalContext == null) {
			initContexts();
		}
		return _lexicalContext;
	}
	
	private void initContexts() {
		LookupContextFactory factory = language().lookupFactory();
    // This must be executed after the namespace is set, so it cannot be in the initialization.
    _typeLocalContext = factory.createTargetLookupStrategy(this);
    _importLocalDirectContext = new ImportLocalDirectContext(this);
//    _importLocalDemandContext = language().lookupFactory().wrapLocalStrategy(new ImportLocalDemandContext(this),this);
    _importLocalDemandContext = new ImportLocalDemandContext(this);
		_importDirectContext = factory.createLexicalLookupStrategy(_importLocalDirectContext, this, _currentNamespaceStrategySelector);
		_importDemandContext = factory.createLexicalLookupStrategy(_importLocalDemandContext, this, _defaultNamespaceSelector);
    // 1 SEARCH IN NAMESPACEPART
		_lexicalContext = factory.createLexicalLookupStrategy(namespaceDeclarationContext(), this, _directImportStrategySelector);
		_lexicalContext.enableCache();
		_importDemandContext.enableCache();
		_importDirectContext.enableCache();
		_importLocalDemandContext.enableCache();
		_importLocalDirectContext.enableCache();
	}
	
	@Override
	public synchronized void flushLocalCache() {
		if(_lexicalContext != null) {
			_lexicalContext.flushCache();
		}
		if(_importDemandContext != null) {
			_importDemandContext.flushCache();
		}
		if(_importDirectContext != null) {
			_importDirectContext.flushCache();
		}
		if(_importLocalDemandContext != null) {
			_importLocalDemandContext.flushCache();
		}
		if(_importLocalDirectContext != null) {
			_importLocalDirectContext.flushCache();
		}
		_importCache = null;
		_importSet = null;
	}

  private DirectImportStrategySelector _directImportStrategySelector = new DirectImportStrategySelector();

	private CurrentNamespaceStrategySelector _currentNamespaceStrategySelector = new CurrentNamespaceStrategySelector();

	private DemandImportStrategySelector _demandImportStrategySelector = new DemandImportStrategySelector();

	private DefaultNamespaceSelector _defaultNamespaceSelector = new DefaultNamespaceSelector();
	
	private LookupContext _importLocalDirectContext;
	
	private LookupContext _importDirectContext;
	
	private LookupContext _importLocalDemandContext;
	
	private LookupContext _importDemandContext;
	
	public LookupContext namespaceDeclarationContext() {
		if(_typeLocalContext == null) {
			initContexts();
		}
		return _typeLocalContext;
	}
	
	private LookupContext _lexicalContext;
	
	public String getName(){
		return namespace().name();
	}

	public String getFullyQualifiedName() {
		return namespace().getFullyQualifiedName();
	}

	public NamespaceDeclaration getNearestNamespacePart() {
		return this;
	}
	
	/**
	 * NAMESPACEPARTS
	 */
	public List<NamespaceDeclaration> namespaceDeclarations() {
		return _subNamespaceParts.getOtherEnds();
	}

	public void addNamespaceDeclaration(NamespaceDeclaration pp) {
		add(_subNamespaceParts,pp);
	}

	public void removeNamespaceDeclaration(NamespaceDeclaration pp) {
		remove(_subNamespaceParts,pp);
	}

	/**
	 * Recursively disconnect this namespace declaration and all descendant namespace declarations
	 * from their namespaces. 
	 */
	public void nonRecursiveDisconnect() {
		// 1) Set the lexical parent to null.
		super.nonRecursiveDisconnect();
//		if(Config.DEBUG) {
//			if(namespace() != null) {
//			  System.out.println("Disconnecting from "+namespace().getFullyQualifiedName());
//			}
////			showStackTrace("Disconnecting from "+namespace().getFullyQualifiedName());
//		}
		// 2) Disconnect from the namespace. 
		setNamespace(null);
//		// 3) IS NOW DONE BY DEFAULT RECURSION Disconnecting the children.
//		for(NamespacePart nsp: namespaceParts()) {
//			nsp.disconnect();
//		}
	}

	/**
	 * A namespace part is disconnected if both its parent and its namespace are null.
	 */
 /*@
   @ public behavior
   @
   @ post \result == (parent() == null) && (namespace() == null);
   @*/
	@Override
	public boolean disconnected() {
		return super.disconnected() && namespaceLink().getOtherEnd() != null;
	}
	
	private Multi<NamespaceDeclaration> _subNamespaceParts = new Multi<NamespaceDeclaration>(this,"subnamespace parts");

	public List<Declaration> declarations() {
      return _declarations.getOtherEnds();
	}
	
	public <T extends Declaration> List<T> declarations(Class<T> kind) {
    return new TypePredicate<T>(kind).downCastedList(declarations());
  }
	
	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}


	/*************
	 * NAMESPACE *
	 *************/
	private Single<Namespace> _namespaceLink = new Single<Namespace>(this,true);

	/**
	 * Return the namespace to which this namespacepart adds declarations.
	 * @return
	 * @throws LookupException 
	 */
	public Namespace namespace() {
		Namespace stored = _namespaceLink.getOtherEnd();
		if(stored == null) {
			//FIXME When multi-language support is added, this must change
//			stored = view().namespace().getOrCreateNamespace(namespaceReference().toString());
			try {
//				System.out.println("resolving "+namespaceReference().toString());
				stored = namespaceReference().getElement();
			} catch (LookupException e) {
				throw new ChameleonProgrammerException(e);
			}
			stored.addNamespacePart(this);
		}
		return stored;
	}
	
	public void activate() {
		namespace();
		for(NamespaceDeclaration part: namespaceDeclarations()) {
			part.activate();
		}
	}
	
	public CrossReference<Namespace> namespaceReference() {
		return _ref.getOtherEnd();
	}
	
	public Single<Namespace> namespaceLink() {
		return _namespaceLink;
	}

	public void setNamespace(Namespace namespace) {
		if (namespace != null) {
			namespace.addNamespacePart(this);
		} else {
			_namespaceLink.connectTo(null);
		}
	}


	/******************
	 * DEMAND IMPORTS *
	 ******************/

	private Multi<Import> _imports = new Multi<Import>(this,"imports"){
		//FIXME SLOW but it works for now.
		protected void fireElementAdded(Import addedElement) {
			super.fireElementAdded(addedElement);
			_importSet = null;
			_importCache = null;
		};
		protected void fireElementRemoved(Import addedElement) {
			super.fireElementRemoved(addedElement);
			_importSet = null;
			_importCache = null;
		};
		protected void fireElementReplaced(Import oldElement, Import newElement) {
			super.fireElementReplaced(oldElement, newElement);
			fireElementRemoved(oldElement);
			fireElementAdded(newElement);
		};
	};
	{
		_imports.enableCache();
	}

	public List<Import> imports() {
////		use guava list builder
//		if(_importCache == null) {
//			_importCache = (ImmutableList<Import>) createImportCache(ImmutableList.<Import>builder());
//		}
//		return _importCache;
		return (ImmutableList<Import>)createImportCache(ImmutableList.<Import>builder());
	}

	protected ImmutableCollection createImportCache(ImmutableCollection.Builder<Import> builder) {
		List<? extends Import> implicitImports = implicitImports();
		List<? extends Import> explicitImports = explicitImports();
		return builder
				.addAll(explicitImports)
				.addAll(implicitImports)
				.build();
	}
	
	private boolean containsImport(Element element) {
		if(_importSet == null) {
			_importSet = (ImmutableSet<Import>) createImportCache(ImmutableSet.<Import>builder());
		}
		return _importSet.contains(element);

	}
	
	private ImmutableSet<Import> _importSet;
	
	private ImmutableList<Import> _importCache;
	
	public List<? extends Import> explicitImports() {
		return _imports.getOtherEnds();
	}

	public List<? extends Import> implicitImports() {
		return Collections.EMPTY_LIST;
		
	}
	public void addImport(Import newImport) {
		add(_imports,newImport);
	}
	
	public void removeImport(Import removedImport) {
		remove(_imports,removedImport);
	}
	
	public void clearImports() {
		_imports.clear();
	}
	
	public void removeDuplicateImports() throws LookupException {
		List<? extends Import> imports = imports();
		int nbImports = imports.size();
		for(int i=0; i< nbImports;i++) {
			Import outer = imports.get(i);
			for(int j=i+1; j< nbImports;) {
				Import inner = imports.get(j);
				if(outer.importsSameAs(inner)) {
					removeImport(inner);
					imports.remove(j);
					nbImports--;
				} else {
					j++;
				}
			}
		}
	}

	/****************
	 * DECLARATIONS *
	 ****************/
	private Multi<Declaration> _declarations = new Multi<Declaration>(this, "declarations");
	{
		_declarations.enableCache();
	}

	/**
	 * Add the given declaration to this namespace part.
	 */
 /*@
   @ public behavior
   @
   @ pre declaration != null;
   @
   @ post declarations().contains(declaration);
   @*/
	public void add(Declaration declaration) {
		add(_declarations,declaration);
	}
	
	public void addAll(Collection<Declaration> declarations) {
		for(Declaration decl: declarations) {
			add(decl);
		}
	}

	/**
	 * Remove the given declaration from this namespace part.
	 */
 /*@
   @ public behavior
   @
   @ pre declaration != null;
   @
   @ post !declarations().contains(declaration);
   @*/
	public void remove(Declaration declaration) {
		remove(_declarations,declaration);
	}

	/**
	 * The parents are possibly other PackageParts and the CompilationUnit
	 */



	/**
	 * ACCESSIBILITY
	 */

	public Language language(){
		Namespace namespace = null;
			namespace = namespace();
		if(namespace != null) {
		  return namespace.language();
		} else {
			return null;
		}
	}
	public LookupContextFactory getContextFactory() {
		return language().lookupFactory();
	}

 /*@
   @ public behavior
   @
   @ post \result = getDeclaredNamespace().getDefaultNamespace(); 
   @*/
	public Namespace getDefaultNamespace() throws LookupException {
		return view().namespace();
	}
  
	protected NamespaceDeclaration cloneSelf() {
  	return new NamespaceDeclaration((CrossReference)null);
  }

	private LookupContext _typeLocalContext;

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public LookupContext localContext() throws LookupException {
		throw new ChameleonProgrammerException("This method should never be invoked.");
	}
}
