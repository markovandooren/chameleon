package org.aikodi.chameleon.oo.view;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.workspace.View;

public abstract class ObjectOrientedView extends View {

	public ObjectOrientedView(RootNamespace namespace, Language language) {
		super(namespace,language);
	}

	public Type findType(String fqn) throws LookupException {
	// return ((ObjectOrientedLanguage)language()).findType(fqn, namespace());
		return namespace().find(fqn, Type.class);
	}

	public abstract Type topLevelType();
}
