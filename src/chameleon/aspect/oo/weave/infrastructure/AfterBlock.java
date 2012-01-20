package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.oo.statement.Block;

public class AfterBlock extends SimpleBlockFactory {
	
	protected AfterBlock() {}

	protected Block add(MatchResult<Block> joinpoint, Block advice) {
		Block finalBlock = new Block();
		finalBlock.addStatement(joinpoint.getJoinpoint().clone());
		finalBlock.addStatement(advice);
		return finalBlock;
	}

	public static AfterBlock create() {
		return _prototype;
	}
	
	private static AfterBlock _prototype = new AfterBlock();
}
