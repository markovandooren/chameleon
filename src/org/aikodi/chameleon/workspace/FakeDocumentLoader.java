package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.document.Document;

/**
 * A document loader that does not do anything.
 * 
 * @author Marko van Dooren
 */
public class FakeDocumentLoader extends EagerDocumentLoaderImpl {

    public FakeDocumentLoader(Document document, DocumentScanner scanner) {
        init(scanner);
        setDocument(document);
    }

    /**
     * We do nothing for a fake document loader. The content was set directly.
     */
    @Override
    public void doRefresh() throws InputException {
    }
    
    @Override
    protected String resourceName() {
      return "directly from memory";
    }
}