package chameleon.util.association;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class Multi<T extends Element> extends OrderedMultiAssociation<Element, T> {

	public Multi(Element element) {
		super(element);
		_max = -1;
	}
	
	public Multi(Element element, int min, int max) {
		this(element);
		if(min > max) {
			throw new IllegalArgumentException();
		}
		_min=min;
		_max=max;
	}
	
	public Multi(Element element, String role) {
		this(element);
		setRole(role);
	}
	
	public Multi(Element element, int min, int max, String role) {
		this(element,min,max);
		setRole(role);
	}
	
	public int min() {
		return _min;
	}
	
	public int max() {
		return _max;
	}
	
	private int _min;
	private int _max;
	
	public boolean isConstrained() {
		return _max < 1;
	}
	
	public VerificationResult verify() {
		VerificationResult result = Valid.create();
		if(isConstrained()) {
			int size = size();
			if(size < min()) {
				result = result.and(new BasicProblem(getObject(), "At least " + min()+" " + role() + " were expected, but only "+size+ " are defined."));
			}
			if(size > max()) {
				result = result.and(new BasicProblem(getObject(), "At most " + max()+" " + role() +" were expected, but "+size+ " are defined."));
			}
		}
		return result;
	}
	
	public String role() {
		return _role;
	}
	
	public void setRole(String role) {
		_role = role;
	}
	
	private String _role = "elements";

}
