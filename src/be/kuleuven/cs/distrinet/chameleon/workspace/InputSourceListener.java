package be.kuleuven.cs.distrinet.chameleon.workspace;

public interface InputSourceListener {

	public void notifyInputSourceAdded(InputSource source);
	
	public void notifyInputSourceRemoved(InputSource source);
}
