package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.statement.Statement;

import java.util.List;

public abstract class SimpleBlockFactory extends AdvisedBlockFactory {

	@Override
	public Block weave(MatchResult<Block> joinpoint, Block block) {
		List<Statement> advice = block.statements();
		Block adviceResultBlock = new Block();
		adviceResultBlock.addStatements(advice);
		
		Block finalBlock = add(joinpoint, adviceResultBlock);
		
		joinpoint.getJoinpoint().clear();
		joinpoint.getJoinpoint().addBlock(finalBlock);
		return joinpoint.getJoinpoint();
	}

	protected abstract Block add(MatchResult<Block> joinpoint, Block advice);

}
