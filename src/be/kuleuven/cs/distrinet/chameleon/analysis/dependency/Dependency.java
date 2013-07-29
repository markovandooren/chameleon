package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public class Dependency<D extends Declaration> {

	public Dependency(D declaration) {
		if(declaration == null) {
			throw new IllegalArgumentException("The declaration of a dependenct cannot be null.");
		}
		_declaration = declaration;
	}
	
	private D _declaration;
	
	public D declaration() {
		return _declaration;
	}
	
}
