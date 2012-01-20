package chameleon.support.member.simplename.operator;


import chameleon.oo.member.DeclarationWithParametersSignature;
import chameleon.oo.method.MethodHeader;
import chameleon.oo.method.RegularMethod;

/**
 * @author Marko van Dooren
 */
public abstract class Operator<E extends Operator<E,H,S>, H extends MethodHeader<H,S>, S extends DeclarationWithParametersSignature> extends RegularMethod<E,H,S> {

  public Operator(H header) {
    super(header);
  }

}
