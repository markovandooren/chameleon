package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

public class ParameterSubstitution<T extends Parameter> {

	private Class<T> _type;
	
	public Class<T> parameterKind() {
		return _type;
	}
	
	public ParameterSubstitution(Class<T> kind, List<T> parameters) {
		_type = kind;
		_parameters = new ArrayList<T>(parameters);
	}
	
	private List<T> _parameters;
	
	public List<T> parameters() {
		return new ArrayList<T>(_parameters);
	}
}
