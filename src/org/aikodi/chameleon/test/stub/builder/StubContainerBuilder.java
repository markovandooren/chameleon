package org.aikodi.chameleon.test.stub.builder;

import org.aikodi.chameleon.builder.Builder;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.test.stub.declaration.StubDeclaration;
import org.aikodi.chameleon.test.stub.declaration.StubDeclarationContainingDeclarationContainer;

import java.util.function.Consumer;

public class StubContainerBuilder<P> extends Builder<P,Declaration> {

  public StubContainerBuilder(P parent, Consumer<Declaration> consumer) {
    super(parent, consumer);
  }

  public StubContainerBuilder<P> declaration(String name) {
    return declaration(name, d ->{});
  }
  public StubContainerBuilder<P> declaration(String name, Consumer<Declaration> peeker) {
    StubDeclaration declaration = new StubDeclaration(name);
    peeker.accept(declaration);
    consumer().accept(declaration);
    return this;
  }
  
  public StubContainerBuilder<StubContainerBuilder<P>> container(String name) {
    return container(name, d-> {});
  }
  
  public StubContainerBuilder<StubContainerBuilder<P>> container(String name, Consumer<Declaration> peeker) {
    StubDeclarationContainingDeclarationContainer container = new StubDeclarationContainingDeclarationContainer(name);
    peeker.accept(container);
    consumer().accept(container);
    return new StubContainerBuilder<StubContainerBuilder<P>>(this, d -> {
      container.addDeclaration(d);
    });
  }
  
  public P done() {
    return parent();
  }
}