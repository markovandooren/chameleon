package be.kuleuven.cs.distrinet.chameleon.workspace;


//FIXME REMOVE CLASS
public abstract class SyntheticProjectLoader extends DocumentLoaderImpl {
	
	public SyntheticProjectLoader() {
		super(true);
		activate();
	}
	
	protected abstract void activate();
}
