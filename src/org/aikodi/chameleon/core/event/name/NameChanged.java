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
  
  /**
   * Create an object representing a name change.
   * 
   * @param oldName The old name.
   * @param newName The new name.
   */
  public NameChanged(String oldName, String newName) {
    this._newName = newName;
    this._oldName = oldName;
  }

  /**
   * @return The new name.
   */
  public String newName() {
    return _newName;
  }

  /**
   * @return The old name.
   */
  public String oldName() {
    return _oldName;
  }
}
