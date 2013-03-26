package be.kuleuven.cs.distrinet.chameleon.support.expression;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 *
 * An empty array index is of the form [] or [,]
 * When getDimension() == 1 it corresponds to []
 *                     == 2 it corresponds to [,]
 *                     == 2 it corresponds to [,,]
 *                     ...
 * 
 */
public class EmptyArrayIndex extends ArrayIndex {
	
	
	public EmptyArrayIndex(int dimensions){
		setDimension(dimensions);
	}
	
	private int _dimensions = 0;
	
	/**
	 * Increase dimension of this empty array index.
	 */
 /*@
   @ public behavior
   @
   @ pre getDimension < Integer.MAX_VALUE;
   @
   @ post getDimension() == \old(getDimension) + 1;
   @*/
	public void addDimension(){
		if(_dimensions == Integer.MAX_VALUE) {
			throw new ChameleonProgrammerException("The dimension of an empty array index cannot be larger than "+Integer.MAX_VALUE+".");
		}
		_dimensions++;
	}
	
	/**
	 * Decrease dimension of this empty array index.
	 */
 /*@
   @ public behavior
   @
   @ pre getDimension > 0;
   @
   @ post getDimension() == \old(getDimension) - 1;
   @*/
	public void removeDimension(){
		if(_dimensions <= 0) {
			throw new ChameleonProgrammerException("The dimension of an empty array index cannot be made negative.");
		}
		_dimensions--;
	}

	/**
	 * Set the dimension of this empty array index.
	 */
 /*@
   @ public behavior
   @
   @ pre dimension >= 0;
   @
   @ post getDimension() == dimension;
   @*/
	public void setDimension(int dimension){
		if(dimension < 0) {
			throw new ChameleonProgrammerException("The dimension of an empty array index cannot be negative.");
		}
		this._dimensions = dimension;
	}
	
	/**
	 * Return the dimension of this array index.
	 */
	public int getDimension(){
		return _dimensions;
	}

	public EmptyArrayIndex clone(){
		return new EmptyArrayIndex(getDimension());
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
}
