package chameleon.workspace;

import java.util.List;

import org.rejuse.association.Association;
import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.language.Language;
import chameleon.core.namespace.RootNamespace;
import chameleon.workspace.DocumentLoaderImpl.TunnelException;

public class View {
	
	public View(RootNamespace namespace, Language language) {
		setNamespace(namespace);
		setLanguage(language);
	}

	private SingleAssociation<View, Project> _projectLink = new SingleAssociation<View, Project>(this);
	
	public SingleAssociation<View, Project> projectLink() {
		return _projectLink;
	}

	public Project project() {
		return _projectLink.getOtherEnd();
	}

  public void setLanguage(Language language) {
  	if(language != null) {
  		_language.connectTo(language.viewLink());
  	}
  }

  public Language language() {
    return _language.getOtherEnd();
  }
  
  public SingleAssociation<View,Language> languageLink() {
  	return _language;
  }
	    
  private SingleAssociation<View,Language> _language = new SingleAssociation<View,Language>(this);

	public RootNamespace namespace() {
		return _namespace.getOtherEnd();
	}
	
	public void setNamespace(RootNamespace namespace) {
		if(namespace != null) {
			_namespace.connectTo(namespace.projectLink());
		} else {
			_namespace.connectTo(null);
		}
	}
	
  public SingleAssociation<View,RootNamespace> namespaceLink() {
  	return _namespace;
  }
	
	private SingleAssociation<View,RootNamespace> _namespace = new SingleAssociation<View,RootNamespace>(this);
	
	/**
	 * Add the given source loader to this project.
	 */
	/*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void addBinary(DocumentLoader loader) throws ProjectException {
		if(loader != null) {
			Association<? extends DocumentLoader, ? super View> projectLink = loader.viewLink();
			try {
				_binaryLoaders.add(projectLink);
			} catch(TunnelException exc) {
				// Rollback
				_binaryLoaders.remove(projectLink);
				throw (ProjectException)exc.getCause();
			}
		}
	}
	
	public void removeBinary(DocumentLoader loader) {
		_binaryLoaders.remove(loader.viewLink());
	}
	
	public List<DocumentLoader> binaryLoaders() {
		return _binaryLoaders.getOtherEnds();
	}

	private OrderedMultiAssociation<View, DocumentLoader> _binaryLoaders = new OrderedMultiAssociation<>(this);

	private OrderedMultiAssociation<View, DocumentLoader> _sourceLoaders = new OrderedMultiAssociation<>(this);

	/**
	 * Add the given source loader to this project.
	 */
	/*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void addSource(DocumentLoader loader) throws ProjectException {
		if(loader != null) {
			Association<? extends DocumentLoader, ? super View> projectLink = loader.viewLink();
			try {
				_sourceLoaders.add(projectLink);
			} catch(TunnelException exc) {
				// Rollback
				_sourceLoaders.remove(projectLink);
				throw (ProjectException)exc.getCause();
			}
		}
	}
	
	public void removeSource(DocumentLoader loader) {
		_sourceLoaders.remove(loader.viewLink());
	}
	
	public List<DocumentLoader> sources() {
		return _sourceLoaders.getOtherEnds();
	}

}
