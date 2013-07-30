package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.analysis.Analysis;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.action.SafeAction;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

import com.google.common.base.Function;

/**
 * An analysis the reports dependencies between elements and declarations.
 * 
 * You can choose of which elements you want to analyze the dependencies
 * via type parameter E, and the corresponding class object passed to
 * the constructor. You can choose in which dependencies you are interested
 * in a number of ways. 
 * 
 * <ol>
 *   <li>Via type parameter D and its corresponding class object pass to the constructor</li>
 *   <li>Via a predicate that is used to filter specific dependencies.</li>
 *   <li>Via a predicate that filter the cross-references that must be analyzed.</li>
 * </ol>
 * 
 * In the most interesting cases, E and T will typically be the same. Usually we
 * want to analyze dependencies between types or modules or other high-level
 * concepts. But you could also analyze on which methods the body of a for-loop depends.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements of which the dependencies must be analyzed.
 * @param <T> The type of the declarations that are analyzed as possible dependencies.
 */
public class DependencyAnalysis<E extends Element, T extends Declaration> extends Analysis<Element, DependencyResult> {

	public DependencyAnalysis(Class<E> elementType,
			                      Class<T> targetType,
														Predicate<Pair<E, T>> declarationPredicate, 
														Predicate<CrossReference<?>> crossReferencePredicate,
														Function<T,T> declarationMapper) {
		super(Element.class);
		if(crossReferencePredicate == null) {
			throw new IllegalArgumentException("The cross reference predicate should not be null");
		}
		if(declarationPredicate == null) {
			throw new IllegalArgumentException("The declaration predicate should not be null");
		}
		if(declarationMapper == null) {
			throw new IllegalArgumentException("The declaration mapper should not be null");
		}
		_elementType = elementType;
		_targetType = targetType;
		_crossReferencePredicate = crossReferencePredicate;
		_dependencyPredicate = declarationPredicate;
		_declarationMapper = declarationMapper;
	}

	private Class<E> _elementType;

	private Class<T> _targetType;

	public Predicate<Pair<E, T>> dependencyPredicate() {
		return _dependencyPredicate;
	}
	
	private Predicate<Pair<E, T>> _dependencyPredicate;
	
	private Predicate<CrossReference<?>> _crossReferencePredicate;
	
	private Function<T, T> _declarationMapper;

	private DependencyResult _result;
	
	@Override
	public DependencyResult result() {
		return _result;
	}
	
	@Override
	protected void doPerform(final Element element) throws Nothing {
		
		
		element.apply(new SafeAction<CrossReference>(CrossReference.class) {
			@Override
			public void doPerform(CrossReference cref) throws Nothing {
				try {
					if(_crossReferencePredicate.eval(cref)) {
						Declaration decl = cref.getElement();
						T container = decl.nearestAncestorOrSelf(DependencyAnalysis.this._targetType);
						if(container != null) {
							T apply = _declarationMapper.apply(container);
							if(dependencyPredicate().eval(new Pair<E, T>(element, apply))) {
								_result.add(element,apply);
							}
						}
					}
				} catch (Exception e) {
					// Only print the stacke trace when an exception is thrown. Don't want the analysis to crash.
					e.printStackTrace();
				}
			}
		});
	}

}
