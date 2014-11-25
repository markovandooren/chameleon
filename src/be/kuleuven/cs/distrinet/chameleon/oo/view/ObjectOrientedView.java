package be.kuleuven.cs.distrinet.chameleon.oo.view;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RootNamespace;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

public abstract class ObjectOrientedView extends View {

	public ObjectOrientedView(RootNamespace namespace, Language language) {
		super(namespace,language);
	}

//	public abstract TypeReference createTypeReference(String fqn);
	
//	public abstract Type getNullType();
	
	public Type findType(String fqn) throws LookupException {
		return ((ObjectOrientedLanguage)language()).findType(fqn, namespace());
	}

}
