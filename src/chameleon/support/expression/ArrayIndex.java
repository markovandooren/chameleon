package chameleon.support.expression;

import chameleon.core.namespace.NamespaceElementImpl;

/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 */
public abstract class ArrayIndex extends NamespaceElementImpl {
//RESEARCH: with type members, the choice of extension is open. With generic params, we are forced to decide on extensibility
//          when we develop this class, possibly preventing unanticipated reuse.
	
	
    public ArrayIndex() {
        super();
    }

    public abstract ArrayIndex clone();
    
}
