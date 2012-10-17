package chameleon.workspace;


public abstract class SyntheticProjectLoader extends DocumentLoaderImpl {
	
	public SyntheticProjectLoader() {
		activate();
	}
	
	protected abstract void activate();
}