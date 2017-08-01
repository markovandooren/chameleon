package org.aikodi.chameleon.analysis;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.Import;
import org.aikodi.chameleon.util.action.GuardedTreeWalker;
import org.aikodi.chameleon.util.action.TopDown;
import org.aikodi.chameleon.workspace.DocumentLoader;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.exception.Handler;

public abstract class Analyzer {

  /**
   * Create a new analyser for the given project.
   * @param project
   */
  /*@
   @ public behavior
   @
   @ pre project != null;
   @ post project() == project;
   @*/
  public Analyzer(Project project) {
    _project = project;
  }

  private Project _project;

  /**
   * Perform the given analysis on the project of this analyzer.
   * 
   * @param analysis The analysis to be executed.
   * @return The result of performing the given analysis top down on every
   * source document in the project. 
   * @throws InputException
   */
  protected <R extends Result<R>, E extends Exception, A extends Exception, I extends Exception> R analysisResult(
      Analysis<? extends Element,R,E> analysis,
      Handler<? super E,A> analysisHandler,
      Handler<Exception,I> handler) throws A, I {
    GuardedTreeWalker<Element, E, A> todo = new GuardedTreeWalker<>(analysis, analysisHandler) ;
    TopDown<Element, A> topDown = new TopDown<>(todo);
    Stream<View> stream = project().views().stream();
    List<DocumentLoader> flatMap = stream.flatMap(v -> v.sourceScanners().stream()).flatMap(s -> s.documentLoaders().stream()).collect(Collectors.toList());
    for(DocumentLoader loader: flatMap) {
      try {
        Document document = loader.load();
      	cleanImports(document);
        topDown.traverse(document.lexical());
      } catch(Exception exc) {
        handler.handle(exc);
      }
    }
    return analysis.result();
  }
  
  private void cleanImports(Document document) {
  	document.lexical().descendants(Import.class).forEach(i -> {
  		try {
  			i.demandImports();
  			i.directImports();
  		}catch (LookupException exc) {
  			i.disconnect();
  		}
  	});
  }

  public Project project() {
    return _project;
  }

  public Collection<Document> sourceDocuments() throws InputException {
    return project().sourceDocuments();
  }

}
