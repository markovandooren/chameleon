package chameleon.core.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.lookup.DeclarationCollector;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacedeclaration.DemandImport;
import chameleon.core.namespacedeclaration.Import;
import chameleon.core.reference.ElementReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import {
  
  public DemandImport(ElementReference<? extends Namespace> ref) {
    setNamespaceReference( (ElementReference<? extends Namespace>) ref);
  }
  
	private Single<ElementReference<? extends Namespace>> _packageOrType = new Single<ElementReference<? extends Namespace>>(this);

  
  public ElementReference<? extends Namespace> namespaceReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceReference(ElementReference<? extends Namespace> ref) {
  	set(_packageOrType,ref);
  }
  
  public DeclarationContainer declarationContainer() throws LookupException {
    return namespaceReference().getElement();
  }
  
  @Override
  public DemandImport clone() {
    return new DemandImport((ElementReference<? extends Namespace>) namespaceReference().clone());
  }


	@Override
	public List<Declaration> demandImports() throws LookupException {
		return (List<Declaration>) declarationContainer().declarations();
	}


	@Override
	public List<Declaration> directImports() throws LookupException {
		return new ArrayList<Declaration>();
	}

//	@Override
//	public synchronized void flushLocalCache() {
//		_declarationCache = null;
//	}

	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
//		if(selector.usesSelectionNameOnly()) {
//			String selectionName = selector.selectionName(null);
//			Class<D> selectedClass = selector.selectedClass();
//			List<D> d = cachedDeclaration(selectionName, selectedClass);
//			if(d != null) {
//			  return d;
//			} else {
//				List<D> result = importedDeclarations(selector);
//				storeCache(selectionName, selectedClass, result);
//				return result;
//			}
//		} else {
			return importedDeclarations(selector);
//	  }
	}

	private <D extends Declaration> List<D> importedDeclarations(DeclarationSelector<D> selector) throws LookupException {
		DeclarationCollector<D> collector = new DeclarationCollector<D>(selector);
		declarationContainer().localStrategy().lookUp(collector);
		List<D> result = new ArrayList<D>();
		if(! collector.willProceed()) { 
		  D selected = collector.result();
		  Util.addNonNull(selected, result);
		}
		return result;
	}

//	private synchronized <D extends Declaration> List<D> cachedDeclaration(String name, Class<D> kind) {
//		if(_declarationCache != null) {
//			Map<Class,List<? extends Declaration>> classMap = _declarationCache.get(name);
//			if(classMap != null) {
//				return (List<D>) classMap.get(kind);
//			}
//		}
//		return null;
//	}
	
//	private synchronized <D extends Declaration> void storeCache(String name, Class<D> kind, List<D> declaration) {
//		if(_declarationCache == null) {
//			_declarationCache = new HashMap<String,Map<Class,List<? extends Declaration>>>();
//		}
//		Map<Class,List<? extends Declaration>> classMap = _declarationCache.get(name);
//		if(classMap == null) {
//			classMap = new HashMap<Class,List<? extends Declaration>>();
//			_declarationCache.put(name, classMap);
//		}
//		classMap.put(kind, declaration);
//	}

//	private Map<String,Map<Class,List<? extends Declaration>>> _declarationCache;

	
	
	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
		return Collections.EMPTY_LIST;
	}


	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
	public Namespace importedNamespace() throws LookupException {
		return namespaceReference().getElement();
	}

	public boolean importsSameAs(Import other) throws LookupException {
		return super.importsSameAs(other);
	}
//	@Override
//	public boolean importsSameAs(Import other) throws LookupException {
//		return other instanceof DemandImport && ((DemandImport)other).importedNamespace().sameAs(importedNamespace());
//	}
//  
}
