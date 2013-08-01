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
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * Calculates the elements for the Editor Tag List View.
 * 
 * @author Tim Vermeiren
 */
public class EditorTagContentProvider implements IStructuredContentProvider {
	
	EditorTagListView view;
	private Predicate<EclipseEditorTag,Nothing> _filterPredicate;
	
	void setFilter(Predicate<EclipseEditorTag,Nothing> filter) {
		_filterPredicate = filter;
	}
	
	public Predicate<EclipseEditorTag,Nothing> filter() {
		return _filterPredicate;
	}
	
	public EditorTagContentProvider(EditorTagListView view, Predicate<EclipseEditorTag,Nothing> filterPredicate) {
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
