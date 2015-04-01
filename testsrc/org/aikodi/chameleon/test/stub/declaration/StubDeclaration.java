package org.aikodi.chameleon.test.stub.declaration;

import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;

public class StubDeclaration extends CommonDeclaration {

  public StubDeclaration(String name) {
    super(new SimpleNameSignature(name));
  }
  
  protected StubDeclaration() {
  }

  @Override
  protected Element cloneSelf() {
    return new StubDeclaration();
  }

}
