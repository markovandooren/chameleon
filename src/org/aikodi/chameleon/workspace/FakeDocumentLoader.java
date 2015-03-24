package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.document.Document;

public class FakeDocumentLoader extends DocumentLoaderImpl {

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
}