package be.kuleuven.cs.distrinet.chameleon.core.element;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.language.WrongLanguageException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.tag.Metadata;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.util.association.ChameleonAssociation;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.chameleon.workspace.WrongViewException;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.association.AbstractMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.AssociationListener;
import be.kuleuven.cs.distrinet.rejuse.association.IAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;
import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;
import be.kuleuven.cs.distrinet.rejuse.property.Conflict;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

import com.google.common.collect.ImmutableList;

/**
 * @author Marko van Dooren
 * 
 * @opt operations
 * @opt attributes
 * @opt visibility
 * @opt types
 */
public abstract class ElementImpl implements Element {

	private static Map<Class,Set<String>> _excludedFieldNames = new HashMap<Class,Set<String>>();

	public ElementImpl() {
		//	  	_parentLink.addListener(new AssociationListener<P>() {
		//
		//				public void notifyElementAdded(P element) {
		//          super.notifyElementAdded(element);
		//					notifyParent(ElementImpl.this);
		//				}
		//
		//				public void notifyElementRemoved(P element) {
		//          super.notifyElementRemoved(element);
		//					notifyParent(ElementImpl.this);
		//				}
		//
		//				public void notifyElementReplaced(P oldElement, P newElement) {
		//          super.notifyElementReplaced(oldElement,newElement);
		//					notifyParent(ElementImpl.this);
		//				}
		//	  		
		//	  	});
	}
	
	private static TreeStructure<Element> _lexical = new LexicalNavigator();
	
	@Override
	public TreeStructure<Element> lexical() {
		return _lexical;
	}
	
	private static TreeStructure<Element> _logical = new LogicalNavigator();
	
	@Override
	public TreeStructure<Element> logical() {
		return _logical;
	}
	
//  private Tree<Element> _lexical;
//  
//  public Tree<Element> lexical() {
//  	if(_lexical == null) {
//  		_lexical = new Tree<Element>() {
//
//				@Override
//				public Element parent(Element element) {
//					return ElementImpl.this.actualParent();
//				}
//			};
//  	}
//  	return _lexical;
//  }
//	private Tree<Element> _logical;

	
	/**
	 * For debugging purposes. Invoke enableParentListening() to enable this functionality.
	 */
	protected void notifyParentSet(Element element) {}
	protected void notifyParentRemoved(Element element) {}
	protected void notifyParentReplaced(Element oldParent, Element newParent) {}
	
	protected void enableParentListening() {
	 parentLink().addListener(new AssociationListener<Element>() {
		@Override
		public void notifyElementAdded(Element element) {
			ElementImpl.this.notifyParentSet(element);
		}

		@Override
		public void notifyElementRemoved(Element element) {
			ElementImpl.this.notifyParentRemoved(element);
		}

		@Override
		public void notifyElementReplaced(Element oldElement, Element newElement) {
			ElementImpl.this.notifyParentReplaced(oldElement, newElement);
		}
	  });
	}

	/********
	 * TAGS *
	 ********/

	// initialization of this Map is done lazily.
	private Map<String, Metadata> _tags;

	public Metadata metadata(String name) {
		if(_tags != null) {
			return _tags.get(name);
		} else {
			//lazy init has not been performed yet.
			return null;
		}
	}

	public void removeMetadata(String name) {
		if(_tags != null) {
			Metadata old = _tags.get(name);
			_tags.remove(name);
			if((old != null) && (old.getElement() == this)){
				old.setElement(null,name);
			}
		}
	}

	public void removeAllMetadata() {
		if(_tags != null) {
			List<String> tagNames = new ArrayList<>(_tags.keySet());
			for(String tagName: tagNames) {
				removeMetadata(tagName);
			}
		}
	}
	
	public void freeze() {
		for(Element element: children()) {
			element.parentLink().lock();
			element.freeze();
		}
		for(IAssociation association: associations()) {
			association.lock();
		}
	}

	public void unfreeze() {
		for(Element element: children()) {
			element.parentLink().unlock();
			element.unfreeze();
		}
		for(IAssociation association: associations()) {
			association.unlock();
		}
	}

	public void setMetadata(Metadata decorator, String name) {
		//Lazy init of hashmap
		if (_tags==null) {
			_tags = new HashMap<String, Metadata>();
		}
		Metadata old = _tags.get(name); 
		if(old != decorator) {
			if((decorator != null) && (decorator.getElement() != this)) {
				decorator.setElement(this,name);
			}
			if (old != null) {
				old.setElement(null,name);
			}
			_tags.put(name, decorator);
		}
	}

	public Collection<Metadata> metadata() {
		if(_tags == null) {
			return Collections.EMPTY_LIST;
		} else {
			return _tags.values();
		}
	}

	public boolean hasMetadata(String name) {
		if(_tags == null) {
			return false;
		} else {
			return _tags.get(name) != null;
		}
	}

