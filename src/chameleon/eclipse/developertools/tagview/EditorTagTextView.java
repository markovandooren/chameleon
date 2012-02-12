package chameleon.eclipse.developertools.tagview;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonEditor;

/**
 * An Eclipse view to show the position of the decorators in text format
 * 
 * @author Tim Vermeiren
 */
public class EditorTagTextView extends ViewPart {
	
	private Text text;
	
	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL);
		text.setText("Editor Tag View");
		
		// create actions:
		Action showAllTagsAction = new ShowEditorTagsAction(text, true);
		Action showTagsAction = new ShowEditorTagsAction(text, false);
		Action clearAction = new ClearTextViewerAction(text);
		
		// create menu
		IMenuManager menuMgr = getViewSite().getActionBars().getMenuManager();
		menuMgr.add(showAllTagsAction);
		menuMgr.add(showTagsAction);
		menuMgr.add(clearAction);
		
		// create toolbar:
		IToolBarManager toolMgr = getViewSite().getActionBars().getToolBarManager();
		toolMgr.add(showAllTagsAction);
		toolMgr.add(showTagsAction);
		toolMgr.add(clearAction);
		
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}
	
	private class ShowEditorTagsAction extends Action {
		private Text text;
		private boolean showOnlyAllEditorTags;
		public ShowEditorTagsAction(Text text, boolean showOnlyAllEditorTags) {
			super();
			if(showOnlyAllEditorTags)
				setText("Show ALL-EditorTags of document");
			else
				setText("Show EditorTags of document");
			this.text = text;
			this.showOnlyAllEditorTags = showOnlyAllEditorTags;
		}
		@Override
		public void run() {
			ChameleonEditor editor = ChameleonEditor.getCurrentActiveEditor();
			if(editor != null){
				ChameleonDocument doc = editor.getDocument();
				String editorTagText = ShowEditorTags.getChameleonEditorPositionsStringOfDocument(doc, doc.get(), showOnlyAllEditorTags);
				text.setText(editorTagText);
			}
		}
	}
	
	public static class ClearTextViewerAction extends Action {
		private Text text;
		// constructor:
		public ClearTextViewerAction(Text text) {
			super("Clear");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("delete.gif"));
			this.text = text;
		}
		@Override
		public void run() {
			text.setText("Editor Tag View");
		}
	}
}
