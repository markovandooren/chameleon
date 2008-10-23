package chameleon.core.declaration;

import chameleon.core.element.Element;

public interface Declaration<E extends Declaration, P extends DeclarationContainer> extends Element<E,P>{

  public Signature signature();
  
  //public boolean defined();
}
