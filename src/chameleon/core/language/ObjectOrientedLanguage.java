package chameleon.core.language;

import org.rejuse.property.Property;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.StaticProperty;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.member.Member;
import chameleon.core.property.Defined;
import chameleon.core.relation.EquivalenceRelation;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public abstract class ObjectOrientedLanguage extends Language {

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

	public ObjectOrientedLanguage(String name) {
		super(name);
	}

	public ObjectOrientedLanguage(String name, LookupStrategyFactory factory) {
		super(name, factory);
	}

  protected final class DummyTypeReference extends TypeReference {
	  public DummyTypeReference(String qn) {
		  super(qn);
		  setUniParent(defaultNamespace());
    }
  }

	public Type getDefaultSuperClass() throws LookupException {
		  TypeReference typeRef = new DummyTypeReference(getDefaultSuperClassFQN());
	    Type result = typeRef.getType();
	    if (result==null) {
	        throw new LookupException("Default super class "+getDefaultSuperClassFQN()+" not found.");
	    }
	    return result;
	}

	/**
	 * Return the fully qualified name of the class that acts as the default
	 * super class.
	 */
	public abstract String getDefaultSuperClassFQN();

	/**
	 * Return the exception thrown by the language when an invocation is done on a 'null' or 'void' target.
	 *
	 * @param defaultPackage The root package in which the exception type should be found.
	 */
	public abstract Type getNullInvocationException() throws LookupException;

	/**
	 * Return the exception representing the top class of non-fatal runtime exceptions. This doesn't include
	 * errors.
	 *
	 * @param defaultPackage The root package in which the exception type should be found.
	 */
	public abstract Type getUncheckedException() throws LookupException;

	/**
	 * Check whether the given type is an exception.
	 */
	public abstract boolean isException(Type type) throws LookupException;

	/**
	 * Check whether the given type is a checked exception.
	 */
	public abstract boolean isCheckedException(Type type) throws LookupException;

	public abstract Type getNullType();

	/**
	 * Return the relation that determines when a member overrides another
	 */
	public abstract WeakPartialOrder<Type> subtypeRelation();

	/**
	 * Return the relation that determines when a member overrides another
	 */
	public abstract StrictPartialOrder<Member> overridesRelation();

	/**
	 * Return the relation that determines when a member hides another
	 */
	public abstract StrictPartialOrder<Member> hidesRelation();

	/**
	 * Return the relation that determines when a member implements another.
	 */
	public abstract StrictPartialOrder<Member> implementsRelation();

	public abstract Type voidType() throws LookupException;

	/**
	 * Return the type that represents the boolean type in this language.
	 */
	public abstract Type booleanType() throws LookupException;

	/**
	 * Return the type that represents class cast exceptions in this language.
	 */
	public abstract Type classCastException() throws LookupException;

	/**
	 * Return the relation that determines when a member is equivalent to another.
	 */
	public abstract EquivalenceRelation<Member> equivalenceRelation();


  protected void initProperties() {
    OVERRIDABLE.addImplication(INHERITABLE);
    OVERRIDABLE.addImplication(REFINABLE);
    EXTENSIBLE.addImplication(REFINABLE);
  }

}
