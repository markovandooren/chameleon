package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.element.ElementImpl;

import java.util.List;

/**
 * A class for representing qualified names. A qualified name is a sequence of signatures where each signature 
 * references a declaration in the declaration that is referenced by the preceding signature.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 */
public abstract class QualifiedName extends ElementImpl {

	/**
	 * Return the signatures that make up this qualified name.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract List<Signature> signatures();
	
	
	/**
	 * Return the last signature of this qualified name.
	 */
 /*@
   @ public behavior
   @
   @ post \result == signatures().get(signatures().size());
   @*/
	public abstract Signature lastSignature();
	
	/**
	 * Return the length of this qualified name.
	 */
 /*@
   @ public behavior
   @
   @ post \result == signatures().size();
   @*/
	public abstract int length();
	
	/**
	 * Return the index-th signature. Indices start at 1.
	 */
 /*@
   @ public behavior
   @
   @ pre index >= 1;
   @ pre index <= length();
   @
   @ post \result == signatures().get(index);
   @*/
	public abstract Signature signatureAt(int index);
	
	/**
	 * Return a new qualified name that contains all signatures of this qualified name, except for the last one.
	 */
 /*@
   @ public behavior
   @
   @ post \fresh(\result);
   @ post \result.length() == length() - 1;
   @ post (\forall int i; i >= 1 && i <= length() - 1; \result.signatureAt(i).sameAs(signatureAt(i)));
   @*/
	public QualifiedName popped() {
		CompositeQualifiedName result = new CompositeQualifiedName();
		signatures().stream().limit(length() - 1).forEachOrdered(s -> result.append(clone(s)));
		return result;
	}
	
}
