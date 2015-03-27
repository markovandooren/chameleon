package org.aikodi.chameleon.core.event.name;

import org.aikodi.chameleon.core.event.Change;

/**
 * An event to indicate that the name of an element has changed.
 * 
 * @author Marko van Dooren
 */
public class NameChanged implements Change {

  private final String _newName;
  private final String _oldName;
  
  public NameChanged(String oldName, String newName) {
    this._newName = newName;
    this._oldName = oldName;
  }

  public String newName() {
    return _newName;
  }

  public String oldName() {
    return _oldName;
  }
  
  
}
