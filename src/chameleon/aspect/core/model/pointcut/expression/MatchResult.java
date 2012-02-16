package chameleon.aspect.core.model.pointcut.expression;

import chameleon.core.element.Element;

public class MatchResult<T extends Element> {
	private boolean match;
	private T joinpoint;
	private PointcutExpression<?> expression;
	
	public MatchResult(PointcutExpression<?> expression, T joinpoint) {
		this(true, expression, joinpoint);
	}
	
	public MatchResult(boolean match, PointcutExpression<?> expression, T joinpoint) {
		setExpression(expression);
		setJoinpoint(joinpoint);
		setMatch(match);
	}
	
	public static <U extends Element> MatchResult<U> noMatch() {
		return new MatchResult<U>(false, null, null);
	}

	public T getJoinpoint() {
		return joinpoint;
	}

	public PointcutExpression<?> getExpression() {
		return expression;
	}

	private void setJoinpoint(T joinpoint) {
		this.joinpoint = joinpoint;
	}

	private void setExpression(PointcutExpression<?> expression) {
		this.expression = expression;
	}
	
	public boolean isMatch() {
		return match;
	}

	private void setMatch(boolean match) {
		this.match = match;
	}
}