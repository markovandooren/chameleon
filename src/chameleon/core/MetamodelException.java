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
package chameleon.core;

import chameleon.core.element.Element;

/**
 * @author marko
 */
public class MetamodelException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2841949559634528013L;

	public MetamodelException() {
  }
  
  public MetamodelException(String message) {
    super(message);
  }
  
  public MetamodelException(String message, Exception exc) {
  	super(message, exc);
  }
  
//  private static int count;
//  
//  /**
//   * Return how many times a MetamodelException has been created.
//   * @return
//   */
//  public static int count() {
//  	return count;
//  }
  
}
