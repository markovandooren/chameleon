package be.kuleuven.cs.distrinet.chameleon.workspace;

/**
 * An interface for receiving notifications when document loaders
 * are added or removed.
 * 
 * @author Marko van Dooren
 */
public interface DocumentLoaderListener {

   /**
    * Invoked when a document loader was added.
    * 
    * @param loader The document loader that was added.
    */
	public void notifyDocumentLoaderAdded(DocumentLoader loader);
	
   /**
    * Invoked when a document loader was removed.
    * 
    * @param loader The document loader that was removed.
    */
	public void notifyDocumentLoaderRemoved(DocumentLoader loader);
}
