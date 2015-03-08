/*
 * Created on 22-feb-2005
 */
package org.aikodi.chameleon.eclipse.editors.reconciler;

import java.util.List;

import org.aikodi.chameleon.eclipse.editors.reconciler.ChameleonReconcilingStrategy.ClonedChameleonPosition;
import org.eclipse.jface.text.reconciler.DirtyRegion;

/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * Represents A "dirty region" from a chameleon document. A dirty region is a region which has
 * been changed in one way or another.  
 */
public class ChameleonDirtyRegion extends DirtyRegion/*implements ITypedRegion*/ {
	
	/** 
	 * Identifies an insert operation.
	 */
	final static public String INSERT= "__insert"; //$NON-NLS-1$
	/**
	 * Identifies a remove operation.
	 */
	final static public String REMOVE= "__remove"; //$NON-NLS-1$
	
	/** The region's offset. */
	private int fOffset2;
	/** The region's length. */
	private int fLength2;
	/** Indicates the type of the applied change. */
	private String fType2;
	/** The text which has been inserted. */
	private String fText2;

	/**
	 * Creates a new dirty region.
	 * 
	 * @param offset the offset within the document where the change occurred
	 * @param length the length of the text within the document that changed
	 * @param type the type of change that this region represents: {@link #INSERT} {@link #REMOVE}
	 * @param text the substitution text
	 */
	public ChameleonDirtyRegion(int offset, int length, String type, String text) {
		super(offset, length, type, text);
		fOffset2= offset;
		fLength2= length;
		fType2= type;
		fText2= text;
	}

	/*
	 * @see ITypedRegion#getOffset()
	 */
	@Override
   public int getOffset() {
		return fOffset2;
	}
	
	/*
	 * @see ITypedRegion#getLength()
	 */
	@Override
   public int getLength() {
		return fLength2;
	}
	
	/*
	 * @see ITypedRegion#getType
	 */
	@Override
   public String getType() {
		return fType2;
	}
	
	/**
	 * Returns the text that changed as part of the region change.
	 * 
	 * @return the changed text
	 */
	@Override
   public String getText() {
		return fText2;
	}
	
	/**
	 * Modify the receiver so that it encompasses the region specified by the dirty region.
	 * 
	 * @param dr the dirty region with which to merge
	 */
	void mergeWith(ChameleonDirtyRegion dr) {
		int start= Math.min(fOffset2, dr.fOffset2);
		int end= Math.max(fOffset2 + fLength2, dr.fOffset2 + dr.fLength2);
		fOffset2= start;
		fLength2= end - start;
		fText2= (dr.fText2 == null ? fText2 : (fText2 == null) ? dr.fText2 : fText2 + dr.fText2);
	}
	
	ClonedChameleonPosition getSmallestCoveringPos(List<ClonedChameleonPosition> clonedPositions){
		ClonedChameleonPosition covPos = null;
		for(ClonedChameleonPosition pos : clonedPositions) {
			// if dirty region is completely in position ...
			if(getOffset()>pos.offset && getOffset()<=(pos.getOffset()+pos.getLength()-1) && 
					(getOffset()+getLength()-1)<(pos.getOffset()+pos.getLength()-1)){
				if(covPos == null || pos.getLength()<covPos.getLength()) {
					covPos = pos;
				}
			}
		}
		
		return covPos;
	}
	

}
