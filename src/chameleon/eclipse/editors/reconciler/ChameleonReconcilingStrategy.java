package chameleon.eclipse.editors.reconciler;


import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;

import chameleon.core.Config;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.namespace.Namespace;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonSourceViewerConfiguration;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.ModelFactory;
import chameleon.oo.type.Type;


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
		_firstDR = true;
		this._configuration = configuration;
	}
	
	/**
	 * sets the document for this ChameleonReconcilingStrategy to the given document
	 * @param document
	 * 		The new document
	 */
	public void setDocument(ChameleonDocument document){	
		_document = document;
	}
	
	/**
	 * @return the document for this ChameleonReconcilingStrategy
	 */
	public ChameleonDocument getDocument(){
		return _document;
	}
	
	// checks if there are dirty regions ?
	private boolean _firstDR;
	
	//check whether reconciling is initiated
	private boolean _alreadyInit;
	//the document to which this ReconcilingStrategy applies
	private ChameleonDocument _document;
	//Vector containing clones of positions
	private ArrayList<ClonedChameleonPosition> clonedPositions = new ArrayList<ClonedChameleonPosition>();
	// Vector containing all the dirtyPositions in this document
	private ArrayList<ClonedChameleonPosition> dirtyPositions = new ArrayList<ClonedChameleonPosition>();
	//states whether the whole document is dirty
	private boolean _wholeDocumentDirty = false;

	public boolean isWholeDocumentDirty() {
		return _wholeDocumentDirty;
	}
	
	private void setWholeDocumentDirty(boolean dirty) {
		if(Config.debug()) {
			System.out.println("Setting _wholeDocumentDirty to "+dirty);
		}
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
			clonedPositions.add(cloneDecorator(eP));
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
		if(Config.debug()) {
		  System.out.println("starting reconciling,in chameleonReconcilingStrategy");
		}
		clonedPositions.clear();
//		if(DEBUG) {
//		  try {
//				System.out.println("Number of positions in document: "+getDocument().getPositions(EclipseEditorTag.CHAMELEON_CATEGORY).length);
//			} catch (BadPositionCategoryException e) {
//				e.printStackTrace();
//			}
//		}
		
		if(isWholeDocumentDirty()){
			if(Config.debug()) {
			  System.out.println("\n 2. verwerken hele document");
			}
			try{
				parseWholeDocument(_document);
				if(Config.debug()) {
				  System.out.println("    verwerken hele document geslaagd!");
				}
			}catch(Exception err){
				err.printStackTrace();
				if(Config.debug()) {
				  System.out.println("    verwerken hele document NIET geslaagd! ");
				}
			}
		}
		else{
      if(Config.debug()) {
			  System.out.println("\n 2. Verwerken kleinst gewijzigde posities");
      }
			removeCoveredPositions();
			boolean[] status = new boolean[dirtyPositions.size()];		
			for(int i=0; i<dirtyPositions.size(); i++){
				status[i] = true;
			}
			Position[] positions = null;
			for(int i=0; i<dirtyPositions.size(); i++){
				if(status[i] == true){
					ClonedChameleonPosition position = dirtyPositions.get(i);
					//System.out.println("  verwerken positie --> offset: "+position2.getOffset()+" - lengte: "+position2.getLength()+" - element: "+position.getElement().getClass().getName());
					try{
						
						// A. Verwijderen decorators van element
						
						getDocument().removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,position);
						// FIXME: positions is always null, making this call useless
//						removeEmbeddedPos(position, positions, status);						
		
						Element element = position.getElement();
						
						// B. reparsen element
						
						ModelFactory factory = getDocument().compilationUnit().language().plugin(ModelFactory.class);
						String text = getDocument().get(position.getOffset(), position.getLength());
						factory.reParse(element);
//						element.reParse(new DocumentEditorToolExtension(getDocument()),getDocument().modelFactory());
						
						
						//System.out.println("  verwerken positie geslaagd\n");
					}catch(Exception e){
						//e.printStackTrace();
						reparseEntireDocument(status, positions, i);
					}
					status[i] = false;
				}
			}
			
		}
		
		// voor test huidige posities even printen
		//System.out.println("--------------------");
		System.out.println("Einde synchronisatie");
		//System.out.println("--------------------");
		//System.out.println(" ==> Posities na synchronisatie");
		//getDocument().printPositions(Decorator.CHAMELEON_CATEGORY);
		dirtyPositions.clear();
		clonePositions();
		this._firstDR = true;
		
		nature().flushProjectCache();
		//checkVerificationErrors();
		fireModelUpdated();
		
	}
	
	public ChameleonProjectNature nature() {
		return getDocument().getProjectNature();
	}

	private void reparseEntireDocument(boolean[] status, Position[] positions, int i) {
		try{
			if(Config.debug()) {
			  System.out.println("Members verwerken mislukt. Proberen hele document verwerken");
			}
			parseWholeDocument(_document);
			//System.out.println("  verwerken hele document geslaagd");
			for(int n=0; n<dirtyPositions.size(); n++){
				status[n] = false;
			}
		}catch(Exception err){
			//System.out.println("  verwerken positie niet geslaagd");
			//System.out.println("   => positie(s) en element verwijderd");
			ClonedChameleonPosition pos = ((ClonedChameleonPosition) dirtyPositions.get(i));
			try{
				pos.getElement().disconnect();
				getDocument().removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,pos);				
				// FIXME: positions is always null, making this call useless
//				removeEmbeddedPos(pos, positions, status);
			}catch(Exception error){
				error.printStackTrace();
			}	
			
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

	/*
	 * 
	 */
	private void removeEmbeddedPos(ClonedChameleonPosition pos, Position[] positions, boolean[] status){
		try{
			ClonedChameleonPosition posB = null;
			for(int i=0; i<positions.length; i++){
				posB = (ClonedChameleonPosition)positions[i];
				if(posB.getOffset()>pos.getOffset() && 
				   (posB.getOffset()+posB.getLength())<(pos.getOffset()+pos.getLength())){
					getDocument().removePosition(EclipseEditorTag.CHAMELEON_CATEGORY,posB);				
					for(int n=0; n<status.length; n++){
						if((dirtyPositions.get(n)) == posB){
							status[n] = false;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
	private void removeCoveredPositions(){
		ClonedChameleonPosition posA, posB;
		for(int i=0; i<dirtyPositions.size(); i++){
			posA = dirtyPositions.get(i); 
			for(int t=0; t<dirtyPositions.size(); t++){
				posB = dirtyPositions.get(t);
				if(posB.getOffset()>posA.getOffset() && 
				   (posB.getOffset()+posB.getLength())<(posA.getOffset()+posA.getLength())){
					dirtyPositions.remove(posB);
				}
			}
		}
	}
	
	/*
	 * parses the whole document 
	 */
	private void parseWholeDocument(IDocument document) throws Exception{
		ChameleonDocument doc = this.getDocument();
		doc.reParse();
	}

	
	/**
	 * Search in the list of cloned positions for the smallest decorator covering the dirty region
	 * 
	 * @param dR
	 * @return
	 */
	private ClonedChameleonPosition getSmallestCoveringPos(ChameleonDirtyRegion dR){
		ClonedChameleonPosition covPos = null;
		for(ClonedChameleonPosition pos : clonedPositions) {
			// if dirty region is completely in position ...
			if(dR.getOffset()>pos.offset && dR.getOffset()<=(pos.getOffset()+pos.getLength()-1) && 
					(dR.getOffset()+dR.getLength()-1)<(pos.getOffset()+pos.getLength()-1)){
				if(covPos == null || pos.getLength()<covPos.getLength()) {
					covPos = pos;
				}
			}
		}
		
		return covPos;
	}
	
	// reconstruct positions (via dirty region)
	private void adaptClonedPositions(ChameleonDirtyRegion dR){
		// dirty region of type _INSERT
		ClonedChameleonPosition eP;
		if(dR.getType()==ChameleonDirtyRegion.INSERT){
			for(int j=0; j<clonedPositions.size(); j++){
				eP = clonedPositions.get(j);
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
			for(int j=0; j<clonedPositions.size(); j++){
				eP = clonedPositions.get(j);
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
	public static void showSize(Namespace<?> ns) {
		int size = ns.getNamespaceParts().size();
		System.out.println(ns.getFullyQualifiedName()+" defined by "+size+" parts");
		for(Namespace nested: ns.getSubNamespaces()) {
			showSize(nested);
		}
	}
	
	public static void showTypeSize(Namespace<?> root) {
		for(Type type:root.descendants(Type.class)) {
		  int size = type.descendants(Element.class).size();
		  System.out.println(type.getFullyQualifiedName()+" contains "+size+" elements");
		}
	}
	public void reconcile(ChameleonDirtyRegion dirtyRegion, IRegion subRegion){
		Language language = getDocument().compilationUnit().language();
		if(language != null) {
			Namespace root = language.defaultNamespace();
//			if(DEBUG) {
//				try {
//					System.out.println("Number of elements in model: "+root.descendants(Element.class).size());
//					System.out.println("Number of namespaces in model: "+root.descendants(Namespace.class).size());
//					System.out.println("Number of namespacesparts in model: "+root.descendants(NamespacePart.class).size());
//					System.out.println("Number of types in model: "+root.descendants(Type.class).size());
////					showSize(root);
////					showTypeSize(root);
//				} catch(Exception exc) {
//
//				}
//			}
			System.out.println("reconciling dirtyregion & subregion,in chameleonReconcilingStrategy");
			//if(_firstDR == false){
			if(_firstDR == true){
				System.out.println("Start synchronisatie");
				System.out.println(" 1. Verwerken vervuilde tekstgebieden");
			}

			setWholeDocumentDirty(false);
			_firstDR = false;
			//System.out.println("   VERVUILD TEKSTGEBIED - offset: "+dirtyRegion.getOffset()+" - lengte: "+dirtyRegion.getLength()+ " - type: "+dirtyRegion.getType()/*+" tekst: "+dirtyRegion.getText()*/);

			if(dirtyRegion.getType() == ChameleonDirtyRegion.INSERT){

				adaptClonedPositions(dirtyRegion);

			}

			ClonedChameleonPosition coveringPos = getSmallestCoveringPos(dirtyRegion);

			if(coveringPos != null){
				//System.out.println("     => KLEINST GEWIJZIGDE POSITIE - offset: "+coveringPos.getOffset()+" - lengte: "+coveringPos.getLength()+" - element: "+coveringPos.getElement().getClass().getName());
				addListDirtyPositions(coveringPos);
			}
			else{
				System.out.println("     => HELE DOCUMENT VERVUILD");
				setWholeDocumentDirty(true);
			}

			if(dirtyRegion.getType() == ChameleonDirtyRegion.REMOVE){
				adaptClonedPositions(dirtyRegion);
			}

			//System.out.println("Klaar met reconcilen");






			/*}
		else{
			_firstDR = false;
		}*/
		}
	}	
//	// 	Geeft de minimale (kleinste) positie met lengte groter dan 'length' terug die de volledige dirtyRegion omvat en null indien geen 
//	// 	positie kan gevonden worden.
//	private Decorator getCoveringPosition(ChameleonDirtyRegion dirtyRegion, int length){
//		Decorator pos = null;
//		int minLength = Integer.MAX_VALUE;
//		int offset = dirtyRegion.getOffset();
//		int len = dirtyRegion.getLength();
//		
//		Position[] positions = null;
//		try{
//			positions = _document.getPositions(Decorator.CHAMELEON_CATEGORY);
//		} catch (BadPositionCategoryException e){}
//		
//		for(int t=0;t<positions.length;t++){
//			//System.out.println(" "+positions[t].getOffset()+" - "+positions[t].getLength()+" / "+offset+" - "+len);
//			if(	positions[t].getOffset() <= offset &
//				(offset+len) <= (positions[t].getOffset()+positions[t].getLength()) &
//				positions[t].getLength() < minLength &
//				positions[t].getLength() > length){
//					minLength = positions[t].getLength();
//					pos = (Decorator) positions[t];
//			}
//		}
//		
//		return pos;
//	}
	
//	//	Geeft de minimale (kleinste) positie waarbij offset binnen positie valt of null indien geen 
//	// 	positie kan gevonden worden.
//	private Decorator getCoveringPosition(int offset){
//		Decorator pos = null;
//		int minLength=Integer.MAX_VALUE;
//		
//		Position[] positions = null;
//		try{
//			positions = _document.getPositions(Decorator.CHAMELEON_CATEGORY);
//		} catch (BadPositionCategoryException e){}
//		
//		for(int t=0;t<positions.length;t++){
//			//System.out.println(" "+positions[t].getOffset()+" - "+positions[t].getLength()+" / "+offset);
//			if(	positions[t].getOffset() <= offset &
//				offset < (positions[t].getOffset()+positions[t].getLength()) &
//				positions[t].getLength() < minLength){
//					minLength = positions[t].getLength();
//					pos = (Decorator) positions[t];
//			}
//		}
//		
//		return pos;
//	}
//	
	private void addListDirtyPositions(ClonedChameleonPosition position){
		if(!dirtyPositions.contains(position)) {
			dirtyPositions.add(position);
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

//private ChameleonPresentationReconciler _presrec;

	
//	public void setPresentationReconciler(ChameleonPresentationReconciler presrec) {
//		_presrec = presrec;
//	}

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
