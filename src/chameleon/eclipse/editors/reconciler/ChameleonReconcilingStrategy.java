package chameleon.eclipse.editors.reconciler;


import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;

import chameleon.core.Config;
import chameleon.core.document.Document;
import chameleon.core.element.Element;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.EclipseDocument;
import chameleon.eclipse.editors.ChameleonSourceViewerConfiguration;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.ModelFactory;
import chameleon.workspace.View;


/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * @author Marko van Dooren
 * 
 * This class does the actual reconciling. it starts & initiates the reconciling 
 */
public class ChameleonReconcilingStrategy implements IChameleonReconcilingStrategy{
	
	//the configuration where this ChameleonReconcilingStrategy is needed
	private ChameleonSourceViewerConfiguration _configuration;

	/**
	 * Constructs a new ChameleonReconcilingStrategy.
	 * @param configuration 
	 * 	the configuration where this ChameleonReconcilingStrategy is needed
	 *
	 */
	public ChameleonReconcilingStrategy(ChameleonSourceViewerConfiguration configuration){
		_alreadyInit = false;
		_configuration = configuration;
	}
	
	/**
	 * sets the document for this ChameleonReconcilingStrategy to the given document
	 * @param document
	 * 		The new document
	 */
	public void setDocument(EclipseDocument document){	
		_document = document;
	}
	
	/**
	 * @return the document for this ChameleonReconcilingStrategy
	 */
	public EclipseDocument getDocument(){
		return _document;
	}
	
	// Check whether reconciling is initiated
	private boolean _alreadyInit;
	// The document to which this ReconcilingStrategy applies
	private EclipseDocument _document;
	// List containing clones of positions
	private ArrayList<ClonedChameleonPosition> _clonedPositions = new ArrayList<ClonedChameleonPosition>();
	// List containing all the dirtyPositions in this document
	private ArrayList<ClonedChameleonPosition> _dirtyPositions = new ArrayList<ClonedChameleonPosition>();
	// States whether the whole document is dirty
	private boolean _wholeDocumentDirty = false;

	public boolean isWholeDocumentDirty() {
		return _wholeDocumentDirty;
	}
	
	private void setWholeDocumentDirty(boolean dirty) {
		_wholeDocumentDirty = dirty;
	}
	
	private ArrayList<ActionListener> modelListeners = new ArrayList<ActionListener>();
	
	// initialize reconciling
	// positions are cloned
	/**
	 * initializes reconciling, during which the positions are saved
	 */
	public void initReconciling(){
		if(_alreadyInit == false){
			clonePositions();
			_alreadyInit = true;
		}
	}
	
	/*
	 * clones all the positions
	 */
	private void clonePositions(){
		Position[] positions = null;
		try {
			positions = _document.getPositions(EclipseEditorTag.CHAMELEON_CATEGORY);
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
		}
				
		EclipseEditorTag eP = null;
		for(int i=0; i<positions.length; i++){
			eP = (EclipseEditorTag)positions[i];
			_clonedPositions.add(cloneDecorator(eP));
		}
	}
	
	private ClonedChameleonPosition cloneDecorator(EclipseEditorTag dec) {
		return new ClonedChameleonPosition(dec.getOffset(),dec.getLength(),dec.getElement(),dec.getName());
	}
	
	// start reconciling
	/**
	 * This processes the document. 
	 * If the whole document is dirty, it tries to process the whole document at once
	 * else it tries to process the smallest altered positions
	 */
	public void startReconciling(){
		try {
		_clonedPositions.clear();
		if(isWholeDocumentDirty()){
			try{
				parseWholeDocument(_document);
			}catch(Exception err){
				err.printStackTrace();
			}
		}
		else{
			removeCoveredPositions();
			
			// Keep track of the successfully reparsed elements. If there are multiple errors
			// on the same element, we don't want to reparse it multiple times.
			Set<Element> successfullyReparsed = new HashSet<Element>();
			ModelFactory factory = view().language().plugin(ModelFactory.class);
			try{
				for(ClonedChameleonPosition position : _dirtyPositions) {
					// A. Remove the position from the document
					// TODO This should be done automatically when the element is removed from the model.
//					getDocument().removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,position);

					// B. reparse element
					Element element = position.getElement();
					if(! successfullyReparsed.contains(element)) {
						factory.refresh(element);
						successfullyReparsed.add(element);
					}
				}
			} catch(Exception e){
				reparseEntireDocument();
			}

		}
		if(! getDocument().getParseErrors().isEmpty()) {
			reparseEntireDocument();
		}
		
		_dirtyPositions.clear();
		clonePositions();
		
		} finally {
			nature().flushSourceCache();
			fireModelUpdated();
		}
	}

