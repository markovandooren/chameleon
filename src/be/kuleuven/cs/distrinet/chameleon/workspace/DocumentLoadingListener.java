package be.kuleuven.cs.distrinet.chameleon.workspace;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;

public interface DocumentLoadingListener {

	public void notifyLoaded(Document document);
	
//	public void notifyUnloaded(Document document);
	
}
