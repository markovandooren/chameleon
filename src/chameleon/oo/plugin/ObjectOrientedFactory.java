package chameleon.oo.plugin;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.oo.type.RegularType;
import chameleon.oo.type.Type;
import chameleon.plugin.PluginImpl;

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


}
