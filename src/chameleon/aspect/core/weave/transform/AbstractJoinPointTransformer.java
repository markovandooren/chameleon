package chameleon.aspect.core.weave.transform;

import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.core.element.Element;

public abstract class AbstractJoinPointTransformer<J extends Element,R> implements JoinPointTransformer<J,R> {

	@Override
	public JoinPointWeaver<J,R> joinPointWeaver() {
		return _joinPointWeaver;
	}

	@Override
	public void setJoinPointWeaver(JoinPointWeaver<J,R> joinPointWeaver) {
		_joinPointWeaver = joinPointWeaver;
	}

	private JoinPointWeaver<J,R> _joinPointWeaver;
}
