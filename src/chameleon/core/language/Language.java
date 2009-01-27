package chameleon.core.language;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.association.ReferenceSet;
import org.rejuse.association.Relation;
import org.rejuse.property.Property;
import org.rejuse.property.PropertyUniverse;
import org.rejuse.property.StaticProperty;

import chameleon.core.MetamodelException;
import chameleon.core.context.ContextFactory;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.namespace.RootNamespace;
import chameleon.core.property.Defined;
import chameleon.core.relation.EquivalenceRelation;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.tool.ToolExtension;

/**
 * @author Marko van Dooren
 */

public abstract class Language implements PropertyUniverse<Element> {
	
	public Language(String name) {
		this(name, new ContextFactory());
	}
	
	public Language(String name, ContextFactory factory) {
		setName(name);
		setContextFactory(factory);
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	private String _name;

	public RootNamespace defaultNamespace() {
        return (RootNamespace)_default.getOtherEnd();
    }

    private final class DummyTypeReference extends TypeReference {
		  private DummyTypeReference(String qn) {
			  super(qn);
			  setUniParent(defaultNamespace());
		  }
	}

		/******************
     * TOOLEXTENSIONS *
     ******************/

    private static class MapWrapper<T> {  //todo iets voor rejuse?
        private Map<Class<? extends T>,T> myMap = new HashMap<Class<? extends T>,T>();

        public <S extends T> S get(Class<S> key) {
            return (S)myMap.get(key);
        }

        public <S extends T> void put(Class<? extends S> key, S value) {
            myMap.put(key,value);
        }

        public <S extends T> void remove(Class<S> key) {
            myMap.remove(key);
        }

        public Collection<T> values() {
            return myMap.values();
        }

        public <S extends T> boolean containsKey(Class<S> key) {
            return myMap.containsKey(key);
        }

        public boolean isEmpty() {
            return myMap.isEmpty();
        }
    }

    private MapWrapper<ToolExtension> toolExtensions = new MapWrapper<ToolExtension>();

    //private Map<Class<? extends ToolExtension>,? extends ToolExtension> toolExtensions = new HashMap<Class<? extends ToolExtension>,ToolExtension>();
    /*private <T extends ToolExtension> Map<Class<T>,T> getMap() {
        return (Map<Class<T>,T>)toolExtensions;
    }*/

    public <T extends ToolExtension> T getToolExtension(Class<? extends T> toolExtensionClass) {
        return toolExtensions.get(toolExtensionClass);//((Map<Class<T>,T>)getMap()).get(toolExtensionClass);
    }

    public <T extends ToolExtension> void removeToolExtension(Class<? extends T> toolExtensionClass) {
        T old = toolExtensions.get(toolExtensionClass);
        if ((old!=null) && (old.getLanguage()!=this)) {
            old.setLanguage(null, toolExtensionClass);
        }
        toolExtensions.remove(toolExtensionClass);
    }

    public <T extends ToolExtension> void setToolExtension(Class<? extends T> toolExtensionClass, T toolExtension) {
        T old = toolExtensions.get(toolExtensionClass);
        if (old!=toolExtension) {
            if ((toolExtension!=null) && (toolExtension.getLanguage()!=this)) {
                toolExtension.setLanguage(this, toolExtensionClass);
            }
            if (old!=null) {
                old.setLanguage(null, toolExtensionClass);
            }
            toolExtensions.put(toolExtensionClass, toolExtension);
        }
    }

    public Collection<ToolExtension> getToolExtensions() {
        return toolExtensions.values();
    }

    public <T extends ToolExtension> boolean hasToolExtension(Class<? extends T> toolExtensionClass) {
        return toolExtensions.containsKey(toolExtensionClass);
    }

    public boolean hasToolExtensions() {
        return ! toolExtensions.isEmpty();
    }

    /**************************************************************************
     *                                 PROPERTIES                             *
     **************************************************************************/
    
    /**
     * Return the properties that can be used for elements in this model.
     * 
     * For every class of properties, one object is in the set.
     * @return
     */
    public Set<Property<Element>> properties() {
      return new HashSet<Property<Element>>(_properties.getOtherEnds());
    }

    /**
     * Return the object representing the association between this language and the
     * properties to which it is attached.
     * 
     * DO NOT MODIFY THE RESULTING OBJECT. IT IS ACCESSIBLE ONLY BECAUSE OF THE 
     * VERY DUMB ACCESS CONTROL IN JAVA.
     */
    public ReferenceSet<PropertyUniverse<Element>,Property<Element>> propertyLink() {
      return _properties;
    }
    
    private ReferenceSet<PropertyUniverse<Element>,Property<Element>> _properties = new ReferenceSet<PropertyUniverse<Element>, Property<Element>>(this);
    
    /**
     * 
     * @param name
     * @return
     * @throws ChameleonProgrammerException
     *         There is no property with the given name.
     */
    public Property<Element> property(String name) throws ChameleonProgrammerException {
    	for(Property<Element> p: properties()) {
    		if(p.name().equals(name)) {
    			return p;
    		}
    	}
    	throw new ChameleonProgrammerException("Property with name: "+name+" not found.");
    }
    
    public final Property<Element> INHERITABLE = new StaticProperty<Element>("inheritable",this);
    public final Property<Element> OVERRIDABLE = new StaticProperty<Element>("overridable",this);
    public final Property<Element> EXTENSIBLE = new StaticProperty<Element>("extensible", this);
    public final Property<Element> REFINABLE = new StaticProperty<Element>("refinable", this);
    
    public final Property<Element> DEFINED = new Defined("defined",this);
    
    public final Property<Element> INSTANCE = new StaticProperty<Element>("instance",this);
    public final Property<Element> CLASS = INSTANCE.inverse();
    {
      CLASS.setName("class");
    }
    public final Property<Element> CONSTRUCTOR = new StaticProperty<Element>("constructor", this);
    public final Property<Element> DESTRUCTOR = new StaticProperty<Element>("destructor", this);
    public final Property<Element> REFERENCE_TYPE = new StaticProperty<Element>("reference type", this);
    public final Property<Element> VALUE_TYPE = REFERENCE_TYPE.inverse();

    private void initProperties() {
      OVERRIDABLE.addImplication(INHERITABLE);
      OVERRIDABLE.addImplication(REFINABLE);
      EXTENSIBLE.addImplication(REFINABLE);
    }
    
    /**************************************************************************
     *                           DEFAULT NAMESPACE                            *
     **************************************************************************/
    
    public void setDefaultNamespace(RootNamespace defaultNamespace) {
        _default.connectTo(defaultNamespace.getParentLink());
    }

    private Reference _default = new Reference(this); //todo wegens setDefaultNamespace kan dit niet generisch worden gemaakt?

    /**
     * @return
     */
    public Relation defaultNamespaceLink() {
        return _default;
    }

    public Type getDefaultSuperClass() throws MetamodelException {
    	  TypeReference typeRef = new DummyTypeReference(getDefaultSuperClassFQN());
        Type result = typeRef.getType();
        if (result==null) {
            throw new MetamodelException("Default super class "+getDefaultSuperClassFQN()+" not found.");
        }
        return result;
    }
    
    /**************************************************************************
     *                             ABSTRACT METHODS                           *
     **************************************************************************/

    /**
     * Return the fully qualified name of the class that acts as the default
     * super class.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public abstract String getDefaultSuperClassFQN();

    /**
     * Return the exception thrown by the language when an invocation is done on a 'null' or 'void' target.
     *
     * @param defaultPackage The root package in which the exception type should be found.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @
     @ signals (NotResolvedException) (* The type could not be found in the package *);
     @*/
    public abstract Type getNullInvocationException() throws MetamodelException;

    /**
     * Return the exception representing the top class of non-fatal runtime exceptions. This doesn't include
     * errors.
     *
     * @param defaultPackage The root package in which the exception type should be found.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @
     @ signals (NotResolvedException) (* The type could not be found in the package *);
     @*/
    public abstract Type getUncheckedException() throws MetamodelException;


    /**
     * Check whether the given type is an exception.
     */
   /*@
     @ public behavior
     @
     @ pre type != null;
     @*/
    public abstract boolean isException(Type type) throws MetamodelException;

    /**
     * Check whether the given type is a checked exception.
     */
   /*@
     @ public behavior
     @
     @ pre type != null;
     @*/
    public abstract boolean isCheckedException(Type type) throws MetamodelException;

    public abstract Type getNullType();

    public ContextFactory contextFactory() {
    	return _contextFactory;
    }
    
    protected void setContextFactory(ContextFactory factory) {
    	_contextFactory = factory;
    }
    
    private ContextFactory _contextFactory;
    
    public abstract StrictPartialOrder<Member> overridesRelation();
    
    public abstract StrictPartialOrder<Member> hidesRelation();

    public abstract Type voidType() throws MetamodelException;
    
    public abstract Type booleanType() throws MetamodelException;
    
    public abstract Type classCastException() throws MetamodelException;

		public abstract EquivalenceRelation<Member> equivalenceRelation();

}

