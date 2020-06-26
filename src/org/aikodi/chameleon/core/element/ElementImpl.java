package org.aikodi.chameleon.core.element;

import static org.aikodi.rejuse.collection.CollectionOperations.filter;
import static org.aikodi.rejuse.collection.CollectionOperations.forAll;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import java.lang.reflect.Field;

import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.association.ChildAdded;
import org.aikodi.chameleon.core.event.association.ChildRemoved;
import org.aikodi.chameleon.core.event.association.ChildReplaced;
import org.aikodi.chameleon.core.event.association.ParentAdded;
import org.aikodi.chameleon.core.event.association.ParentRemoved;
import org.aikodi.chameleon.core.event.association.ParentReplaced;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.language.WrongLanguageException;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
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
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.association.Association;
import org.aikodi.rejuse.association.AssociationListener;
import org.aikodi.rejuse.association.OrderedMultiAssociation;
import org.aikodi.rejuse.association.SingleAssociation;
import org.aikodi.rejuse.data.tree.TreeStructure;
import org.aikodi.rejuse.debug.StackTrace;
import org.aikodi.rejuse.logic.ternary.Ternary;
import org.aikodi.rejuse.predicate.Predicate;
import org.aikodi.rejuse.property.Conflict;
import org.aikodi.rejuse.property.PropertyMutex;
import org.aikodi.rejuse.property.PropertySet;

/**
 * A class that implement most methods of {@link Element}.
 * 
 * @author Marko van Dooren
 */
public abstract class ElementImpl implements Element {

   /**
    * Static hack to improve performance of reflective code.
    */
	private static Map<Class<? extends Element>,Set<String>> _excludedFieldNames = new HashMap<Class<? extends Element>,Set<String>>();

  private PropertySet<Element,ChameleonProperty> _properties;
	private AssociationListener<Element> _childChangePropagationListener;
  private AssociationListener<Element> _parentChangePropagationListener;
	
	/**
	 * Construct a new object without children and without a parent.
	 */
	public ElementImpl() {
	}
	
	protected boolean changeNotificationEnabled() {
	  return _childChangePropagationListener != null;
	}
	
	protected void disableChangeNotification() {
	  if(_childChangePropagationListener != null) {
      associations().forEach(a -> a.removeListener(_childChangePropagationListener));
	    _childChangePropagationListener = null;
	    parentLink().removeListener(_parentChangePropagationListener);
	    _parentChangePropagationListener = null;
	  }
	}
	
	protected void enableChangeNotification() {
	  if(! changeNotificationEnabled()) {
	    _childChangePropagationListener = new AssociationListener<Element>() {

	      public void notifyElementAdded(Element element) {
	        ElementImpl.this.notify(new Event<ChildAdded,Element>(new ChildAdded(element),ElementImpl.this));
	      }

	      public void notifyElementRemoved(Element element) {
          ElementImpl.this.notify(new Event<ChildRemoved,Element>(new ChildRemoved(element),ElementImpl.this));
	      }

	      public void notifyElementReplaced(Element oldElement, Element newElement) {
	        ElementImpl.this.notify(new Event<ChildReplaced,Element>(new ChildReplaced(oldElement, newElement),ElementImpl.this));
	      }
	    };
	    associations().forEach(a -> a.addListener(_childChangePropagationListener));
	    _parentChangePropagationListener = new AssociationListener<Element>() {

        @Override
        public void notifyElementAdded(Element element) {
          ElementImpl.this.notify(new Event<ParentAdded,Element>(new ParentAdded(element),ElementImpl.this));
        }

        @Override
        public void notifyElementRemoved(Element element) {
          ElementImpl.this.notify(new Event<ParentRemoved,Element>(new ParentRemoved(element),ElementImpl.this));
        }
        
        public void notifyElementReplaced(Element oldElement, Element newElement) {
          ElementImpl.this.notify(new Event<ParentReplaced,Element>(new ParentReplaced(oldElement, newElement),ElementImpl.this));
        }
      };
      parentLink().addListener(_parentChangePropagationListener);
	  }
	}
	
	protected void notify(Event<? extends Change,? extends Element> event) {
	  if(_eventManager != null) {
	    _eventManager.notify(event);
	  }
	}
	
