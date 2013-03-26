package be.kuleuven.cs.distrinet.chameleon.plugin;


/**
 * Convenience super class for processors.
 * 
 * @author Marko van Dooren
 */
public abstract class ProcessorImpl<C extends ProcessorContainer<P>, P extends Processor> implements Processor<C,P> {

  private C _container;

  public C container() {
      return _container;
  }

  /**
   * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
   */
  @Override
  public <T extends P> void setContainer(C container, Class<T> connectorInterface) {
    if (container!=_container) {
    	  // 1) remove old backpointer
        if (_container!=null) {
            _container.removeProcessor(connectorInterface, (T)this);
        }
        // 2) set _language
        _container = container;
        // 3) set new backpointer
        if (_container!=null && ! container().processors(connectorInterface).contains(this)) {
            _container.addProcessor(connectorInterface, (T)this);
        }
    }
  }

  public abstract Processor clone();
}
