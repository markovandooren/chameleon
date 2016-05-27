package org.aikodi.chameleon.aspect.core.model.language;

import org.aikodi.chameleon.aspect.core.model.advice.property.AfterProperty;
import org.aikodi.chameleon.aspect.core.model.advice.property.AroundProperty;
import org.aikodi.chameleon.aspect.core.model.advice.property.BeforeProperty;
import org.aikodi.chameleon.core.language.LanguageImpl;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.property.ChameleonProperty;

import be.kuleuven.cs.distrinet.rejuse.junit.Revision;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

public abstract class AbstractAspectOrientedLanguage extends LanguageImpl implements AspectOrientedLanguage {

	private final ChameleonProperty BEFORE;
	private final ChameleonProperty AFTER;
	private final ChameleonProperty AROUND;
	private final PropertyMutex<ChameleonProperty> ADVICETYPE_MUTEX = new PropertyMutex<ChameleonProperty>();;


	public AbstractAspectOrientedLanguage(String name, LookupContextFactory factory, Revision version) {
		super(name,factory,version);
		BEFORE = add(new BeforeProperty(ADVICETYPE_MUTEX));
		AFTER = add(new AfterProperty(ADVICETYPE_MUTEX));
		AROUND = add(new AroundProperty(ADVICETYPE_MUTEX));
	}

	public AbstractAspectOrientedLanguage(String name, Revision version) {
		this(name,new LookupContextFactory(), version);
	}

	@Override
	public ChameleonProperty BEFORE() {
		return BEFORE;
	}

	@Override
	public ChameleonProperty AFTER() {
		return AFTER;
	}

	@Override
	public ChameleonProperty AROUND() {
		return AROUND;
	}
}
