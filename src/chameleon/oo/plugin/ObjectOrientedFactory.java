package chameleon.oo.plugin;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.factory.Factory;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.oo.expression.MethodInvocation;
import chameleon.oo.method.Method;
import chameleon.oo.method.MethodHeader;
import chameleon.oo.method.SimpleNameMethodHeader;
import chameleon.oo.type.RegularType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.support.member.simplename.method.NormalMethod;
import chameleon.support.member.simplename.method.RegularMethodInvocation;
import chameleon.support.modifier.Constructor;

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
	public Type createRegularType(SimpleNameSignature signature) {
		return new RegularType(signature);
	}

	/**
	 * Create a new invocation based on the name of the invoked method and the target.
	 *  
	 * @param name
	 * @param target
	 * @return
	 */
	public MethodInvocation createInvocation(String name, CrossReferenceTarget target) {
		return new RegularMethodInvocation(name, target);
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
