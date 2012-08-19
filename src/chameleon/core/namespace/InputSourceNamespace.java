package chameleon.core.namespace;

import java.util.List;

import chameleon.workspace.InputSource;

public interface InputSourceNamespace extends Namespace {

	public void addInputSource(InputSource source);
	
	public List<InputSource> inputSources();
}
