package chameleon.core.scope;


/**
 * @author Marko van Dooren
 */
public class UniversalScope extends Scope {

 /*@
   @ also public behavior
   @
   @ post \result == true;
   @*/
  public boolean geRecursive(Scope other) {
    return true;
  }

  public boolean equals(Object o) {
    return (o instanceof UniversalScope);
  }

}
