package org.aikodi.chameleon.builder;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.workspace.View;

/**
 * A factory to create document.
 * 
 * @author Marko van Dooren
 */
public class DocumentFactory {

   /**
    * Create a new document for placement in the root namespace and
    * return its namespace declaration for the root namespace.
    * 
    * The document is not attached to a parent! The view is needed
    * to obtain a factory for crating the namespace declaration.
    * 
    * @param view The view in which the document will be placed.
    * @return The namespace declaration in of the document.
    */
   public NamespaceDeclaration createDocument(View view) {
      NamespaceDeclaration nsd = view.language().plugin(Factory.class).createRootNamespaceDeclaration();
      createDocument(nsd);
      return nsd;
   }
   
   /**
    * Create a new document for placement in the namespace with the
    * given fully qualified name and return its namespace declaration 
    * for the root namespace.
    * 
    * The document is not attached to a parent! The view is needed
    * to obtain a factory for crating the namespace declaration.
    * 
    * @param view The view in which the document will be placed.
    * @return The namespace declaration in of the document.
    */
   public NamespaceDeclaration createDocument(String namespaceFQN, View view) {
      NamespaceDeclaration nsd = view.language().plugin(Factory.class).createNamespaceDeclaration(namespaceFQN);
      createDocument(nsd);
      return nsd;
   }

   /**
    * Create a document containing the given namespace declaration.
    *  
    * @param namespaceDeclaration The namespace declaration that is added
    *                             to the next project.
    * @return A document containing the given namespace declaration.
    */
   private Document createDocument(NamespaceDeclaration namespaceDeclaration) {
      return new Document(namespaceDeclaration);
   }
}
