package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;


public abstract class SWTWidgetFactory implements WidgetFactory<Control> {

	@Override
	public Control createCheckbox(String text, boolean initialState, final CheckboxListener listener) {
		final Button button = new Button(parent(), SWT.CHECK);
		button.setText(text);
		button.setSelection(initialState);
		button.addSelectionListener(new SelectionListener(){
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				listener.selectionChanged(button.getSelection());
			}
		
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				listener.selectionChanged(button.getSelection());
			}
		});
		return button;
	}
	
	public abstract Composite parent();

}
