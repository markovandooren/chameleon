package be.kuleuven.cs.distrinet.chameleon.plugin;

public abstract class PluginImpl<C extends PluginContainer<P>, P extends Plugin> implements Plugin<C,P> {

	public abstract PluginImpl<C, P> clone();
	
	@Override
	public <T extends P> void setContainer(C container, Class<T> keyInterface) {
  	if (container!=_container) {
  		C old = _container;
  		// 1) set _language
  		_container = container;
  		// 2) remove old backpointer
  		if (old!=null) {
  			old.removePlugin(keyInterface);
  		}
  		// 3) set new backpointer
  		if (_container!=null) {
  			_container.setPlugin(keyInterface, (T)this);
  		}
  		containerConnected(old, container, keyInterface);
  	}
	}
	
	protected <T extends P> void containerConnected(C oldContainer, C newContainer, Class<T> keyInterface) {
		
	}
	
	private C _container;

	@Override
  public C container() {
      return _container;
  }
}
