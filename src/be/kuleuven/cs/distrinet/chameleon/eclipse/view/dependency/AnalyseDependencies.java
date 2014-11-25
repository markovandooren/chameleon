//package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;
//
//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.core.runtime.Status;
//import org.eclipse.core.runtime.jobs.Job;
//import org.eclipse.jface.action.Action;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.ui.IEditorInput;
//import org.eclipse.ui.IFileEditorInput;
//
//import be.kuleuven.cs.distrinet.chameleon.analysis.AnalysisOptions;
//import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyOptions;
//import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
//import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
//import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Projects;
//import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
//
//public class AnalyseDependencies extends Action {
//	
//	/**
//	 * 
//	 */
//	private final DependencyView _dependencyView;
//	
//	AnalysisOptions<?, ?> _options;
//
//	/**
//	 * @param dependencyView
//	 */
//	AnalyseDependencies(AnalysisOptions<?, ?> options, DependencyView dependencyView, IEditorInput input) {
//		_dependencyView = dependencyView;
//		_options = options;
//		_input = input;
//	}
//	
//	private IEditorInput _input;
//
//	@Override
//	public void run() {
//
//			Job job = new Job("Dependency Analysis") {
//				//FIXME Expand and make this reusable.
//				private Project currentProject() throws CoreException {
//					Project cproject = null;
//					IEditorInput editorInput = _input;
//					if(editorInput instanceof IFileEditorInput) {
//						IFile file = ((IFileEditorInput)editorInput).getFile();
//						IProject project = file.getProject();
//						if(project != null) {
//						  ChameleonProjectNature nature = Projects.chameleonNature(project);
//						  cproject = Projects.chameleonProject(project);
//						}
//					}
//					return cproject;
//				}
//				
//				@Override
//				protected IStatus run(IProgressMonitor monitor) {
//					try {
//						// Obtaining the document can trigger loading of a chameleon background project.
//						// We must do this inside the Job to avoid blocking the UI.
//						final Project project = currentProject();
//						final DependencyResult result = performAnalysis(project,monitor);
//						// If you want to update the UI
//						Display.getDefault().syncExec(new Runnable(){
//							@Override
//							public void run() {
//								if(result != null) {
//									AnalyseDependencies.this._dependencyView._viewer2.setInput(result);
//								}
//							}
//						});
//						return Status.OK_STATUS;
//					} catch(CoreException exc) {
//						exc.printStackTrace();
//						return Status.CANCEL_STATUS;
//					}
//				}
//			};
//			job.schedule(); 
//
//
//	}
//
//	@SuppressWarnings("rawtypes")
//	protected DependencyResult performAnalysis(Project project, IProgressMonitor monitor) {
//		return ((DependencyOptions)_options).analyze();
//	}
//	
//
//}