	private View view() {
		EclipseDocument document = getDocument();
		if(document != null) {
			Document chameleonDocument = document.document();
			if(chameleonDocument != null) {
				return chameleonDocument.view();
			}
		}
		return null;
	}
	
	public ChameleonProjectNature nature() {
		return getDocument().getProjectNature();
	}

	/**
	 * parses the whole document 
	 */
	private void parseWholeDocument(IDocument document) throws Exception{
		getDocument().reParse();
	}

	private void reparseEntireDocument() { 
		try{
			//FIXME This does not flush the cache.
			for(ClonedChameleonPosition pos: _dirtyPositions) {
				pos.getElement().disconnect();
				getDocument().removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,pos);				
			}
		}catch(Exception err){
		}
		try {
			parseWholeDocument(_document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addUpdateListener(ActionListener newListener){
		modelListeners.add(newListener);
	}
	
	private void fireModelUpdated() {
		for (ActionListener listener: modelListeners) {
			listener.actionPerformed(null);
		}
	}

//	/*
//	 * 
//	 */
//	private void removeEmbeddedPos(ClonedChameleonPosition pos, Position[] positions, boolean[] status){
//		try{
//			ClonedChameleonPosition posB = null;
//			for(int i=0; i<positions.length; i++){
//				posB = (ClonedChameleonPosition)positions[i];
//				if(posB.getOffset()>pos.getOffset() && 
//				   (posB.getOffset()+posB.getLength())<(pos.getOffset()+pos.getLength())){
//					getDocument().removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,posB);				
//					for(int n=0; n<status.length; n++){
//						if((dirtyPositions.get(n)) == posB){
//							status[n] = false;
//						}
//					}
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	/*
	 * 
	 */
	private void removeCoveredPositions(){
		ClonedChameleonPosition posA, posB;
		for(int i=0; i<_dirtyPositions.size(); i++){
			posA = _dirtyPositions.get(i); 
			for(int t=0; t<_dirtyPositions.size(); t++){
				posB = _dirtyPositions.get(t);
				if(posB.getOffset()>posA.getOffset() && 
				   (posB.getOffset()+posB.getLength())<(posA.getOffset()+posA.getLength())){
					_dirtyPositions.remove(posB);
				}
			}
		}
	}
	
	
	/**
	 * Search in the list of cloned positions for the smallest decorator covering the dirty region
	 * 
	 * @param dR
	 * @return
	 */
	// reconstruct positions (via dirty region)
	private void adaptClonedPositions(ChameleonDirtyRegion dR){
		// dirty region of type _INSERT
		ClonedChameleonPosition eP;
		if(dR.getType()==ChameleonDirtyRegion.INSERT){
			for(int j=0; j<_clonedPositions.size(); j++){
				eP = _clonedPositions.get(j);
				// offset dirty region before position (adjust offset position)
				if(dR.getOffset()<=eP.offset){
					eP.setOffset(eP.getOffset()+dR.getLength());
				}
				// offset dirty region after position (do nothing)
				else if(dR.getOffset()>(eP.getOffset()+eP.getLength()-1)){
					// do nothing
				}
				// offset dirty region in position (adjust length position)
				else{
					eP.setLength(eP.getLength()+dR.getLength());
				}
			}				
		}
		// dirty region of type _REMOVE
		else{
			int beginOffsetDR = dR.getOffset();
			int endOffsetDR = dR.getOffset()+dR.getLength()-1;
			int beginOffsetPos;
			int endOffsetPos;
			for(int j=0; j<_clonedPositions.size(); j++){
				eP = _clonedPositions.get(j);
				beginOffsetPos = eP.getOffset();
				endOffsetPos = eP.getOffset()+eP.getLength()-1;
				// dirty region completely in position (adjust length position)
				if(beginOffsetDR >= beginOffsetPos && endOffsetDR <= endOffsetPos){
					eP.setLength(eP.getLength()-dR.getLength());
				}
				// dirty region completely before position (adjust offset position)
				else if(endOffsetDR < beginOffsetPos){
					eP.setOffset(eP.getOffset()-dR.getLength());
				}
				// dirty region completely covers position (set length position zero)
				else if(beginOffsetDR <= beginOffsetPos && endOffsetDR >= endOffsetPos){
					eP.setLength(0);
				}
				// dirty region paritally covers position, only first part position (adjust offset and length position)
				else if(beginOffsetDR < beginOffsetPos && endOffsetDR < endOffsetPos &&  endOffsetDR >= beginOffsetPos){
					eP.setOffset(dR.getOffset());
					eP.setLength(endOffsetPos-endOffsetDR);
				}
				// dirty region partially covers position, only last part position (adjust length position)
				else if(beginOffsetDR > beginOffsetPos && endOffsetDR > endOffsetPos &&  beginOffsetDR <= endOffsetPos){
					eP.setLength(beginOffsetDR - beginOffsetPos);
				}
			}
		}
	}
	
	
	
	
	/**
	 * Activates incremental reconciling of the specified dirty region.
	 * As a dirty region might span multiple content types, the segment of the
	 * dirty region which should be investigated is also provided to this 
	 * reconciling strategy. The given regions refer to the document passed into
	 * the most recent call of {@link #setDocument(IDocument)}.
	 *
	 * @param dirtyRegion the document region which has been changed
	 * @param subRegion the sub region in the dirty region which should be reconciled 
	 */
	public void reconcile(ChameleonDirtyRegion dirtyRegion, IRegion subRegion){
		View view = view();
		if(view != null) {
			setWholeDocumentDirty(false);

			if(dirtyRegion.getType() == ChameleonDirtyRegion.INSERT){
				adaptClonedPositions(dirtyRegion);
			}

			ClonedChameleonPosition coveringPos = dirtyRegion.getSmallestCoveringPos(_clonedPositions);
			if(coveringPos != null){
				addDirtyPositions(coveringPos);
			}
			else{
				setWholeDocumentDirty(true);
			}

			if(dirtyRegion.getType() == ChameleonDirtyRegion.REMOVE){
				adaptClonedPositions(dirtyRegion);
			}
		}
	}	

	private void addDirtyPositions(ClonedChameleonPosition position){
		if(!_dirtyPositions.contains(position)) {
			_dirtyPositions.add(position);
		}
	}

	/**
	 * Activates non-incremental reconciling. The reconciling strategy is just told
	 * that there are changes and that it should reconcile the given partition of the
	 * document most recently passed into {@link #setDocument(IDocument)}.
	 *
	 * @param partition the document partition to be reconciled
	 */
	public void reconcile(IRegion partition){
		
	}

	/**
	 * 
	 * @return the configuration where this strategy is used
	 */
	public ChameleonSourceViewerConfiguration getConfiguration() {
		return _configuration;
	}
	
	public static class ClonedChameleonPosition extends Position {

		public ClonedChameleonPosition(int offset, int length, Element element, String name){
			super(Math.max(0,offset),Math.max(0,length));
	  	if(element == null) {
	  		throw new ChameleonProgrammerException("Initializing decorator with null element");
	  	}
			setElement(element);
			setName(name);
		}

		private Element _element;
		
		public Element getElement() {
			return _element;
		}
		
		public void setElement(Element element) {
		  _element = element;
		}
		
		private String _name;

		private void setName(String name) {
			_name = name;
		}
		public String getName() {
			return _name;
		}

	}
}
