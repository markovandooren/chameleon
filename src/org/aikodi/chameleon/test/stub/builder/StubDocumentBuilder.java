package org.aikodi.chameleon.test.stub.builder;

import org.aikodi.chameleon.builder.DocumentBuilder;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.workspace.View;

import java.util.function.Consumer;

public class StubDocumentBuilder<P> extends DocumentBuilder<P> {

  public StubDocumentBuilder(String namespaceFQN, View view, P parent, Consumer<Document> consumer) {
    super(namespaceFQN, view, parent, consumer);
  }

  public StubDocumentBuilder(View view, P parent, Consumer<Document> consumer) {
    super(view, parent, consumer);
  }

  public StubContainerBuilder<StubDocumentBuilder<P>> builder() {
    return new StubContainerBuilder<StubDocumentBuilder<P>>(this, d -> {
      namespaceDeclaration().add(d);
    });
  }
  
}