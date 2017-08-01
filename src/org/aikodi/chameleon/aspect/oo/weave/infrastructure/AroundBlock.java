package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.oo.model.advice.ProceedCall;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.util.Util;
import org.aikodi.rejuse.association.Association;

public class AroundBlock extends AdvisedBlockFactory {
	
	protected AroundBlock() {}

	@Override
	public Block weave(MatchResult<Block> joinpoint, Block adviceBlock) {
		Block adviceResultBlock = Util.clone(adviceBlock);
		Block originalCode = Util.clone(joinpoint.getJoinpoint());
		
		List<ProceedCall> descendants = adviceResultBlock.lexical().descendants(ProceedCall.class);

		for (ProceedCall pc : descendants) {
			Statement statement = pc.nearestAncestor(Statement.class);
			statement.parentLink().getOtherRelation().replace((Association)statement.parentLink(), (Association)originalCode.clone().parentLink());	
		}
		
		joinpoint.getJoinpoint().clear();
		joinpoint.getJoinpoint().addBlock(adviceResultBlock);
		return joinpoint.getJoinpoint();
	}

	public static AroundBlock create() {
		return _prototype;
	}
	
	private static AroundBlock _prototype = new AroundBlock();
}
