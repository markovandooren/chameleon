package chameleon.eclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;

import chameleon.core.document.Document;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Invalid;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.connector.EclipseEditorExtension;
import chameleon.eclipse.editors.EclipseDocument;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.eclipse.project.ChameleonResourceDeltaFileVisitor;
import chameleon.plugin.build.BuildException;
import chameleon.plugin.build.BuildProgressHelper;
import chameleon.plugin.build.Builder;
import chameleon.plugin.build.NullBuilder;
import chameleon.workspace.InputException;
import chameleon.workspace.View;

public class ChameleonBuilder extends IncrementalProjectBuilder {

	public static final String DESTDIR = "destdir";

	public ChameleonBuilder() {
		super();
	}
	
	public Builder builder() throws CoreException {
		 _nature = (ChameleonProjectNature)getProject().getNature(ChameleonProjectNature.NATURE);
		Builder result = nature().view().plugin(Builder.class);
		if(result == null) {
			return new NullBuilder();
		}
		return result;
	}
	
	private ChameleonProjectNature _nature;
	
	public ChameleonProjectNature nature() {
		return _nature;
	}
	
	public static final String BUILDER_ID = ChameleonEditorPlugin.PLUGIN_ID+".ChameleonBuilder";
	
	@Override
	protected IProject[] build(int kind, Map arguments, IProgressMonitor monitor) throws CoreException {
		if(kind == INCREMENTAL_BUILD || kind == AUTO_BUILD) {
			return incrementalBuild(arguments, monitor);
		} else {
		  return fullBuild(arguments, monitor);
		}
	}

	protected IProject[] fullBuild(Map arguments, IProgressMonitor monitor) throws CoreException {
		System.out.println("RUNNING FULL BUILD!");
		chameleonNature().clearMarkers();
		buildHelper(arguments, chameleonNature().compilationUnits(), monitor);
		return new IProject[0];
	}

	public ChameleonProjectNature chameleonNature() throws CoreException {
		return ((ChameleonProjectNature)getProject().getNature(ChameleonProjectNature.NATURE));
	}

	protected IProject[] incrementalBuild(Map arguments, IProgressMonitor monitor) throws CoreException {
		IResourceDelta delta = getDelta(getProject());
		incrementalBuild(delta, monitor);
		return new IProject[0];
	}

	public void incrementalBuild(IResourceDelta delta, final IProgressMonitor monitor) throws CoreException {
		System.out.println("RUNNING INCREMENTAL BUILD!");
		
		// First collect all the compilation units. If we pass them in one call to
		// the build method, the progress monitor works without writing additional code.
		final List<Document> cus = new ArrayList<Document>();
		delta.accept(new ChameleonResourceDeltaFileVisitor(chameleonNature()){
		
			@Override
			public void handleRemoved(IResourceDelta delta) throws CoreException {
				System.out.println("build: removed "+delta.getProjectRelativePath());
			}
		
			@Override
			public void handleChanged(IResourceDelta delta) throws CoreException {
				EclipseDocument doc = documentOf(delta);
				System.out.println("build: changed "+delta.getProjectRelativePath());
				if(doc != null) {
					Document cu = doc.document();
					cus.add(cu);
				}
			}

			@Override
			public void handleAdded(IResourceDelta delta) throws CoreException {
				System.out.println("build: added "+delta.getProjectRelativePath());
			}
		});

		buildHelper(null,cus,monitor);
	}

	private void checkForCancellation(IProgressMonitor monitor) throws CoreException {
		if(monitor.isCanceled()) {
			forgetLastBuiltState();
			getProject().deleteMarkers(IMarker.PROBLEM,true,IResource.DEPTH_INFINITE);
			throw new OperationCanceledException("The build was cancelled by the user.");
		}
	}
	
	private String buildName() {
		return "Building project";
	}

	protected void buildHelper(Map<String,String> arguments, List<Document> compilationUnits, final IProgressMonitor monitor) throws CoreException {
		File outputDir = null;
		if(arguments != null) {
		  String outputPath = arguments.get(DESTDIR);
		  if(outputPath != null) {
		  	chameleonNature().chameleonProject().absoluteFile(outputPath);
		  }
		}
		if(outputDir == null) {
		  ChameleonProjectNature nature = nature();
		  outputDir = projectRoot(nature);
		}
		// We only know how many CUs we are going to build after we have verified them. Therefore, the progress bar is only updated for building,
		// not for verifying. Not ideal, but currently no other way.
		List<Document> validCompilationUnits = new ArrayList<Document>();
		
		for(Document cu: compilationUnits) {
			IMarker[] problemMarkers = chameleonNature().document(cu).getFile().findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
			// Only start verification if there are no parser errors.
			if(problemMarkers.length == 0) {
				VerificationResult ver = checkVerificationErrors(cu);
				// Only build the document when there are no errors.
				if(Valid.create().equals(ver)) {
					validCompilationUnits.add(cu);
				}
			}
		}

		boolean released = true;
		try {
			Builder builder = builder();
			View view = chameleonNature().view();
			File output = view.language().plugin(EclipseEditorExtension.class).buildDirectory(outputDir);
			List<Document> projectCompilationUnits;
			try {
				projectCompilationUnits = view.sourceDocuments();
			} catch (InputException e1) {
				throw new CoreException(Status.CANCEL_STATUS);
			}
			if(builder != null) {
				int totalWork = builder.totalAmountOfWork(validCompilationUnits, projectCompilationUnits);
				System.out.println(totalWork);
				//monitor.setTaskName(buildName());

				monitor.beginTask(buildName(), totalWork);
				monitor.subTask(buildName());
				chameleonNature().acquire();
				released = false;
				chameleonNature().flushSourceCache();

				BuildProgressHelper helper = new BuildProgressHelper() {

					public void checkForCancellation()  {
						try {
							ChameleonBuilder.this.checkForCancellation(monitor);
						} catch (CoreException e) {
							// Wrap in RTE
							throw new RuntimeException(e);
						}
					}

					public void addWorked(int n) {
						monitor.worked(n);
					}
				};


				try {
					builder.build(validCompilationUnits, output, helper);
				}
				catch (BuildException e) {
					e.printStackTrace();
				} 
			}
		} 
		catch(InterruptedException exc) 
		{
			exc.printStackTrace();
		}
		finally {
			if(! released) {
				chameleonNature().release();
			}
			monitor.done();
		}
	}

	private File projectRoot(ChameleonProjectNature nature) {
		IFile projectFile = getProject().getFile(".project");
		IPath location = projectFile.getLocation();
		File file = null;
		if (location != null) {
			file = location.toFile().getParentFile();
		}
		return file;
	}
	
	public VerificationResult checkVerificationErrors(Document cu) throws CoreException {
		VerificationResult result = null;
		EclipseDocument document = chameleonNature().document(cu);
		try {
		  result = cu.verify();
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		if(result instanceof Invalid) {
		  for(BasicProblem problem: ((Invalid)result).problems()) {
			  document.markError(problem);
		  }
		}
		return result;
	}

}
