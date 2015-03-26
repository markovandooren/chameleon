package org.aikodi.chameleon.core.element;

import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.exists;
import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.filter;
import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.forAll;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.language.WrongLanguageException;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.tag.Metadata;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.ChameleonAssociation;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.chameleon.workspace.WrongViewException;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.association.AbstractMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.AssociationListener;
import be.kuleuven.cs.distrinet.rejuse.association.IAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.debug.StackTrace;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;
import be.kuleuven.cs.distrinet.rejuse.property.Conflict;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * A class that implement most methods of {@link Element}.
 * 
 * @author Marko van Dooren
 */
public abstract class ElementImpl implements Element {

   /**
    * Filthy static hack to improve performance of reflective code.
    * 
    * FIXME: replace this with better code.
    */
	private static Map<Class<? extends Element>,Set<String>> _excludedFieldNames = new HashMap<Class<? extends Element>,Set<String>>();

	/**
	 * Construct a new object without children and without a parent.
	 */
	public ElementImpl() {
	}
	
	private AssociationListener<Element> _changePropagationListener;
	
	public boolean changePropagationEnabled() {
	  return _changePropagationListener != null;
	}
	
	public void disableChangePropagation() {
	  if(_changePropagationListener != null) {
      associations().forEach(a -> a.removeListener(_changePropagationListener));
	    _changePropagationListener = null;
	  }
	}
	
	public void enableChangePropagation() {
	  if(! changePropagationEnabled()) {
	    _changePropagationListener = new AssociationListener<Element>() {

	      public void notifyElementAdded(Element element) {
	        notifyChildAdded(element);
	        element.enableChangePropagation();
	      }

	      public void notifyElementRemoved(Element element) {
	        notifyChildRemoved(element);
	      }

	      public void notifyElementReplaced(Element oldElement, Element newElement) {
	        notifyChildReplaced(oldElement, newElement);
	        newElement.enableChangePropagation();
	      }
	    };
	    associations().forEach(a -> a.addListener(_changePropagationListener));
	    children().forEach(e -> e.enableChangePropagation());
	  }
	}
	
//	private final static TreeStructure<Element> _lexical = new LexicalNavigator();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Navigator lexical() {
//		return _lexical;
		return new LexicalNavigator();
	}
	
//	private final static Navigator _logical = new LogicalNavigator();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Navigator logical() {
		return new LogicalNavigator();
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

	
//	/**
//	 * A method that is mostly used for debugging purposes. 
//	 * Invoke {@link #enableParentListening()} to enable this functionality and
//	 * override the method for the appropriate element.
//	 */
//	protected void notifyParentSet(Element element) {}
//	
//   /**
//    * A method that is mostly used for debugging purposes. 
//    * Invoke {@link #enableParentListening()} to enable this functionality and
//    * override the method for the appropriate element.
//    */
//	protected void notifyParentRemoved(Element element) {}
//	
//   /**
//    * A method that is mostly used for debugging purposes. 
//    * Invoke {@link #enableParentListening()} to enable this functionality and
//    * override the method for the appropriate element.
//    */
//	protected void notifyParentReplaced(Element oldParent, Element newParent) {}
	
