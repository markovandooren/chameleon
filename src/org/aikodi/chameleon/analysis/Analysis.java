package org.aikodi.chameleon.analysis;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.util.action.GuardedTreeWalker;
import org.aikodi.chameleon.util.action.TopDown;
import org.aikodi.chameleon.util.action.TreeWalker;
import org.aikodi.chameleon.workspace.DocumentLoader;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.contract.Contracts;
import org.aikodi.rejuse.exception.Handler;

import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

/**
 * A class of objects that analyze a model.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of element that is analyzed.
 * @param <R> The type of the result of the analysis.
 */
public abstract class Analysis<E extends Element, R extends Result<R>, EX extends Exception> implements TreeWalker<Element, EX> {

  private Class<E> _type;


  /**
   * Create a new analysis that analyzes element of the given type, and
   * that starts from the given initial result.
   * 
   * @param type The type of the elements to be analyzed.
   * @param initial The initial result. This is the result that is returned
   * when no elements of the given type are returned.
   */
  public Analysis(Class<E> type, R initial) {
    Contracts.notNull(type, initial);
    _type = type;
    setResult(initial);
  }

  /**
   * @return The result of the analysis.
   */
  public final R result() {
    return _result;
  }

  /**
   * Set the result of the analysis.
   * 
   * @param result The result of the analysis.
   */
  protected void setResult(R result) {
    _result = result;
  }

  private R _result;

  /**
   * @return The class object that represents the type of elements that is
   * being analysed.
   */
  public Class<E> type() {
    return _type;
  }


  /**
   * {@inheritDoc}
   * 
   * Visit the given tree structure, and call {{@link #analyze(Element)} if
   * the element at the node is of the correct {{@link #type()}.
   */
  @Override
  public <X extends Element> void traverse(TreeStructure<X> tree) throws EX {
    X node = tree.node();
    if(type().isInstance(node)) {
      analyze((E)node);
    }
  }


  /**
   * Perform the actual analysing.
   * @param element
   */
  protected abstract void analyze(E element) throws EX;

  @Override
  public final void enter(TreeStructure<?> tree) {
    Object node = tree.node();
    if(type().isInstance(node)) {
      doEnter((E)node);
    }
  }

  protected void doEnter(E element) {
  }
  
  
  public <X extends Exception> R analysisResult(Project project,
      Handler<? super EX,X> analysisHandler,
      Handler<Exception,X> finalHandler) throws X {
    GuardedTreeWalker<Element, EX, X> guarded = new GuardedTreeWalker<Element, EX, X>(this, analysisHandler) ;
    TopDown<Element, X> topDown = new TopDown<>(guarded);
    Stream<View> stream = project.views().stream();
    List<DocumentLoader> flatMap = stream.flatMap(v -> v.sourceScanners().stream()).flatMap(s -> s.documentLoaders().stream()).collect(Collectors.toList());
    for(DocumentLoader loader: flatMap) {
      try {
        Document document = loader.load();
        topDown.traverse(document.lexical());
      } catch(Exception exc) {
        finalHandler.handle(exc);
      }
    }
    return result();
  }

}