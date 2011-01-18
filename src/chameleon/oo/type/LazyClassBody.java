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
import chameleon.core.reference.CrossReference;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.generics.TypeParameter;

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

	protected List<Declaration> declarations(String selectionName) throws LookupException {
		if(_initializedElements) {
			return super.declarations(selectionName);
		} else {
			List<Declaration> result = cachedDeclarations(selectionName);
			if(result == null) {
				List<Declaration> declarationsFromBaseType = original().declarations(selectionName);
				if(declarationsFromBaseType != null) {
					// If the cache was empty, and the aliased body contains matches
					// we clone the matches and store them in the cache.
					result = new ArrayList<Declaration>();
					// Use lazy initialization for the type parameters. We want to reuse the collection
					// for different elements in declarationsFromBaseType, but we don't want to compute
					// it unnecessarily when we don't need it, or compute it more than once if we do need it.
					ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
					for(Declaration<?,?,?,?> declarationFromBaseType: declarationsFromBaseType) {
						Element parent = declarationFromBaseType.parent();
						// Replace the references to the formal type parameters of base class with
						// references to the actual type arguments of the derived type that is the parent of
						// this lazy class body.
						Declaration clone = null;
						if(parent instanceof TypeElementStub) {
							clone = caseElementFromStub(selectionName, declarationFromBaseType, parent);
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
			}
			return result;
		}
	}

	private Declaration caseElementFromStub(String selectionName, Declaration<?, ?, ?, ?> declarationFromBaseType, Element parent) throws LookupException {
		Declaration clone = null;
		List<TypeParameter> typeParameters = null;
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		if(typeParameters == null) {
			typeParameters = original().nearestAncestor(Type.class).parameters(TypeParameter.class);
		}
		Declaration<?,?,?,?> declClone = declarationFromBaseType.clone();
		((Declaration)declClone).setUniParent(parent);
		Iterator<TypeParameter> parameterIter = typeParameters.iterator();
		while(parameterIter.hasNext()) {
			TypeParameter par = parameterIter.next();
			TypeReference tref = language.createTypeReference(par.signature().name());
			tref.setUniParent(this);
			language.replace(tref, par, declClone, Declaration.class);
		}
		TypeElementStub<?> stub = (TypeElementStub<?>) parent;
		TypeElement generator = stub.generator();
		TypeElement newGenerator = null;
		for(TypeElement element:super.elements()) {
			if(element.origin().sameAs(generator)) {
				newGenerator = element;
				break;
			}
		}
		if(newGenerator == null) {
			newGenerator = generator.clone();
			newGenerator.setOrigin(generator);
			super.add(newGenerator);
		}
		List<? extends Member> introducedByNewGenerator = newGenerator.getIntroducedMembers();
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
			flushLocalCache();
			clear();
			_statics = new ArrayList<TypeElement>();
			for(TypeElement element: original().elements()) {
				ChameleonProperty clazz = language(ObjectOrientedLanguage.class).CLASS;
				if(! element.isTrue(clazz)) {
				  super.add(element.clone());
				} else {
					_statics.add(element);
				}
			}
			_initializedElements = true;
		}
		List<TypeElement> result = super.elements();
		result.addAll(_statics);
		return result;
	}
	
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

	@Override
	public LazyClassBody clone() {
		LazyClassBody result = new LazyClassBody(original());
		return result;
	}

}
