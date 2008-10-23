package chameleon.core.declaration;

import chameleon.core.context.Target;

public interface TargetDeclaration<E extends TargetDeclaration,P extends DeclarationContainer> extends Declaration<E,P>, Target<E,P> {

}
