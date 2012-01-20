package chameleon.aspect.core.weave;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.aspect.Aspect;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

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
	public void weave(CompilationUnit compilationUnit, Collection<CompilationUnit> aspectCompilationUnits) throws LookupException {
//		if (!compilationUnit.hasDescendant(Aspect.class)) {
			Map<Element, List<JoinPointWeaver>> weavingMap = getAllWeavers(compilationUnit, aspectCompilationUnits);
			
			for (Entry<Element, List<JoinPointWeaver>> entry : weavingMap.entrySet()) {
				List<JoinPointWeaver> weavingEncapsulators = entry.getValue();
				
				// Sort all weaving that has to be done
				Collections.sort(weavingEncapsulators, sorter());
				
				// Transform the weaving encapsulation list to a double linked list
				JoinPointWeaver weavingChain = JoinPointWeaver.fromIterable(weavingEncapsulators);
				
				// Start the weaving
				weavingChain.weave();
			}
//		}
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
	private Map<Element, List<JoinPointWeaver>> getAllWeavers(CompilationUnit compilationUnit, Collection<CompilationUnit> aspectCompilationUnits) throws LookupException {
		// Get a list of all advices
		List<Advice> advices = new ArrayList<Advice>();
		for (CompilationUnit cu : aspectCompilationUnits) {
			advices.addAll(cu.descendants(Advice.class));
		}
		
		// Keep a map, per joinpoint: the weaving encapsulators that weave it
		Map<Element, List<JoinPointWeaver>> weavingMap = new HashMap<Element, List<JoinPointWeaver>>();
		
		// Weave all advices
		for (Advice<?,?> advice : advices) {			
			// Get all joinpoints matched by that expression
			List<MatchResult<? extends Element>> joinpoints = advice.getJoinPoints(compilationUnit);
			
			// For each joinpoint, get all necessairy weaving info and add it to the list
			for (MatchResult<? extends Element> joinpoint : joinpoints) {
				if (!weavingMap.containsKey(joinpoint.getJoinpoint()))
					weavingMap.put(joinpoint.getJoinpoint(), new ArrayList<JoinPointWeaver>());
				
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
