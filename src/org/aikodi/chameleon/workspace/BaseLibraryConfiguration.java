package org.aikodi.chameleon.workspace;

import java.util.HashMap;
import java.util.Map;

import org.aikodi.chameleon.core.language.Language;

/**
 * <p>
 * An object for configuring for which languages the libraries must be loaded.
 * </p>
 * 
 * <p>
 * For user projects, all base libraries must be loaded, but when loading the
 * base library of a language, that is not the case.
 * </p>
 * 
 * @author Marko van Dooren
 */
public class BaseLibraryConfiguration {

  /**
   * Create a new base library configuration for the given workspace. The
   * workspace is used to retreive the language objects.
   * 
   * @param workspace
   */
  public BaseLibraryConfiguration(Workspace workspace) {
    setWorkspace(workspace);
  }

  private void setWorkspace(Workspace workspace) {
    if (workspace == null) {
      throw new IllegalArgumentException("The workspace of a base library configuration should not be null.");
    }
    _workspace = workspace;
  }

  private Workspace _workspace;

  /**
   * @return The workspace that contains the language repository.
   */
  public Workspace workspace() {
    return _workspace;
  }

  private Map<String, Boolean> _baseLibraryMap = new HashMap<>();

  /**
   * Check whether the base library of the given language must be loaded.
   * 
   * @param language The language for which must be calculated whether the
   * base library should be loaded.
   * 
   * @return True if the base library of the language should be loaded,
   * false otherwise.
   */
  public boolean mustLoad(Language language) {
    return mustLoad(language.name());
  }

  
  /**
   * Check whether the base library of the language with the given name
   * must be loaded.
   * 
   * @param name The name of the language for which must be calculated whether 
   * the base library should be loaded.
   * 
   * @return True if the base library of the language should be loaded,
   * false otherwise.
   */
  public boolean mustLoad(String name) {
    Boolean tmp = _baseLibraryMap.get(name.toLowerCase());
    return tmp == null ? true : tmp;
  }

  /**
   * Register whether or not the base library of the language with the given
   * name must be loaded.
   * 
   * @param languageName The name of the language for which is registered 
   * whether the base library should be loaded.
   * 
   * @param load True if the base library of the language should be loaded,
   * false otherwise.
   */
  public void put(String languageName, boolean load) {
    _baseLibraryMap.put(languageName.toLowerCase(), load);
  }
}