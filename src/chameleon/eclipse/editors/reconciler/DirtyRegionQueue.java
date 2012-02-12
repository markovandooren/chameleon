package chameleon.eclipse.editors.reconciler;

import java.util.ArrayList;
import java.util.List;

//import org.eclipse.jface.text.reconciler.DirtyRegion; // ChameleonDirtyRegion

/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * Queue used by {@link org.eclipse.jface.text.reconciler.Reconciler} to manage
 * dirty regions. When a dirty region is inserted into the queue, the queue tries
 * to fold it into the neighboring dirty region.
 *
 * @see org.eclipse.jface.text.reconciler.Reconciler
 * @see org.eclipse.jface.text.reconciler.DirtyRegion
 */
class DirtyRegionQueue {
	
	/** The list of dirty regions. */
	private List<ChameleonDirtyRegion> fDirtyRegions= new ArrayList<ChameleonDirtyRegion>();

	/**
	 * Creates a new empty dirty region.
	 */
	public DirtyRegionQueue() {
		super();
	}
	
	/**
	 * Adds a dirty region to the end of the dirty-region queue.
	 *
	 * @param dr the dirty region to add
	 */
	public void addDirtyRegion(ChameleonDirtyRegion dr) {
		// If the dirty region being added is directly after the last dirty
		// region on the queue then merge the two dirty regions together.
		ChameleonDirtyRegion lastDR= getLastDirtyRegion();
		boolean wasMerged= false;
		if (lastDR != null)
			if (lastDR.getType() == dr.getType())
				if (lastDR.getType() == ChameleonDirtyRegion.INSERT) {
					if (lastDR.getOffset() + lastDR.getLength() == dr.getOffset()) {
						lastDR.mergeWith(dr);
						wasMerged= true;
					}
				} else if (lastDR.getType() == ChameleonDirtyRegion.REMOVE) {
					if (dr.getOffset() + dr.getLength() == lastDR.getOffset()) {
						lastDR.mergeWith(dr);
						wasMerged= true;
					}
				}

		if (!wasMerged)
			// Don't merge- just add the new one onto the queue.
			fDirtyRegions.add(dr);
	}
	
	/**
	 * Returns the last dirty region that was added to the queue.
	 *
	 * @return the last DirtyRegion on the queue
	 */
	private ChameleonDirtyRegion getLastDirtyRegion() {
		int size= fDirtyRegions.size();
		return (size == 0 ? null : (ChameleonDirtyRegion) fDirtyRegions.get(size - 1));
	}

	/**
	 * Returns the number of regions in the queue.
	 *
	 * @return the dirty-region queue-size
	 */
	public int getSize() {
		return fDirtyRegions.size();
	}

	/**
	 * Throws away all entries in the queue.
	 */
	public void purgeQueue() {
		fDirtyRegions.clear();
	}

	/**
	 * Removes and returns the first dirty region in the queue
	 *
	 * @return the next dirty region on the queue
	 */
	public ChameleonDirtyRegion removeNextDirtyRegion() {
		if (fDirtyRegions.size() == 0)
			return null;
		ChameleonDirtyRegion dr= fDirtyRegions.get(0);
		fDirtyRegions.remove(0);
		return dr;
	}
}

