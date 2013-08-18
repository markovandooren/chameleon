package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public abstract class PredicateSelector<T,C extends Control>  {

	public PredicateSelector() {
	}
	
	public abstract C createControl(Composite parent);
	
	public abstract UniversalPredicate<? super T, Nothing> predicate();
}
