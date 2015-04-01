package org.aikodi.chameleon.builder;

import java.util.function.Consumer;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.namespace.LazyRootNamespace;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.LanguageRepository;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.chameleon.workspace.Workspace;

/**
 * A convenience builder for a project with a single view.
 * 
 * Multiple scanners can be added by attaching them directly to
 * the view using {@link View#addSource(DocumentScanner)} or
 * {@link View#addBinary(DocumentScanner)}, depending on whether
 * to documents form an immutable library, or source documents for the
 * project.
 * 
 * @author Marko van Dooren
 */
public class ProjectBuilder {

  private Project _project;

  /**
   * Create a builder for new project with the given name using the 
   * language for functional analysis.
   * 
   * The view is connected to a project, which is connected to a workspace.
   * The language repository of the workspace contains only the language
   * for functional analysis.
   * 
   * To create a document scanner, {@link #createDocumentScanner()} is
   * invoked. This scanner is then attached to the {@link #view()}.
   * 
   * @param name The name of the project.
   * @throws ProjectException 
   */
  public ProjectBuilder(String name) throws ProjectException {
    createProject(name);
  }

  /**
   * Initialize the project with the given name.
   * 
   * @param name The name of the project.
   * @throws ProjectException
   */
  protected void createProject(String name) throws ProjectException {
    LanguageRepository repository = new LanguageRepository();
    Workspace workspace = new Workspace(repository);
    _project = new Project(name, null);
    workspace.add(_project);
  }

  /**
   * Add a view with the given language to the project.
   * 
   * @param language The language for the new view.
   * @return This project builder.
   */
  public ProjectBuilder withLanguage(Language language) {
    return withLanguage(language, v -> {});
  }

  /**
   * Add a view with the given language to the project, and also
   * pass it to the given consumer.
   * 
   * @param language The language for the new view.
   * @return This project builder.
   */
  public ProjectBuilder withLanguage(Language language, Consumer<View> consumer) {
    project().workspace().languageRepository().add(language);
    View createView = language.createView();
    consumer.accept(createView);
    return withView(createView);
  }

  /**
   * Add the given view to the project.
   * 
   * @param view The view to be added.
   * @return This project builder.
   */
  public ProjectBuilder withView(View view) {
    project().addView(view);
    return this;
  }

  /**
   * Create a new  root namespace.
   * 
   * @return a new root namespace.
   */
  protected RootNamespace createRootNamespace() {
    return new LazyRootNamespace();
  }

  /**
   * @return The project that was created.
   */
  public Project project() {
    return _project;
  }

}
