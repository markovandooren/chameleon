package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public abstract class JumpStatement extends StatementImpl {
  
  public JumpStatement(String label) {
	 setLabel(label);
  }
  
  private String _label;
  
  public String getLabel() {
    return _label;
  }
  
  public void setLabel(String label) {
    String old = _label;
    _label = label;
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, label));
    }
  }
  
}
