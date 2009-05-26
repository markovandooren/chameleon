/*
 * Copyright 2000-2004 the Jnome development team.
 *
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 * This file is part of Jnome.
 *
 * Jnome is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;


/**
 * @author Marko van Dooren
 */
public abstract class ModifierImpl<E extends Modifier,P extends ModifierContainer> extends ElementImpl<E,P> implements Modifier<E,P> {



  public ModifierImpl() {
  }
 
  /**
   * Clone this modifier.
   */
  public abstract E clone();

  /*@
    @ public behavior
    @
    @ post \result.isEmpty();
    @*/
  public List<? extends Element> children() {
    return new ArrayList<Element>();
  }
  
  protected PropertySet<Element> createSet() {
    return new PropertySet<Element>(); 
  }
  
  protected PropertySet<Element> createSet(Property p) {
    PropertySet<Element> result = createSet();
    result.add(p);
    return result;
  }

  protected PropertySet<Element> createSet(Property p1, Property p2) {
  	PropertySet<Element> result = createSet(p1);
    result.add(p2);
    return result;
  }
  
  protected PropertySet<Element> createSet(Property p1, Property p2, Property p3) {
  	PropertySet<Element> result = createSet(p1, p2);
    result.add(p3);
    return result;
  }
  
}
