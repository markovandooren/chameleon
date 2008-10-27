package chameleon.core.context;

import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.DeclarationSelector;

/**
 * 
 * @author Marko van Dooren
 */
public class TargetContext extends Context {

	public TargetContext(DeclarationCollector collector) {
		_collector = collector;
	}

	public DeclarationCollector collector() {
		return _collector;
	}

	private DeclarationCollector _collector;

	@Override
	public <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws MetamodelException {
	  Set<T> tmp = collector().declarations(selector);
	  int size = tmp.size();
	  if(size == 1) {
	    return tmp.iterator().next();
	  } else if (size == 0) {
	    return null;
	  } else {
	    throw new MetamodelException("Multiple matches found in "+collector().element().toString() + " using selector "+selector.toString());
	  }

	}

}
