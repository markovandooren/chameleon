package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.catchclause;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.SubtypeMarker;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.support.statement.CatchClause;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class TypeCatchClausePointcutExpression extends CatchClausePointcutExpression {
	
	private Single<TypeReference> _exceptionType = new Single<TypeReference>(this);
	
	// TODO: check wanted behavior for subtypes
	private Single<SubtypeMarker> _subtypeMarker = new Single<SubtypeMarker>(this);
	
	public TypeReference exceptionType() {
		return _exceptionType.getOtherEnd();
	}
	
	public void setExceptionType(TypeReference exceptionType) {
		set(_exceptionType, exceptionType);
	}
	
	public SubtypeMarker subtypeMarker() {
		return _subtypeMarker.getOtherEnd();
	}
	
	public boolean hasSubtypeMarker() {
		return subtypeMarker() != null;
	}
	
	public void setSubtypeMarker(SubtypeMarker marker) {
		set(_subtypeMarker, marker);
	}
	
//	@Override
//	public MatchResult match(Statement element) throws LookupException {	
////		if (!super.matches(element).isMatch())
////			return MatchResult.noMatch();
////		
////		// Currently not using the sub type marker for our checks...
//		if (!((CatchClause) element.parent()).getExceptionParameter().getType().assignableTo(exceptionType().getType()))
//			return MatchResult.noMatch();
//		
//		return new MatchResult(this, element);
//	}

	@Override
   protected boolean doesMatch(Statement joinpoint) throws LookupException {
		return super.doesMatch(joinpoint) && ((CatchClause) joinpoint.parent()).getExceptionParameter().getType().assignableTo(exceptionType().getType());
	}
	
  @Override
  protected TypeCatchClausePointcutExpression cloneSelf() {
		return new TypeCatchClausePointcutExpression();
	}
}
