package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.util.Util;

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
