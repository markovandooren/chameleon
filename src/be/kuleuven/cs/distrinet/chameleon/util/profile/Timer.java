package be.kuleuven.cs.distrinet.chameleon.util.profile;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Timer {

	private Stopwatch _stopwatch = Stopwatch.createUnstarted();
	
	private int _stack;
	
	public void start() {
		if(! _stopwatch.isRunning()) {
			_stopwatch.start();
		} else {
			_stack++;
		}
	}
	
	public void stop() {
		if(_stack == 0) {
			_stopwatch.stop();
		} else {
			_stack--;
		}
	}
	
	public long elapsedTime(TimeUnit unit) {
		return _stopwatch.elapsed(unit);
	}
	
	public long elapsedMillis() {
		return elapsedTime(TimeUnit.MILLISECONDS);
	}
	
	public void reset() {
		_stopwatch.reset();
	}
	
//	public final static Timer INFIX_OPERATOR_INVOCATION = new Timer();
//	public final static Timer POSTFIX_OPERATOR_INVOCATION = new Timer();
//	public final static Timer PREFIX_OPERATOR_INVOCATION = new Timer();
}
