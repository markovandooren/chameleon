package chameleon.tool.analysis;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.rejuse.java.collections.TransitiveClosure;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.exception.ChameleonProgrammerException;

public class DependencyAnalyzer {

	public Set<Declaration> dependenciesOfAll(Collection<? extends Element> elements) throws LookupException {
		Set<Declaration> result = new HashSet<Declaration>();
		for(Element element: elements) {
			result.addAll(dependencies(element));
		}
		return result;
	}
	
  public Set<Declaration> dependencies(Element element) throws LookupException {
  	Set<Declaration> result = directDependencies(element);
  	try {
			result = new TransitiveClosure<Declaration>() {

				@Override
				public void addConnectedNodes(Declaration node, Set<Declaration> accumulator) throws LookupException {
					accumulator.addAll(directDependencies(node));
				}
			}.closureFromAll(result);
		} catch (RuntimeException e) {
			throw e;
		} catch (LookupException e) {
			throw e;
		} catch (Exception e) {
			throw new ChameleonProgrammerException(e);
		}
		return result;
  }

  public Set<Declaration> directDependencies(Element element) throws LookupException {
  	List<CrossReference> crossReferences = element.descendants(CrossReference.class);
  	Set<Declaration> result = new HashSet<Declaration>();
  	for(CrossReference ref: crossReferences) {
  		result.add(ref.getElement());
  	}
  	return result;
  }
  
  public <T extends Element> Set<T> nearestAncestors(Collection<? extends Element> elements, Class<T> kind) {
  	Set<T> result = new HashSet<T>();
  	for(Element element: elements) {
  		result.add(element.nearestAncestor(kind));
  	}
  	return result;
  }
  
}
