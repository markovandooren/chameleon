package chameleon.oo.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.property.ChameleonProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.generics.TypeParameter;

/**
 *
 * All non-static declarations of a lazy class body have their origin set to the corresponding
 * declaration in the base type.
 * 
 * @author Marko van Dooren
 */
public class LazyClassBody extends ClassBody {
	
	public LazyClassBody(ClassBody original) {
		setClassBody(original);
	}
	
	private ClassBody _original;
	
	public ClassBody original() {
		return _original;
	}
	
	protected void setClassBody(ClassBody original) {
		_original = original;
	}

	/**
	 * Return the declarations with the given name. The declarations are loaded lazily from the base type.
	 */
	protected List<Declaration> declarations(String selectionName) throws LookupException {
		if(_initializedElements) {
			return super.declarations(selectionName);
		} else {
			List<Declaration> result = cachedDeclarations(selectionName);
			if(result == null) {
				List<Declaration> declarationsFromBaseType = original().declarations(selectionName);
				result = fetchMembers(selectionName, declarationsFromBaseType);
			}
			return result;
		}
	}

	public List<Declaration> fetchMembers(String selectionName, List<Declaration> declarationsFromBaseType)
			throws LookupException {
		ArrayList<Declaration> result = new ArrayList<Declaration>();
		if(declarationsFromBaseType != null) {
			// If the cache was empty, and the aliased body contains matches
			// we clone the matches and store them in the cache.
			// Use lazy initialization for the type parameters. We want to reuse the collection
			// for different elements in declarationsFromBaseType, but we don't want to compute
			// it unnecessarily when we don't need it, or compute it more than once if we do need it.
			ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
			for(Declaration<?,?,?> declarationFromBaseType: declarationsFromBaseType) {
				Element parent = declarationFromBaseType.parent();
				// Replace the references to the formal type parameters of base class with
				// references to the actual type arguments of the derived type that is the parent of
				// this lazy class body.
				Declaration clone = null;
				if(parent instanceof TypeElementStub) {
					clone = caseElementFromStub(selectionName, declarationFromBaseType, (TypeElementStub) parent);
				} else {
					if(! declarationFromBaseType.isTrue(language.CLASS)) {
					  clone = declarationFromBaseType.clone();
					  super.add((TypeElement) clone); //FIX ME there should be a separate stub for type elements.
					  clone.setOrigin(declarationFromBaseType);
					} else {
						clone = declarationFromBaseType;
					}
				}
				result.add(clone);
			}
			storeCache(selectionName, result);
			result = new ArrayList<Declaration>(result);
		}
		return result;
	}

	private Declaration caseElementFromStub(String selectionName, Declaration<?, ?, ?> declarationFromBaseType, TypeElementStub stub) throws LookupException {
		Declaration clone = null;
		// 1. Clone the declaration 
		Declaration<?,?,?> declClone = declarationFromBaseType.clone();
		// 2. Substitute the type parameters with those of the surrounding DerivedType.
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		List<TypeParameter> typeParameters = original().nearestAncestor(Type.class).parameters(TypeParameter.class);
		declClone.setUniParent(stub);
		Iterator<TypeParameter> parameterIter = typeParameters.iterator();
		while(parameterIter.hasNext()) {
			TypeParameter par = parameterIter.next();
			// Creating a new type reference with the same name will ensure that the name is
			// resolved in the current context, and thus point to the corresponding type
			// parameter in the surrounding DerivedType.
			TypeReference tref = language.createTypeReference(par.signature().name());
			tref.setUniParent(this);
			language.replace(tref, par, declClone, Declaration.class);
		}
		// 3. Find out who generated the stub
		TypeElement generator = stub.generator();
		// 4. Search for the clone of the generator and store it in newGenerator.
		TypeElement newGenerator = null;
		for(TypeElement element:super.elements()) {
			if(element.origin().sameAs(generator)) {
				newGenerator = element;
				break;
			}
		}
		// 5. If the generator had not yet been cloned before, we do it now.
		if(newGenerator == null) {
			newGenerator = generator.clone();
			newGenerator.setOrigin(generator);
			super.add(newGenerator);
		}
		// 6. Ask which members are introduced by the new (cloned) generator.
		List<? extends Member> introducedByNewGenerator = newGenerator.getIntroducedMembers();
		// 7. Search for the one with the same signature as the clone of the declaration (which is in the same context). 
		for(Member m: introducedByNewGenerator) {
			Signature msig = m.signature();
			if(msig.name().equals(selectionName) && msig.sameAs(declClone.signature())) {
				clone = m;
				break;
			}
		}
		if(clone == null) {
			//DEBUG CODE
			for(Member m: introducedByNewGenerator) {
				Signature msig = m.signature();
				if(msig.name().equals(selectionName) && msig.sameAs(declClone.signature())) {
					clone = m;
					break;
				}
			}
			throw new ChameleonProgrammerException();
		}
		return clone;
	}

