package be.kuleuven.cs.distrinet.chameleon.util.concurrent;


public class SafeFixedThreadExecutor extends FixedThreadRunnableExecutor {

	public SafeFixedThreadExecutor(RunnableFactory factory) {
		super(factory);
	}

	@Override
	public void run() throws InterruptedException {
		try {
			super.run();
		} catch(InterruptedException e) {
			throw e;
		}
		catch (Exception e) {
			throw new Error();
		}
	}
	
	

}
