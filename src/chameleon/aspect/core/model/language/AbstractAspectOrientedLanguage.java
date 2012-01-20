package chameleon.aspect.core.model.language;

import org.rejuse.property.PropertyMutex;

import chameleon.aspect.core.model.advice.property.AfterProperty;
import chameleon.aspect.core.model.advice.property.AroundProperty;
import chameleon.aspect.core.model.advice.property.BeforeProperty;
import chameleon.core.language.LanguageImpl;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.property.ChameleonProperty;

public abstract class AbstractAspectOrientedLanguage extends LanguageImpl implements AspectOrientedLanguage {

	private final ChameleonProperty BEFORE;
	private final ChameleonProperty AFTER;
	private final ChameleonProperty AROUND;
	private final PropertyMutex<ChameleonProperty> ADVICETYPE_MUTEX = new PropertyMutex<ChameleonProperty>();;


	public AbstractAspectOrientedLanguage(String name, LookupStrategyFactory factory) {
		super(name,factory);
		BEFORE = new BeforeProperty(this, ADVICETYPE_MUTEX);
		AFTER = new AfterProperty(this, ADVICETYPE_MUTEX);
		AROUND = new AroundProperty(this, ADVICETYPE_MUTEX);
	}

	public AbstractAspectOrientedLanguage(String name) {
		this(name,new LookupStrategyFactory());
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
