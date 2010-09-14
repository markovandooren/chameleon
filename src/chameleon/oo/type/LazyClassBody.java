package chameleon.oo.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.Stub;
import chameleon.core.member.Member;
import chameleon.exception.ChameleonProgrammerException;

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
				List<Declaration> tmp = original().declarations(selectionName);
				if(tmp != null) {
					// If the cache was empty, and the aliased body contains matches
					// we clone the matches and store them in the cache.
					result = new ArrayList<Declaration>();
					for(Declaration decl: tmp) {
						Element parent = decl.parent();
						Declaration clone = null;
						if(parent instanceof TypeElementStub) {
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
							List<? extends Member> introduced = newGenerator.getIntroducedMembers();
							for(Member m: introduced) {
								Signature msig = m.signature();
								if(msig.name().equals(selectionName) && msig.sameAs(decl.signature())) {
									clone = m;
									break;
								}
							}
							if(clone == null) {
								throw new ChameleonProgrammerException();
							}
						} else {
						  clone = decl.clone();
						  super.add((TypeElement) clone); //FIX ME there should be a separate stub for type elements.
						}
					  clone.setOrigin(decl);
						result.add(clone);
					}
					storeCache(selectionName, result);
					result = new ArrayList<Declaration>(result);
				}
			}
			return result;
		}
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

	public List<TypeElement> elements() {
		if(! _initializedElements) {
			flushLocalCache();
			clear();
			for(TypeElement element: original().elements()) {
				super.add(element.clone());
			}
			_initializedElements = true;
		}
		List<TypeElement> result = super.elements();
		return result;
	}
	
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
