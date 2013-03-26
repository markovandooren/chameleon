package be.kuleuven.cs.distrinet.chameleon.aspect.core.weave;

import java.util.Iterator;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.Advice;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.infrastructure.AdviceInfrastructureFactory;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.transform.JoinPointTransformer;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

/**
 * 	This class encapsulates everything needed to perform weaving:
 * 
 * 	- The weaving provider, the object that ties the joinpoint and weaving result together
 * 	- The weaing result provider, the object that returns the weaving result
 * 	- The advice transformation provider, the object that performs additional transformations on the advice
 * 
 *  - The joinpoint that was matched
 *  - The advice that has to be woven
 *  
 *  Furthermore, since multiple advices can be applied to the same joinpoint (with different providers), all encapsulators for a given joinpoint
 *  are organised in a double linked list. This is needed because sometimes, the result of the providers depends on the next or previous advice.
 *  
 *  This class ultimately starts the weaving process through its start method.
 *  
 * 	@author Jens De Temmerman
 *
 * 	@param <T>	The type of the joinpoint
 * 	@param <U>	The type of the result
 */
public class JoinPointWeaver<T extends Element, U extends Element> {
	/**
	 * 	The weaving provider
	 */
//	private JoinPointTransformer<T, U> _weavingProvider;
	
	/**
	 * 	The weave result provider
	 */
	private JoinPointTransformer<T,U> _joinPointTransformer;
	
	/**
	 * 	The transformation provider
	 */
	private AdviceInfrastructureFactory _adviceTransformationProvider;
	
	/**
	 * 	The joinpoint
	 */
	private MatchResult<T> _joinpoint;
	
	/**
	 * 	The advice
	 */
	private Advice<?> _advice;
	
	/**
	 * 	The next encapsulator in the chain
	 */
	private JoinPointWeaver<T, U> _next;
	
	public JoinPointWeaver<T, U> next() {
		return _next;
	}
	
	/**
	 * 	The previous encapsulator in the chain
	 */
	private JoinPointWeaver<T, U> _previous;
	
	public JoinPointWeaver<T, U> previous() {
		return _previous;
	}
	
	/**
	 * 	Constructor
	 * 
	 * 	@param 	weavingProvider
	 * 			The weaving provider
	 * 	@param 	weavingResultProvider
	 * 			The weaving result provider
	 * 	@param 	adviceTransformationProvider
	 * 			The advice transformation provider
	 * 	@param 	advice
	 * 			The advice
	 * 	@param 	joinpoint
	 * 			The joinpoint
	 */
	public JoinPointWeaver(JoinPointTransformer<T,U> weavingResultProvider, AdviceInfrastructureFactory adviceTransformationProvider, 
			                   Advice<?> advice, MatchResult<T> joinpoint) {
		_joinPointTransformer = weavingResultProvider;
		_joinPointTransformer.setJoinPointWeaver(this);
		_adviceTransformationProvider = adviceTransformationProvider;
		_adviceTransformationProvider.setJoinPointWeaver(this);
		
		_advice = advice;
		_joinpoint = joinpoint;
	}
	
	/**
	 * 	Start the weaving process
	 * 	
	 * 	@throws LookupException	FIXME: check this
	 */
	public U weave() throws LookupException {

		// Get the weaving result
		U result = getJoinPointTransformer().transform();
		
		// Generate the advice infrastructure.
		getAdviceInfrastructureFactory().transform(getAdvice(), getJoinpoint());

		T joinpointShadow = getJoinpoint().getJoinpoint();
		SingleAssociation parentLink = joinpointShadow.parentLink();
		parentLink.getOtherRelation().replace(parentLink, (Association)result.parentLink());

		return result;
//		if (_next != null)
//			_next.weave();
	}
	
	/**
	 * 	Set the next weaving encapsulator in the chain
	 * 
	 * 	@param 	next
	 * 			The next weaving encapsulator
	 */
	private void setNext(JoinPointWeaver<T, U> next) {
		this._next = next;
	}
	
	/**
	 * 	Set the previous weaving encapsulator in the chain
	 * 
	 * @param 	previous
	 * 			The previous weaving encapsulator
	 */
	private void setPrevious(JoinPointWeaver<T, U> previous) {
		this._previous = previous;
	}
	
	/**
	 * 	Transform an iterable (e.g. a list) of weaving encapsulators to a double linked list
	 * 
	 * 	@param 	list
	 * 			The list of weaving encapsulators
	 * 	@return The head of the double linked list of weaving encapsulators
	 */
	public static JoinPointWeaver fromIterable(Iterable<JoinPointWeaver> list) {
		if (list == null)
			return null;
		
		Iterator<JoinPointWeaver> iterator = list.iterator();
		
		if (!iterator.hasNext())
			return null;
		
		JoinPointWeaver head = iterator.next();
		JoinPointWeaver current = head;
		
		while (iterator.hasNext()) {
			JoinPointWeaver next = iterator.next();
			current.setNext(next);
			next.setPrevious(current);
			
			current = next;
		}
		
		return head;
	}
	
	/**
	 * 	Get the weaving result provider
	 * 
	 * 	@return	The weaving result provider
	 */
	public JoinPointTransformer<T,U> getJoinPointTransformer() {
		return _joinPointTransformer;
	}

	/**
	 * 	Get the advice transformation provider
	 * 
	 * 	@return	The advice transformation provider
	 */
 /*@
   @ public behavior
   @
   @ post \result.joinPointWeaver() == this;
   @*/
	public AdviceInfrastructureFactory getAdviceInfrastructureFactory() {
		return _adviceTransformationProvider;
	}
	
	/**
	 * 	Get the joinpoint
	 * 
	 * 	@return	The joinpoint
	 */
	public MatchResult<T> getJoinpoint() {
		return _joinpoint;
	}

	/**
	 * 	Get the advice
	 * 
	 * 	@return	The advice
	 */
	public Advice<?> getAdvice() {
		return _advice;
	}
}
