package org.aikodi.chameleon.eclipse.project;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

public class ProjectChangeListener implements IResourceChangeListener {

	/**
	 * FIXME: why is the project nature stored when it is only used
	 * to obtain the chameleon project. Might as well pass it directly.
	 * @param nature
	 */
	public ProjectChangeListener(ChameleonProjectNature nature) {
		_nature = nature;
	}

	private ChameleonProjectNature _nature;

	public ChameleonProjectNature nature() {
		return _nature;
	}

	@Override
   public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		try {
			delta.accept( new ChameleonResourceDeltaVisitor(nature()) {

				/**
				 * If the resource is a file, it is given to all file scanners, which
				 * will try to add the file. A file scanner will only add the file when
				 * it determines that it is responsible to that file.
				 */
				@Override
				public void handleAdded(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();
					Project chameleonProject = nature().chameleonProject();
					System.out.println("*** Chameleon project "+chameleonProject.getName()+" receives add resource event for: "+resource.getFullPath());
					if(resource instanceof IFile) {
						File file = resource.getRawLocation().makeAbsolute().toFile();
						if(file.isFile()) {
							System.out.println("*** Chameleon project "+chameleonProject.getName()+" receives add file event for: "+file.getAbsolutePath());
							chameleonProject.tryToAdd(file);
						}
					}
				}

				@Override
				public void handleChanged(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();
					if(resource instanceof IFile) {
						IFile ifile = (IFile) resource;
						Collection<ChameleonEditor> editors = ChameleonEditor.getActiveChameleonEditors();
						EclipseDocument doc = documentOf(delta);
						if(doc != null) {
							int flags = delta.getFlags();
							// FIXME: I don't like this "solution"
							// We must prevent a recursive call-back in case the file is edited
							// by the Chameleon editor.
							if(! ((flags & IResourceDelta.MARKERS) == IResourceDelta.MARKERS)) {
								boolean updateChameleonEditor = true;
								// Check if an editor is open.
								for(ChameleonEditor editor: editors) {
									if(editor.getDocument() == doc) {
										updateChameleonEditor = false;
										break;
									}
								}
								// Only update if no editor is open.
								if(updateChameleonEditor) {
									System.out.println("### UPDATING FILE IN MODEL ###");
									//	FIXME must refresh the content of the document.

									try {
										IPath location = ifile.getLocation();
										File file = null;
										if (location != null) {
											file = location.toFile();
										}
										byte[] bytes = new byte[(int) file.length()];
										BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
										stream.read(bytes);
										stream.close();
										doc.set(new String(bytes));
										_nature.updateModel(doc);
										nature().flushSourceCache();
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									System.out.println("### FOUND EDITOR FOR FILE ###"+ delta.getResource());
								}
							}

							
						} else {
								Document document = nature().chameleonDocumentOfFile(ifile);
								try {
									if(document != null) {
										document.loader().refresh();
									}
								} catch(InputException exc) {
									exc.printStackTrace();
								}
								nature().flushSourceCache();
						}
					}
				}

				/**
				 * If the resource is a file, it is given to all file scanners, which
				 * will try to add the file. A file scanner will only add the file when
				 * it determines that it is responsible to that file.
				 */
				@Override
				public void handleRemoved(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();
					if(resource instanceof IFile) {
						IPath rawLocation = resource.getRawLocation();
						if(rawLocation != null) {
							File file = rawLocation.makeAbsolute().toFile();
							System.out.println("### ADDING FILE TO MODEL :"+file.getAbsolutePath());
							nature().chameleonProject().tryToRemove(file);
						}
					}
				}

				//@Override
				//					public void handleRemoved(IResourceDelta delta) throws CoreException {
				//						System.out.println("### REMOVING FILE FROM MODEL ###");
				//						ChameleonDocument doc = documentOf(delta);
				//						if(doc != null) {
				////							FIXME: this message should go to the project directly, which should notify the nature.
				//							nature().removeDocument(doc);
				//							// FIXME: this should not be done by a separate call. Removing (and adding) a document should
				//							//        automatically flush the cache.
				//							nature().flushSourceCache();
				//						}
				//					}

			}
					);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
