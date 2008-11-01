package chameleon.core.declaration;

public interface Definition<E extends Definition<E,P,S>, P extends DeclarationContainer,S extends Signature> extends Declaration<E,P,S> {

	public boolean complete();
}
