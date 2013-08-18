package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.ui.progress.UIJob;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Projects;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.action.TopDown;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

import com.google.common.base.Function;

public class AnalyseDependencies extends Action {
	
	/**
	 * 
	 */
	private final DependencyView _dependencyView;

	/**
	 * @param dependencyView
	 */
	AnalyseDependencies(DependencyView dependencyView, IEditorInput input) {
		_dependencyView = dependencyView;
		_input = input;
	}
	
	private IEditorInput _input;

	@Override
	public void run() {

			Job job = new Job("Dependency Analysis") {
				//FIXME Expand and make this reusable.
				private Document currentDocument() throws CoreException {
					Document document = null;
					IEditorInput editorInput = _input;
					if(editorInput instanceof IFileEditorInput) {
						IFile file = ((IFileEditorInput)editorInput).getFile();
						IProject project = file.getProject();
						if(project != null) {
						  ChameleonProjectNature nature = Projects.chameleonNature(project);
						  if(nature != null) {
						  	document = nature.chameleonDocumentOfFile(file);
						  }
						}
					}
					return document;
				}
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						// Obtaining the document can trigger loading of a chameleon background project.
						// We must do this inside the Job to avoid blocking the UI.
						final Document document = currentDocument();
						final DependencyAnalysis<Type, Type> analysis = performAnalysis(document);				
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
	protected DependencyAnalysis<Type, Type> performAnalysis(Document document) {
		Function<Type,Type> identity = new Function<Type, Type>() {
			@Override
			public Type apply(Type type) {
				return type;
			}
		};
		
		//True sourceDeclarationPredicate = new True();
		final List sourceListHack = new ArrayList();
		final List targetListHack = new ArrayList();
		Display.getDefault().syncExec(new Runnable(){
		
			@Override
			public void run() {
				sourceListHack.add(_dependencyView.sourcePredicate());
				targetListHack.add(_dependencyView.targetPredicate());
			}
		});
		UniversalPredicate sourceDeclarationPredicate = (UniversalPredicate) sourceListHack.get(0);
		UniversalPredicate targetDeclarationPredicate = (UniversalPredicate) targetListHack.get(0);
		final DependencyAnalysis<Type, Type> analysis = 
				new DependencyAnalysis<Type,Type>(
						Type.class, sourceDeclarationPredicate, 
						new True(), 
						Type.class, identity, targetDeclarationPredicate, 
						new True());
		if(document != null) {
			TopDown<Element, Nothing> topDown = new TopDown<>(analysis);
			topDown.perform(document);

		}
		return analysis;
	}
	

}