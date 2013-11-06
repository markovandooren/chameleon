package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.TypeParameter;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

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
		setOrigin(original);
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
		Builder<Declaration> builder = ImmutableList.builder();
		List<Declaration> result = ImmutableList.of();
		if(declarationsFromBaseType != null) {
			// If the cache was empty, and the aliased body contains matches
			// we clone the matches and store them in the cache.
			// Use lazy initialization for the type parameters. We want to reuse the collection
			// for different elements in declarationsFromBaseType, but we don't want to compute
			// it unnecessarily when we don't need it, or compute it more than once if we do need it.
			ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
			for(Declaration declarationFromBaseType: declarationsFromBaseType) {
				Element parent = declarationFromBaseType.parent();
				// Replace the references to the formal type parameters of base class with
				// references to the actual type arguments of the derived type that is the parent of
				// this lazy class body.
				Declaration clone = null;
				if(parent instanceof TypeElementStub) {
					clone = caseElementFromStub(selectionName, declarationFromBaseType, (TypeElementStub) parent);
				} else {
					if(! declarationFromBaseType.isTrue(language.CLASS)) {
					  clone = clone(declarationFromBaseType);
					  super.add((TypeElement) clone); //FIX ME there should be a separate stub for type elements.
					  clone.setOrigin(declarationFromBaseType);
					} else {
						clone = declarationFromBaseType;
					}
				}
				builder.add(clone);
			}
			result = builder.build();
			storeCache(selectionName, result);
		}
		return result;
	}

	private Declaration caseElementFromStub(String selectionName, Declaration declarationFromBaseType, TypeElementStub stub) throws LookupException {
		Declaration clone = null;
		// 1. Clone the declaration 
		Declaration declClone = clone(declarationFromBaseType);
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
			TypeReference tref = language.createTypeReference(par.name());
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
			newGenerator = clone(generator);
			newGenerator.setOrigin(generator);
			super.add(newGenerator);
		}
		// 6. Ask which members are introduced by the new (cloned) generator.
		List<? extends Member> introducedByNewGenerator = newGenerator.getIntroducedMembers();
		// 7. Search for the one with the same signature as the clone of the declaration (which is in the same context). 
		for(Member m: introducedByNewGenerator) {
			if(m.name().equals(selectionName) && m.sameSignatureAs(declClone)) {
				clone = m;
				break;
			}
		}
		if(clone == null) {
			//DEBUG CODE
			for(Member m: introducedByNewGenerator) {
				if(m.sameSignatureAs(declClone)) {
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
			_statics = Lists.create();
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
						clonedElement = clone(element);
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
	
	public <D extends Member> List<? extends SelectionResult> members(DeclarationSelector<D> selector) throws LookupException {
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
		List<D> result = Lists.create();
		for(D original:originals) {
			List<Declaration> clones = declarations(original.name());
			for(Declaration clone:clones) {
				if(kind.isInstance(clone)) {
					result.add((D) clone);
				}
			}
		}
		return result;
	}

	@Override
	public LazyClassBody cloneSelf() {
		return new LazyClassBody(original());
	}

}
