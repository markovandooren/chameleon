package org.aikodi.chameleon.test.stub.declaration;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainingDeclarationImpl;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.util.association.Multi;

public class StubDeclarationContainingDeclarationContainer extends DeclarationContainingDeclarationImpl {

  private Multi<Declaration> _declarations = new Multi<Declaration>(this);
  
  public void addDeclaration(Declaration d) {
    add(_declarations,d);
  }

  public List<? extends Declaration> locallyDeclaredDeclarations() {
    return _declarations.getOtherEnds();
  }
  

  protected StubDeclarationContainingDeclarationContainer() {
    super(null);
  }

  public StubDeclarationContainingDeclarationContainer(String name) {
    super(new SimpleNameSignature(name));
  }

  protected Element cloneSelf() {
    return new StubDeclarationContainingDeclarationContainer();
  }

}
