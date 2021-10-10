package org.aikodi.chameleon.eclipse.view.outline;


import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorExtension;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx
 * @author Marko van Dooren
 * 
 * Represents the model of the outline view.
 */
public class ChameleonOutlineTree {
	//the current element
	private Element _node;
	//the current element's children
	private List<ChameleonOutlineTree> _children;
	//A listener for this tree
	private IChameleonOutlineTreeListener listener= NullChameleonOutlineTreeListener.getSoleInstance();
	
	//contains string representation of all the elements that may be contained in this tree
//	private static HashMap<String, List<String>> allowedTreeElements = new HashMap<String, List<String>>(0);
	
	public int size() {
		int result = 0;
		for(ChameleonOutlineTree tree: _children) {
			result += tree.size();
		}
		result+=_children.size();
		return result;
	}
	
	/**
	 * Creates a new empty tree with empty current node, and no children
	 */
	public ChameleonOutlineTree() {
		// this.hashElements = hashElements;
		//  this.decorators = decorators_All;
		this._node = null;
		//this.editor = editor;
		this._children = new ArrayList<ChameleonOutlineTree>();  
	}
	
	/*
	 * Creates a new  tree with a given node and no children
	 */
	public ChameleonOutlineTree(Element node) {
		//  this.hashElements = hashElements;
		//  this.decorators = decorators_All;
		this._node = node;
		//this.editor = editor;
		this._children = new ArrayList<ChameleonOutlineTree>();
	}
	
	
	/*
	 * if there are no children, a new vector is created. either way , 
	 * the child tree is added 
	 */
	private ChameleonOutlineTree addTreeNodeChild(ChameleonOutlineTree childTNode) {
		if (_children == null)
			_children = new  ArrayList<ChameleonOutlineTree>(0);
		_children.add(childTNode);
		return childTNode;
	}
	
	/**
	 * Return a list of children that must be shown in the outline page.
	 * 
	 * @param element
	 * 	The parent element for which the displayed children are returned
	 */
	protected List<Element> getChildren(Element element) {
		try {
			Language language = element.language();
			if(language != null) {
			EclipseEditorExtension plugin = language.plugin(EclipseEditorExtension.class);
			ChameleonOutlineSelector outlineSelector = plugin.outlineSelector();
			return outlineSelector.outlineChildren(element);
			}
		} 
		catch (ChameleonProgrammerException e) {
			// simply stop processing if the declarations cannot be computed.
		}
		catch (ModelException e) {
			// simply stop processing if the declarations cannot be computed.
		}
		_tainted = true;
		return new ArrayList<Element>();
	}
	
	private boolean _tainted = false;
	
	public boolean isTainted() {
		if(! _tainted) {
			for(ChameleonOutlineTree tree: _children) {
				if(tree.isTainted()) {
					return true;
				}
			}
		}
		return _tainted;
	}
	
	public boolean isAllowed(Element element) {
		try {
			return element.language().plugin(EclipseEditorExtension.class).outlineSelector().isAllowed(element);
		} 
		catch (ChameleonProgrammerException e) {
			// simply stop processing if the declarations cannot be computed.
		}
		catch (ModelException e) {
			// simply stop processing if the declarations cannot be computed.
		}
		catch(NullPointerException e) {
			
		}
		return false;
	}
	

	/*
	 * check whether the given description matches one of the allowed ones
	 */
	private static boolean isAllowedDescription(Language language, String shortDescription) {
		//FIXME: I just set it to true.
		return true;
//		return allowedTreeElements.get(language).contains(shortDescription);
	}
	
	/**
	 * @return the children of this tree
	 */
	public List<ChameleonOutlineTree> getChildren() {
		return new ArrayList<ChameleonOutlineTree>(_children);
	}
	
	public boolean hasChildren() {
		return _children.size() > 0;
	}
	
	/**
	 * 
	 * @return the current element (= current node in the tree)
	 */
	public Element getElement() {
		return _node;
	}
	
	
	/**
	 * Recursively adds the children to this tree,  from the given elements children
	 * if the treeElement is not effective or doesn't has children, nothing happens
	 * @param treeRootElement
	 * 	The beginElement.
	 */
	public void composeTree() {
		composeTree(getChildren(getElement()));
	}

	protected void composeTree(List<Element> treeElementChildren) {
		for (Element element: treeElementChildren) {
			if(isAllowed(element)) {
				ChameleonOutlineTree child = new ChameleonOutlineTree(element);
				_children.add(child);
				child.composeTree();
			} else {
				composeTree(getChildren(element));
			}
		}
	}
	/**
	 * String representation of this tree
	 */
	@Override
   public String toString(){
		return "chameleon tree with node " + _node.toString();
	}
	
	
	/**
	 * adds a listener to this tree
	 * @param listener
	 * 	The given listener to be added
	 */
	public void addListener(IChameleonOutlineTreeListener listener) {
		this.listener = listener;
	}
	
	
	/**
	 * Removes a listener from this tree
	 * @param listener
	 */
	public void removeListener(IChameleonOutlineTreeListener listener) {
		if(this.listener.equals(listener)) {
			this.listener = NullChameleonOutlineTreeListener.getSoleInstance();
		}
	}
	
	/**
	 * Notifies any listeners that an element has been added to this tree
	 * @param added
	 * 		the added element
	 */
	protected void fireAdd(Element added) {
		System.out.println("ChameleonOutlineTree.fireAdd");
		listener.add(new ChameleonOutlineTreeEvent(added));
	}
	
	/**
	 * Notifies any listeners that an element has been removed from this tree
	 * @param removed
	 * 	The element removed from the tree
	 */
	protected void fireRemove(Element removed) {
		System.out.println("ChameleonOutlineTree.fireRemove");
		listener.remove(new ChameleonOutlineTreeEvent(removed));
	}
	
}
