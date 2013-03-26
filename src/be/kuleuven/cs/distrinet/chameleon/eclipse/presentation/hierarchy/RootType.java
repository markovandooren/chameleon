/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.hierarchy;

import org.eclipse.core.resources.IFile;

import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

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
			Type type = projectNature.chameleonDocumentOfFile(docFile).descendants(Type.class).iterator().next();
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
	public HierarchyTreeNode getParent(){
		return null;
	}
	
	
	
}
