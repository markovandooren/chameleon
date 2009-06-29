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
package chameleon.core.namespace;

import chameleon.core.context.LookupException;
import chameleon.util.Util;

/**
 * @author marko
 */
public class NamespaceReference extends NamespaceOrTypeReference<NamespaceReference,Namespace> {

  public NamespaceReference(NamespaceReference target, String name) {
    super(target, name);
  }
  
  public NamespaceReference(String qn) {
    super(getTarget(Util.getAllButLastPart(qn)), Util.getLastPart(qn));
  }

  protected static NamespaceReference getTarget(String qn) {
    if(qn == null) {
      return null;
    }
    NamespaceReference target = new NamespaceReference(Util.getFirstPart(qn));
    qn = Util.getSecondPart(qn);
    while(qn != null) {
      NamespaceReference newTarget = new NamespaceReference(null, Util.getFirstPart(qn));
      newTarget.setTarget(target);
      target = newTarget;
      qn = Util.getSecondPart(qn);
    }
    return target;
  }
  
  
  public Namespace findNamespace() throws LookupException {
    return (Namespace)getNamespaceOrType();
  }

  public NamespaceReference clone() {
    return new NamespaceReference((NamespaceReference)getTarget().clone(),getName());
  }

  
}
