package chameleon.core.namespace;

import java.util.List;

import chameleon.workspace.InputException;
import chameleon.workspace.InputSource;

public interface InputSourceNamespace extends Namespace {

	public void addInputSource(InputSource source) throws InputException;
	
	public List<InputSource> inputSources();
}
