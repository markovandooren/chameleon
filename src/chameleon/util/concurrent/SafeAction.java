package chameleon.util.concurrent;


public abstract class SafeAction<T> extends Action<T> {
	
	public void perform(T t) {
		try {
		  actuallyPerform(t);
		} catch(Exception e) {
			throw new WrappedException(e);
		}
	}
	

}
