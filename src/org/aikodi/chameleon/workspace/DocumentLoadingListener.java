package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.document.Document;

public interface DocumentLoadingListener {

	public void notifyLoaded(Document document);
	
//	public void notifyUnloaded(Document document);
	
}
