package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

public interface WidgetFactory<W> {

	public W createCheckbox(String text, boolean initialState, CheckboxListener listener);
}
