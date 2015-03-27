package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

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
		int old = _dimensions;
		_dimensions++;
		if(changeNotificationEnabled()) {
		  notify(new DimensionChanged(old, _dimensions));
		}
	}
	
	private static class DimensionChanged implements Change {
	  private int _oldDimensions;
	  private int _newDimensions;
    public DimensionChanged(int oldDimensions, int newDimensions) {
      this._oldDimensions = oldDimensions;
      this._newDimensions = newDimensions;
    }
	  
    public int oldDimension() {
      return _oldDimensions;
    }
	  
    public int newDimension() {
      return _newDimensions;
    }
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

	@Override
   protected EmptyArrayIndex cloneSelf(){
		return new EmptyArrayIndex(getDimension());
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(getDimension() < 0) {
			result = result.and(new BasicProblem(this, "The dimension of an array cannot be negative."));
		}
		return result;
	}
	
}
