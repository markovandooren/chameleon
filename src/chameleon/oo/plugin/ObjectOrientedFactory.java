package chameleon.oo.plugin;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.reference.SimpleReference;
import chameleon.oo.expression.MethodInvocation;
import chameleon.oo.method.Method;
import chameleon.oo.method.MethodHeader;
import chameleon.oo.method.SimpleNameMethodHeader;
import chameleon.oo.type.RegularType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.plugin.PluginImpl;
import chameleon.support.member.simplename.method.NormalMethod;
import chameleon.support.member.simplename.method.RegularMethodInvocation;
import chameleon.support.modifier.Constructor;

/**
 * A factory class for object oriented languages.
 * 
 * @author Marko van Dooren
 */
public abstract class ObjectOrientedFactory extends PluginImpl {

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
	 * Create a new namespace declaration for the given namespace.
	 *
	 * @param ns The namespace in which the namespace declaration will add elements.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre ns != null;
   @ post \result != null;
   @ post \result.namespace() == ns;
   @*/
	public NamespaceDeclaration createNamespaceDeclaration(String fqn) {
		return new NamespaceDeclaration(new SimpleReference<Namespace>(fqn, Namespace.class));
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

	public Method transformToConstructor(Method m) {
		m.addModifier(new Constructor());
		return m;
	}

}
