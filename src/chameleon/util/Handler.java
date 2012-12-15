package chameleon.util;

public abstract class Handler {
	public abstract <E extends Exception> void handle(E exc) throws E;
	
	protected Handler() {
		
	}
	
	public final static Handler IGNORE = new Handler() {
		@Override
		public <E extends Exception> void handle(E exception) {
		}
	};
	
	public final static Handler PROPAGATE = new Handler() {
		@Override
		public <E extends Exception> void handle(E exception) throws E {
			throw exception;
		}
	};
}