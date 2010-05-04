package chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.Association;
import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.UnsafePredicate;

import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeIndirection;
import chameleon.oo.type.TypeReference;


public abstract class AbstractInstantiatedTypeParameter<E extends AbstractInstantiatedTypeParameter<E>> extends TypeParameter<E> {

	public AbstractInstantiatedTypeParameter(SimpleNameSignature signature, ActualTypeArgument argument) {
		super(signature);
		setArgument(argument);
	}

	public void substitute(Element<?,?> element) throws LookupException {
		Type type = nearestAncestor(Type.class);
		List<CrossReference> crossReferences = 
			 element.descendants(CrossReference.class, 
					              new UnsafePredicate<CrossReference,LookupException>() {
	
													@Override
													public boolean eval(CrossReference object) throws LookupException {
														return object.getElement().equals(selectionDeclaration());
													}
				 
			                  });
		for(CrossReference cref: crossReferences) {
			SingleAssociation parentLink = cref.parentLink();
			Association childLink = parentLink.getOtherRelation();
			TypeReference namedTargetExpression = argument().substitutionReference().clone();
			childLink.replace(parentLink, namedTargetExpression.parentLink());
		}
	}

	public List<Element> children() {
		return new ArrayList<Element>();
	}
	
	private void setArgument(ActualTypeArgument type) {
		_argument = type;
	}
	
	public ActualTypeArgument argument() {
		return _argument;
	}
	
	private ActualTypeArgument _argument;

	public Type selectionDeclaration() throws LookupException {
		return new ActualType(signature().clone(), argument().type());
	}

	public static class ActualType extends TypeIndirection {

		public ActualType(SimpleNameSignature sig, Type aliasedType) {
			super(sig,aliasedType);
		}

		@Override
		public Type clone() {
			return new ActualType(signature().clone(), aliasedType());
		}
		
		@Override
		public Type actualDeclaration() {
			return aliasedType();
		}
		
		@Override
		public boolean uniSameAs(Element element) {
			return element.equals(aliasedType());
		}
		
	}


	@Override
	public Type resolveForRoundTrip() throws LookupException {
//		return this;
  	Type result = new LazyTypeAlias(signature().clone(), this);
  	result.setUniParent(parent());
  	return result;
	}

	private static class LazyTypeAlias extends TypeIndirection {

		public LazyTypeAlias(SimpleNameSignature sig, AbstractInstantiatedTypeParameter param) {
			super(sig,null);
			_param = param;
		}
		
		public Type aliasedType() {
			try {
				return parameter().upperBound();
			} catch (LookupException e) {
				throw new Error("LookupException while looking for aliasedType of a lazy alias",e);
			}
		}
		
		public AbstractInstantiatedTypeParameter parameter() {
			return _param;
		}
		
		private final AbstractInstantiatedTypeParameter _param;

		@Override
		public Type clone() {
			return new LazyTypeAlias(signature().clone(), _param);
		}
		
	}
	

	public TypeParameter capture(FormalTypeParameter formal) {
//		return argument().capture(formal);
		throw new Error();
	}
	
	@Override
	public Type lowerBound() throws LookupException {
		return argument().lowerBound();
	}

	@Override
	public Type upperBound() throws LookupException {
		return argument().upperBound();
	}
	
	@Override
	public VerificationResult verifySelf() {
		VerificationResult tmp = super.verifySelf();
		if(argument() != null) {
		  return tmp;
		} else {
			return tmp.and(new MissingSignature(this)); 
		}
	}

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
//		return other == this;
		boolean result = false;
		if(other instanceof InstantiatedTypeParameter) {
		 result = argument().sameAs(((InstantiatedTypeParameter)other).argument());
		}
		return result;
	}
	
	public int hashCode() {
		return argument().hashCode();
	}

	@Override
	public TypeReference upperBoundReference() throws LookupException {
		return argument().substitutionReference();
	}

}
