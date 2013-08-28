package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import be.kuleuven.cs.distrinet.chameleon.analysis.AnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Projects;
import be.kuleuven.cs.distrinet.chameleon.util.action.TopDown;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;

public class AnalyseDependencies extends Action {
	
	/**
	 * 
	 */
	private final DependencyView _dependencyView;
	
	AnalysisOptions<?, ?> _options;

	/**
	 * @param dependencyView
	 */
	AnalyseDependencies(AnalysisOptions<?, ?> options, DependencyView dependencyView, IEditorInput input) {
		_dependencyView = dependencyView;
		_options = options;
		_input = input;
	}
	
	private IEditorInput _input;

	@Override
	public void run() {

			Job job = new Job("Dependency Analysis") {
				//FIXME Expand and make this reusable.
				private Project currentProject() throws CoreException {
					Project cproject = null;
					IEditorInput editorInput = _input;
					if(editorInput instanceof IFileEditorInput) {
						IFile file = ((IFileEditorInput)editorInput).getFile();
						IProject project = file.getProject();
						if(project != null) {
						  ChameleonProjectNature nature = Projects.chameleonNature(project);
						  cproject = Projects.chameleonProject(project);
//						  if(nature != null) {
//						  	document = nature.chameleonDocumentOfFile(file);
//						  }
						}
					}
					return cproject;
				}
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						// Obtaining the document can trigger loading of a chameleon background project.
						// We must do this inside the Job to avoid blocking the UI.
						final Project project = currentProject();
						final DependencyAnalysis<Declaration, Declaration> analysis = performAnalysis(project);				
						// If you want to update the UI
						Display.getDefault().syncExec(new Runnable(){
							@Override
							public void run() {
								DependencyResult result = analysis.result();
								if(result != null) {
									AnalyseDependencies.this._dependencyView._viewer.setInput(result);
								}
							}
						});
						return Status.OK_STATUS;
					} catch(CoreException exc) {
						exc.printStackTrace();
						return Status.CANCEL_STATUS;
					}
				}
			};
			job.schedule(); 


	}

	@SuppressWarnings("rawtypes")
	protected DependencyAnalysis<Declaration, Declaration> performAnalysis(Project project) {
		final DependencyAnalysis<Declaration, Declaration> analysis = (DependencyAnalysis)_options.createAnalysis();
		if(project != null) {
			TopDown<Element, Nothing> topDown = new TopDown<>(analysis);
			try {
//				project.applyToSource(topDown);
				for(Document document: project.sourceDocuments()) {
					topDown.perform(document);
				}
			} catch (InputException e) {
			}
		}
		return analysis;
	}
	

}