package chameleon.eclipse.project;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import chameleon.core.compilationunit.Document;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.namespace.Namespace;
import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.LanguageMgt;
import chameleon.eclipse.builder.ChameleonBuilder;
import chameleon.eclipse.connector.EclipseEditorInputProcessor;
import chameleon.eclipse.connector.EclipseSourceManager;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.eclipse.presentation.PresentationModel;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.InputProcessor;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;
import chameleon.input.SourceManager;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx
 * @author Tim Vermeiren
 * @author Marko van Dooren
 * 
 * Defines the nature for ChameleonProjects, it contains the elements in the model 
 * of the nature, the language of the project, and knows the project it is created for.
 */
public class ChameleonProjectNature implements IProjectNature {
	public ChameleonProjectNature() {
		_documents=new ArrayList<ChameleonDocument>();
	}
	
	//the project this natures resides
	private IProject _project;
	
	//the language of this nature
	private Language _language;
	
	public static final String CHAMELEON_PROJECT_FILE_EXTENSION = "CHAMPROJECT";

	//The elements in the model of this nature
	private ArrayList<ChameleonDocument> _documents;
	
	public static final String NATURE = ChameleonEditorPlugin.PLUGIN_ID+".ChameleonNature";
	
	private PresentationModel _presentationModel;
	
	public PresentationModel presentationModel() {
		return LanguageMgt.getInstance().getPresentationModel(_language.name());
//		PresentationModel result = _presentationModel;
//		if(result == null) {
//      String filename = "/xml/presentation.xml";
//      InputStream stream = language().getClass().getClassLoader().getResourceAsStream(filename);
//      result = new PresentationModel(language().name(), stream);
//		}
//		return result;
	}

	public void init(Language language){
//		setMetaModelFactory(LanguageMgt.getInstance().getModelFactory(language));
		if(language == null) {
			throw new ChameleonProgrammerException("Cannot set the language of a Chameleon project nature to null.");
		}
		this._language = language;
		language.setPlugin(SourceManager.class, new EclipseSourceManager(this));
		language.addProcessor(InputProcessor.class, new EclipseEditorInputProcessor(this));
	}
	
