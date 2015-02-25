package org.aikodi.chameleon.eclipse.editors.preferences;

import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.presentation.OptionalColor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * field editor that allows the optional definition of a color
 */
public class OptionalColorFieldEditor extends FieldEditor{

	private Group parentc;
	private ColorFieldEditor colorfield;
	private BooleanFieldEditor booleanfield;
	
	public OptionalColorFieldEditor(String fieldName, String labeltext, Composite parent) {
		parentc = new Group(parent, SWT.NONE);
		parentc.setText(labeltext);
		parentc.setLayout(new GridLayout(1, false));
		booleanfield = new BooleanFieldEditor(fieldName+"_set", "Set", parentc);
		colorfield = new ColorFieldEditor(fieldName+"_color", "", parentc);
		booleanfield.setPreferenceStore(ChameleonEditorPlugin.getDefault().getPreferenceStore());
		booleanfield.setPreferenceName(fieldName+"_set");
		booleanfield.load();
		colorfield.setPreferenceStore(ChameleonEditorPlugin.getDefault().getPreferenceStore());
		colorfield.setPreferenceName(fieldName+"_color");
		colorfield.load();
	}

	public OptionalColor getOptionalColor() {
		if (!booleanfield.getBooleanValue()) {
			return new OptionalColor();
		}
		return new OptionalColor(colorfield.getColorSelector().getColorValue());
	}


	public void setOptionalColor(OptionalColor c) {
		if (c.isDefined()) {
			System.out.println(booleanfield.getPreferenceName());
			IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
			store.setValue(booleanfield.getPreferenceName(), true);
			booleanfield.load();
			colorfield.getColorSelector().setColorValue(c.getColor().getRGB());
		} else {
			IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
			store.setValue(booleanfield.getPreferenceName(), false);
			booleanfield.load();
			colorfield.getColorSelector().setColorValue(new RGB(0,0,0));
			
		}
	}	
	
	@Override
	protected void adjustForNumColumns(int arg0) {
		
	}

	@Override
	protected void doFillIntoGrid(Composite arg0, int arg1) {
	}

	@Override
	protected void doLoad() {
		booleanfield.load();
		colorfield.load();
	}

	@Override
	protected void doLoadDefault() {
		booleanfield.loadDefault();
		colorfield.loadDefault();
	}

	@Override
	protected void doStore() {
		booleanfield.store();
		colorfield.store();
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

}
