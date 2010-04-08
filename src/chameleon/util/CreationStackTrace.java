package chameleon.util;

public class CreationStackTrace {

	private StackTraceElement[] _stackTrace;

	public CreationStackTrace() {
		try {
			throw new Exception();
		} catch (Exception e) {
			_stackTrace = e.getStackTrace();
		}
	}
	
}