  protected void notify(Change change) {
    notify(createEvent(change));
  }
  
  
  /**
   * <p>Return the direct children of this element.</p>
   * 
   * <p>The result will never be null. All elements in the collection will have this element as
   * their parent.</p>
   * 
   * <p>Note that there can exist non-child elements that have this element as their parent. 
   * The reason is that e.g. not all generic instances of a class can be constructed, so the collection
   * can never be complete anyway. Context elements are also not counted as children, there are merely a
   * help for the lookup algorithms. We only keep references to the lexical children, those that are 'physically'
   * part of the program.</p>
   *
	 * <p>DO NOT OVERRIDE UNLESS YOU REALLY KNOW WHAT YOU ARE DOING!</p>
	 * 
	 * <p>This method uses the reflection mechanism, which avoids the need for a
	 * children() implementation in each class that would only compute the union of all the 
	 * {@link Association} objects referenced by this element. If an association element
	 * should <b>not</b> be included in the list of children, use the following code in the 
	 * class (class name is "X", field name is "_f").</p>
	 * 
	 * <pre>
	 *   static {
   *     excludeFieldName(X.class,"_f");
   *   }
	 * </pre>
	 * 
	 * <p>This method currently is overridden only to provide support for lazy loading in
	 * LazyNamespace.</p>
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post (\forall Element e; \result.contains(e); e.parent() == this);
   @*/
  protected List<Element> children() {
  	List<Element> reflchildren = Lists.create();
		for (ChameleonAssociation<?> association : associations()) {
			association.addOtherEndsTo(reflchildren);
		}
		return reflchildren;
  }


  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Navigator<Nothing> lexical() {
		return new LexicalNavigator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Navigator<LookupException> logical() {
		return new LogicalNavigator();
	}
	
	/********
	 * TAGS *
	 ********/

	/**
	 *  Initialization of this Map is done lazily to reduce memory usage
	 */
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
			if((old != null) && (old.element() == this)){
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
   public void setMetadata(Metadata decorator, String name) {
		//Lazy init of hashmap
		if (_tags==null) {
			_tags = new HashMap<String, Metadata>();
		}
		Metadata old = _tags.get(name); 
		if(old != decorator) {
			if((decorator != null) && (decorator.element() != this)) {
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
			return Collections.emptyList();
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
   * Check whether or not this element has metadata.
   */
 /*@
   @ public behavior
   @
   @ post \result == ! metadata().isEmpty();
   @*/
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
  public final <E extends Element> Element clone(final BiConsumer<E, E> consumer, Class<E> type) {
    Element result = cloneSelf();
    if (canHaveChildren()) {
      List<ChameleonAssociation<?>> mine = myAssociations();
      int size = mine.size();
      List<ChameleonAssociation<?>> others = result.associations();
      for (int i = 0; i < size; i++) {
        ChameleonAssociation<? extends Element> m = mine.get(i);
        final ChameleonAssociation<? extends Element> o = others.get(i);
        m.mapTo(o, e -> e.clone(consumer,type));
      }
    }
    if (consumer != null && type.isInstance(this)) {
      consumer.accept(type.cast(this), type.cast(result));
    }
    return result;
  }

	@Override
  public Element clone() {
		return clone(null, Element.class);
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
        _viewCache = result;
	    }
	  }
	  return result;
	}
	
	private View _viewCache;
	
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
   public void setOrigin(Element origin) {
		_origin = origin;
	}

	private Element _origin = this;

	@Override
   public void disconnect() {
		nonRecursiveDisconnect();
		disconnectChildren();
		if(_eventManager != null) {
		  _eventManager.disconnect();
		}
	}

  /**
   * Completely disconnect this element and all children from the parent.
   * This method also removes associations with any logical parents.
   * 
   * If an element has additional logical parents, this method must be overridden 
   * to remove its references to its logical parents.
   */
 /*@
   @ public behavior
   @
   @ post disconnected();
   @ post (\forall Element e; \old(this.contains(e)); e.disconnected());
   @*/
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

	private static Map<Class<?>, List<Field>> _fieldMap = new HashMap<Class<?>, List<Field>>();

	private static List<Field> getAllFieldsTillClass(Class<?> currentClass){
		List<Field> result = _fieldMap.get(currentClass);
		if(result == null) {
			result = Lists.create();
			addAllFieldsTillClass(currentClass, result);
			_fieldMap.put(currentClass, result);
		}
		return result;
	}


	private volatile List<ChameleonAssociation<?>> _associations;

	@Override
   public List<ChameleonAssociation<?>> associations() {
		return myAssociations();
	}
	
  private final AtomicBoolean _associationLock = new AtomicBoolean();

  private List<ChameleonAssociation<?>> myAssociations() {
  	List<ChameleonAssociation<?>> result = _associations;
  	if(result == null) {
  		if(_associationLock.compareAndSet(false, true)) {
  			try {
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
  					_associations = Collections.emptyList();
  				}
  				result = _associations;
  			} finally {
  				_associationLock.compareAndSet(true, false);
  			}
  		} else {
  			//spin lock
  			while(result == null) {
  				result = _associations;
  			}
  		}
  	}
  	return result;
	}
	
	private boolean canHaveChildren() {
		return ! getAllFieldsTillClass(getClass()).isEmpty();
	}
	
	private static void addAllFieldsTillClass(final Class<?> currentClass, Collection<Field> accumulator){
		Field[] fields = currentClass.getDeclaredFields();
		for(Field field: fields) {
			Set<String> set = _excludedFieldNames.get(currentClass);
			boolean excluded = (set != null) && set.contains(field.getName());
			if(! excluded && ChameleonAssociation.class.isAssignableFrom(field.getType())) {
				accumulator.add(field);
			}
		}
		Class<?> superClass = currentClass.getSuperclass();
		if (superClass != null) {
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

	/**
	 * Return the internal properties of this element. 
	 * 
	 * @return The union of the explicit and default properties of this element.
	 */
	protected PropertySet<Element,ChameleonProperty> internalProperties() {
	  if(_properties == null) {
			 _properties = explicitProperties();
			 _properties.addAll(defaultProperties(_properties));
		 }
		 return _properties;
	 }

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
   * Return the default properties of this element. A default property
   * is a property that an element has when no inherent or explicitly
   * defined property contradicts that property.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
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
   * Return a the properties that are inherent to this element.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public PropertySet<Element,ChameleonProperty> inherentProperties() {
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
		if (_propertyCache != null) {
			result = _propertyCache.get(property);
		}
		if (result == null) {
			result = property.appliesTo(this);
			if (result == Ternary.UNKNOWN) {
				// Check if the properties() set implies the given property.
				result = internalProperties().implies(property);
			} 
			if (_propertyCache == null) {
				_propertyCache = new HashMap<ChameleonProperty, Ternary>();
			}
			_propertyCache.put(property, result);
		}
		return result;
	}

	 private HashMap<ChameleonProperty,Ternary> _propertyCache;

	/**
	 * Return the property of this element for the given property mutex. The property mutex
	 * can be seen as the family of properties that a property belongs to. An example of a
	 * property mutex is accessibility.
	 * 
	 * @param mutex
	 * @throws LookupException 
   */
 /*@
	 @ public behavior
	 @
   @ pre mutex != null;
   @
   @ post properties().contains(\result);
   @ post \result.mutex() == mutex;
   @ post (\num_of Property p; properties().contains(p);
   @       p.mutex() == mutex) == 1;
   @
   @ signals ModelException (\num_of Property p; properties().contains(p);
   @       p.mutex() == mutex) != 1; 
   @*/
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
	  * Completely disconnect all children from this element.
	  */
	/*@
	  @ public behavior
	  @
	  @ post (\forall Element e; \old(this.contains(e)); e.disconnected());
	  @*/
   protected void disconnectChildren() {
		 for(Element child: lexical().children()) {
			 if(child != null) {
				 child.disconnect();
			 } else {
				 throw new ChameleonProgrammerException("CHILD IS NULL for element of Type "+getClass().getName());
				 //    			showStackTrace("CHILD IS NULL for element of Type "+getClass().getName());    		
			 }
		 }
	 }

	public void notifyChildAdded(Element descendant) {
	}

	public void notifyChildRemoved(Element oldChild) {
	}

	public void notifyChildReplaced(Element oldChild, Element newChild) {
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
			 for(Element element: lexical().children()) {
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
	
  /**
   * <p>Perform a local verification.</p>
   * 
   * <p>The check for a loop in the lexical structure is already implemented in
   * {@link #verifySelf()}, which is called in {@link #verify()}. 
   * The checks to verify that all properties of this element actually apply to it 
   * and that there are no conflicting properties are both implemented in 
   * verifyProperties(), which is also called in verify().
   * 
   * @return A verification object that indicates whether or not this is valid,
   *         and if not, what the problems are. The result does <b>not</b>
   *         include the problems found in the descendants. It only performs a
   *         local check.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  protected Verification verifySelf() {
    return Valid.create();
  }

  /**
   * <p>Verify that there are no loops in the lexical structure.</p>
   * 
   * <p>FIXME: Now that the associations are computed automatically and
   * are all bidirectional, this verification seems unnecessary.</p>
   * 
   * @return If there is a loop in the lexical structure, a problem is reported.
   *         Otherwise a valid result is returned.
   */
  protected Verification verifyLoops() {
    Verification result = Valid.create();
    Element e = parent();
    while(e != null) {
      if(e == this) {
        result = result.and(new BasicProblem(this, "There is a loop in the lexical structure. This element is an ancestor of itself."));
      }
      e = e.lexical().parent();
    }
    return result;
  }

  /**
   * Verify the properties of this element.
   * 
   * @return The conjunction of the verification results
   *         of all properties of this element.
   */
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

  /**
   * Verify the associations of this element.
   * 
   * @return The conjunction of the verification results of
   *         all associations of this object.
   */
  protected Verification verifyAssociations() {
    Verification result = Valid.create();
    for(ChameleonAssociation<?> association: associations()) {
      result = result.and(association.verify());
    }
    return result;
  }


	 /**
	  * A TreeStructure for Element.
	  * 
	  * @author Marko van Dooren
	  *
	  * @param <N> The exception that can be thrown while navigating the tree.
	  */
	 public abstract class Navigator<N extends Exception> implements TreeStructure<Element, N> {

     public ElementImpl node() {
       return ElementImpl.this;
     }
   /**
	    * {@inheritDoc}
	    * 
	    * @return the parent of the given element.
	    */
	   @Override
	   public Element parent() {
	     return node().actualParent();
	   }

	   /**
	    * {@inheritDoc}
	    * 
	    * @return The {@link Element#children()} of the element.
	    */
	   @Override
	   public List<Element> children() throws N {
	     return node().children();
	   }
	 }	
	 
  /**
   * A tree structure for the main logical structure of the model.
   * 
   * @author Marko van Dooren
   */
  public class LogicalNavigator extends Navigator<LookupException> {

    /**
     * {@inheritDoc}
     * 
     * @return The {@link Element#logical()} tree structure of the element.
     */
    @Override
    public Navigator<LookupException> tree(Element element) {
      return element.logical();
    }

  }

  /**
   * A tree structure for the lexical structure of the model.
   * 
   * @author Marko van Dooren
   */
  public class LexicalNavigator extends Navigator<Nothing> {

    /**
     * {@inheritDoc}
     * 
     * @return The {@link Element#logical()} tree structure of the element.
     */
    @Override
    public Navigator<Nothing> tree(Element element) {
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
	 protected <T extends Element> void add(OrderedMultiAssociation<? extends Element, ? super T> association, Collection<T> elements) {
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
	protected <E extends Element> void add(OrderedMultiAssociation<? extends Element, ? super E> association, E element) {
		if (element != null) {
			association.add((Association) element.parentLink());
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
         throw new LookupExceptionInEquals(e.getMessage(),e);
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
      for (Element child : lexical().children()) {
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
     _viewCache = null;
     _propertyCache = null;
     _properties = null;
   }

   /**
    * Create a new event for the given change in the given source element.
    * 
    * @param change An object representing the change
    * @param source The object that was changed.
    * @return a new event for the given change in the given source element.
    */
   protected <C,S> Event<C,S> createEvent(C change, S source) {
     return new Event<C,S>(change, source);
   }
   
   /**
    * Create an event for the given change.
    * 
    * @param change The change for which an event must be created.
    * @return an event with the given change as its change, and this
    *         element as its source.
    */
   protected <C> Event<C,Element> createEvent(C change) {
     return createEvent(change,this);
   }
   
   /**
    * {@inheritDoc}
    */
   public ElementEventSourceSelector when() {
     if(_eventManager == null) {
       _eventManager = new ElementEventSourceSelector() {
         /**
          * @return the element from which the events are gathered.
          */
         public ElementImpl element() {
           return ElementImpl.this;
         }

         @Override
         protected void startNotification() {
           element().enableChangeNotification();
         }

         @Override
         protected void stopNotification() {
           element().disableChangeNotification();
         }

         @Override
         protected void tearDown() {
           element()._eventManager = null;
         }
       }
       ;
     }
     return _eventManager;
   }

   ElementEventSourceSelector _eventManager;

   /**
    * A helper method that unfortunately has to be public.
    * 
    * @param object The object to be checked.
    * @throws IllegalArgumentException the object is null.
    */
   protected void notNull(Object object) {
  	 if(object == null) {
  		 throw new IllegalArgumentException("The object cannot be null.");
  	 }
   }


}
