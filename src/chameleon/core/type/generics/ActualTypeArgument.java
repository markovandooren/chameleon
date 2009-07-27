package chameleon.core.type.generics;



import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public abstract class ActualTypeArgument<E extends ActualTypeArgument> extends ElementImpl<ActualTypeArgument, InstantiatedTypeParameter> {

	public ActualTypeArgument() {
	}
	
	public abstract E clone();
	
	public abstract Type type() throws LookupException;
	
	public final boolean contains(ActualTypeArgument other) throws LookupException {
		return other.upperBound().subTypeOf(upperBound()) && lowerBound().subTypeOf(other.lowerBound());
	}
	
	public abstract Type upperBound() throws LookupException;
	
	public abstract Type lowerBound() throws LookupException;

	public abstract TypeParameter capture(FormalTypeParameter formal);
}
