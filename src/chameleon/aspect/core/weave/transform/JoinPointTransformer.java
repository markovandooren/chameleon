package chameleon.aspect.core.weave.transform;

import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * 	Objects implementing this interface are responsible for transforming join points. They define how advice and join point
 * 	are combined to a result. (Note that these objects do *not* specify how the weaving itself should happen!) 
 * 
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 *
 * 	@param <T>	The type of join point. This extends Element.
 */
public interface JoinPointTransformer<T extends Element,U> {
	
	public JoinPointWeaver<T,U> joinPointWeaver();
	
	public void setJoinPointWeaver(JoinPointWeaver<T,U> joinPointWeaver);
	
	/**
	 * 	Get the result of the weaving
	 * 
	 * 	@param 	advice
	 * 			The advice to weave
	 * 	@param 	joinpoint
	 * 			The given join point that was matched
	 * 	@return	The result of weaving
	 * 	@throws LookupException
	 */
	public U transform() throws LookupException;
}
