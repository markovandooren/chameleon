package org.aikodi.chameleon.test.stub.declaration;

import org.aikodi.chameleon.core.declaration.CommonDeclarationContainingDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;

public class StubDeclarationContainingDeclarationContainer extends CommonDeclarationContainingDeclaration {

  protected StubDeclarationContainingDeclarationContainer() {
    super();
  }

  public StubDeclarationContainingDeclarationContainer(String name) {
    super(new SimpleNameSignature(name));
  }

  protected Element cloneSelf() {
    return new StubDeclarationContainingDeclarationContainer();
  }

}
