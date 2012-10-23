package chameleon.plugin;

import chameleon.workspace.View;

public abstract class ViewProcessorImpl extends ProcessorImpl<View,ViewProcessor> implements ViewProcessor {

	public View view() {
		return container();
	}
	
	@Override
	public <T extends ViewProcessor> void setView(View view, Class<T> keyInterface) {
		setContainer(view, keyInterface);
	}

	@Override
	public abstract ViewProcessorImpl clone();


}
