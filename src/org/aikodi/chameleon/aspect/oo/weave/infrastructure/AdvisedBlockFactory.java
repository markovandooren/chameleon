package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.oo.statement.Block;

public abstract class AdvisedBlockFactory {

	public abstract Block weave(MatchResult<Block> joinpoint, Block advice);
}
