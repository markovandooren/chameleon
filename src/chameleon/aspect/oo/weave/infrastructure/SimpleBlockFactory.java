package chameleon.aspect.oo.weave.infrastructure;

import java.util.List;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.oo.statement.Block;
import chameleon.oo.statement.Statement;

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
