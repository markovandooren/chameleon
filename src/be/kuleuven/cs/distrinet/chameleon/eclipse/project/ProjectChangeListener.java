package be.kuleuven.cs.distrinet.chameleon.eclipse.project;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;

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

		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				delta.accept( new ChameleonResourceDeltaVisitor(nature()) {

					/**
					 * If the resource is a file, it is given to all FileLoaders, which
					 * will try to add the file. A file loader will only add the file when
					 * it determines that it is responsible to that file.
					 */
					@Override
					public void handleAdded(IResourceDelta delta) throws CoreException {
						IResource resource = delta.getResource();
						if(resource instanceof IFile) {
							File file = resource.getRawLocation().makeAbsolute().toFile();
							System.out.println("### ADDING FILE TO MODEL :"+file.getAbsolutePath());
							if(file.isFile()) {
								nature().chameleonProject().tryToAdd(file);
							}
						}
					}

					@Override
					public void handleChanged(IResourceDelta delta) throws CoreException {
						boolean update = true;
						Collection<ChameleonEditor> editors = ChameleonEditor.getActiveChameleonEditors();
						EclipseDocument doc = documentOf(delta);
						if(doc != null) {
							// Check if an editor is open.
							for(ChameleonEditor editor: editors) {
								if(editor.getDocument() == doc) {
									update = false;
									break;
								}
							}
							// Only update if no editor is open.
							if(update) {
								System.out.println("### UPDATING FILE IN MODEL ###");
								//	FIXME must refresh the content of the document.
								IResource resource = delta.getResource();
								if(resource instanceof IFile) {
									try {
										IFile youfile = (IFile) resource;
										IPath location = youfile.getLocation();
										File file = null;
										if (location != null) {
											file = location.toFile();
										}
										byte[] bytes = new byte[(int) file.length()];
										BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
										stream.read(bytes);
										doc.set(new String(bytes));
										_nature.updateModel(doc);
										nature().flushSourceCache();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else {
								System.out.println("### FOUND EDITOR FOR FILE ###"+ delta.getResource());
							}
						}
					}

					/**
					 * If the resource is a file, it is given to all FileLoaders, which
					 * will try to add the file. A file loader will only add the file when
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