	/**
	 * Configures this nature. to be called after initialisation.
	 */
	public void configure() throws CoreException {
		System.out.println("Configuring Chameleon project nature.");
		//later om de builders in te steken (compiler?)
		addBuilder(getProject(), ChameleonBuilder.BUILDER_ID);
		new Job("Chameleon Project Build"){
		
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					getProject().build(ChameleonBuilder.FULL_BUILD, ChameleonBuilder.BUILDER_ID, null, monitor);
					return Status.OK_STATUS;
				}catch(CoreException exc) {
					return Status.CANCEL_STATUS;
				}
			}
		}.schedule();
	}
	
  private void addBuilder(IProject project, String id) throws CoreException {
    IProjectDescription desc = project.getDescription();
    ICommand[] commands = desc.getBuildSpec();
    for (int i = 0; i < commands.length; ++i) {
       if (commands[i].getBuilderName().equals(id)) {
          return;
       }
    }
    //add builder to project
    ICommand command = desc.newCommand();
    command.setBuilderName(id);
    ICommand[] nc = new ICommand[commands.length + 1];
    // Add it before other builders.
    System.arraycopy(commands, 0, nc, 1, commands.length);
    nc[0] = command;
    desc.setBuildSpec(nc);
    project.setDescription(desc, null);
 }


	/**
	 * Deconfigures this nature. To be called before object destruction.
	 */
	public void deconfigure() throws CoreException {
		getProject().getWorkspace().removeResourceChangeListener(_projectListener);
	}

	/**
	 * returns the project where this nature is linked with
	 */
	public IProject getProject() {
		return _project;
	}

	/**
	 * A new project for this nature is set.
	 * All documents (if any) are loaded and the according model is built.
	 */
	public void setProject(IProject project) {
		if(project != _project) {
			IProject old = _project;
			this._project = project;
			if(project != null) {
				try {
					BufferedReader f = new BufferedReader(new FileReader(new File(project.getLocation()+"/."+CHAMELEON_PROJECT_FILE_EXTENSION)));
					String lang = f.readLine();
					f.close();
					Language language = LanguageMgt.getInstance().createLanguage(lang);
					init(language);
					loadDocuments();
					_projectListener = new ProjectChangeListener(this);
					getProject().getWorkspace().addResourceChangeListener(_projectListener, IResourceChangeEvent.POST_CHANGE);
					//			updateAllModels();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("No initfile...");
				}
			} else {
				old.getWorkspace().removeResourceChangeListener(_projectListener);
			}
		}
	}
	
	private IResourceChangeListener _projectListener;
	
	public class ProjectChangeListener implements IResourceChangeListener {
		
		public ProjectChangeListener(ChameleonProjectNature nature) {
			_nature = nature;
		}
		
		private ChameleonProjectNature _nature;
		
		public ChameleonProjectNature nature() {
			return _nature;
		}

		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				//nature().getProject().build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
				delta.accept( new ChameleonResourceDeltaVisitor(nature()) {

					@Override
					public void handleAdded(IResourceDelta delta) throws CoreException {
						System.out.println("### ADDING FILE TO MODEL ###");
						IResource resource = delta.getResource();
						if(resource instanceof IFile) {
							nature().addResourceToModel(resource);
							nature().flushProjectCache();
						}
					}

					@Override
					public void handleChanged(IResourceDelta delta) throws CoreException {
						boolean update = true;
						Collection<ChameleonEditor> editors = ChameleonEditor.getActiveChameleonEditors();
						ChameleonDocument doc = documentOf(delta);
						if(doc != null) {
							for(ChameleonEditor editor: editors) {
								if(editor.getDocument() == doc) {
									update = false;
									break;
								}
							}
							if(update) {
								System.out.println("### UPDATING FILE IN MODEL ###");
								//							xx must refresh the content of the document.
								IResource resource = delta.getResource();
								if(resource instanceof IFile) {
									try {
										IFile youfile = (IFile) resource;
										IPath location = youfile.getLocation();
										File file = null;
										if (location != null) {
											file = location.toFile();
										}
										;
										byte[] bytes = new byte[(int) file.length()];
										BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
										stream.read(bytes);
										doc.set(new String(bytes));
										updateModel(doc);
										nature().flushProjectCache();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else {
								System.out.println("### FOUND EDITOR FOR FILE ###"+ delta.getResource());
							}
						}
					}

					@Override
					public void handleRemoved(IResourceDelta delta) throws CoreException {
						System.out.println("### REMOVING FILE FROM MODEL ###");
						ChameleonDocument doc = documentOf(delta);
						if(doc != null) {
//							doc.compilationUnit().disconnect();
							nature().removeDocument(doc);
							nature().flushProjectCache();
						}
					}
					
				}
				);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see chameleonEditor.editors.IChameleonDocument#getMetaModelFactory()
	 */
	public ModelFactory modelFactory() {
		return language().plugin(ModelFactory.class);
	}


	
	/**
	 * Creates a new model for this document. If another model was set, anything of it is removed
	 * and is replaced by the new model.
	 */
	public void updateAllModels() {

			try {
				for (ChameleonDocument doc : _documents) {
					updateModel(doc);
				}
//				if (Config.DEBUG) {
//					// DEBUG: This prints the size of the entire model so memory problems
//					// can be detected. If there is a problem,
//					// the number will keep getting bigger while the size of the source
//					// files remains constant.
//					ChameleonReconcilingStrategy.showSize(language().defaultNamespace());
//					for (Iterator<ChameleonDocument> iter = _documents.iterator(); iter.hasNext();) {
//						ChameleonDocument element = iter.next();
//						System.out.println(element);
//					}
//				}
			} catch (Exception exc) {
				exc.printStackTrace();

			}
		
	}

	/**
	 * Removes the old file markers and document positions, and asks the model factory
	 * to replace the contents of the compilation unit associated with the document.
	 * @param doc
	 */
	public void updateModel(ChameleonDocument doc) {
		
		// Removing the file markers is already done someplace else, and it gives a ResourceException
		// when this method is ran via the project listener of this nature.
		
//		try {
//			IFile file = doc.getFile();
//			if(documentOfFile(file) != null) {
//			  file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
//			}
//			// getDocument().removeParseErrors();
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
		if(doc != null) {
			doc.dumpPositions();
			try {
				modelFactory().addToModel(doc.get(), doc.chameleonDocument());
			} catch (ParseException e) {
				// FIXME Can we ignore this exception? Normally, the parse error markers should have been set.
				e.printStackTrace();
			} finally {
				flushProjectCache();
			}
		}
	}

	/**
	 * Clear the list of documents, and loads all the project files with a project extension into the model.
	 */
	public void loadDocuments(){
		_documents.clear();
		IResource[] resources;
		try {
			resources = getProject().members();
			for (int i = 0; i < resources.length; i++) {
				IResource resource = resources[i];
				addResourceToModel(resource);
			}
		
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Check whether the given resource is the Chameleon project description file.
	 */
	protected boolean isChameleonProjectFile(IResource resource) {
		String ext = extension(resource);
		return ext != null && ext.equals(CHAMELEON_PROJECT_FILE_EXTENSION);
	}

	/**
	 * Check whether the given resource is the Eclipse project description file.
	 */
	protected boolean isEclipseProjectFile(IResource resource) {
		String ext = extension(resource);
		return ext != null && ext.equals("project");
	}

	protected String extension(IResource resource) {
		return resource.getFullPath().getFileExtension();
	}

	/**
	 * Adds another resource to the model. This can be either a file or a folder
	 * @param resource
	 */
	public void addResourceToModel(IResource resource) {
		List<String> extensions = LanguageMgt.getInstance().extensions(language());
		if (resource instanceof IFile)  {
			if(!(isEclipseProjectFile(resource) || (isChameleonProjectFile(resource)))) {
				IPath fullPath = resource.getFullPath();
				String fileExtension = fullPath.getFileExtension();
				if(extensions.contains(fileExtension)) {
					System.out.println("ADDING :: "+resource.getName());
					addToModel(new ChameleonDocument(this,(IFile)resource,resource.getFullPath()));
				}
			}
		}
		if (resource instanceof IFolder) {
			IFolder folder = (IFolder) resource ;
			IResource[] resources = null;
			try {
				resources = folder.members();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < resources.length; i++) {
				IResource folderResource = resources[i];
				addResourceToModel(folderResource);
			}
			
		}
	}


	


	/* (non-Javadoc)
	 * @see chameleonEditor.editors.IChameleonDocument#getModel()
	 */
	public Namespace getModel(){
		return language().defaultNamespace();
	}

	/**
	 * Adds an extra chameleonDocument to this nature
	 * @param document
	 */
	public void addToModel(ChameleonDocument document) {
		//FIXME: why do we remove the 'same' document and add the new one if
		//       the document was already in the model?
		ChameleonDocument same = null;
		for (ChameleonDocument element: _documents) {
			if (element.isSameDocument(document)) {
				same = element;
			}
		}
		if (same!=null) {
			removeDocument(same);
		}
		_documents.add(document);
		updateModel(document);
	}

	public List<ChameleonDocument> documents(){
		return new ArrayList<ChameleonDocument>(_documents);
	}
	
	/**
	 * Return the Chameleon/Eclipse document for the compilation unit of the
	 * given element.
	 * 
	 * @param element
	 * @return
	 */
	public ChameleonDocument document(Element element) {
		if(element != null) {
			Document cu = element.nearestAncestorOrSelf(Document.class);
			for(ChameleonDocument doc : _documents) {
				if(doc.chameleonDocument().equals(cu)) {
					return doc;
				}
			}
		}
		return null;
	}

	/**
	 * Remove the given document from the project. The document is removed from the
	 * list of documents in the project, and the compilation unit of the document
	 * is disconnected from the project.
	 * 
	 * If the given document is null, nothing happens.
	 */
 /*@
   @ post ! documents().contains(document);
   @ post document != null ==> document.compilationUnit().disconnected();
   @*/
	public void removeDocument(ChameleonDocument document){
		if(document != null) {
			_documents.remove(document);
			document.chameleonDocument().disconnect();
		}
	}

	public Language language() {
		return _language;
	}

	public void addModelElement(ChameleonDocument document, Element parent) {
		_documents.add(document);
		try {
			modelFactory().addToModel(document.get(), document.chameleonDocument());
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}
	
	public ChameleonDocument documentOfPath(IPath path) {
		ChameleonDocument result = null;
		for(ChameleonDocument doc:_documents) {
			if(doc.path().equals(path)) {
				result = doc;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the document with the given file
	 * @param file must be effective
	 * @return returns null if no appropriate document found.
	 */
	public ChameleonDocument documentOfFile(IFile file){
		if(file == null) {
			return null;
		}
		for(ChameleonDocument doc : _documents){
			if(file.equals(doc.getFile())){
				return doc;
			}
		}
		return null;
	}

	/**
	 * Static method to retrieve the language of the current editor. This
	 * is required because the views are global, and not connected to a particular
	 * project.
	 * @return
	 */
	public static Language getCurrentLanguage(){
		ChameleonEditor editor = ChameleonEditor.getCurrentActiveEditor();
		if(editor!=null){
			ChameleonDocument doc = editor.getDocument();
			if(doc!= null) {
				return doc.language();
			}
		}
		return null;
	}

	public List<Document> compilationUnits() {
		ArrayList<Document> result = new ArrayList<Document>();
		for(ChameleonDocument document: documents()) {
			result.add(document.chameleonDocument());
		}
		return result;
	}

	public void flushProjectCache() {
		for(Document compilationUnit: compilationUnits()) {
			compilationUnit.flushCache();
		}
		language().flushCache();
	}
	
	public void acquire() throws InterruptedException {
		_semaphore.acquire();
	}

	public void release() {
		_semaphore.release();
	}
	
	private Semaphore _semaphore = new Semaphore(1);
	
	public void clearMarkers() throws CoreException {
		for(ChameleonDocument document: _documents) {
			IFile file = document.getFile();
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		}
	}
}
