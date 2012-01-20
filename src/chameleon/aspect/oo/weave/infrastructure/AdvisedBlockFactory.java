package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.oo.statement.Block;

public abstract class AdvisedBlockFactory {

	public abstract Block weave(MatchResult<Block> joinpoint, Block advice);
}
