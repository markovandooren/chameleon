package chameleon.eclipse.editors.reconciler;


import java.util.HashMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonEditor;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A class that handles the representation of the ChameleonDocument. It can process 
 * the dirty regions and is responsible for example syntax highlighting
 */
public class ChameleonPresentationReconciler extends AbstractChameleonReconciler implements IPresentationReconciler {
	//The strategy for this presentation reconciler
	private ChameleonReconcilingStrategy _strategy;
	//	The editor where this presentation reconciler is used
	//private ChameleonEditor editor;
	private ChameleonEditor _editor;
	
	/**
	 * Creation of a new PresentationReconciler for the given document with given strategy
	 * @param chamEditor
	 * 	The editor where this PresentationReconciler is used
	 * @param chameleonReconcilingStrategy
	 * 	The strategy that is being used
	 * @param incremental
	 * 	Denotes whether this  PresentationReconciler is incremental
	 * @param delay
	 * 	Denotes the delay for this PresentationReconciler
	 */
	public ChameleonPresentationReconciler(ChameleonEditor chamEditor,ChameleonReconcilingStrategy chameleonReconcilingStrategy) {
	       _strategy = chameleonReconcilingStrategy;
	       _editor = chamEditor;
	} 

	/**
	 * @return null;
	 */
	public IPresentationDamager getDamager(String contentType) {
		return null;
	}

	/**
	 * @return null;
	 */
	public IPresentationRepairer getRepairer(String contentType) {
		return null;
	}

	private boolean presenting =false;
//	private IPresentationDamager damager;
//	private IPresentationRepairer repairer;
//	//private HashMap<String, IPresentationDamager> fDamagers;
//	//private HashMap<String, IPresentationRepairer> fRepairers;
//	
	/**
	 * Colors the document where this presentationReconciler is used
	 * Folding is also done.
	 */
	public void doPresentation(){
		if (presenting) return;
		presenting = true;
		
		doFolding();
		doColoring();
//    doVerificationErrors();
		
		presenting = false;
	}
	
//	private void doVerificationErrors() {
//		VerificationResult result = null;
//		try {
//		  CompilationUnit cu = getDocument().compilationUnit();
//		  result = cu.verify();
//		} catch(Exception exc) {
//			exc.printStackTrace();
//		}
//		if(result instanceof Invalid) {
//		  for(BasicProblem problem: ((Invalid)result).problems()) {
//			  markError(problem);
//		  }
//		}
//		
//	}
	
	@Override
	public ChameleonDocument getDocument() {
		return (ChameleonDocument)super.getDocument();
	}
	
	//FIXME move this to a sensible place.
	public static HashMap<String, Object> createProblemMarkerMap(String message) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(IMarker.SEVERITY,IMarker.SEVERITY_ERROR);
		attributes.put(IMarker.MESSAGE, message);
		return attributes;
	}

	//Do the coloring
	private void doColoring(){
		try {
			((ChameleonDocument)this._document).doPresentation(_textViewer);
		} catch (ClassCastException e){
			e.printStackTrace();
		}
		 catch (NullPointerException npe){
			 npe.printStackTrace();
			System.out.println("assuming closure");
		 }
	}
	
	//Do the folding
	private void doFolding(){
		_editor.updateFoldingStructure();
		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see chameleonEditor.editors.reconciler.AbstractChameleonReconciler#reconcilerDocumentChanged(org.eclipse.jface.text.IDocument)
	 */
	protected void reconcilerDocumentChanged(ChameleonDocument newDocument) {
	//	System.out.println("ChameleonPresentationReconciler.reconcilerDocumentChanged is opgeroepen");
		_strategy.setDocument(newDocument);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.text.reconciler.IReconciler#getReconcilingStrategy(java.lang.String)
	 */
	public IReconcilingStrategy getReconcilingStrategy(String contentType) {
		return (IReconcilingStrategy) _strategy;
	}

	/**
	 * @return the editor for this PresentationReconciler
	 */
	public ChameleonEditor getEditor() {
		return _strategy.getConfiguration().getChameleonEditor();
	}

	/*
	 *  (non-Javadoc)
	 * @see chameleonEditor.editors.reconciler.AbstractChameleonReconciler#initialProcess()
	 */
	protected void initialProcess(){

		doPresentation();

		//FIXME als er elementen gefold zijn, en er wordt tekst bijgetypt, dan worden alle foldings ongedaan gemaakt
		getEditor().fold(((ChameleonDocument)_document).getFoldedElementsFromModel());

		

	}

	/**
	 * recoloring is done for the document this presenatationreconciler is working for
	 *
	 */
	public void reColor() {
		try {
			((ChameleonDocument)this._document).doPresentation(_textViewer);
		} catch (ClassCastException e){}
	}
	
	/**
	 * returns the text viewer
	 */
	public ITextViewer getTextViewer(){
		return _textViewer;
	}

	@Override
	/**
	 * does nothing at all
	 */
	protected void process(ChameleonDirtyRegion dirtyRegion) {
			
	}









	


}