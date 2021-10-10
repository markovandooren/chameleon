package org.aikodi.chameleon.core.modifier;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.rejuse.property.PropertyMutex;

import java.util.List;

/**
 * A default implementation for elements with modifiers. This class defines the
 * field to store the modifiers and provides methods to add and remove them.
 * 
 * @author Marko van Dooren
 */
public abstract class ElementWithModifiersImpl extends ElementImpl implements ElementWithModifiers {

  private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

  @Override
  public List<Modifier> modifiers() {
    return _modifiers.getOtherEnds();
  }
  
  public boolean hasModifiers() {
    return _modifiers.size() > 0;
  }

  @Override
  public void addModifier(Modifier modifier) {
    add(_modifiers, modifier);
  }

  @Override
  public void removeModifier(Modifier modifier) {
    remove(_modifiers, modifier);
  }

  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  /**
   * {@inheritDoc}
   */
	public List<Modifier> modifiers(PropertyMutex<ChameleonProperty> mutex) throws ModelException {
    ChameleonProperty property = property(mutex);
    List<Modifier> result = Lists.create();
    for (Modifier mod : modifiers()) {
      if (mod.impliesTrue(property)) {
        result.add(mod);
      }
    }
    return result;
	}

}
