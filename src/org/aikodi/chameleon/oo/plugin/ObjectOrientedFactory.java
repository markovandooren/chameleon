package org.aikodi.chameleon.oo.plugin;

import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.MethodHeader;
import org.aikodi.chameleon.oo.method.SimpleNameMethodHeader;
import org.aikodi.chameleon.oo.type.RegularType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.support.member.simplename.method.NormalMethod;
import org.aikodi.chameleon.support.modifier.Constructor;

/**
 * A factory class for object oriented languages.
 * 
 * @author Marko van Dooren
 */
public abstract class ObjectOrientedFactory extends Factory {

	/**
	 * Create a new basic lexical class with the given signature.
	 */
 /*@
   @ public behavior
   @
   @ pre signature != null;
   @
   @ post \result != null;
   @ post \result.signature() == signature;
   @*/
	public Type createRegularType(String name) {
		return new RegularType(name);
	}

	public Method createNormalMethod(String name, TypeReference returnType) {
		return createNormalMethod(new SimpleNameMethodHeader(name, returnType));
	}

	public NormalMethod createNormalMethod(MethodHeader header) {
		return new NormalMethod(header);
	}
	
	public abstract InheritanceRelation createDefaultInheritanceRelation(Type type);

	public Method transformToConstructor(Method m) {
		m.addModifier(new Constructor());
		return m;
	}

}
