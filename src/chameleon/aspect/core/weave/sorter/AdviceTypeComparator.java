package chameleon.aspect.core.weave.sorter;

import java.util.Comparator;

import org.rejuse.property.Property;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.weave.JoinPointWeaver;

/**
 * 	This comparator orders WeavingEncapsulators according to the advice type:
 * 
 * 	Around, Before, After
 * 
 * 	Order of advice of the same type is undefined
 * 
 * 	@author Jens De Temmerman
 *
 */
public class AdviceTypeComparator implements Comparator<JoinPointWeaver> {

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public int compare(JoinPointWeaver encapsulator1, JoinPointWeaver encapsulator2) {
		Advice advice1 = encapsulator1.getAdvice();
		Advice advice2 = encapsulator2.getAdvice();
		Property around = advice1.language().property("advicetype.around");
		Property before = advice1.language().property("advicetype.before");
		Property after = advice1.language().property("advicetype.after");
		
		if (advice1.properties().contains(around)) {
			if (advice2.properties().contains(around))
				return 0;
			else 
				return -1;
		} else if (advice1.properties().contains(before)) {
			if (advice2.properties().contains(around))
				return 1;
			else if (advice2.properties().contains(before))
				return 0;
			else
				return -1;
		} else {
			if (advice2.properties().contains(after))
				return 0;
			else
				return 1;
		}
	}
}
