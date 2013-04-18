package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.language;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.property.AfterProperty;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.property.AroundProperty;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.property.BeforeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.language.LanguageImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContextFactory;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.rejuse.junit.Revision;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

public abstract class AbstractAspectOrientedLanguage extends LanguageImpl implements AspectOrientedLanguage {

	private final ChameleonProperty BEFORE;
	private final ChameleonProperty AFTER;
	private final ChameleonProperty AROUND;
	private final PropertyMutex<ChameleonProperty> ADVICETYPE_MUTEX = new PropertyMutex<ChameleonProperty>();;


	public AbstractAspectOrientedLanguage(String name, LookupContextFactory factory, Revision version) {
		super(name,factory,version);
		BEFORE = new BeforeProperty(this, ADVICETYPE_MUTEX);
		AFTER = new AfterProperty(this, ADVICETYPE_MUTEX);
		AROUND = new AroundProperty(this, ADVICETYPE_MUTEX);
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
