package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputSource;

public interface InputSourceNamespace extends Namespace {

	public void addInputSource(InputSource source) throws InputException;
	
	public List<InputSource> inputSources();
}
