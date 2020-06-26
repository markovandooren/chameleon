package org.aikodi.chameleon.oo.type;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.util.Lists;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * <p>A class body that lazily copies its member from another class
 * body.</p>
 *
 * <p>Members are not synchronized with their origin.
 * All non-static declarations of a lazy class body have their origin 
 * set to the corresponding declaration in the base type.</p>
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
	@Override
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

	protected List<Declaration> fetchMembers(String selectionName, List<Declaration> declarationsFromBaseType)
			throws LookupException {
		Builder<Declaration> builder = ImmutableList.builder();
		List<Declaration> result = ImmutableList.of();
		if(declarationsFromBaseType != null) {
			// If the cache was empty, and the aliased body contains matches
			// we clone the matches and store them in the cache.
			// Use lazy initialization for the type parameters. We want to reuse the collection
			// for different elements in declarationsFromBaseType, but we don't want to compute
			// it unnecessarily when we don't need it, or compute it more than once if we do need it.
			for(Declaration declarationFromBaseType: declarationsFromBaseType) {
				Element parent = declarationFromBaseType.lexical().parent();
				// Replace the references to the formal type parameters of base class with
				// references to the actual type arguments of the derived type that contains
				// this lazy class body.
				Declaration clone = null;
				if(parent instanceof DeclaratorStub) {
					clone = caseElementFromStub(selectionName, declarationFromBaseType, (DeclaratorStub) parent);
				} else {
					if(mustCopy(declarationFromBaseType)) {
						clone = clone(declarationFromBaseType);
						super.add((Declarator) clone); //FIX ME there should be a separate stub for type elements.
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

	private boolean mustCopy(Declaration declarationFromBaseType) {
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		return ! declarationFromBaseType.isTrue(language.CLASS);
	}

	/**
	 * Clone with given declaration.
	 * 
	 * @param selectionName
	 * @param declarationFromBaseType
	 * @param stub
	 * @return
	 * @throws LookupException
	 */
	private Declaration caseElementFromStub(String selectionName, Declaration declarationFromBaseType, DeclaratorStub stub) throws LookupException {
		Declaration clone = null;
		// 1. Clone the declaration 
		Declaration declClone = clone(declarationFromBaseType);
		// 2. Substitute the type parameters with those of the surrounding DerivedType.
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		List<TypeParameter> typeParameters = original().lexical().nearestAncestor(Type.class).parameters(TypeParameter.class);
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
		Declarator generator = stub.generator();
		// 4. Search for the clone of the generator and store it in newGenerator.
		Declarator newGenerator = null;
		for(Declarator element:super.elements()) {
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
		List<? extends Declaration> introducedByNewGenerator = newGenerator.declaredDeclarations();
		// 7. Search for the one with the same signature as the clone of the declaration (which is in the same context). 
		for(Declaration m: introducedByNewGenerator) {
			if(m.name().equals(selectionName) && m.sameSignatureAs(declClone)) {
				clone = m;
				break;
			}
		}
		if(clone == null) {
			//DEBUG CODE
			for(Declaration m: introducedByNewGenerator) {
				if(m.sameSignatureAs(declClone)) {
					clone = m;
					break;
				}
			}
			throw new ChameleonProgrammerException();
		}
		return clone;
	}

	/**
	 * Trying to add something to a lazy class body will result in an exception.
	 * 
	 * @throws ChameleonProgrammerException Always
	 */
	@Override
	public void add(Declarator element) {
		throw new ChameleonProgrammerException("Trying to add an element to a lazy class body.");
	}

	/**
	 * Trying to remove something from a lazy class body will result in an exception.
	 * 
	 * @throws ChameleonProgrammerException Always
	 */
	@Override
	public void remove(Declarator element) {
		throw new ChameleonProgrammerException("Trying to remove an element from a lazy class body.");
	}

	private boolean _initializedElements = false;



	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		_initializedElements = false;
	}

	@Override
	public synchronized List<Declarator> elements() {
		if(! _initializedElements) {
			_statics = Lists.create();
			List<Declarator> alreadyCloned = super.elements();
			for(Declarator element: original().elements()) {
				ChameleonProperty clazz = language(ObjectOrientedLanguage.class).CLASS;
				Declarator clonedElement=null;
				for(Declarator already: alreadyCloned) {
					if(already.origin().equals(element)) { //EQUALS
						clonedElement = already;
						break;
					}
				}
				if(clonedElement == null) {
					if(! element.isTrue(clazz)) {
						clonedElement = clone(element);
						clonedElement.setOrigin(element);
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
		List<Declarator> result = super.elements();
		result.addAll(_statics);
		return result;
	}

	private List<Declarator> _statics;

	@Override
	public <D extends Declaration> List<? extends SelectionResult<D>> members(DeclarationSelector<D> selector) throws LookupException {
		if(selector.usesSelectionName()) {
			List<? extends Declaration> list = null;
			list = declarations(selector.selectionName(this));
			if(list == null) {
				list = Collections.emptyList();
			}
			return selector.selection(Collections.unmodifiableList(list));
		} else {
			return selector.selection(declarations());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <D extends Declaration> List<D> members(Class<D> kind) throws LookupException {
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
