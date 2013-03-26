/**
 * Created on 16-nov-06
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.editors.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * Delagates the chameleon actions. 
 * Can be used to handle menu and toolbars-actions.
 * 
 * @author Tim Vermeiren
 */
public class ChameleonActionDelegate extends ActionDelegate {
	
	@Override
	public void run(IAction action) {
		System.out.println("ChameleonAction is being executed...");
	}
	
}
