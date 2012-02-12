/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.hierarchy;

import org.eclipse.core.resources.IFile;

import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.exception.ModelException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;

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
		ChameleonDocument elementDoc = projectNature.document(rootType);
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
			Type type = projectNature.documentOfFile(docFile).compilationUnit().descendants(Type.class).iterator().next();
			if(type != null){
				_typeCache = type;
				return new Object[]{new HierarchyTypeNode(type, projectNature, this)};
			}
		}
		// CHANGE show nothing if no type is found
		return new Object[]{new HierarchyTypeNode(_typeCache, projectNature, this)};
	}
	
	public HierarchyTreeNode getParent(){
		// the root item has no parent:
		return null;
	}
	
	
	
}
