package chameleon.workspace;


public abstract class SyntheticProjectLoader extends ProjectLoaderImpl {
	
	public SyntheticProjectLoader() {
		activate();
	}
	
	protected abstract void activate();
}