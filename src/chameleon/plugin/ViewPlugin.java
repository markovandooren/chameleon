package chameleon.plugin;

import chameleon.workspace.View;

public interface ViewPlugin extends Plugin<View, ViewPlugin> {
	
	/**
	 * Return the view of this view plugin.
	 * @return
	 */
  public View view();

  /**
   * Set the language to which this plugin is connected. The bidirectional
   * relation is kept in a consistent state.
   * 
   * T, which represents the plugin interface, must be a super type of the type of this object.
   * 
   * @param view
   * @param connectorInterface
   */
  public <T extends ViewPlugin> void setView(View view, Class<T> keyInterface);

}
