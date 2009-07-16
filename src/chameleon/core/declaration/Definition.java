package chameleon.core.declaration;

public interface Definition<E extends Definition<E,P,S,F>, P extends DeclarationContainer,S extends Signature, F extends Declaration> extends Declaration<E,P,S,F> {

	public boolean complete();
}
