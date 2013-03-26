package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;

/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 */
public abstract class ArrayIndex extends ElementImpl {
    public ArrayIndex() {
        super();
    }

    public abstract ArrayIndex clone();
    
}
