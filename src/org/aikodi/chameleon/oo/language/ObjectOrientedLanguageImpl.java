package org.aikodi.chameleon.oo.language;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.language.LanguageImpl;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.Defined;
import org.aikodi.chameleon.core.property.DynamicChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.core.relation.EquivalenceRelation;
import org.aikodi.chameleon.core.relation.StrictPartialOrder;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.VariableDeclarator;
import org.aikodi.rejuse.junit.Revision;

/**
 * A convenience class for object-oriented programming languages.
 * It defined a number of properties that are applicable to object-oriented
 * programming languages.
 * 
 * @author Marko van Dooren
 */
public abstract class ObjectOrientedLanguageImpl extends LanguageImpl implements ObjectOrientedLanguage {
	
	//TODO document the properties. This is becoming complicated without an explanation.
	protected final ChameleonProperty EXTENSIBLE;
	protected final DynamicChameleonProperty DEFINED;
	protected final ChameleonProperty ABSTRACT;
	protected final StaticChameleonProperty INSTANCE;
	protected final ChameleonProperty CLASS;
	protected final ChameleonProperty CONSTRUCTOR;
	protected final ChameleonProperty DESTRUCTOR;
	protected final ChameleonProperty REFERENCE_TYPE;
	protected final ChameleonProperty VALUE_TYPE;
	protected final ChameleonProperty NATIVE;
	protected final ChameleonProperty INTERFACE;
	protected final ChameleonProperty FINAL;
	protected final DynamicChameleonProperty PRIMITIVE_TYPE;

	public ObjectOrientedLanguageImpl(String name, Revision version) {
		this(name,null,version);
	}

	public ObjectOrientedLanguageImpl(String name, LookupContextFactory factory, Revision version) {
		super(name, factory,version);
		// 1) Create the properties.
  	EXTENSIBLE = add(new StaticChameleonProperty("extensible", Declaration.class));
  	DEFINED = add(new Defined("defined"));
  	DEFINED().addValidElementType(Variable.class);
//  	ABSTRACT = DEFINED.inverse();
  	ABSTRACT = add(new StaticChameleonProperty("abstract", Declaration.class));
  	INSTANCE = add(new StaticChameleonProperty("instance",Declaration.class));
  	INSTANCE().addValidElementType(VariableDeclarator.class);
  	CLASS = INSTANCE().inverse();
    CLASS().setName("class");
    CONSTRUCTOR = add(new StaticChameleonProperty("constructor",Method.class));
    DESTRUCTOR = add(new StaticChameleonProperty("destructor",Method.class));
  	REFERENCE_TYPE = add(new StaticChameleonProperty("reference type", Type.class));
  	VALUE_TYPE = REFERENCE_TYPE.inverse();
  	NATIVE = add(new StaticChameleonProperty("native", Type.class));
		INTERFACE = add(new StaticChameleonProperty("interface", Type.class));
    FINAL = add(new StaticChameleonProperty("final", Declaration.class));
  	//2) Add relations between the properties.
    FINAL().addImplication(REFINABLE().inverse());
    FINAL().addImplication(DEFINED());
    EXTENSIBLE().addImplication(REFINABLE());
    NATIVE().addImplication(DEFINED());
    INTERFACE().addImplication(ABSTRACT());
    ABSTRACT().addImplication(DEFINED().inverse());
	PRIMITIVE_TYPE = add(createPrimitiveTypeProperty());

	}

	public ChameleonProperty PRIMITIVE_TYPE() {
		return PRIMITIVE_TYPE;
	}

	public ChameleonProperty CONSTRUCTOR() {
		return CONSTRUCTOR;
	}

	/**
	 * Return the property for a primitive type.
	 * @return
	 */
	protected abstract DynamicChameleonProperty createPrimitiveTypeProperty();

	/**
	 * Return the exception representing the top class of non-fatal runtime exceptions. This doesn't include
	 * errors.
	 *
	 * @param ns The root package in which the exception type should be found.
	 */
	public abstract Type getUncheckedException(Namespace ns) throws LookupException;

	/**
	 * Return the relation that determines when a member implements another.
	 */
	public abstract StrictPartialOrder<Declaration> implementsRelation();

	/**
	 * Return the relation that determines when a member is equivalent to another.
	 */
	public abstract EquivalenceRelation<Declaration> equivalenceRelation();


	public TypeReference createNonLocalTypeReference(TypeReference tref) {
		return createNonLocalTypeReference(tref, tref.lexical().parent());
	}

	@Override
	public ChameleonProperty EXTENSIBLE() {
		return EXTENSIBLE;
	}

	@Override
	public DynamicChameleonProperty DEFINED() {
		return DEFINED;
	}

	@Override
	public ChameleonProperty ABSTRACT() {
		return ABSTRACT;
	}

	@Override
	public StaticChameleonProperty INSTANCE() {
		return INSTANCE;
	}

	@Override
	public ChameleonProperty CLASS() {
		return CLASS;
	}

	@Override
	public ChameleonProperty DESTRUCTOR() {
		return DESTRUCTOR;
	}

	@Override
	public ChameleonProperty VALUE_TYPE() {
		return VALUE_TYPE;
	}

	@Override
	public ChameleonProperty NATIVE() {
		return NATIVE;
	}

	@Override
	public ChameleonProperty INTERFACE() {
		return INTERFACE;
	}

	@Override
	public ChameleonProperty FINAL() {
		return FINAL;
	}

	@Override
	public ChameleonProperty REFERENCE_TYPE() {
		return REFERENCE_TYPE;
	}
}
