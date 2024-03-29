/**
 * Created on 7-mei-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.developertools.tagview;

import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.predicate.Predicate;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Calculates the elements for the Editor Tag List View.
 * 
 * @author Tim Vermeiren
 */
public class EditorTagContentProvider implements IStructuredContentProvider {
	
	private EditorTagListView view;
	private Predicate<? super EclipseEditorTag,Nothing> _filterPredicate;
	
	void setFilter(Predicate<? super EclipseEditorTag,Nothing> filter) {
		_filterPredicate = filter;
	}
	
	public Predicate<? super EclipseEditorTag,Nothing> filter() {
		return _filterPredicate;
	}
	
	public EditorTagContentProvider(EditorTagListView view, Predicate<? super EclipseEditorTag,Nothing> filterPredicate) {
		this.view = view;
		this._filterPredicate = filterPredicate;
	}

	@Override
   public Object[] getElements(Object inputObject) {
		if(inputObject instanceof EclipseDocument){
			EclipseDocument doc = (EclipseDocument)inputObject;
			Collection<EclipseEditorTag> tags = new TreeSet<EclipseEditorTag>(EclipseEditorTag.beginoffsetComparator);
			doc.getEditorTagsWithPredicate(_filterPredicate, tags);
			// set label of view:
			view.label().setText(tags.size() + " editor tags found for document "+doc.getFile());
			return tags.toArray();
		}
		return null;
	}

	@Override
   public void dispose() {
		// NOP
	}

	@Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// NOP
	}

}
