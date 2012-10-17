package chameleon.test.provider;

import java.util.Collection;

import chameleon.workspace.View;

/**
 * An element provider provides test data to a test class. It typically determines which elements
 * must be tested. Often, not all elements in the model are tested. By creating test objects with
 * different element providers, you can for example run a very limited test if you know in which
 * element a test fails.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the element provided by this element provider.
 */
public interface ElementProvider<T> {

	public Collection<T> elements(View view);
}