	public boolean hasMetadata() {
		if(_tags == null) {
			return false;
		} else {
			return _tags.size() > 0;
		}
	}
	
	public final <T extends Element> T clone(T element) {
		return (T) element.clone();
	}
	
	public Element clone() {
//		return clone((Mapper)null);
//	}
//
//	public final Element clone(final Mapper mapper) {
		Element result = cloneSelf();
		if(canHaveChildren()) {
			List<ChameleonAssociation<?>> mine = myAssociations();
			int size = mine.size();
			List<ChameleonAssociation<?>> others = result.associations();
			for(int i = 0; i<size;i++) {
				ChameleonAssociation<? extends Element> m = mine.get(i);
				final ChameleonAssociation<? extends Element> o = others.get(i);
				m.apply(new Action<Element,Nothing>(Element.class) {
					@Override
					public void doPerform(Element myElement) {
						Element clone = myElement.clone();
						clone.parentLink().connectTo((Association)o);
					}
				});
			}
		}
//		if(mapper != null) {
//			mapper.process(this, result);
//		}
		return result;
	}
	
	protected abstract Element cloneSelf();
	
	@Override
	public View view() {
		Element parent = parent();
		if(parent != null) {
			return parent.view();
		} else {
			return null;
		}
	}
	
	@Override
	public <T extends View> T view(Class<T> kind) {
		if(kind == null) {
			throw new ChameleonProgrammerException("The given language class is null.");
		}
		View view = view();
		if(kind.isInstance(view) || view == null) {
			return (T) view;
		} else {
			throw new WrongViewException("The view of this element is of the wrong kind. Expected: "+kind.getName()+" but got: " +view.getClass().getName());
		}
	}

	@Override
	public final Project project() {
		return view().project();
	}

	/**********
	 * PARENT *
	 **********/

	// WORKING AROUND LACK OF MULTIPLE INHERITANCE

	// THESE VARIABLES MUST NOT BE USED AT THE SAME TIME
	//
	// IF _parentLink IS NULL, THE ELEMENT IS NOT LEXICALLY IN THE MODEL,
	// IN WHICH CASE _parent PROVIDES THE UNIDIRECTIONAL ASSOCIATION
	// WITH THE PARENT. IN THAT CASE, THE ORIGIN IS SET TO THE ELEMENT
	// OF WHICH THIS ELEMENT IS A DERIVED ELEMENT
	private Single<Element> _parentLink = createParentLink();//new SingleAssociation<E,P>((E) this);

	/**
	 * This is the undirectional association with the parent in case this element is derived.
	 */
	private Element _parent;

	/**
	 * Return the bidirectional link to the parent in case the element IS NOT derived.
	 * DO NOT USE THIS TO OBTAIN THE PARENT
	 * 
	 * @throws ChameleonProgrammerException
	 *    The method is invoked on a derived element. 
	 */
	public final SingleAssociation<Element,Element> parentLink() {
		if(_parentLink != null) {
			return _parentLink;
		} else {
			throw new ChameleonProgrammerException("Invoking getParentLink() on automatic derivation");
		}
	}

	protected Single<Element> createParentLink() {
		return new Single<Element>(this);
	}

	/**
	 * Return the parent of this element
	 */
	public final Element actualParent() {
		if(_parentLink != null) {
			return _parentLink.getOtherEnd();
		} else {
			return _parent;
		}
	}
	
	public final Element parent() {
		return lexical().parent(this);
	}

	/**
	 * Check if this element is derived or not.
	 * 
	 * @return True if this element is derived, false otherwise.
	 */
	public boolean isDerived() {
		return _parent != null;
	}

	/**
	 * The default behavior is to return 'this'.
	 */
	public Element origin() {
		return _origin;
	}

	public Element farthestOrigin() {
		Element element = this;
		Element origin = origin();
		while(element != origin) {
			element = origin;
			origin = element.origin();
		}
		return origin;
	}

	public Element rootOrigin() {
		Element current = this;
		Element origin = origin();
		while(current != origin) {
			current = origin;
			origin = current.origin();
		}
		return current;
	}

	public void setOrigin(Element origin) {
		_origin = origin;
	}

	private Element _origin = this;

	public void disconnect() {
		nonRecursiveDisconnect();
		disconnectChildren();
	}

	public boolean disconnected() {
		return parent() == null;
	}

	public void nonRecursiveDisconnect() {
		if(_parentLink != null) {
			_parentLink.connectTo(null);
		} else {
			_parent = null;
		}
		removeAllMetadata();
	}

	/**
	 * DO NOT OVERRIDE THIS METHOD UNLESS YOU *REALLY* KNOW WHAT YOU ARE DOING! We don't see
	 * any use other than diagnostic purposes.
	 */
	public void setUniParent(Element parent) {
		if(_parentLink != null) {
			_parentLink.connectTo(null);
		}
		if(parent != null) {
			_parentLink = null;
		} else if(_parentLink == null) {
			_parentLink = createParentLink();
		}
		_parent = parent;
	}

