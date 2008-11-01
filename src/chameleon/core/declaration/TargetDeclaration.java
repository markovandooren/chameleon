package chameleon.core.declaration;

import chameleon.core.context.Target;

public interface TargetDeclaration<E extends TargetDeclaration<E,P,S>,P extends DeclarationContainer,S extends Signature> extends Declaration<E,P,S>, Target<E,P> {

}
