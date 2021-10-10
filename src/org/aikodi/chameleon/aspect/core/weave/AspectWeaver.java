package org.aikodi.chameleon.aspect.core.weave;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.Lists;

import java.util.*;
import java.util.Map.Entry;

public abstract class AspectWeaver {
	/**
	 * 	The object that performs the actual weaving. A chain of responsibility pattern is used for this object
	 */
	private Weaver elementWeaver;
	
	/**
	 * 	Constructor
	 */
	public AspectWeaver(Comparator<JoinPointWeaver> sorter) {
		_sorter = sorter;
		setElementWeaver(initialiseWeavers());
	}
	
	protected abstract Weaver initialiseWeavers();

	/**
	 * 	Weave the given compilation unit
	 * 
	 * 	@param 	compilationUnit
	 * 			The compilation unit to weave
	 * 	@param 	aspectCompilationUnits
	 * 			All compilation units that contain aspects
	 * 	@param 	otherCompilationUnits
	 * 			All other compilation units
	 * 	@throws LookupException
	 */
	public List<JoinPointWeaver> weave(Document compilationUnit, Collection<Document> aspectCompilationUnits) throws LookupException {
//		if (!compilationUnit.hasDescendant(Aspect.class)) {
			Map<Element, List<JoinPointWeaver>> weavingMap = getAllWeavers(compilationUnit, aspectCompilationUnits);
			List<JoinPointWeaver> heads = Lists.create();
			for (Entry<Element, List<JoinPointWeaver>> entry : weavingMap.entrySet()) {
				List<JoinPointWeaver> weavingEncapsulators = entry.getValue();
				
				// Sort all weaving that has to be done
				Collections.sort(weavingEncapsulators, sorter());
				
				// Transform the weaving encapsulation list to a double linked list
				JoinPointWeaver weavingChain = JoinPointWeaver.fromIterable(weavingEncapsulators);
				heads.add(weavingChain);
			}
			return heads;
//			for(JoinPointWeaver weaver:heads) {
//				// Start the weaving
//				weaver.weave();
//			}
	}
		
	public Comparator<JoinPointWeaver> sorter() {
		return _sorter;
	}
	
	private Comparator<JoinPointWeaver> _sorter;
	
	/**
	 * 
	 * 	Weave a given regular type
	 * 
	 * 	@param 	compilationUnit
	 * 			The compilation unit to weave
	 * 	@param 	aspectCompilationUnits
	 * 			All compilation units that contain aspects
	 * 	@return	The map of joinpoints to weaving encapsulators that handle this joinpoint
	 * 	@throws LookupException
	 */
	private Map<Element, List<JoinPointWeaver>> getAllWeavers(Document compilationUnit, Collection<Document> aspectCompilationUnits) throws LookupException {
		// Get a list of all advices
		List<Advice> advices = Lists.create();
		for (Document cu : aspectCompilationUnits) {
			advices.addAll(cu.lexical().descendants(Advice.class));
		}
		
		// Keep a map, per joinpoint: the weaving encapsulators that weave it
		Map<Element, List<JoinPointWeaver>> weavingMap = new HashMap<Element, List<JoinPointWeaver>>();
		
		// Weave all advices
		for (Advice<?> advice : advices) {			
			// Get all joinpoints matched by that expression
			List<MatchResult<? extends Element>> joinpoints = advice.getJoinPoints(compilationUnit);
			
			// For each joinpoint, get all necessairy weaving info and add it to the list
			for (MatchResult<? extends Element> joinpoint : joinpoints) {
				if (!weavingMap.containsKey(joinpoint.getJoinpoint()))
					weavingMap.put(joinpoint.getJoinpoint(), Lists.<JoinPointWeaver>create());
				
				JoinPointWeaver encapsulator = getElementWeaver().weave(advice, joinpoint);
				weavingMap.get(joinpoint.getJoinpoint()).add(encapsulator);
			}
		}
		
		return weavingMap;
	}	
	
	private Weaver getElementWeaver() {
		return elementWeaver;
	}

	private void setElementWeaver(Weaver elementWeaver) {
		this.elementWeaver = elementWeaver;
	}
}
