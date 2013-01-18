package chameleon.eclipse.editors.reconciler;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

import chameleon.eclipse.editors.EclipseDocument;
import chameleon.eclipse.editors.ChameleonEditor;


/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A reconciler for a ChameleonDocument.
 * It processes Dirty regions and reconciles documents 
 *
 */
public class ChameleonReconciler extends AbstractChameleonReconciler{
//	/**The editor where this reconciler is used **/
//	private ChameleonEditor editor;
	/** The reconciling strategy. */
	private IChameleonReconcilingStrategy fStrategy;


	/**
	 * Creates a new ChameleonReconciler for the given editor, with given strategies & delay
	 * @param editor
	 * 		The editor where this chameleonReconciler is used
	 * @param strategy
	 * 		The strategy for the reconciler
	 * @param isIncremental
	 * 		This states whether the reconciler is incremental
	 * @param delay
	 * 		The delay for when the reconciler is called
	 */
	public ChameleonReconciler(/*ChameleonEditor editor,*/IChameleonReconcilingStrategy strategy, boolean isIncremental, int delay) {
		fStrategy= strategy;
		setIsIncrementalReconciler(isIncremental);
		setDelay(delay);
	}
	

	
	
	
	// called when complete dirty region queue is reconciled
	protected void reconciled() {
		try {
			getEditor().getDocument().getProjectNature().acquire();
			fStrategy.startReconciling();
			getEditor().getDocument().getProjectNature().release();
		} catch(InterruptedException exc) {
		}
	}
		
	/*
	 * @see IReconciler#getReconcilingStrategy(String)
	 */
	public IReconcilingStrategy getReconcilingStrategy(String contentType) {
		//Assert.isNotNull(contentType);
		return (IReconcilingStrategy) fStrategy;
	}
		
	/*
	 * @see AbstractReconciler#process(DirtyRegion)
	 */
	protected void process(ChameleonDirtyRegion dirtyRegion) {
		if(dirtyRegion != null){
			fStrategy.reconcile(dirtyRegion,dirtyRegion);
		}	
		else {
			IDocument document= getDocument();
			if (document != null)	
				fStrategy.reconcile(new Region(0, document.getLength()));
		}
	}
	
	/*
	 * @see AbstractReconciler#reconcilerDocumentChanged(IDocument)
	 */
	protected void reconcilerDocumentChanged(EclipseDocument document) {
		fStrategy.setDocument(document);
	}	
		
	/*
	 * @see AbstractReconciler#setProgressMonitor(IProgressMonitor)
	 */
	public void setProgressMonitor(IProgressMonitor monitor) {
		super.setProgressMonitor(monitor);
		if (fStrategy instanceof IReconcilingStrategyExtension) {
			IReconcilingStrategyExtension extension= (IReconcilingStrategyExtension) fStrategy;
			extension.setProgressMonitor(monitor);
		}
	}
		
	/*
	 * @see AbstractReconciler#initialProcess()
	 */
	/*
	 * This also sets up the folding process.
	 */
	protected void initialProcess() {
		if (fStrategy instanceof IReconcilingStrategyExtension) {
			IReconcilingStrategyExtension extension= (IReconcilingStrategyExtension) fStrategy;
			extension.initialReconcile();
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see chameleonEditor.editors.reconciler.AbstractChameleonReconciler#docAboutToBeChanged()
	 */
    protected void docAboutToBeChanged(){
    	fStrategy.initReconciling();
    }

    public ChameleonEditor getEditor(){
    	return ((ChameleonReconcilingStrategy)fStrategy).getConfiguration().getChameleonEditor();
    }








	
}

