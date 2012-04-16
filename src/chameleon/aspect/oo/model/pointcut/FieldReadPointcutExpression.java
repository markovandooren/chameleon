package chameleon.aspect.oo.model.pointcut;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.staticexpression.DeclarationReference;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.type.RegularType;
import chameleon.oo.type.TypeReference;
import chameleon.oo.variable.RegularMemberVariable;
import chameleon.support.expression.AssignmentExpression;
import chameleon.util.association.Single;

public class FieldReadPointcutExpression extends AbstractPointcutExpression<NamedTargetExpression> {

	public FieldReadPointcutExpression(TypeReference typeReference, DeclarationReference reference) {
		setFieldReference(reference);
		setTypeReference(typeReference);
	}
	
	private void setTypeReference(TypeReference typeReference) {
		set(_typeReference, typeReference);
	}

	private void setFieldReference(DeclarationReference reference) {
		set(_fieldReference, reference);
	}

	private Single<DeclarationReference> _fieldReference = new Single<DeclarationReference>(this);
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);
	
	public DeclarationReference fieldReference() {
		return _fieldReference.getOtherEnd();
	}
	
	public TypeReference typeReference() {
		return _typeReference.getOtherEnd();
	}

	
	/**
	 * 	{@inheritDoc}
	 * 
	 * 	Matching works as follows (consistent with AspectJ):
	 * 
	 * 	The field must be declared in the type referenced by the signature, or a sub type. If it is re-defined in a sub type, it isn't matched.
	 * 
	 * 	E.g.
	 * 
	 * 	Class A : int foo
	 * 	Class B extends A
	 * 
	 *  get(A.foo) matches both a.foo and b.foo
	 *  
	 *  Class A : int foo
	 * 	Class B extends A : int foo
	 * 
	 *  get(A.foo) only matches a.foo
	 *  
	 *  But:
	 *  
	 *  Class A : int foo
	 * 	Class B extends A
	 * 
	 *  get(B.foo) doesn't match a.foo OR b.foo
	 */
	@Override
	public MatchResult match(NamedTargetExpression joinpoint) throws LookupException {
		if (!(joinpoint.getElement() instanceof RegularMemberVariable))
			return MatchResult.noMatch();
		
		if (joinpoint.parent() instanceof AssignmentExpression && ((AssignmentExpression) joinpoint.parent()).getVariableExpression().sameAs(joinpoint))
				return MatchResult.noMatch();
		
		
		// Typecheck: no inheritance (as AspectJ does it)
		if (!joinpoint.getType().sameAs(typeReference().getType()))
			return MatchResult.noMatch();
		
		// Get the fully qualified name of this field
		String fqn = ((RegularType) joinpoint.getElement().nearestAncestor(RegularType.class)).getFullyQualifiedName() + "." + joinpoint.signature().name();
		
		if (fqn.equals(fieldReference().reference()))
			return new MatchResult(this, joinpoint);
		
		return MatchResult.noMatch();
	}

	@Override
	public FieldReadPointcutExpression clone() {
		DeclarationReference fieldRefClone = null;
		TypeReference typeRefClone = null;
		
		if (fieldReference() != null)
			fieldRefClone = fieldReference().clone();
		
		if (typeReference() != null)
			typeRefClone = typeReference().clone();
		
		return new FieldReadPointcutExpression(typeRefClone, fieldRefClone);
	}

	@Override
	public Class<? extends NamedTargetExpression> joinPointType() throws LookupException {
		return NamedTargetExpression.class;
	}
}