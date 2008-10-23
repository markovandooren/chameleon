package chameleon.core.declaration;

public interface Definition<E extends Definition, P extends DeclarationContainer> extends Declaration<E,P> {

	public boolean complete();
}
