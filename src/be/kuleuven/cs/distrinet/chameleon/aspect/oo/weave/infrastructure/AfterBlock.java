package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;

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
