package chameleon.oo.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
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
		List<Declaration> result = cachedDeclarations(selectionName);
  	if(result == null) {
  		List<Declaration> tmp = original().declarations(selectionName);
  		if(tmp != null) {
  			// If the cache was empty, and the aliased body contains matches
  			// we clone the matches and store them in the cache.
  			result = new ArrayList<Declaration>();
  			for(Declaration decl: tmp) {
  				Declaration clone = decl.clone();
  				super.add((TypeElement) clone);
					result.add(clone);
  			}
  			storeCache(selectionName, result);
  			result = new ArrayList<Declaration>(result);
  		}
  	}
  	return result;
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
			List<TypeElement> tmp = super.elements();
			for(TypeElement element: original().elements()) {
				// PROBLEM This will introduce doubles!
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

	public List<Member> members() throws LookupException {
		List<Member> result = new ArrayList<Member>();
    for(TypeElement m: elements()) {
      result.addAll(m.getIntroducedMembers());
    }
    return result;
	}

	@Override
	public LazyClassBody clone() {
		LazyClassBody result = new LazyClassBody(original());
		return result;
	}

}
