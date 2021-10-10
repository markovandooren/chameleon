package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.NameSelector;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.CrossReferenceWithName;
import org.aikodi.chameleon.core.reference.CrossReferenceWithTarget;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.type.DeclarationWithType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

import java.lang.ref.SoftReference;

public class NameExpression extends TargetedExpression
		implements CrossReferenceWithName<DeclarationWithType>, CrossReferenceWithTarget<DeclarationWithType> {

	public NameExpression(String identifier) {
		_name = identifier;
	}

	public NameExpression(String identifier, CrossReferenceTarget target) {
		this(identifier);
		setTarget(target);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<DeclarationWithType> referencedType() {
		return DeclarationWithType.class;
	}

	/********
	 * NAME *
	 ********/

	@Override
	public String toString() {
		return name();
	}

	@Override
	public String name() {
		return _name;
	}

	@Override
	public void setName(String name) {
		String old = _name;
		_name = name;
		if (changeNotificationEnabled()) {
			notify(new NameChanged(old, name));
		}
	}

	private String _name;
	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

	@Override
	public CrossReferenceTarget getTarget() {
		return _target.getOtherEnd();
	}

	@Override
	public void setTarget(CrossReferenceTarget target) {
		set(_target, target);
	}

	@Override
	protected Type actualType() throws LookupException {
		return getElement().declarationType();
	}

	@Override
	public NameExpression cloneSelf() {
		return new NameExpression(name());
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		try {
			Element element = getElement();
		} catch (LookupException e) {
			result = result.and(new BasicProblem(this, "The referenced element cannot be found."));
		}
		return result;
	}

	@Override
	public DeclarationWithType getElement() throws LookupException {
		return getElement(selector());
	}

	private SoftReference<DeclarationWithType> _cache;

	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		_cache = null;
	}

	protected DeclarationWithType getCache() {
		return (_cache == null ? null : _cache.get());
	}

	protected void setCache(DeclarationWithType value) {
		_cache = new SoftReference<DeclarationWithType>(value);
	}

	public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		X result = null;

		// OPTIMISATION
		boolean cache = selector.equals(selector());
		if (cache) {
			result = (X) getCache();
		}
		if (result != null) {
			return result;
		}
		synchronized (this) {

			DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
			CrossReferenceTarget target = getTarget();
			if (target != null) {
				target.targetContext().lookUp(collector);
			} else {
				lexicalContext().lookUp(collector);
			}
			result = collector.result();
			if (cache) {
				setCache((DeclarationWithType) result);
			}
			return result;
		}
	}

	public DeclarationSelector<DeclarationWithType> selector() {
		return _selector;
	}

	private DeclarationSelector<DeclarationWithType> _selector = new NameSelector<DeclarationWithType>(
			DeclarationWithType.class) {
		@Override
		public String name() {
			return NameExpression.this.name();
		}
	};

}
