package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.ArrayList;
import java.util.LinkedList;

import be.kuleuven.cs.distrinet.chameleon.analysis.Analysis;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.contract.Contracts;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

/**
 * An analysis the reports dependencies between elements and declarations.
 * 
 * You can choose of which elements you want to analyze the dependencies
 * via type parameter E, and the corresponding class object passed to
 * the constructor. You can choose in which dependencies you are interested
 * in a number of ways: 
 * 
 * <ol>
 *   <li>Via a predicate that is used to filter the elements 
 *       of which the dependencies must be computed.</li>
 *   <li>Via a predicate that filter the cross-references that must be analyzed.</li>
 *   <li>Via a function that maps dependency declarations to other dependency declarations.
 *       This can for example be used to map arrays to their component types. Typically
 *       a dependency on A[] is just a dependency on A. If there are no such cases in
 *       your analysis, simply provide the identity function.
 *   <li>Via a predicate that filters the resulting dependency declaration.</li>
 *   <li>Via a predicate that filters the entire dependency. Only use this when necessary
 *       as it is applied last. If the criteria for element, cross-reference, and dependency
 *       declaration are independent, it is best to use the more fine-grained predicates.
 *       That way, pruning happens earlier, avoiding pointless computations.</li>
 * </ol>
 * 
 * In the most interesting cases, E and T will typically be the same. Usually we
 * want to analyze dependencies between types or modules or other high-level
 * concepts. But you could also analyze on which methods the body of a for-loop depends.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements of which the dependencies must be analyzed.
 * @param <D> The type of the declarations that are analyzed as possible dependencies.
 */
public class DependencyAnalysis<E extends Element, D extends Declaration> extends Analysis<Element, DependencyResult> {

	
	public DependencyAnalysis(UniversalPredicate<E,Nothing> elementPredicate,
      UniversalPredicate<? super CrossReference<?>,Nothing> crossReferencePredicate,
      Function<D,D,Nothing> declarationMapper,
			UniversalPredicate<D,Nothing> declarationPredicate, 
			UniversalPredicate<? super Dependency<? super E, ? super CrossReference, ? super D>,Nothing> dependencyPredicate) {
		this(elementPredicate.type(), 
				 elementPredicate, 
				 crossReferencePredicate,
				 declarationPredicate.type(), 
				 declarationMapper,
				 declarationPredicate,
				 dependencyPredicate);
	}

	public DependencyAnalysis(Class<? extends E> elementType,
														UniversalPredicate<? super E,Nothing> elementPredicate,
			                      UniversalPredicate<? super CrossReference<?>,Nothing> crossReferencePredicate,
														Class<D> declarationType,
			                      Function<D,D,Nothing> declarationMapper,
														UniversalPredicate<? super D,Nothing> declarationPredicate, 
														UniversalPredicate<? super Dependency<? super E, ? super CrossReference, ? super D>,Nothing> dependencyPredicate) {
		super(Element.class, new DependencyResult());
		Contracts.notNull(elementType, "The element type should not be null");
		Contracts.notNull(elementPredicate, "The element predicate should not be null");
		Contracts.notNull(crossReferencePredicate, "The cross-reference predicate should not be null");
		Contracts.notNull(declarationType, "The declaration type should not be null");
		Contracts.notNull(declarationMapper,"The declaration mapper should not be null");
		Contracts.notNull(declarationPredicate, "The declaration predicate should not be null");
		Contracts.notNull(dependencyPredicate, "The dependency predicate should not be null");
		_elementPredicate = elementPredicate;
		_declarationPredicate = declarationPredicate;
		_crossReferencePredicate = crossReferencePredicate.makeUniversal(CrossReference.class);
		_dependencyPredicate = _noSelfReference.and(dependencyPredicate);
		_declarationMapper = declarationMapper;
		_elementType = elementType;
		_declarationType = declarationType;
	}
	
	private UniversalPredicate<Dependency<? super E,? super CrossReference,? super D>,Nothing> _noSelfReference = (UniversalPredicate)new UniversalPredicate<Dependency, Nothing>(Dependency.class) {
		@Override
		public boolean uncheckedEval(Dependency t) throws Nothing {
			return t.source() != t.target();
		}
	};
	
	private Class<D> _declarationType;

	private Class<? extends E> _elementType;

	private final UniversalPredicate<?,Nothing> _elementPredicate;
	
	private final UniversalPredicate<? super D,Nothing> _declarationPredicate;
	
	private final UniversalPredicate<? super CrossReference<?>,Nothing> _crossReferencePredicate;
	
	public UniversalPredicate<? super Dependency<? super E, ? super CrossReference, ? super D>,Nothing> dependencyPredicate() {
		return _dependencyPredicate;
	}
	
//	private final UniversalPredicate<? super Pair<E, D>,Nothing> _dependencyPredicate;
	
	private final UniversalPredicate<? super Dependency<? super E, ? super CrossReference, ? super D>,Nothing> _dependencyPredicate;
	
	private final Function<D, D,Nothing> _declarationMapper;

	@Override
	public void doEnter(Element object) {
		if(_elementType.isInstance(object) && _elementPredicate.eval(object)) {
			_elements.addLast(object);
		}
	}
	
	@Override
	public void doExit(Element object) {
		if(! _elements.isEmpty()) {
			if(_elements.getLast() == object) {
				_elements.removeLast();
			}
		}
	}
	
	private LinkedList<Element> _elements = new LinkedList<>();
	
	private ArrayList<Object> done = new ArrayList<>();
	
	@Override
	protected void doPerform(final Element element) throws Nothing {
		done.add(element);
		try {
			if(! _elements.isEmpty()) {
				if(_crossReferencePredicate.eval(element)) {
					CrossReference<?> cref = (CrossReference<?>) element;
					Declaration decl = cref.getElement();

					D container = decl.nearestAncestorOrSelf(new UniversalPredicate<D,Nothing>(_declarationType) {
						@Override
						public boolean uncheckedEval(D t) throws Nothing {
							return _declarationPredicate.eval(_declarationMapper.apply(t));
						}
					});

					if(container != null) {
						for(Element e: _elements) {
							if(_dependencyPredicate.eval(new Dependency(e,cref,container))) {
								result().add(e,container);
							}
						}
					}
				}
			}
		} catch (LookupException e) {
			// No edge is added when we cannot determine the dependency
			e.printStackTrace();
		}
	}

}
