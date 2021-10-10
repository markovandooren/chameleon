package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.document.Document;

/**
 * A document loader that "loads" an existing document into the model.
 * No IO is performed.
 * 
 * @author Marko van Dooren
 */
public class DirectDocumentLoader extends DocumentLoaderImpl {

   /**
    * Create a new direct loader that loads the given document into
    * the project that is attached to the given scanner.
    * The new direct loader will also attach itself to the scanner.
    * 
    * The document will be initialized properly. All namespace declarations
    * in the document will be connected to the corresponding namespaces.
    * 
    * @param scanner The scanner to which the loader will be connected,
    *                and whose project is used to add the document.
    * @param document The document to be added.
    */
   public DirectDocumentLoader(DocumentScanner scanner, Document document) {
      // 1. set the document
      setDocument(document);
      // 2. add ourselves to the scanner such that the refresh() call
      //    can attach the loader and the declarations properly to
      //    the namespace
      scanner.add(this);
      try {
         refresh();
      } catch (InputException e) {
         // Because a fake document loader does not perform IO,
         // no exception should be thrown.
         // Should this happen anyway, something is very wrong so we at least
         // throw an error instead of swallowing the exception.
         throw new Error(e);
      }
   }
   
   /**
    * {@inheritDoc}
    * 
    * A direct loader does nothing while refreshing because the document
    * was not loaded from a resource by this loader.
    */
   @Override
   protected void doRefresh() throws InputException {
      // No need to refresh anything.
   }
   
   @Override
  protected String resourceName() {
  	return "document from memory";
  }

}