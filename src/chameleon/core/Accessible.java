///*
// * Copyright 2000-2004 the Jnome development team.
// *
// * @author Marko van Dooren
// * @author Nele Smeets
// * @author Kristof Mertens
// * @author Jan Dockx
// *
// * This file is part of Jnome.
// *
// * Jnome is free software; you can redistribute it and/or modify it under the
// * terms of the GNU General Public License as published by the Free Software
// * Foundation; either version 2 of the License, or (at your option) any later
// * version.
// *
// * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
// * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// * details.
// *
// * You should have received a copy of the GNU General Public License along with
// * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
// * Suite 330, Boston, MA 02111-1307 USA
// */
//package chameleon.core;
//
//import chameleon.core.accessibility.AccessibilityDomain;
//import chameleon.core.element.Element;
//
///**
// * @author marko
// */
//
//public interface Accessible<E extends Element, P extends Element> extends Element<E,P> {
//
//	/**
//	 * Check whether or not given element has the right to access this Accessible
//	 * <b>IF</b> is can see this Accessible. Otherwise the result is useless.
//	 * 
//	 * @param other
//	 * @return
//	 * @throws MetamodelException
//	 * 
//	 * @uml.property name="accessibilityDomain"
//	 * @uml.associationEnd 
//	 * @uml.property name="accessibilityDomain" multiplicity="(0 1)"
//	 */
//	public AccessibilityDomain getAccessibilityDomain()
//		throws MetamodelException;
//
//  
//}