	public final List<Element> descendants() {
		return descendants(Element.class);
	}

	/**
	 * Exclude the given field name of the given class from the lexical associations.
	 * 
	 * @param type
	 * @param fieldName
	 */
	public static void excludeFieldName(Class<? extends Element> type, String fieldName) {
		Set<String> set = _excludedFieldNames.get(type);
		if(set == null) {
			set = new HashSet<String>();
			_excludedFieldNames.put(type, set);
		}
		set.add(fieldName);
	}

	static {
		excludeFieldName(ElementImpl.class,"_parentLink");
	}
	private static Set<String> excludedFieldNames(Class<? extends Element> type) {
		return _excludedFieldNames.get(type);
	}

	private static Map<Class, List<Field>> _fieldMap = new HashMap<Class, List<Field>>();

	private static List<Field> getAllFieldsTillClass(Class currentClass){
		List<Field> result = _fieldMap.get(currentClass);
		if(result == null) {
			result = Lists.create();
			addAllFieldsTillClass(currentClass, result);
			_fieldMap.put(currentClass, result);
		}
		return result;
	}


	private List<ChameleonAssociation<?>> _associations;

	public synchronized List<ChameleonAssociation<?>> associations() {
		return myAssociations();
	}

	private synchronized List<ChameleonAssociation<?>> myAssociations() {
		if(_associations == null) {
			List<Field> fields = getAllFieldsTillClass(getClass());
			int size = fields.size();
			if(size > 0) {
				List<ChameleonAssociation<?>> tmp = new ArrayList<>(size);
				for (Field field : fields) {
					Object content = getFieldValue(field);
					tmp.add((ChameleonAssociation<?>) content);
				}
				_associations = ImmutableList.copyOf(tmp);
			}
			else {
				_associations = Collections.EMPTY_LIST;
			}
		}
		return _associations;
	}
	
	private boolean canHaveChildren() {
		return ! getAllFieldsTillClass(getClass()).isEmpty();
	}
	


	private static void addAllFieldsTillClass(final Class currentClass, Collection<Field> accumulator){
		Field[] fields = currentClass.getDeclaredFields();
		for(Field field: fields) {
			Set<String> set = _excludedFieldNames.get(currentClass);
			boolean excluded = (set != null) && set.contains(field.getName());
			if(! excluded && ChameleonAssociation.class.isAssignableFrom(field.getType())) {
				accumulator.add(field);
			}
		}
		if(currentClass != ElementImpl.class) {
			Class superClass = currentClass.getSuperclass();
			accumulator.addAll(getAllFieldsTillClass(superClass));
		}
	}

