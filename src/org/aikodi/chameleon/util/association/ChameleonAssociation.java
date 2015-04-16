package org.aikodi.chameleon.util.association;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.Verification;

import be.kuleuven.cs.distrinet.rejuse.association.IAssociation;

/**
 * A interface for lexical associations used for Chameleon models.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of element at the other end of the association.
 */
public interface ChameleonAssociation<T extends Element> extends IAssociation<Element, T> {

  /**
   * Verify whether the constraints for this association end are satisfied.
   * 
   * @return Verification.valid() if the constraints are satisfied. Otherwise
   * an object representing the problems.
   */
	public Verification verify();
	
	/**
	 * @return A string that describes the role of the elements at the
	 * other end of the association.
	 */
	public String role();
	
	public void pairWise(ChameleonAssociation<?> other, BiConsumer<Element, Element> consumer);
	
  public void mapTo(ChameleonAssociation<?> other, Function<Element, Element> mapper);

}
