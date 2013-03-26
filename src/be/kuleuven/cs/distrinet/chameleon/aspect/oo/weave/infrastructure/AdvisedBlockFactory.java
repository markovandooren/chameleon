package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;

public abstract class AdvisedBlockFactory {

	public abstract Block weave(MatchResult<Block> joinpoint, Block advice);
}