	public Object getFieldValue(Field field){
		try {
			field.setAccessible(true);
			return field.get(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {	
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Do no override unless you know what you are doing!
	 * 
	 * This method uses the reflection mechanism, which saves a lot of
	 * children() methods that would only compute the union of all the 
	 * Association objects referenced by this element. If an association element
	 * should not be included in the list of children, use the following code in the 
	 * class (class name is "X", field name is "_f").
	 * 
	 * <pre>
	 *   static {
   *     excludeFieldName(X.class,"_f");
   *   }
	 * </pre>
	 * 
	 * This method currently is overridden only to provide support for lazy loading in
	 * LazyNamespace.
	 */
  public List<? extends Element> children() {
  	List<Element> reflchildren = Lists.create();
		for (ChameleonAssociation association : associations()) {
			association.addOtherEndsTo(reflchildren);
		}
		return reflchildren;
  }

	public final <T extends Element> List<T> children(Class<T> c) {
		return new TypePredicate<T>(c).downCastedList(children());
	}

	public final <E extends Exception> List<Element> children(Predicate<? super Element,E> predicate) throws E {
		List<? extends Element> tmp = children();
		predicate.filter(tmp);
		return (List<Element>)tmp;
	}

	public final <T extends Element> List<T> descendants(Class<T> c) {
		List<T> result = children(c);
		for (Element e : children()) {
			result.addAll(e.descendants(c));
		}
		return result;
	}

	public final <T extends Element> boolean hasDescendant(Class<T> c) {
		List<Element> tmp = (List<Element>) children();
		new TypePredicate<T>(c).filter(tmp);

		if (!tmp.isEmpty())
			return true;

		for (Element e : children()) {
			if (e.hasDescendant(c))
				return true;
		}

		return false;
	}

	@Override
	public final <T extends Element, E extends Exception> boolean hasDescendant(UniversalPredicate<T,E> predicate) throws E {
		List<T> result = children(predicate);
		if (!result.isEmpty())
			return true;

		for (Element e : children()) {
			if (e.hasDescendant(predicate))
				return true;
		}

		return false;
	}
	
	public final <T extends Element> List<T> nearestDescendants(Class<T> c) {
		List<? extends Element> tmp = children();
		List<T> result = Lists.create();
		Iterator<? extends Element> iter = tmp.iterator();
		while(iter.hasNext()) {
			Element e = iter.next();
			if(c.isInstance(e)) {
				result.add((T)e);
				iter.remove();
			}
		}
		for (Element e : tmp) {
			result.addAll(e.nearestDescendants(c));
		}
		return result;
	}

  public <T extends Element, E extends Exception> List<T> nearestDescendants(UniversalPredicate<T,E> predicate) throws E {
		List<? extends Element> tmp = children();
		List<T> result = Lists.create();
		Iterator<? extends Element> iter = tmp.iterator();
		while(iter.hasNext()) {
			Element e = iter.next();
			if(predicate.eval(e)) {
				result.add((T)e);
				iter.remove();
			}
		}
		for (Element e : tmp) {
			result.addAll(e.nearestDescendants(predicate));
		}
		return result;
  }

	
	public final <E extends Exception> List<Element> descendants(Predicate<? super Element,E> predicate) throws E {
		// Do not compute all descendants, and apply predicate afterwards.
		// That is way too expensive.
		List<? extends Element> tmp = children();
		predicate.filter(tmp);
		List<Element> result = (List<Element>)tmp;
		for (Element e : children()) {
			result.addAll(e.descendants(predicate));
		}
		return result;
	}

	@Override
	public final <T extends Element, E extends Exception> List<T> children(UniversalPredicate<T,E> predicate) throws E {
		return predicate.downCastedList(children());
	}

	public final <T extends Element> List<T> children(Class<T> c, final ChameleonProperty property) {
		List<T> result = children(new UniversalPredicate<T,Nothing>(c) {
			public boolean uncheckedEval(T element) {
				return element.isTrue(property);
			}
		});
		return result;
	}

	public final <T extends Element> List<T> descendants(Class<T> c, ChameleonProperty property) {
		List<T> result = children(c, property);
		for (Element e : children()) {
			result.addAll(e.descendants(c, property));
		}
		return result;
	}

	public final <T extends Element,E extends Exception> List<T> descendants(Class<T> c, Predicate<T,E> predicate) throws E {
		List<T> result = children(c);
		predicate.filter(result);
		for (Element e : children()) {
			result.addAll(e.descendants(c, predicate));
		}
		return result;
	}

	public final <T extends Element, E extends Exception>  void apply(Action<T,E> action) throws E {
		if(action.type().isInstance(this)) {
			action.perform((T)this);
		}
		for (Element e : children()) {
			e.apply(action);
		}
	}

	public final <T extends Element> List<T> ancestors(Class<T> c) {
		List<T> result = Lists.create();
		T el = nearestAncestor(c);
		while (el != null){
			result.add(el);
			el = el.nearestAncestor(c);
		}
		return result;
	}

	@Override
	public <T extends Element, E extends Exception> List<T> ancestors(UniversalPredicate<T, E> predicate) throws E {
		return predicate.downCastedList(ancestors());
	}

	public final List<Element> ancestors() {
		if (parent()!=null) {
			List<Element> result = parent().ancestors();
			result.add(0, parent());
			return result;
		} else {
			return Lists.create();
		}
	}

	public <T extends Element> T nearestAncestorOrSelf(Class<T> c) {
		Element el = this;
		while ((el != null) && (! c.isInstance(el))){
			el = el.parent();
		}
		return (T) el;
	}

	@Override
	public <T extends Element, E extends Exception> T nearestAncestorOrSelf(UniversalPredicate<T, E> predicate) throws E {
		Element el = this;
		while ((el != null) && (! predicate.eval((T)el))) {
			el = el.parent();
		}
		return (T) el;
	}

	@Override
	public Element farthestAncestor() {
		Element parent = parent();
		if(parent == null) {
			return this;
		} else {
			return parent.farthestAncestor();
		}
	}

	public <T extends Element> T farthestAncestor(Class<T> c) {
		Element el = parent();
		T anc = null;
		while(el != null) {
			while ((el != null) && (! c.isInstance(el))){
				el = el.parent();
			}
			if(el != null) {
				anc = (T) el;
				el = el.parent();
			}
		}
		return anc;
	}
	
	public <T extends Element, E extends Exception> T farthestAncestor(UniversalPredicate<T,E> p) throws E {
		Element el = parent();
		T anc = null;
		while(el != null) {
			while ((el != null) && (!p.eval((T)el))) {
				el = el.parent();
			}
			if(el != null) {
				anc = (T) el;
				el = el.parent();
			}
		}
		return anc;
	}

	public <T extends Element> T farthestAncestorOrSelf(Class<T> c) {
		T result = farthestAncestor(c);
		if((result == null) && (c.isInstance(this))) {
			result = (T) this;
		}
		return result;
	}



	public boolean hasAncestor(Element ancestor) {
		Element el = parent();
		while ((el != null) && (el != ancestor)){
			el = el.parent();
		}
		return el == ancestor;
	}

	public <T extends Element> T nearestAncestor(Class<T> c) {
		Element el = parent();
		while ((el != null) && (! c.isInstance(el))){
			el = el.parent();
		}
		return (T) el;
	}

	@Override
	public <T extends Element, E extends Exception> T nearestAncestor(UniversalPredicate<T,E> predicate) throws E {
		Element el = parent();
		while ((el != null) && (! predicate.eval((T)el))) {
			el = el.parent();
		}
		return (T) el;
	}

//	public abstract Element clone();

	public Language language() {
		Language result = null;
		if(Config.cacheLanguage() == true) {
			result = _languageCache;
		}
		if(result == null) {
			Element parent = parent();
			if(parent != null) {
				result = parent.language();
				if(Config.cacheLanguage() == true) {
					_languageCache = result;
				}
			} 
		}
		return result;
	}

	private Language _languageCache;

	public <T extends Language> T language(Class<T> kind) {
		if(kind == null) {
			throw new ChameleonProgrammerException("The given language class is null.");
		}
		Language language = language();
		if(kind.isInstance(language) || language == null) {
			return (T) language;
		} else {
			throw new WrongLanguageException("The language of this model is of the wrong kind. Expected: "+kind.getName()+" but got: " +language.getClass().getName());
		}
	}

	/**
	 * @see Element#lookupContext(Element) 
	 */
	 public LookupContext lookupContext(Element child) throws LookupException {
		return lexicalContext();
	}

	/**
	 * @see Element#lexicalContext() 
	 */
	 public LookupContext lexicalContext() throws LookupException {
		try {
			return parent().lookupContext(this);
		} catch(NullPointerException exc) {
			if(parent() == null) {
				throw new LookupException("Requesting the lexical context of an element without a parent: " +getClass().getName());
			} else {
				throw exc;
			}
		}
	 }
	 
	 public PropertySet<Element,ChameleonProperty> properties() {
		 return new PropertySet<>(internalProperties());
	 }

	 protected PropertySet<Element,ChameleonProperty> internalProperties() {
		 if(_properties == null) {
			 _properties = explicitProperties();
			 _properties.addAll(defaultProperties(_properties));
		 }
		 return _properties;
	 }

	 private PropertySet<Element,ChameleonProperty> _properties;
	 
	/**
	 * Return the set of explicit properties of this element. 
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.containsAll(declaredProperties());
   @ post \result.containsAll(inherentProperties());
   @*/
	protected PropertySet<Element, ChameleonProperty> explicitProperties() {
		PropertySet<Element,ChameleonProperty> result = declaredProperties();
		 result.addAll(inherentProperties());
		return result;
	}

	 public PropertySet<Element,ChameleonProperty> defaultProperties() {
		 return defaultProperties(explicitProperties());
	 }

	 public PropertySet<Element,ChameleonProperty> defaultProperties(PropertySet<Element,ChameleonProperty> explicit) {
		 return language().defaultProperties(this,explicit);
	 }

	 public PropertySet<Element,ChameleonProperty> inherentProperties() {
		 return new PropertySet<Element,ChameleonProperty>();
	 }

	 public PropertySet<Element,ChameleonProperty> declaredProperties() {
		 return new PropertySet<Element,ChameleonProperty>();
	 }

	 public boolean isTrue(ChameleonProperty property) {
		 return is(property) == Ternary.TRUE;
	 }

	 public boolean isFalse(ChameleonProperty property) {
		 return is(property) == Ternary.FALSE;
	 }

	 public boolean isUnknown(ChameleonProperty property) {
		 return is(property) == Ternary.UNKNOWN;
	 }

	 public synchronized Ternary is(ChameleonProperty property) {
		 Ternary result = null;
		 if(Config.cacheElementProperties()) {
			 if(_propertyCache != null) {
				 result = _propertyCache.get(property);
			 }
		 }
		 if(result == null){
			 result = property.appliesTo(this);
			 if(result == Ternary.UNKNOWN) {
				 // Check if the properties() set implies the given property.
				 result = internalProperties().implies(property);
				 if(Config.cacheElementProperties()) {
					 if(_propertyCache == null) {
						 _propertyCache = new HashMap<ChameleonProperty,Ternary>();
					 }
					 _propertyCache.put(property, result);
				 }
			 } else {
				 if(Config.cacheElementProperties()) {
					 if(_propertyCache == null) {
						 _propertyCache = new HashMap<ChameleonProperty,Ternary>();
					 }
					 _propertyCache.put(property, result);
				 }
			 }
		 }
		 return result;
	 }

	 private HashMap<ChameleonProperty,Ternary> _propertyCache;

	 public ChameleonProperty property(final PropertyMutex<ChameleonProperty> mutex) throws ModelException {
		 return property(new AbstractPredicate<ChameleonProperty,ModelException>() {
			@Override
			public boolean eval(ChameleonProperty property) throws ModelException
				 {return mutex != null && mutex.equals(property.mutex());}
		 });
	 }
	 
//	 public ChameleonProperty implyingProperty(final ChameleonProperty implied) throws ModelException {
//		 return property(new UnsafePredicate<ChameleonProperty,ModelException>() {
//			@Override
//			public boolean eval(ChameleonProperty property) throws ModelException
//				 {return property.implies(implied);}
//		 });
//	 }
	 
//	 public ChameleonProperty property(SafePredicate<ChameleonProperty> predicate) throws ModelException {
//		 List<ChameleonProperty> properties = new ArrayList<ChameleonProperty>();
//		 for(ChameleonProperty p : properties().properties()) {
//			 if(predicate.eval(p)) {
//				 properties.add(p);
//			 }
//		 }
//		 if(properties.size() == 1) {
//			 return properties.get(0);
//		 } else {
//			 throw new ModelException("Element of type " +getClass().getName()+ " has "+properties.size()+" properties that satisfy the given condition.");
//		 }
//	 }

	 public <X extends Exception> ChameleonProperty property(Predicate<ChameleonProperty,X> predicate) throws ModelException, X {
//		 List<ChameleonProperty> properties = new ArrayList<ChameleonProperty>();
		 ChameleonProperty result = null;
		 for(ChameleonProperty p : internalProperties().properties()) {
			 if(predicate.eval(p)) {
				 if(result == null) {
				   result = p;
				 } else {
					 throw new ModelException("Element of type " +getClass().getName()+ " has more than one property that satisfy the given condition.");
				 }
			 }
		 }
		 if(result != null) {
			 return result;
		 } else {
			 throw new ModelException("Element of type " +getClass().getName()+ " has no properties that satisfy the given condition.");
		 }
	 }


	 public boolean hasProperty(PropertyMutex<ChameleonProperty> mutex) throws ModelException {
		 return internalProperties().hasPropertyFor(mutex);
	 }

	 /*@
      @ public behavior
      @
      @ post (\forall Property p; \result.contains(p); base.contains(p) && ! overriding.contradicts(p) ||
      @        overriding.contains(p) && (\exists Property p2; base.contains(p2); overriding.contradicts(p2))); 
      @*/
	 protected PropertySet<Element,ChameleonProperty> filterProperties(PropertySet<Element,ChameleonProperty> overriding, PropertySet<Element,ChameleonProperty> base) {
		 Set<ChameleonProperty> baseProperties = base.properties();
		 final Set<ChameleonProperty> overridingProperties = overriding.properties();
		 new SafePredicate<ChameleonProperty>() {
			 @Override
			 public boolean eval(final ChameleonProperty aliasedProperty) {
				 return new SafePredicate<ChameleonProperty>() {
					 @Override
					 public boolean eval(ChameleonProperty myProperty) {
						 return !aliasedProperty.contradicts(myProperty);
					 }
				 }.forAll(overridingProperties);
			 }

		 }.filter(baseProperties);
		 baseProperties.addAll(overridingProperties);
		 return new PropertySet<Element,ChameleonProperty>(baseProperties);
	 }

	 public void disconnectChildren() {
		 for(Element child:children()) {
			 if(child != null) {
				 child.disconnect();
			 } else {
				 throw new ChameleonProgrammerException("CHILD IS NULL for element of Type "+getClass().getName());
				 //    			showStackTrace("CHILD IS NULL for element of Type "+getClass().getName());    		
			 }
		 }
	 }

	 //    private static int stackTraceCount = 0;
	 //    
	 //    /**
	 //     * This debugging method throws an exception, catches it, and prints
	 //     * the stacktrace.
	 //     */
	 //    protected void showStackTrace(String message) {
	 //    	try {
	 //    		throw new Exception(++stackTraceCount + ":: "+message);
	 //    	} catch(Exception e) {
	 //    		e.printStackTrace();
	 //    	}
	 //    }
	 //
	 //    /**
	 //     * This debugging method throws an exception, catches it, and prints
	 //     * the stacktrace.
	 //     */
	 //    protected void showStackTrace() {
	 //    	showStackTrace(null);
	 //    }

	 public List<? extends Element> directDependencies() {
		 return children();
	 }

	 /**
	  * Notify this element that the given descendant was modified. This
	  * method first calls reactOnDescendantChange with the given element. After that,
	  * the event is propagated to the lexical parent, if the parent is not null.
	  */
	 public void notifyDescendantChanged(Element descendant) {
		 reactOnDescendantChange(descendant);
		 notifyParent(descendant);
	 }

	 private void notifyParent(Element descendant) {
		 Element parent = parent();
		 if(parent != null) {
			 parent.notifyDescendantChanged(descendant);
		 }
	 }

	 /**
	  * Actually react on a change of the given descendant.
	  * 
	  * By default, there is no reaction.
	  */
	 public void reactOnDescendantChange(Element descendant) {
	 }

	 public void reactOnDescendantAdded(Element descendant) {
	 }

	 public void reactOnDescendantRemoved(Element descendant) {
	 }

	 public void reactOnDescendantReplaced(Element oldElement, Element newElement) {
	 }

	 public final Verification verify() {
		 try {
			 Verification result = verifySelf();
			 if(result == null) {
				 throw new ChameleonProgrammerException("The verifySelf method of "+getClass().getName()+" returned null.");
			 }
			 result = result.and(verifyProperties());
			 result = result.and(verifyLoops());
			 for(Element element:children()) {
				 result = result.and(element.verify());
			 }
			 result = result.and(verifyAssociations());
			 result = result.and(language().verify(this));
			 return result;
		 } catch(Exception exc) {
			 exc.printStackTrace();
			 return new BasicProblem(this, "Internal error during verification.");
		 }
	 }
	 
	 protected Verification verifyAssociations() {
		 Verification result = Valid.create();
		 for(ChameleonAssociation<?> association: associations()) {
			 result = result.and(association.verify());
		 }
		 return result;
	 }

	 public final Verification verifyLoops() {
		 Verification result = Valid.create();
		 Element e = parent();
		 while(e != null) {
			 if(e == this) {
				 result = result.and(new BasicProblem(this, "There is a loop in the lexical structure. This element is an ancestor of itself."));
			 }
			 e = e.parent();
		 }
		 return result;
	 }

	 /**
	  * Perform a local verification. The check for a loop in the lexical structure is already implemented
	  * in verifyLoops(), which is used in verify(). The checks to verify that all properties of this element actually
	  * apply to it and that there are no conflicting properties are both implemented in verifyProperties(), which is also used in verify().
	  * @return
	  */
	 //    public abstract VerificationResult verifySelf();
	 public Verification verifySelf() {
		 return Valid.create();
	 }

	 public final Verification verifyProperties() {
		 Verification result = Valid.create();
		 PropertySet<Element,ChameleonProperty> properties = internalProperties();
		 Collection<Conflict<ChameleonProperty>> conflicts = properties.conflicts();
		 for(Conflict<ChameleonProperty> conflict: conflicts) {
			 result = result.and(new ConflictProblem(this,conflict));
		 }
		 for(ChameleonProperty property: properties.properties()) {
			 result = result.and(property.verify(this));
		 }
		 return result;
	 }

	 public static class LogicalNavigator extends TreeStructure<Element> {
		@Override
		public Element parent(Element element) {
			return ((ElementImpl)element).actualParent();
		}

		@Override
		public TreeStructure<Element> tree(Element element) {
			return element.logical();
		}

		@Override
		public List<? extends Element> children(Element element) {
			return element.children();
		}
	}

	public static class LexicalNavigator extends TreeStructure<Element> {
		@Override
		public Element parent(Element element) {
			return ((ElementImpl)element).actualParent();
		}

		@Override
		public TreeStructure<Element> tree(Element element) {
			return element.lexical();
		}

		@Override
		public List<? extends Element> children(Element element) {
			return element.children();
		}
	}

	public static class ConflictProblem extends BasicProblem {

		 private Conflict<ChameleonProperty> _conflict;

		 public ConflictProblem(Element element, Conflict<ChameleonProperty> conflict) {
			 super(element, "Property "+conflict.first().name()+" conflicts with property "+conflict.second().name());
			 _conflict = conflict;
		 }

		 public Conflict<ChameleonProperty> conflict() {
			 return _conflict;
		 }

	 }

	 protected Verification checkNull(Object element, String message, Verification result) {
		 if(element == null) {
			 result = result.and(new BasicProblem(this, message));
		 }
		 return result;
	 }

	 /**
	  * Set the given SingleAssociation object (which is typically connected to 'this') as the parent of the given element.
	  * @param <T>
	  * @param association
	  * @param element
	  */
	 /*@
     @ public behavior
     @
     @ pre association != null;
     @ pre (element != null) ==> (! element.isDerived());
     @
     @ post (element != null) ==> (element.parent() == association.getObject() && association.getOtherEnd() == element);
     @ post (element == null) ==> association.getOtherEnd() == null;
     @*/
	 protected <T extends Element> void set(SingleAssociation<? extends Element, ? super T> association, T element) {
		 if(element != null) {
			 association.connectTo((Association) element.parentLink());
		 } else {
			 association.connectTo(null);
		 }
	 }

	 /**
	  * Set the given AbstractMultiAssociation object (which is typically connected to 'this') as the parent of the given element.
	  * @param <T>
	  * @param association
	  * @param element
	  */
	 /*@
     @ public behavior
     @
     @ pre association != null;
     @ pre (element != null) ==> (! element.isDerived());
     @
     @ post (element != null) ==> (element.parent() == association.getObject() && association.contains(element.parentLink());
     @*/
	 protected <T extends Element> void add(AbstractMultiAssociation<? extends Element, ? super T> association, T element) {
		 if(element != null) {
			 association.add((Association)element.parentLink());
		 }
	 }

	 /**
	  * Set the given AbstractMultiAssociation object (which is typically connected to 'this') as the parent of the given element.
	  * @param <T>
	  * @param association
	  * @param element
	  */
	 /*@
     @ public behavior
     @
     @ pre association != null;
     @ pre (element != null) ==> (! element.isDerived());
     @
     @ post (element != null) ==> (element.parent() == association.getObject() && association.contains(element.parentLink());
     @*/
	 protected <T extends Element> void add(AbstractMultiAssociation<? extends Element, ? super T> association, Collection<T> elements) {
		 for(T t: elements) {
			 add(association,t);
		 }
	 }

	 /**
	  * Add the given element to the given association end.
	  * 
	  * @param association The association end to which the element must be added.
	  * @param element The element that must be added to the association end.
	  */
	 /*@
     @ public behavior
     @
     @ pre association != null;
     @ pre element != null;
     @
     @ post association.getOtherRelations().contains(element.parentLink());
     @*/
	 protected <E extends Element> void add(OrderedMultiAssociation<? extends Element,E> association, E element) {
		 if(element != null) {
			 association.add((Association)element.parentLink());
		 }
	 }

	 /**
	  * Remove the given element from the given association end.
	  * 
	  * @param association The association end to which the element must be removed.
	  * @param element The element that must be removed to the association end.
	  */
	 /*@
     @ public behavior
     @
     @ pre association != null;
     @ pre element != null;
     @
     @ post !association.getOtherRelations().contains(element.parentLink());
     @*/
	 protected <E extends Element> void remove(OrderedMultiAssociation<? extends Element,E> association, E element) {
		 if(element != null) {
			 association.remove((Association)element.parentLink());
		 }
	 }


	 public final boolean equals(Object other) {
		 try {
			 return (other instanceof Element) && sameAs((Element) other);
		 } catch (LookupException e) {
			 throw new ChameleonProgrammerException(e);
		 }
	 }

	 public final boolean sameAs(Element other) throws LookupException {
		 return other == this || (uniSameAs(other) || ((other != null) && (other.uniSameAs(this))));
	 }

	 /**
	  * By default, reference equality is used.
	  */
	 public boolean uniSameAs(Element other) throws LookupException {
		 return other == this;
	 }

	 /**
	  * Flush the cache. This method flushes the local cache using "flushLocalCache()" and then
	  * recurses into the children.
	  */
	 public void flushCache() {
		 flushLocalCache();
		 for(Element child:children()) {
			 if(child != null) {
				 child.flushCache();
			 } else {
				 throw new ChameleonProgrammerException("The children method of class "+getClass()+" returns a collection that contains a null reference");
			 }
		 }
	 }

	 /**
	  * Flush language cache and property cache.
	  */
	 public synchronized void flushLocalCache() {
		 _propertyCache = null;
		 _properties = null;
	 }
	 
	 public Namespace namespace() {
		 NamespaceDeclaration ancestor = nearestAncestor(NamespaceDeclaration.class);
		 if(ancestor != null) {
			 return ancestor.namespace();
		 } else {
			 return null;
		 }
	 }

//	 /**
//	  * Clone the descendants of this element and make the clones the descendants of
//	  * the given element (which will typically be a clone of this element). Type
//	  * E must be the class of the current element; otherwise e does not have the
//	  * same associations as the current object.
//	  * @param e
//	  * @return
//	  */
//	 protected <E extends Element> E cloneDescendantsTo(E e) {
//		 List<ChameleonAssociation<?>> mine = associations();
//		 List<ChameleonAssociation<?>> others = e.associations();
//		 int size = mine.size();
//		 for(int i = 0; i < size; i++) {
//			 mine.get(i).cloneTo((ChameleonAssociation) others.get(i));
//		 }
//		 return e;
//	 }
	 
	 //    public Iterator<Element> depthFirstIterator() {
	 //    	return new Iterator<Element>() {
	 //
	 //    		
	 //    		
	 //    		private Element _current;
	 //    		
	 //    		private Iterator<Element> _currentIterator;
	 //    		
	 //    		private Element _last;
	 //				public boolean hasNext() {
	 //					return _current != _last;
	 //				}
	 //
	 //				public Element next() {
	 //					compile error
	 //				}
	 //
	 //				public void remove() {
	 //					_current.parentLink().connectTo(null);
	 //				}
	 //			};
	 //    }

}
