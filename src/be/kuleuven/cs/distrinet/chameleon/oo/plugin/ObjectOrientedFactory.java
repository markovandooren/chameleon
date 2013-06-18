package be.kuleuven.cs.distrinet.chameleon.oo.plugin;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.factory.Factory;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.MethodHeader;
import be.kuleuven.cs.distrinet.chameleon.oo.method.SimpleNameMethodHeader;
import be.kuleuven.cs.distrinet.chameleon.oo.type.RegularType;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.inheritance.InheritanceRelation;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.method.NormalMethod;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.method.RegularMethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.infix.InfixOperatorInvocation;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.postfix.PostfixOperatorInvocation;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.prefix.PrefixOperatorInvocation;
import be.kuleuven.cs.distrinet.chameleon.support.modifier.Constructor;

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
	
	public MethodInvocation createInfixOperatorInvocation(String name, CrossReferenceTarget target) {
		return new InfixOperatorInvocation(name, target);
	}

	public MethodInvocation createPrefixOperatorInvocation(String name, CrossReferenceTarget target) {
		return new PrefixOperatorInvocation(name, target);
	}

	public MethodInvocation createPostfixOperatorInvocation(String name, CrossReferenceTarget target) {
		return new PostfixOperatorInvocation(name, target);
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
