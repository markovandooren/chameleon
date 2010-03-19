package chameleon.core.type.generics;



import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.exception.ChameleonProgrammerException;

public abstract class ActualTypeArgument<E extends ActualTypeArgument> extends ElementImpl<ActualTypeArgument, Element> {

	public ActualTypeArgument() {
	}
	
	public abstract E clone();
	
//	@Override
//	public boolean uniSameAs(Element element) throws LookupException {
//		boolean result = false;
//		if(element instanceof ActualTypeArgument) {
//			return upperBound().sameAs(((ActualTypeArgument) element).upperBound())
//			      && lowerBound().sameAs(((ActualTypeArgument) element).lowerBound());
//		}
//		return result;
//	}
	
	public abstract Type type() throws LookupException;
	
	public final boolean contains(ActualTypeArgument other) throws LookupException {
		return other.upperBound().subTypeOf(upperBound()) && lowerBound().subTypeOf(other.lowerBound());
	}
	
	public abstract Type upperBound() throws LookupException;
	
	public abstract Type lowerBound() throws LookupException;

	public abstract TypeParameter capture(FormalTypeParameter formal);
	
	/**
	 * Return the type reference that must be used for substitution of a formal parameter.
	 * 
	 * @param parameter
	 * @return
	 */
	public TypeReference substitutionReference() {
		throw new ChameleonProgrammerException();
	}

//	public boolean alwaysSameAs(ActualTypeArgument argument) throws LookupException {
//		return false;
//	}

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		return (other instanceof ActualTypeArgument) && (type().sameAs(((ActualTypeArgument)other).type()));
	}

	
}
