package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.oo.statement.Block;

public class BeforeBlock extends SimpleBlockFactory{
	
	protected BeforeBlock() {}
	
	@Override
	protected Block add(MatchResult<Block> joinpoint, Block advice) {
		Block finalBlock = new Block();
		finalBlock.addStatement(advice);
		finalBlock.addStatement(joinpoint.getJoinpoint().clone());
		return finalBlock;
	}

	public static BeforeBlock create() {
		return _prototype;
	}
	
	private static BeforeBlock _prototype = new BeforeBlock();
}
