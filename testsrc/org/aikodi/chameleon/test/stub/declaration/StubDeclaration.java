package org.aikodi.chameleon.test.stub.declaration;

import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;

public class StubDeclaration extends BasicDeclaration {

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
