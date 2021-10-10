/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.eclipse.core.resources.IFile;

/**
 * This is just an ObjectWrapper to wrap round the root object
 * of the HierarchyTree, because otherwise the root object isn't shown.
 * 
 * @author Tim Vermeiren
 */
public class RootType implements HierarchyTreeNode {
	
	private Type _typeCache;
	private IFile docFile;
	private String rootTypeFqn;
	private ChameleonProjectNature projectNature;
	
	public RootType(Type rootType, ChameleonEditor editor) {
		this.rootTypeFqn = rootType.getFullyQualifiedName();
		projectNature = editor.getDocument().getProjectNature();
		EclipseDocument elementDoc = projectNature.document(rootType);
		if(elementDoc != null){
			this.docFile = elementDoc.getFile();
		}
	}
	
	public Object[] getChildren(){
		// try to lookup type with fqn (so the newest rootTypeElement in the model is found)
		try {
			TypeReference tref = projectNature.getModel().language(ObjectOrientedLanguage.class).createTypeReference(rootTypeFqn);
			tref.setUniParent(projectNature.getModel());
			Type type = tref.getElement();
			if(type!=null){
				_typeCache = type;
				return new Object[]{new HierarchyTypeNode(type, projectNature, this)};
			}
		} catch (ModelException e) {
			e.printStackTrace();
		}
		// if not succeeded, try to get the element of the docFile
		if(docFile!=null){
			Type type = projectNature.chameleonDocumentOfFile(docFile).lexical().descendants(Type.class).iterator().next();
			if(type != null){
				_typeCache = type;
				return new Object[]{new HierarchyTypeNode(type, projectNature, this)};
			}
		}
		// CHANGE show nothing if no type is found
		return new Object[]{new HierarchyTypeNode(_typeCache, projectNature, this)};
	}

	/**
	 * Returns null because the root element has no parent.
	 */
	@Override
   public HierarchyTreeNode getParent(){
		return null;
	}
	
	
	
}
