package be.kuleuven.cs.distrinet.chameleon.plugin.build;

public interface BuildProgressHelper {
	public void checkForCancellation();
	public void addWorked(int n);
}
