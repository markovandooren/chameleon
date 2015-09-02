package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<D extends Declaration> extends CommonCrossReferenceWithTarget<D> implements CrossReferenceWithName<D>, CrossReferenceWithTarget<D> {

  /**
   * The class object for the type of declaration that is referenced.
   */
  private Class<D> _specificClass;
  private String _name;

  /**
   * Create a new element reference that references an element with the given name, and
   * of the given specific type.
   * 
   * @param name The name of the referenced element.
   * @param specificType The type of the referenced element.
   */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName().equals(name);
   @*/
  public ElementReference(String name, Class<D> specificType) {
    super(null);
    _specificClass = specificType;
    setName(name);
  }

  /**
   * Return the {@link Class} object of the kind of elements that this
   * reference can point at.
   * 
   * @return
   */
  public Class<D> referencedType() {
     return _specificClass;
  }

  @Override
  public String name() {
    return _name;
  }

  @Override
  public final void setName(String name) {
    if(name == null) {
      throw new ChameleonProgrammerException("The name of an element reference cannot be null");
    } else if(name.equals("")) {
      throw new ChameleonProgrammerException("The name of an element reference cannot be the empty string");
    }
    flushLocalCache();
    String old = _name;
    _name = name;
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, name));
    }
  }


  @Override
  public String toString() {
    return (getTarget() == null ? "" : getTarget().toString()+".")+name();
  }

}
