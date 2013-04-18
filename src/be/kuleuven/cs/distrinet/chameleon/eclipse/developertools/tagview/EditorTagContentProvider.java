/**
 * Created on 7-mei-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.developertools.tagview;

import java.util.Collection;
import java.util.TreeSet;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorTag;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * Calculates the elements for the Editor Tag List View.
 * 
 * @author Tim Vermeiren
 */
public class EditorTagContentProvider implements IStructuredContentProvider {
	
	EditorTagListView view;
	private Predicate<EclipseEditorTag> _filterPredicate;
	
	void setFilter(Predicate<EclipseEditorTag> filter) {
		_filterPredicate = filter;
	}
	
	public Predicate<EclipseEditorTag> filter() {
		return _filterPredicate;
	}
	
	public EditorTagContentProvider(EditorTagListView view, Predicate<EclipseEditorTag> filterPredicate) {
		this.view = view;
		this._filterPredicate = filterPredicate;
	}

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

	public void dispose() {
		// NOP
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// NOP
	}

}
