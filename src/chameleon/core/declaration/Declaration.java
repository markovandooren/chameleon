package chameleon.core.declaration;

import chameleon.core.element.Element;

public interface Declaration<E extends Declaration<E,P,S>, P extends DeclarationContainer, S extends Signature> extends Element<E,P>{

  public S signature();
  
  /**
   * Return an alias 
   * @return
   */
  public Declaration alias(S sig);
  
  //public boolean defined();
}
