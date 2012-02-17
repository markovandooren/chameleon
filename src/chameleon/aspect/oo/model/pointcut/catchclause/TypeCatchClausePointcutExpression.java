package chameleon.aspect.oo.model.pointcut.catchclause;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.oo.model.pointcut.SubtypeMarker;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.Statement;
import chameleon.oo.type.TypeReference;
import chameleon.support.statement.CatchClause;
import chameleon.util.Util;

public class TypeCatchClausePointcutExpression extends CatchClausePointcutExpression {
	
	private SingleAssociation<TypeCatchClausePointcutExpression, TypeReference> _exceptionType = new SingleAssociation<TypeCatchClausePointcutExpression, TypeReference>(this);
	
	// TODO: check wanted behavior for subtypes
	private SingleAssociation<TypeCatchClausePointcutExpression, SubtypeMarker> _subtypeMarker = new SingleAssociation<TypeCatchClausePointcutExpression, SubtypeMarker>(this);
	
	public TypeReference exceptionType() {
		return _exceptionType.getOtherEnd();
	}
	
	public void setExceptionType(TypeReference exceptionType) {
		setAsParent(_exceptionType, exceptionType);
	}
	
	public SubtypeMarker subtypeMarker() {
		return _subtypeMarker.getOtherEnd();
	}
	
	public boolean hasSubtypeMarker() {
		return subtypeMarker() != null;
	}
	
	public void setSubtypeMarker(SubtypeMarker marker) {
		setAsParent(_subtypeMarker, marker);
	}
	
	@Override
	public MatchResult match(Statement element) throws LookupException {	
		if (!super.matches(element).isMatch())
			return MatchResult.noMatch();
		
		// Currently not using the sub type marker for our checks...
		if (!((CatchClause) element.parent()).getExceptionParameter().getType().assignableTo(exceptionType().getType()))
			return MatchResult.noMatch();
		
		return new MatchResult(this, element);
	}

	@Override
	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		
		Util.addNonNull(exceptionType(), result);
		Util.addNonNull(subtypeMarker(), result);
		
		return result;
	}

	@Override
	public TypeCatchClausePointcutExpression clone() {
		TypeCatchClausePointcutExpression clone = new TypeCatchClausePointcutExpression();
		
		if (exceptionType() != null)
			clone.setExceptionType(exceptionType().clone());
		
		if (subtypeMarker() != null)
			clone.setSubtypeMarker(subtypeMarker().clone());
		
		return clone;
	}
}