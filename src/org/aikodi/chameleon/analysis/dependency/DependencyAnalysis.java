package org.aikodi.chameleon.analysis.dependency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.aikodi.chameleon.analysis.Analysis;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.contract.Contracts;
import org.aikodi.rejuse.exception.Handler;
import org.aikodi.rejuse.function.Function;
import org.aikodi.rejuse.predicate.Predicate;
import org.aikodi.rejuse.predicate.UniversalPredicate;
import org.aikodi.rejuse.tree.TreeStructure;

/**
 * An analysis the reports dependencies between elements and declarations.
 * 
 * You can choose of which elements you want to analyze the dependencies via
 * type parameter E, and the corresponding class object passed to the
 * constructor. You can choose in which dependencies you are interested in a
 * number of ways:
 * 
 * <ol>
 * <li>Via a predicate that is used to filter the elements of which the
 * dependencies must be computed.</li>
 * <li>Via a predicate that filter the cross-references that must be analyzed.
 * </li>
 * <li>Via a function that maps dependency declarations to other dependency
 * declarations. This can for example be used to map arrays to their component
 * types. Typically a dependency on A[] is just a dependency on A. If there are
 * no such cases in your analysis, simply provide the identity function.
 * <li>Via a predicate that filters the resulting dependency declaration.</li>
 * <li>Via a predicate that filters the entire dependency. Only use this when
 * necessary as it is applied last. If the criteria for element,
 * cross-reference, and dependency declaration are independent, it is best to
 * use the more fine-grained predicates. That way, pruning happens earlier,
 * avoiding pointless computations.</li>
 * </ol>
 * 
 * In the most interesting cases, E and T will typically be the same. Usually we
 * want to analyze dependencies between types or modules or other high-level
 * concepts. But you could also analyze on which methods the body of a for-loop
 * depends.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements of which the dependencies must be
 *          analyzed.
 * @param <D> The type of the declarations that are analyzed as possible
 *          dependencies.
 */
public class DependencyAnalysis<S extends Element, D extends Element> extends Analysis<Element, DependencyResult, LookupException> {

  /**
   * Create a new dependency analysis.
   * 
   * @param sourcePredicate A predicate that determines of which elements the
   *          dependencies must be computed.
   * @param crossReferencePredicate A predicate that determines which
   *          cross-references must be analyzed.
   * @param decomposer A function that is used to decompose an algebraic
   *        declaration (such as a union type) into its components. If the
   *        given value is null, and identity function is used instead.
   * @param declarationPredicate A predicate that determines whether a declaration
   *          is the target of a dependency. After a cross-reference has been
   *          resolved, the logical structure of the resolved element (including
   *          the resolved element itself) is traversed to find the nearest
   *          declaration that matches the predicate. The matching declaration
   *          is the target of the dependency.
   * @param dependencyPredicate A predicate that determines whether a given
   *          dependency is recorded.
   * @param historyFilter
   */
  public DependencyAnalysis(UniversalPredicate<S, Nothing> sourcePredicate,
      UniversalPredicate<? super CrossReference<?>, Nothing> crossReferencePredicate,
      Function<Declaration, List<Declaration>, Nothing> decomposer, 
      UniversalPredicate<D, Nothing> declarationPredicate,
      UniversalPredicate<? super Dependency<? super S, ? super CrossReference, ? super D>, Nothing> dependencyPredicate,
      HistoryFilter<S, D> historyFilter) {
    this(sourcePredicate.type(), sourcePredicate, crossReferencePredicate, declarationPredicate.type(),
        decomposer, declarationPredicate, dependencyPredicate, historyFilter);
  }

//  public static <S extends Element, D extends Element> DependencyAnalysis<S,D,ModelException> create(UniversalPredicate<S, Nothing> sourcePredicate,
//      UniversalPredicate<? super CrossReference<?>, Nothing> crossReferencePredicate,
//      Function<Declaration, List<Declaration>, Nothing> decomposer, 
//      UniversalPredicate<D, Nothing> declarationPredicate,
//      UniversalPredicate<? super Dependency<? super S, ? super CrossReference, ? super D>, Nothing> dependencyPredicate,
//      HistoryFilter<S, D> historyFilter) {
//    return new DependencyAnalysis<S,D,ModelException>(sourcePredicate, crossReferencePredicate, decomposer,declarationPredicate,dependencyPredicate,historyFilter, Guard.<ModelException>propagate());
//  }


  public <C extends CrossReference<?>> DependencyAnalysis(
      Class<S> sourceType, 
      Predicate<S, Nothing> sourcePredicate,
      Class<C> crossReferenceType,
      Predicate<C, Nothing> crossReferencePredicate, 
      Class<D> targetType,
      Predicate<? super D, Nothing> targetPredicate,
      Function<Declaration, List<Declaration>, Nothing> decomposer, 
      Predicate<? super Dependency<? super S, ? super CrossReference, ? super D>, Nothing> dependencyPredicate,
      HistoryFilter<S, D> historyFilter) {
    super(null,null);
    _sourcePredicate = null;
    _dependencyPredicate = null;
    _dependencyFinder = null;
    _decomposer = null;
    _historyFilter = null;
    _crossReferencePredicate = null;
    
  }
  
