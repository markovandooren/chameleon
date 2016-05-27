package org.aikodi.chameleon.analysis.dependency;

import java.util.List;

import org.aikodi.chameleon.analysis.Analyzer;
import org.aikodi.chameleon.analysis.dependency.DependencyAnalysis.HistoryFilter;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.rejuse.exception.Handler;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.graph.Edge;
import be.kuleuven.cs.distrinet.rejuse.graph.Path;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdge;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public abstract class DependencyAnalyzer<D extends Declaration> extends Analyzer {

  protected abstract UniversalPredicate<D,Nothing> sourcePredicate();

  protected UniversalPredicate<? super CrossReference<?>,Nothing> crossReferencePredicate() {
    return dependencyPredicate();
  }

  protected Function<Declaration,List<Declaration>,Nothing> createMapper() {
    return d -> Lists.create(d);
  }

  protected abstract UniversalPredicate<D,Nothing> declarationPredicate();

  public DependencyAnalyzer(Project project) {
    super(project);
  }

  /**
   * <p>An interface for constructing a graph.</p>
   * 
   * @author Marko van Dooren
   *
   * @param <V> The type of the vertices in the graph.
   */
  public static interface GraphBuilder<V> {

    /**
     * Add the given vertex to the graph.
     * 
     * @param vertex The vertex to be added. The vertex cannot be null.
     */
    public void addVertex(V vertex);

    public void addEdge(V first, V second);
  }

  public <E extends Exception> void buildGraph(
      final GraphBuilder<Element> builder, 
      Handler<LookupException, E> analysisGuard, 
      Handler<Exception,E> inputGuard) throws E {
    DependencyResult result = dependencyResult(analysisGuard, inputGuard);
    Action<Element, Nothing> nodeAction = new Action<Element, Nothing>(Element.class) {
      @Override
      protected void doPerform(Element d) {
        builder.addVertex(d);
      }
    };
    Action<Edge, Nothing> edgeAction = new Action<Edge, Nothing>(Edge.class) {
      @Override
      protected void doPerform(Edge d) {
        UniEdge<D> edge = (UniEdge<D>) d;
        builder.addEdge(edge.start(), edge.end());
      }
    };
    result.traverse(nodeAction, edgeAction);
    List<Path<Element>> cycles = result.graph().simpleCycles();
		System.out.println("#########" + cycles);
  }

  /**
   * @return
   * @throws InputException
   */
  public <L extends Exception, I extends Exception> DependencyResult dependencyResult(
      Handler<LookupException, L> analysisGuard, 
      Handler<Exception, I> inputGuard) throws L,I {
    UniversalPredicate<D, Nothing> sourcePredicate = sourcePredicate();
    DependencyAnalysis<D, D> analysis = new DependencyAnalysis<D,D>(
        sourcePredicate, 
        crossReferencePredicate(),
        createMapper(),
        declarationPredicate(), 
        dependencyPredicate(),
        historyFilter());

    DependencyResult result = analysisResult(analysis, analysisGuard, inputGuard);
    return result;
  }

  protected True dependencyPredicate() {
    return new True();
  }

  protected HistoryFilter<D, D> historyFilter() {
    return new DependencyAnalysis.NOOP();
  }

}
