package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

public class BeforeBlock extends SimpleBlockFactory{
	
	protected BeforeBlock() {}
	
	@Override
	protected Block add(MatchResult<Block> joinpoint, Block advice) {
		Block finalBlock = new Block();
		finalBlock.addStatement(advice);
		finalBlock.addStatement(Util.clone(joinpoint.getJoinpoint()));
		return finalBlock;
	}

	public static BeforeBlock create() {
		return _prototype;
	}
	
	private static BeforeBlock _prototype = new BeforeBlock();
}
