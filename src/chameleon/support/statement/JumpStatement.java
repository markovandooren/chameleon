package chameleon.support.statement;

import chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public abstract class JumpStatement<E extends JumpStatement> extends StatementImpl<E> {
  
  public JumpStatement(String label) {
	 setLabel(label);
  }
  
  private String _label;
  
  public String getLabel() {
    return _label;
  }
  
  public void setLabel(String label) {
    _label = label;
  }
  
}