	public void add(TypeElement element) {
		throw new ChameleonProgrammerException("Trying to add an element to a lazy class body.");
	}

	public void remove(TypeElement element) {
		throw new ChameleonProgrammerException("Trying to remove an element from a lazy class body.");
	}

	private boolean _initializedElements = false;
	
	
	
	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		_initializedElements = false;
	}

	public synchronized List<TypeElement> elements() {
		if(! _initializedElements) {
//			flushLocalCache();
//			clear();
			_statics = new ArrayList<TypeElement>();
			List<TypeElement> alreadyCloned = super.elements();
			for(TypeElement element: original().elements()) {
				ChameleonProperty clazz = language(ObjectOrientedLanguage.class).CLASS;
				TypeElement clonedElement=null;
				for(TypeElement already: alreadyCloned) {
					if(already.origin().equals(element)) { //EQUALS
						clonedElement = already;
						break;
					}
				}
				if(clonedElement == null) {
					if(! element.isTrue(clazz)) {
						clonedElement = element.clone();
						super.add(clonedElement);
					} else {
						_statics.add(element);
					}
				} else {
					super.add(clonedElement);
				}
			}
			_initializedElements = true;
		}
		List<TypeElement> result = super.elements();
		result.addAll(_statics);
		return result;
	}

	//	public synchronized List<TypeElement> elements() {
//		if(! _initializedElements) {
//			flushLocalCache();
//			clear();
//			_statics = new ArrayList<TypeElement>();
//			for(TypeElement element: original().elements()) {
//				ChameleonProperty clazz = language(ObjectOrientedLanguage.class).CLASS;
//				if(! element.isTrue(clazz)) {
//				  super.add(element.clone());
//				} else {
//					_statics.add(element);
//				}
//			}
//			_initializedElements = true;
//		}
//		List<TypeElement> result = super.elements();
//		result.addAll(_statics);
//		return result;
//	}
	
	private List<TypeElement> _statics;
	
	public <D extends Member> List<D> members(DeclarationSelector<D> selector) throws LookupException {
		if(selector.usesSelectionName()) {
			List<? extends Declaration> list = null;
			if(Config.cacheDeclarations()) {
				list = declarations(selector.selectionName(this));
			} else {
				list = members();
			}
			if(list == null) {
				list = Collections.EMPTY_LIST;
			}
			return selector.selection(Collections.unmodifiableList(list));
		} else {
			return selector.selection(declarations());
		}
	}
	
	public <D extends Member> List<D> members(Class<D> kind) throws LookupException {
		List<D> originals = original().members(kind);
		List<D> result = new ArrayList<D>();
		for(D original:originals) {
			List<Declaration> clones = declarations(original.signature().name());
			for(Declaration clone:clones) {
				if(kind.isInstance(clone)) {
					result.add((D) clone);
				}
			}
		}
		return result;
	}

	@Override
	public LazyClassBody clone() {
		LazyClassBody result = new LazyClassBody(original());
		return result;
	}

}
