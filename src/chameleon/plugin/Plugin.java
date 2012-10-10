package chameleon.plugin;

public interface Plugin<C extends PluginContainer<P>, P extends Plugin> extends Cloneable {

  /**
   * Return the container to which this connector is connected.
   */
  public C container();

  /**
   * Set the container to which this plugin is connected. The bidirectional
   * relation is kept in a consistent state.
   * 
   * T, which represents the plugin interface, must be a super type of the type of this object.
   * It is used as the key.
   * 
   * @param container
   * @param keyInterface
   */
  public <T extends P> void setContainer(C container, Class<T> keyInterface);
  
  /**
   * Clone this connector.
   * @return
   */
  public Plugin<C,P> clone();
  
}
