package org.aikodi.chameleon.builder;

import java.util.function.Consumer;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.DemandImport;
import org.aikodi.chameleon.core.namespacedeclaration.DirectImport;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.workspace.View;

public class DocumentBuilder<P> extends Builder<P, Document>{

   private NamespaceDeclaration _namespaceDeclaration;
   
   public DocumentBuilder(View view, P parent, Consumer<Document> consumer) {
      super(parent, consumer);
      init(new DocumentFactory().createDocument(view));
   }

   public DocumentBuilder(String namespaceFQN, View view, P parent, Consumer<Document> consumer) {
      super(parent, consumer);
      init(new DocumentFactory().createDocument(namespaceFQN, view));
   }

  protected void init(NamespaceDeclaration namespaceDeclaration) {
    _namespaceDeclaration = namespaceDeclaration;
    consumer().accept(_namespaceDeclaration.lexical().nearestAncestor(Document.class));
  }
   
//   public <D extends Builder<DocumentBuilder<P>,E>,E extends Declaration> D nested(String name, Function<DocumentBuilder<P>, D>  factory) {
//     return factory.apply(this);
//   }
//
   public DocumentBuilder<P> directImport(String fullyQualifiedName) {
      NameReference<Declaration> nameReference = new NameReference<Declaration>(fullyQualifiedName, Declaration.class);
      DirectImport<Declaration> directImport = new DirectImport<Declaration>(nameReference, Declaration.class);
      namespaceDeclaration().addImport(directImport);
      return this;
   }

   public DocumentBuilder<P> demandImport(String fullyQualifiedName) {
      NameReference<Namespace> nameReference = new NameReference<Namespace>(fullyQualifiedName, Namespace.class);
      DemandImport directImport = new DemandImport(nameReference);
      namespaceDeclaration().addImport(directImport);
      return this;
   }
   
   protected NamespaceDeclaration namespaceDeclaration() {
     return _namespaceDeclaration;
   }
   
   public Document document() {
     return _namespaceDeclaration.lexical().nearestAncestor(Document.class);
   }
   
   public P endDocument() {
      return parent();
   }
}
