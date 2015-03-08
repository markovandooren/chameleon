/**
 * Created on 7-mei-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.developertools.tagview;

import java.util.Collection;
import java.util.TreeSet;

import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * Calculates the elements for the Editor Tag List View.
 * 
 * @author Tim Vermeiren
 */
public class EditorTagContentProvider implements IStructuredContentProvider {
	
	EditorTagListView view;
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
			view.label.setText(tags.size() + " editor tags found for document "+doc.getFile());
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
