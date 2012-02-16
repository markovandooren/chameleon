package chameleon.aspect.oo.weave.infrastructure;

import java.util.List;

import org.rejuse.association.Association;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.oo.model.advice.ProceedCall;
import chameleon.oo.statement.Block;
import chameleon.oo.statement.Statement;

public class AroundBlock extends AdvisedBlockFactory {
	
	protected AroundBlock() {}

	@Override
	public Block weave(MatchResult<Block> joinpoint, Block adviceBlock) {
		Block adviceResultBlock = adviceBlock.clone();
		Block originalCode = joinpoint.getJoinpoint().clone();
		
		List<ProceedCall> descendants = (List<ProceedCall>) adviceResultBlock.descendants(ProceedCall.class);

		for (ProceedCall pc : descendants) {
			Statement statement = (Statement) pc.nearestAncestor(Statement.class);
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
