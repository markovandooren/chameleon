package org.aikodi.chameleon.analysis;

import org.aikodi.chameleon.core.element.Element;

import java.util.List;

/**
 * An interface for setting the configuration options for an analysis.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements that are analyzed.
 * @param <R> The type of the result of an analysis.
 */
public interface AnalysisOptions<E extends Element, R extends Result<R>> {

  /**
   * Perform the analysis based on the configured options.
   * 
   * @return The result of the analysis. 
   */
  public Result<?> analyze();
  
	public List<? extends OptionGroup> optionGroups();

	public void setContext(Object context);
}
