package chameleon.workspace;

import chameleon.core.document.Document;

public interface DocumentLoadingListener {

	public void notifyLoaded(Document document);
	
//	public void notifyUnloaded(Document document);
	
}
