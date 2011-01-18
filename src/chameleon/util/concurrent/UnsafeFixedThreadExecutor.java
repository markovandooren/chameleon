package chameleon.util.concurrent;


public class UnsafeFixedThreadExecutor<E extends Exception> extends FixedThreadRunnableExecutor {

	public UnsafeFixedThreadExecutor(RunnableFactory factory, Class<E> clazz) {
		super(factory);
		_clazz = clazz;
	}
	
	public Class<E> clazz() {
		return _clazz;
	}
	
	private Class<E> _clazz;

	@Override
	public void run() throws InterruptedException, E {
		try {
			super.run();
		} catch(InterruptedException e) {
			throw e;
		} catch (WrappedException e) {
			if(_clazz.isInstance(e)) {
				throw (E)e.getCause();
			} else {
				throw new Error();
			}
		} catch(Exception e) {
			throw new Error();
		}
	}
	
	

}