//	/**
//	 * Enable notification when the parent is changed. By default, this
//	 * is turned off to prevent a massive amount of events when loading
//	 * a document.
//	 */
//	protected void enableParentListening() {
//	 parentLink().addListener(new AssociationListener<Element>() {
//		@Override
//		public void notifyElementAdded(Element element) {
//			ElementImpl.this.notifyParentSet(element);
//		}
//
//		@Override
//		public void notifyElementRemoved(Element element) {
//			ElementImpl.this.notifyParentRemoved(element);
//		}
//
//		@Override
//		public void notifyElementReplaced(Element oldElement, Element newElement) {
//			ElementImpl.this.notifyParentReplaced(oldElement, newElement);
//		}
//	  });
//	}

	/********
	 * TAGS *
	 ********/

	// Initialization of this Map is done lazily to reduce memory usage
	private Map<String, Metadata> _tags;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
   public Metadata metadata(String name) {
		if(_tags != null) {
			return _tags.get(name);
		} else {
			//lazy init has not been performed yet.
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
   public void removeMetadata(String name) {
		if(_tags != null) {
			Metadata old = _tags.get(name);
			_tags.remove(name);
			if((old != null) && (old.getElement() == this)){
				old.setElement(null,name);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
   public void removeAllMetadata() {
		if(_tags != null) {
			// We must clone the key set, or otherwise the removal of the keys
			// may cause undefined behavior in the set.
			List<String> tagNames = Lists.create(_tags.keySet());
			for(String tagName: tagNames) {
				removeMetadata(tagName);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void freeze() {
		for(Element element: children()) {
			element.parentLink().lock();
			element.freeze();
		}
		for(IAssociation association: associations()) {
			association.lock();
		}
	}

   /**
    * {@inheritDoc}
    */
	@Override
   public void unfreeze() {
		for(Element element: children()) {
			element.parentLink().unlock();
			element.unfreeze();
		}
		for(IAssociation association: associations()) {
			association.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 */
	@Override
   public Collection<Metadata> metadata() {
		if(_tags == null) {
			return Collections.EMPTY_LIST;
		} else {
			return _tags.values();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
   public boolean hasMetadata(String name) {
		if(_tags == null) {
			return false;
		} else {
			return _tags.get(name) != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
   public boolean hasMetadata() {
		if(_tags == null) {
			return false;
		} else {
			return _tags.size() > 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * The default implementation uses {@link #cloneSelf()} to
	 * create a deep clone of this element, and then iterates over all
	 * associations using reflection and deep clones the children
	 * using {@link #clone()}.
	 * 
	 * @return A deep clone of this element.
	 */
	@Override
   public Element clone() {
		return clone(null, Element.class);
	}
	
   public final <E extends Element> Element clone(final BiConsumer<E, E> consumer, Class<E> type) {
		Element result = cloneSelf();
		if(canHaveChildren()) {
			List<ChameleonAssociation<?>> mine = myAssociations();
			int size = mine.size();
			List<ChameleonAssociation<?>> others = result.associations();
			for(int i = 0; i<size;i++) {
				ChameleonAssociation<? extends Element> m = mine.get(i);
				final ChameleonAssociation<? extends Element> o = others.get(i);
            m.mapTo(o, e -> e.clone());
			}
		}
		if(consumer != null && type.isInstance(this)) {
		   consumer.accept((E)this, (E)result);
		}
		return result;
	}
	
	/**
	 * Create a shallow clone of the current element.
	 * @return
	 */
  /*@
    @ public behavior
    @
    @ post \result != null;
    @ post \result.parent() == null;
    @ post \result.children().isEmpty();
    @*/
	protected abstract Element cloneSelf();
	
	@Override
	public View view() {
	  View result = _viewCache;
	  if(result == null) {
	    Element parent = parent();
	    if(parent != null) {
	      result = parent.view();
	      if(Config.cacheLanguage()) {
	        _viewCache = result;
	      }
	    }
	  }
	  return result;
	}
	
	private View _viewCache;
	
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

	// WORKING AROUND LACK OF SUBOBJECTS IN JAVA

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
	 * DO NOT USE THIS TO OBTAIN THE PARENT! This method is public only because of
	 * limitation in the Java language.
	 * 
	 * @throws ChameleonProgrammerException
	 *    The method is invoked on a derived element. 
	 */
	@Override
   public final SingleAssociation<Element,Element> parentLink() {
		if(_parentLink != null) {
			return _parentLink;
		} else {
			throw new ChameleonProgrammerException("Invoking getParentLink() on automatic derivation");
		}
	}

	/**
	 * A factory method to create association objects. This method
	 * is typically only overridden for debugging purposes to allow
	 * the addition of breakpoints, or to store a {@link StackTrace} object
	 * to find out when the parent has changed.
	 */
  /*@
    @ public behavior
    @
    @ post \result != null;
    @ post \result.getObject() == this;
    @ post \result.getOtherEnds().isEmpty();
    @*/
	protected Single<Element> createParentLink() {
		return new Single<Element>(this);
	}

	/**
	 * Return the parent of this element
	 */
	final Element actualParent() {
		if(_parentLink != null) {
			return _parentLink.getOtherEnd();
		} else {
			return _parent;
		}
	}
	
	@Override
   public Element parent() {
		return lexical().parent();
	}

	/**
	 * Check if this element is derived or not.
	 * 
	 * @return True if this element is derived, false otherwise.
	 */
	@Override
   public boolean isDerived() {
		return _parent != null;
	}

	/**
	 * The default behavior is to return 'this' because most elements
	 * are not derived.
	 */
	@Override
   public Element origin() {
		return _origin;
	}

	@Override
   public Element farthestOrigin() {
		Element current = this;
		Element origin = origin();
		while(current != origin) {
			current = origin;
			origin = current.origin();
		}
		return origin;
	}

	@Override
   public void setOrigin(Element origin) {
		_origin = origin;
	}

	private Element _origin = this;

	@Override
   public void disconnect() {
		nonRecursiveDisconnect();
		disconnectChildren();
	}

	@Override
   public boolean disconnected() {
		return parent() == null;
	}

	@Override
   public void nonRecursiveDisconnect() {
		if(_parentLink != null) {
			_parentLink.connectTo(null);
		} else {
			_parent = null;
		}
		removeAllMetadata();
	}

	/**
	 * DO NOT OVERRIDE THIS METHOD UNLESS YOU *REALLY* KNOW WHAT YOU ARE DOING! 
	 * We do not see any use other than diagnostic purposes.
	 */
	@Override
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

	@Override
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

	@Override
   public List<ChameleonAssociation<?>> associations() {
		return myAssociations();
	}

	private List<ChameleonAssociation<?>> myAssociations() {
		if(_associations == null) {
			synchronized (this) {
				if(_associations == null) {
					List<Field> fields = getAllFieldsTillClass(getClass());
					int size = fields.size();
					if(size > 0) {
						List<ChameleonAssociation<?>> tmp = Lists.create(size);
						for (Field field : fields) {
							Object content = getFieldValue(field);
							tmp.add((ChameleonAssociation<?>) content);
						}
						_associations = Collections.unmodifiableList(tmp);
					}
					else {
						_associations = Collections.EMPTY_LIST;
					}
				}
			}
		}
		return _associations;
	}
	
	private boolean canHaveChildren() {
		return ! getAllFieldsTillClass(getClass()).isEmpty();
	}
	


	private static void addAllFieldsTillClass(final Class<? extends Element> currentClass, Collection<Field> accumulator){
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
  @Override
public List<? extends Element> children() {
  	List<Element> reflchildren = Lists.create();
		for (ChameleonAssociation association : associations()) {
			association.addOtherEndsTo(reflchildren);
		}
		return reflchildren;
  }

  @SuppressWarnings("rawtypes")
	@Override
   public final <T extends Element> List<T> children(Class<T> c) {
    List result = children();
		filter(result, child -> c.isInstance(child));
    return result;
	}

	@Override
   public final <E extends Exception> List<Element> children(Predicate<? super Element,E> predicate) throws E {
		List<? extends Element> tmp = children();
		filter(tmp,predicate);
		return (List<Element>)tmp;
	}

	@Override
   public final <T extends Element> List<T> descendants(Class<T> c) {
		List<T> result = children(c);
		for (Element e : children()) {
			result.addAll(e.descendants(c));
		}
		return result;
	}

	@Override
   public final <T extends Element> boolean hasDescendant(Class<T> c) {
		List<Element> tmp = (List<Element>) children();
		filter(tmp, child -> c.isInstance(child));
		if (!tmp.isEmpty()) {
			return true;
		}
		return exists(children(), child -> child.hasDescendant(c));
	}

	@Override
	public final <T extends Element, E extends Exception> boolean hasDescendant(UniversalPredicate<T,E> predicate) throws E {
    return (!children(predicate).isEmpty()) || 
           exists(children(), child -> child.hasDescendant(predicate));
	}
	
	@Override
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

  @Override
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

	
	@Override
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

	@Override
   public final <T extends Element> List<T> children(Class<T> c, final ChameleonProperty property) {
		List<T> result = children(new UniversalPredicate<T,Nothing>(c) {
			@Override
         public boolean uncheckedEval(T element) {
				return element.isTrue(property);
			}
		});
		return result;
	}

	@Override
   public final <T extends Element> List<T> descendants(Class<T> c, ChameleonProperty property) {
		List<T> result = children(c, property);
		for (Element e : children()) {
			result.addAll(e.descendants(c, property));
		}
		return result;
	}

	@Override
   public final <T extends Element,E extends Exception> List<T> descendants(Class<T> c, Predicate<T,E> predicate) throws E {
		List<T> result = children(c);
		predicate.filter(result);
		for (Element e : children()) {
			result.addAll(e.descendants(c, predicate));
		}
		return result;
	}

	@Override
   public final <T extends Element, E extends Exception>  void apply(Action<T,E> action) throws E {
		if(action.type().isInstance(this)) {
			action.perform(this);
		}
		for (Element e : children()) {
			e.apply(action);
		}
	}
	
	public final <T extends Element, E extends Exception>  void apply(Consumer<T> action, Class<T> kind) throws E {
	  if(kind.isInstance(this)) {
	     action.accept((T)this);
	  }
     for (Element e : children()) {
        e.apply(action,kind);
     }
	}

	@Override
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

	@Override
   public final List<Element> ancestors() {
		if (parent()!=null) {
			List<Element> result = parent().ancestors();
			result.add(0, parent());
			return result;
		} else {
			return Lists.create();
		}
	}

	@Override
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
		while ((el != null) && (! predicate.eval(el))) {
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

	@Override
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
	
	@Override
   public <T extends Element, E extends Exception> T farthestAncestor(UniversalPredicate<T,E> p) throws E {
		Element el = parent();
		T anc = null;
		while(el != null) {
			while ((el != null) && (!p.eval(el))) {
				el = el.parent();
			}
			if(el != null) {
				anc = (T) el;
				el = el.parent();
			}
		}
		return anc;
	}

	@Override
   public <T extends Element> T farthestAncestorOrSelf(Class<T> c) {
		T result = farthestAncestor(c);
		if((result == null) && (c.isInstance(this))) {
			result = (T) this;
		}
		return result;
	}



	@Override
   public boolean hasAncestor(Element ancestor) {
		Element el = parent();
		while ((el != null) && (el != ancestor)){
			el = el.parent();
		}
		return el == ancestor;
	}

	@Override
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
		while ((el != null) && (! predicate.eval(el))) {
			el = el.parent();
		}
		return (T) el;
	}

//	public abstract Element clone();

	@Override
   public Language language() {
//		Language result = _languageCache;
//		if(result == null) {
//			Element parent = parent();
//			if(parent != null) {
//				result = parent.language();
//				if(Config.cacheLanguage() == true) {
//					_languageCache = result;
//				}
//			} 
//		}
//		return result;
	  return view().language();
	}

//	private Language _languageCache;

	@Override
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
	 * {@inheritDoc}
	 */
	@Override
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
	 
	 /**
	 * {@inheritDoc}
	 */
	@Override
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
	 * @return A property set for the explicitly declared properties
	 *         of this element.
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

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public PropertySet<Element,ChameleonProperty> defaultProperties() {
		 return defaultProperties(explicitProperties());
	 }

	 /**
	 * @param explicit The set of explicitly declared properties
	 * @return The default properties of this element, taking into account
	 *         the explicitly declared properties.
	 */
	protected PropertySet<Element,ChameleonProperty> defaultProperties(PropertySet<Element,ChameleonProperty> explicit) {
		 return language().defaultProperties(this,explicit);
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public PropertySet<Element,ChameleonProperty> inherentProperties() {
		 return new PropertySet<Element,ChameleonProperty>();
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public PropertySet<Element,ChameleonProperty> declaredProperties() {
		 return new PropertySet<Element,ChameleonProperty>();
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public boolean isTrue(ChameleonProperty property) {
		 return is(property) == Ternary.TRUE;
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public boolean isFalse(ChameleonProperty property) {
		 return is(property) == Ternary.FALSE;
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public boolean isUnknown(ChameleonProperty property) {
		 return is(property) == Ternary.UNKNOWN;
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
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

	 /**
	 * {@inheritDoc}
	 */
	@Override
   public ChameleonProperty property(final PropertyMutex<ChameleonProperty> mutex) throws ModelException {
		 return property(property -> mutex != null && mutex.equals(property.mutex()));
	 }
	 
   /**
    * Return the property that satisfies the given predicate.
    * 
    * @param predicate The predicate that determines the requested property.
    * @return The property that satisfies the given predicate, if any.
    * @throws IllegalArgumentException This element has more that one property that
    *         satisfies the predicate.
    * @throws IllegalArgumentException This element has no property that satisfies
    *         the given predicate.
    * @throws X The predicate has thrown exception X
    */
   protected <X extends Exception> ChameleonProperty property(Predicate<ChameleonProperty, X> predicate)
         throws X {
      ChameleonProperty result = null;
      for (ChameleonProperty p : internalProperties().properties()) {
         if (predicate.eval(p)) {
            if (result == null) {
               result = p;
            } else {
               throw new IllegalArgumentException("Element of type " + getClass().getName()
                     + " has more than one property that satisfy the given condition.");
            }
         }
      }
      if (result != null) {
         return result;
      } else {
         throw new IllegalArgumentException("Element of type " + getClass().getName()
               + " has no properties that satisfy the given condition.");
      }
   }

	 /**
	 * {@inheritDoc}
	 */
	@Override
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
		 filter(baseProperties, 
		     baseProperty -> forAll(overridingProperties, 
		         overridingProperty -> !baseProperty.contradicts(overridingProperty)));
		 baseProperties.addAll(overridingProperties);
		 return new PropertySet<Element,ChameleonProperty>(baseProperties);
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
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

	 /**
	  * {@inheritDoc}
	  */
//	 @Override
//   public void notifyDescendantChanged(Element descendant) {
//		 reactOnDescendantChange(descendant);
//		 notifyParent(descendant);
//	 }

//	 private void notifyParent(Element descendant) {
//		 Element parent = parent();
//		 if(parent != null) {
//			 parent.notifyDescendantChanged(descendant);
//		 }
//	 }

	public void notifyChanged() {
	}
	
	public void notifyDescendantChanged(Element descendant) {
	  if(changePropagationEnabled()) {
      Element parent = parent();
      if(parent != null) {
        parent.notifyDescendantChanged(descendant);
      }
	  }
	}
	  public void notifyChildAdded(Element descendant) {
	    if(changePropagationEnabled()) {
	      Element parent = parent();
	      if(parent != null) {
	        parent.notifyDescendantAdded(descendant);
	      }
	    }
	  }
	  
    public void notifyDescendantAdded(Element descendant) {
    }
    
    public void notifyChildRemoved(Element oldChild) {
      if(changePropagationEnabled()) {
        Element parent = parent();
        if(parent != null) {
          parent.notifyDescendantRemoved(oldChild);
        }
      }
    }

   public void notifyDescendantRemoved(Element oldDescendant) {
   }
   
   public void notifyChildReplaced(Element oldChild, Element newChild) {
     if(changePropagationEnabled()) {
       Element parent = parent();
       if(parent != null) {
         parent.notifyDescendantReplaced(oldChild, newChild);
       }
     }
   }
   
   public void notifyDescendantReplaced(Element oldChild, Element newChild) {
   }
   

	 /**
	  * {@inheritDoc}
	  * 
	  * By default, there is no reaction.
	  */
	 @Override
   public void reactOnDescendantChange(Element descendant) {
	 }

    /**
     * {@inheritDoc}
     * 
     * By default, there is no reaction.
     */
	 @Override
   public void reactOnDescendantAdded(Element descendant) {
	 }

    /**
     * {@inheritDoc}
     * 
     * By default, there is no reaction.
     */
	 @Override
   public void reactOnDescendantRemoved(Element descendant) {
	 }

    /**
     * {@inheritDoc}
     * 
     * By default, there is no reaction.
     */
	 @Override
   public void reactOnDescendantReplaced(Element oldElement, Element newElement) {
	 }

	 /**
	 * {@inheritDoc}
	 */
	@Override
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

	 protected Verification verifyLoops() {
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
	 protected Verification verifySelf() {
		 return Valid.create();
	 }

	 protected Verification verifyProperties() {
		 Verification result = Valid.create();
		 PropertySet<Element,ChameleonProperty> properties = internalProperties();
		 Collection<Conflict<ChameleonProperty>> conflicts = properties.conflicts();
		 for(Conflict<ChameleonProperty> conflict: conflicts) {
			 result = result.and(new ConflictingProperties(this,conflict));
		 }
		 for(ChameleonProperty property: properties.properties()) {
			 result = result.and(property.verify(this));
		 }
		 return result;
	 }

	 protected abstract class CommonNavigator extends Navigator {
     @Override
     public Element node() {
       return ElementImpl.this;
     }
	 }
	 
  /**
   * A tree structure for the main logical structure of the model.
   * 
   * @author Marko van Dooren
   */
  public class LogicalNavigator extends CommonNavigator {

    /**
     * {@inheritDoc}
     * 
     * @return The {@link Element#logical()} tree structure of the element.
     */
    @Override
    public Navigator tree(Element element) {
      return element.logical();
    }

  }

  /**
   * A tree structure for the lexical structure of the model.
   * 
   * @author Marko van Dooren
   */
  public class LexicalNavigator extends CommonNavigator {

    /**
     * {@inheritDoc}
     * 
     * @return The {@link Element#logical()} tree structure of the element.
     */
    @Override
    public Navigator tree(Element element) {
      return element.lexical();
    }
  }

	/**
	 * A class of problems caused by conflicting properties.
	 * 
	 * @author Marko van Dooren
	 */
	public static class ConflictingProperties extends BasicProblem {

		 private Conflict<ChameleonProperty> _conflict;

		 public ConflictingProperties(Element element, Conflict<ChameleonProperty> conflict) {
			 super(element, "Property "+conflict.first().name()+" conflicts with property "+conflict.second().name());
			 _conflict = conflict;
		 }

		 /**
		  * 
		  * @return The conflict that caused this problem.
		  */
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


   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object other) {
      try {
         return (other instanceof Element) && sameAs((Element) other);
      } catch (LookupException e) {
         throw new ChameleonProgrammerException(e);
      }
   }
   
   /**
    * {@inheritDoc}
    * 
    * YOU MUST OVERRIDE THIS METHOD WHEN YOU OVERRIDE {@link #uniSameAs(Element)}.
    */
   @Override
   public int hashCode() {
      return super.hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean sameAs(Element other) throws LookupException {
      return other == this || (uniSameAs(other) || ((other != null) && (other.uniSameAs(this))));
   }

	 /**
	  * {@inheritDoc}
	  * 
	  * By default, reference equality is used.
	  */
	 @Override
   public boolean uniSameAs(Element other) throws LookupException {
		 return other == this;
	 }

   /**
    * {@inheritDoc}
    * 
    * Flush the cache. This method flushes the local cache using
    * "flushLocalCache()" and then recurses into the children.
    */
   @Override
   public void flushCache() {
      flushLocalCache();
      for (Element child : children()) {
         if (child != null) {
            child.flushCache();
         } else {
            throw new ChameleonProgrammerException("The children method of class " + getClass()
                  + " returns a collection that contains a null reference");
         }
      }
   }

   /**
    * Flush language cache and property cache.
    */
   protected synchronized void flushLocalCache() {
//     _languageCache = null;
     _viewCache = null;
     _propertyCache = null;
     _properties = null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Namespace namespace() {
      NamespaceDeclaration ancestor = nearestAncestor(NamespaceDeclaration.class);
      if (ancestor != null) {
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

   
   public EventManager when() {
     if(_eventManager == null) {
       _eventManager = new EventManager(this);
     }
     return _eventManager;
   }

   private EventManager _eventManager;
}