  public DependencyAnalysis(Class<S> sourceType, 
      Predicate<? super S, Nothing> sourcePredicate,
      UniversalPredicate<? super CrossReference<?>, Nothing> crossReferencePredicate, 
      Class<D> targetType,
      Function<Declaration, List<Declaration>, Nothing> decomposer, 
      UniversalPredicate<? super D, Nothing> targetPredicate,
      UniversalPredicate<? super Dependency<? super S, ? super CrossReference, ? super D>, Nothing> dependencyPredicate,
      HistoryFilter<S, D> historyFilter) {
    super(Element.class, new DependencyResult());
    Contracts.notNull(sourceType, "The source type should not be null");
    Contracts.notNull(sourcePredicate, "The source predicate should not be null");
    Contracts.notNull(crossReferencePredicate, "The cross-reference predicate should not be null");
    Contracts.notNull(targetType, "The target type should not be null");
    //    Contracts.notNull(decomposer, "The decomposer should not be null");
    Contracts.notNull(targetPredicate, "The target predicate should not be null");
    Contracts.notNull(dependencyPredicate, "The dependency predicate should not be null");
    _sourcePredicate = UniversalPredicate.of(sourceType, sourcePredicate);
    _crossReferencePredicate = crossReferencePredicate.makeUniversal(CrossReference.class);
    _dependencyPredicate = _noSelfReference.and(dependencyPredicate);
    if(decomposer == null) {
      _decomposer = d -> Lists.create(d);
    } else {
      _decomposer = decomposer;
    }
    _historyFilter = historyFilter;
    _dependencyFinder = UniversalPredicate.of(targetType, d -> targetPredicate.eval(d));

  }

  private UniversalPredicate<Dependency<? super S, ? super CrossReference, ? super D>, Nothing> _noSelfReference = (UniversalPredicate) new UniversalPredicate<Dependency, Nothing>(
      Dependency.class) {
    @Override
    public boolean uncheckedEval(Dependency t) throws Nothing {
      return t.source() != t.target();
    }
  };

  public static interface HistoryFilter<E extends Element, D extends Element> {

    public default boolean process(Dependency<E, CrossReference, D> dependency, DependencyResult result) {
      return true;
    }

    public default boolean processSource(E source, DependencyResult result) {
      return true;
    }

    public default HistoryFilter<E, D> and(HistoryFilter<E, D> other) {
      if (other instanceof NOOP) {
        return this;
      } else {
        return new AndHistoryFilter<>(this, other);
      }
    }
  }

  public static class AndHistoryFilter<E extends Element, D extends Element> implements HistoryFilter<E, D> {
    private HistoryFilter<E, D> _first;
    private HistoryFilter<E, D> _second;

    public AndHistoryFilter(HistoryFilter<E, D> first, HistoryFilter<E, D> second) {
      _first = first;
      _second = second;
    }

    @Override
    public boolean process(Dependency<E, CrossReference, D> dependency, DependencyResult result) {
      return _first.process(dependency, result) && _second.process(dependency, result);
    }

    @Override
    public boolean processSource(E source, DependencyResult result) {
      return _first.processSource(source, result) && _second.processSource(source, result);
    }
  }

  public static class NOOP<E extends Element, D extends Declaration> implements HistoryFilter<E, D> {
    @Override
    public HistoryFilter<E, D> and(HistoryFilter<E, D> other) {
      return other;
    }
  }

  private final UniversalPredicate<D, Nothing> _dependencyFinder;

  private final UniversalPredicate<? super S, Nothing> _sourcePredicate;

  private final UniversalPredicate<? super CrossReference<?>, Nothing> _crossReferencePredicate;

  public UniversalPredicate<? super Dependency<? super S, ? super CrossReference, ? super D>, Nothing> dependencyPredicate() {
    return _dependencyPredicate;
  }

  private final UniversalPredicate<? super Dependency<? super S, ? super CrossReference, ? super D>, Nothing> _dependencyPredicate;

  private final Function<Declaration, List<Declaration>, Nothing> _decomposer;

  private HistoryFilter<S, D> _historyFilter;

  @Override
  public void doEnter(Element object) {
    if (_sourcePredicate.eval(object)) {
      _elements.addLast((Element) object);
    }
  }

  @Override
  public void exit(TreeStructure<?> tree) {
    Object object = tree.node();
    if (!_elements.isEmpty()) {
      if (_elements.getLast() == object) {
        _elements.removeLast();
      }
    }
  }

  private LinkedList<Element> _elements = new LinkedList<>();

  private List<D> map(List<Declaration> initial) {
    java.util.function.Function<? super Declaration, ? extends D> mapper = i -> i.logical().nearestAncestorOrSelf(_dependencyFinder);
    return initial.stream().map(mapper).filter(d -> d != null).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void analyze(Element element) throws LookupException {
      if (!_elements.isEmpty()) {
        if (_crossReferencePredicate.eval(element)) {
          CrossReference<?> cref = (CrossReference<?>) element;
          // This is the expensive part. File loading is relatively large part
          // if the documents have not yet been loaded.
          Declaration declaration = cref.getElement();
          List<Declaration> initial = _decomposer.apply(declaration);
          List<D> targets = map(initial);

          boolean fixed = false;
          while(! fixed) {
            List<D> tmp = new ArrayList<>();
            for(D target: targets) {
              if(target instanceof Declaration) {
                List<Declaration> decomp = _decomposer.apply((Declaration) target);
                List<D> newTargets = map(decomp);
                tmp.addAll(newTargets);
              } else {
                tmp.add(target);
              }
            }
            if(tmp.containsAll(targets) && targets.containsAll(tmp)) {
              fixed = true;
            }
            targets = tmp;
          }

          for(D target: targets) {
            for (Element e : _elements) {
              Dependency dependency = new Dependency(e, cref, target);
              if (_dependencyPredicate.eval(dependency)) {
                DependencyResult result = result();
                if (_historyFilter.process(dependency, result)) {
                  result.add(dependency);
                }
              }
            }
          }
        }
      }
  }

}
