package org.aikodi.chameleon.oo.type;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.*;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguageImpl;
import org.aikodi.chameleon.oo.member.HidesRelation;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.rejuse.collection.CollectionOperations;
import org.aikodi.rejuse.predicate.TypePredicate;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 * A class representing types in object-oriented programs.
 * </p>
 *
 * <p>
 * A class contains <a href="Member.html">members</a> as its content.
 * </p>
 *
 * @author Marko van Dooren
 */
public abstract class ClassImpl extends BasicDeclaration implements Type {

	private List<? extends Declaration> _declarationCache = null;

	/**
	 * Initialize a new class with the given name.
	 */
	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ post name() == name;
	 * 
	 * @ post parent() == null;
	 * 
	 * @
	 */
	public ClassImpl(String name) {
		super(name);
	}

	@Override
	public Type declarationType() {
		return this;
	}

	@Override
	public <P extends Parameter> void addAllParameters(Class<P> kind, Collection<P> parameters) {
		for (P p : parameters) {
			addParameter(kind, p);
		}
	}

	private synchronized List<? extends Declaration> declarationCache() {
		if (_declarationCache != null) {
			return new ArrayList<Declaration>(_declarationCache);
		} else {
			return null;
		}
	}

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return directlyDeclaredMembers();
	}

	@Override
	public String infoName() {
		try {
			try {
				return getFullyQualifiedName();
			} catch (Exception exc) {
				return name();
			}
		} catch (NullPointerException exc) {
			return "";
		}
	}

	@Override
	public Verification verifySubtypeOf(Type otherType, String meaningThisType, String meaningOtherType,
			Element cause) {
		Verification result = Valid.create();
		String messageOther = meaningOtherType + " (" + otherType.infoName() + ").";
		String messageThis = meaningThisType + " (" + infoName() + ")";
		try {
			boolean subtype = subtypeOf(otherType);
			if (!subtype) {
				result = result.and(new BasicProblem(cause, messageThis + " is not a subtype of " + messageOther));
			}
		} catch (Exception e) {
			result = result.and(
					new BasicProblem(cause, "Cannot determine if " + messageThis + " is a subtype of " + messageOther));
		}
		return result;
	}

	@Override
	public synchronized void flushLocalCache() {
		super.flushLocalCache();
		if (_lexicalMembersLookupStrategy != null) {
			_lexicalMembersLookupStrategy.flushCache();
		}
		_declarationCache = null;
		_membersCache = null;
		_superTypeCache = null;
		_judge = null;
		_superTypeAndSelfCache = null;
	}

	private synchronized void setDeclarationCache(List<? extends Declaration> cache) {
		_declarationCache = new ArrayList<Declaration>(cache);
	}

	protected ClassImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#getFullyQualifiedName()
	 */
	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ getPackage().getFullyQualifiedName().equals("") ==> \result == getName();
	 * 
	 * @ ! getPackage().getFullyQualifiedName().equals("") == >
	 * \result.equals(getPackage().getFullyQualifiedName() + getName());
	 * 
	 * @
	 */
	@Override
	public String getFullyQualifiedName() {
		String prefix;
		Type nearest = lexical().nearestAncestor(Type.class);
		if (nearest != null) {
			prefix = nearest.getFullyQualifiedName();
		} else {
			Namespace namespace = lexical().nearestAncestor(NamespaceDeclaration.class).namespace();
			if (namespace != null) {
				prefix = namespace.fullyQualifiedName();
			} else {
				prefix = null;
			}
		}
		return prefix == null ? null : (prefix.equals("") ? "" : prefix + ".") + name();
	}

	@Override
	public String toString() {
		try {
			try {
				return getFullyQualifiedName();
			} catch (Exception exc) {
				return name();
			}
		} catch (NullPointerException exc) {
			return "";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#targetContext()
	 */

	@Override
	public LocalLookupContext<?> targetContext() throws LookupException {
		return localContext();
	}

	private LocalLookupContext<?> _targetContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#localStrategy()
	 */
	@Override
	public LocalLookupContext<?> localContext() throws LookupException {
		if (_targetContext == null) {
			Language language = language();
			if (language != null) {
				_targetContext = language.lookupFactory().createTargetLookupStrategy(this);
			} else {
				throw new LookupException("Element of type " + getClass().getName()
						+ " is not connected to a language. Cannot retrieve target context.");
			}
		}
		return _targetContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#lookupContext(chameleon.core.element.Element)
	 */
	@Override
	public LookupContext lookupContext(Element element) throws LookupException {
		if (element instanceof InheritanceRelation && hasInheritanceRelation((InheritanceRelation) element)) {
			Element parent = parent();
			if (parent != null) {
				return lexicalParametersLookupStrategy();
			} else {
				throw new LookupException("Parent of type is null when looking for the parent context of a type.");
			}
		} else {
			return lexicalMembersLookupStrategy();
		}
	}

	/**
	 * Check whether the given element is an inheritance relation of this type. The
	 * default implementation checks whether the given element is in the collection
	 * returned by inheritanceRelations().
	 * 
	 * This method can be overridden for example to deal with generated inheritance
	 * relations, which are not lexically part of the type.
	 * 
	 * @throws LookupException
	 */
	public boolean hasInheritanceRelation(InheritanceRelation relation) throws LookupException {
		return inheritanceRelations().contains(relation);
	}

	protected LookupContext lexicalMembersLookupStrategy() throws LookupException {
		LookupContext result = _lexicalMembersLookupStrategy;
		// Lazy initialization
		if (result == null) {
			Language language = language();
			if (language == null) {
				throw new LookupException("Parent of type " + name() + " is null.");
			}
			_lexicalMembersLookupStrategy = language.lookupFactory().createLexicalLookupStrategy(targetContext(), this,
					new LookupContextSelector() {

						@Override
						public LookupContext strategy() throws LookupException {
							return lexicalParametersLookupStrategy();
						}
					});
			_lexicalMembersLookupStrategy.enableCache();
			result = _lexicalMembersLookupStrategy;
		}
		return result;
	}

	protected LookupContext _lexicalMembersLookupStrategy;

	protected LookupContext lexicalParametersLookupStrategy() {
		LookupContext result = _lexicalParametersLookupStrategy;
		// lazy initialization
		if (result == null) {
			_lexicalParametersLookupStrategy = language().lookupFactory()
					.createLexicalLookupStrategy(_localInheritanceLookupStrategy, this);
			result = _lexicalParametersLookupStrategy;
		}
		return result;
	}

	protected LookupContext _lexicalParametersLookupStrategy;

	protected LocalParameterBlockLookupStrategy _localInheritanceLookupStrategy = new LocalParameterBlockLookupStrategy(
			this);

	protected class LocalParameterBlockLookupStrategy extends LocalLookupContext<Type> {
		public LocalParameterBlockLookupStrategy(Type element) {
			super(element);
		}

		@Override
		public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector)
				throws LookupException {
			// return selector.selection(parameters());
			List<SelectionResult<D>> result = Lists.create();
			List<ParameterBlock<?>> parameterBlocks = parameterBlocks();
			Iterator<ParameterBlock<?>> iter = parameterBlocks.iterator();
			// If the selector found a match, we stop.
			// We must iterate in reverse.
			while (result.isEmpty() && iter.hasNext()) {
				ParameterBlock<?> imporT = iter.next();
				result.addAll(selector.selection(imporT.parameters()));
			}
			return result;
		}
	}

	@Override
	public <P extends Parameter> int nbTypeParameters(Class<P> kind) {
		return parameterBlock(kind).nbTypeParameters();
	}

	@Override
	public <P extends Parameter> List<P> parameters(Class<P> kind) {
		List<P> result;
		ParameterBlock<P> parameterBlock = parameterBlock(kind);
		if (parameterBlock != null) {
			result = parameterBlock.parameters();
		} else {
			result = ImmutableList.of();
		}
		return result;
	}

	@Override
	public <P extends Parameter> P parameter(Class<P> kind, int index) {
		return parameterBlock(kind).parameter(index);
	}

	@Override
	public List<Declaration> getIntroducedMembers() {
		return ImmutableList.<Declaration>of(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#complete()
	 */
	@Override
	public boolean complete() throws LookupException {
		List<Declaration> members = localMembers(Declaration.class);
		// Only check for actual definitions
		new TypePredicate<Declaration>(Declaration.class).filter(members);
		Iterator<Declaration> iter = members.iterator();
		boolean result = true;
		while (iter.hasNext()) {
			Declaration member = iter.next();
			result = result && (mustBeOverridden(member));
		}
		return result;
	}

	/***********
	 * MEMBERS *
	 ***********/

	@Override
	public void accumulateAllSuperTypes(Set<Type> acc) throws LookupException {
		List<Type> temp = getProperDirectSuperTypes();
		for (Type type : temp) {
			boolean add = true;
			for (Type acced : acc) {
				if (acced.baseType().sameAs(type.baseType())) {
					add = false;
					break;
				}
			}
			if (add) {
				acc.add(type);
				type.accumulateAllSuperTypes(acc);
			}
		}
	}

	@Override
	public Set<Type> getAllSuperTypes() throws LookupException {
		if (_superTypeCache == null) {
			synchronized (this) {
				if (_superTypeCache == null) {
					Set<Type> elements = new HashSet<Type>();
					accumulateAllSuperTypes(elements);
					_superTypeCache = ImmutableSet.<Type>builder().addAll(elements).build();
				}
			}
		}
		return _superTypeCache;
	}

	@Override
	public Set<Type> getSelfAndAllSuperTypesView() throws LookupException {
		try {
			if (_superTypeAndSelfCache == null) {
				synchronized (this) {
					if (_superTypeAndSelfCache == null) {
						Set<Type> elements = new HashSet<Type>();
						newAccumulateSelfAndAllSuperTypes(elements);
						_superTypeAndSelfCache = ImmutableSet.<Type>builder().addAll(elements).build();
					}
				}
			}
			return _superTypeAndSelfCache;

		} catch (ChameleonProgrammerException exc) {
			if (exc.getCause() instanceof LookupException) {
				throw (LookupException) exc.getCause();
			} else {
				throw exc;
			}
		}
	}

	protected volatile SuperTypeJudge _judge;
	protected final AtomicBoolean _judgeLock = new AtomicBoolean();

	public SuperTypeJudge superTypeJudge() throws LookupException {
		SuperTypeJudge result = _judge;
		if (result == null) {
			if (_judgeLock.compareAndSet(false, true)) {
				try {
					result = new SuperTypeJudge();
					accumulateSuperTypeJudge(result);
					_judge = result;
				} catch (LookupException e) {
					throw e;
				} finally {
					_judgeLock.compareAndSet(true, false);
				}
			} else {
				// spin lock
				while (result == null) {
					result = _judge;
				}
			}
		}
		return result;
	}

	@Override
	public void newAccumulateAllSuperTypes(Set<Type> acc) throws LookupException {
		List<Type> temp = getProperDirectSuperTypes();
		for (Type type : temp) {
			boolean add = true;
			for (Type acced : acc) {
				if (acced.baseType().sameAs(type.baseType())) {
					add = false;
					break;
				}
			}
			if (add) {
				type.newAccumulateSelfAndAllSuperTypes(acc);
			}
		}
	}

	@Override
	public void newAccumulateSelfAndAllSuperTypes(Set<Type> acc) throws LookupException {
		acc.add(this);
		newAccumulateAllSuperTypes(acc);
	}

	private Set<Type> _superTypeCache;

	private Set<Type> _superTypeAndSelfCache;

	// TODO: rename to properSubTypeOf

	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ post \result == equals(other) || subTypeOf(other);
	 * 
	 * @
	 */
	@Override
	public boolean assignableTo(Type other) throws LookupException {
		return subtypeOf(other);
	}

	@Override
	public abstract List<InheritanceRelation> explicitNonMemberInheritanceRelations();

	@Override
	public <I extends InheritanceRelation> List<I> explicitNonMemberInheritanceRelations(Class<I> kind) {
		List<I> result = (List) explicitNonMemberInheritanceRelations();
		CollectionOperations.filter(result, d -> kind.isInstance(d));
		return result;
	}

	@Override
	public <I extends InheritanceRelation> List<I> nonMemberInheritanceRelations(Class<I> kind) {
		List<I> result = (List) nonMemberInheritanceRelations();
		CollectionOperations.filter(result, d -> kind.isInstance(d));
		return result;
	}

	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ pre relation != null;
	 * 
	 * @ post inheritanceRelations().contains(relation);
	 * 
	 * @
	 */
	// FIXME rename to addNonMemberInheritanceRelation.
	@Override
	public abstract void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#removeInheritanceRelation(chameleon.oo.type.
	 * inheritance.InheritanceRelation)
	 */
	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ pre relation != null;
	 * 
	 * @ post ! inheritanceRelations().contains(relation);
	 * 
	 * @
	 */
	@Override
	public abstract void removeNonMemberInheritanceRelation(InheritanceRelation relation)
			throws ChameleonProgrammerException;

	@Override
	public void removeAllNonMemberInheritanceRelations() {
		for (InheritanceRelation relation : nonMemberInheritanceRelations()) {
			removeNonMemberInheritanceRelation(relation);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#localMembers(java.lang.Class)
	 */
	@Override
	public <D extends Declaration> List<D> localMembers(final Class<D> kind) throws LookupException {
		return (List<D>) localMembers().stream().filter(o -> kind.isInstance(o)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#localMembers()
	 */
	@Override
	public abstract List<Declaration> localMembers() throws LookupException;

	@Override
	public List<Declaration> implicitMembers() {
		return Collections.emptyList();
	}

	@Override
	public <M extends Declaration> List<M> implicitMembers(Class<M> kind) {
		// implicitMembers returns an immutable list.
		return (List<M>) implicitMembers().stream().filter(o -> kind.isInstance(o)).collect(Collectors.toList());
	}

	public <D extends Declaration> List<? extends SelectionResult<D>> implicitMembers(DeclarationSelector<D> selector)
			throws LookupException {
		return selector.selection(implicitMembers());
	}

	@Override
	public <D extends Declaration> List<D> directlyDeclaredMembers(Class<D> kind) {
		return (List<D>) directlyDeclaredMembers().stream().filter(o -> kind.isInstance(o))
				.collect(Collectors.toList());
	}

	@Override
	public <T extends Declaration> List<T> directlyDeclaredMembers(Class<T> kind, ChameleonProperty property) {
		List<T> result = directlyDeclaredMembers(kind);
		Iterator<T> iter = result.iterator();
		while (iter.hasNext()) {
			T t = iter.next();
			if (!t.isTrue(property)) {
				iter.remove();
			}
		}
		return result;
	}

	@Override
	public List<Declaration> directlyDeclaredMembers() {
		List<Declaration> result = Lists.create();
		for (Declarator m : directlyDeclaredElements()) {
			result.addAll(m.declaredDeclarations());
		}
		return result;
	}

	@Override
	public <D extends Declaration> List<SelectionResult<D>> members(DeclarationSelector<D> selector)
			throws LookupException {
		// 1) perform local search
		boolean nonGreedy = !selector.isGreedy();
		List<SelectionResult<D>> result = (List) localMembers(selector);
		if (nonGreedy || result.isEmpty()) {
			List<SelectionResult<D>> implicitMembers = (List) implicitMembers(selector);
			if (result == Collections.EMPTY_LIST) {
				result = implicitMembers;
			} else {
				result.addAll(implicitMembers);
			}
		}
		// 2) process inheritance relations
		// only if the selector isn't greedy or
		// there are not results.
		if (nonGreedy || result.isEmpty()) {
			result = inheritedMembers(selector, result);
			return result;
		} else {
			return result;
		}
	}

	protected <D extends Declaration> List<SelectionResult<D>> inheritedMembers(DeclarationSelector<D> selector,
			List<SelectionResult<D>> result) throws LookupException {
		for (InheritanceRelation rel : inheritanceRelations()) {
			result = rel.accumulateInheritedMembers(selector, result);
		}
		// We cannot take a shortcut and test for > 1 because if
		// the inheritance relation transforms the member (as is done with subobjects)
		// the transformed member may have to be removed, even if there is only 1.
		selector.filter(result);
		return result;
	}

	@Override
	public void addAllInheritanceRelations(Collection<InheritanceRelation> relations) {
		for (InheritanceRelation rel : relations) {
			addInheritanceRelation(rel);
		}
	}

	@Override
	public List<Declaration> members() throws LookupException {
		return members(Declaration.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <M extends Declaration> List<M> members(final Class<M> kind) throws LookupException {

		// 1) All defined members of the requested kind are added.
		boolean foundInCache = false;
		List<M> result = null;
		if (_membersCache != null) {
			result = _membersCache.get(kind);
			if (result != null) {
				foundInCache = true;
				result = new ArrayList<M>(result);
			}
		}
		if (!foundInCache) {
			result = localMembers(kind);
			result.addAll(implicitMembers(kind));
			// 2) Fetch all potentially inherited members from all inheritance relations
			for (InheritanceRelation rel : inheritanceRelations()) {
				result = rel.accumulateInheritedMembers(kind, result);
			}
			if (_membersCache == null) {
				_membersCache = new HashMap<Class, List>();
			}
			_membersCache.put(kind, new ArrayList(result));
		}
		return result;
	}

	private Map<Class, List> _membersCache;

	@Override
	public <T extends Declarator> List<T> directlyDeclaredElements(Class<T> kind) {
		List<? extends Declarator> tmp = directlyDeclaredElements();
		new TypePredicate<>(kind).filter(tmp);
		return (List<T>) tmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#declarations()
	 */
	@Override
	public List<? extends Declaration> declarations() throws LookupException {
		List<? extends Declaration> result = declarationCache();
		if (result == null) {
			result = members();
			setDeclarationCache(result);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#declarations(chameleon.core.lookup.
	 * DeclarationSelector)
	 */
	@Override
	public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector)
			throws LookupException {
		// FIXME Get rid of this cast (and the members method).
		return members((DeclarationSelector) selector);
	}

	protected void copyContents(Type from) {
		copyContents(from, false);
	}

	protected void copyContents(Type from, boolean link) {
		copyInheritanceRelations(from, link);
		copyEverythingExceptInheritanceRelations(from, link);
	}

	protected void copyInheritanceRelations(Type from, boolean link) {
		List<InheritanceRelation> relations = from.explicitNonMemberInheritanceRelations();
		for (InheritanceRelation relation : relations) {
			InheritanceRelation clone = clone(relation);
			if (link) {
				clone.setOrigin(relation);
			}
			addInheritanceRelation(clone);
		}
	}

	protected void copyEverythingExceptInheritanceRelations(Type from, boolean link) {
		copyParameterBlocks(from, link);
		copyModifiers(from, link);
		copyTypeElements(from, link);
	}

	private void copyTypeElements(Type from, boolean link) {
		for (Declarator el : from.directlyDeclaredElements()) {
			Declarator clone = clone(el);
			if (link) {
				clone.setOrigin(el);
			}
			add(clone);
		}
	}

	protected void copyParameterBlocks(Type from, boolean link) {
		for (ParameterBlock par : parameterBlocks()) {
			removeParameterBlock(par);
		}
		for (ParameterBlock par : from.parameterBlocks()) {
			ParameterBlock clone = clone(par);
			if (link) {
				clone.setOrigin(par);
			}
			addParameterBlock(clone);
		}
	}

	protected void copyModifiers(Type from, boolean link) {
		for (Modifier mod : from.modifiers()) {
			Modifier clone = clone(mod);
			if (link) {
				clone.setOrigin(mod);
			}
			addModifier(clone);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * chameleon.oo.type.Tajp#alias(chameleon.core.declaration.SimpleNameSignature)
	 */
	@Override
	public Type alias(String name) {
		return new TypeAlias(name, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#intersection(chameleon.oo.type.Type)
	 */
	@Override
	public Type intersection(Type type) throws LookupException {
		return type.intersectionDoubleDispatch(this);
	}

	@Override
	public Type intersectionDoubleDispatch(Type type) throws LookupException {
		Type result = language().plugin(ObjectOrientedFactory.class).createIntersectionType(this, type);
		// result.setUniParent(parent());
		return result;
	}

	@Override
	public Type intersectionDoubleDispatch(IntersectionType type) throws LookupException {
		IntersectionType result = clone(type);
		result.addType(type);
		return result;
	}

	@Override
	public Type union(Type type) throws LookupException {
		return type.unionDoubleDispatch(this);
	}

	@Override
	public Type unionDoubleDispatch(Type type) throws LookupException {
		Type result = language().plugin(ObjectOrientedFactory.class).createUnionType(this, type);
		result.setUniParent(parent());
		return result;
	}

	@Override
	public Type unionDoubleDispatch(UnionType type) throws LookupException {
		UnionType result = clone(type);
		result.addType(type);
		return result;
	}

	@Override
	public abstract void replace(Declarator oldElement, Declarator newElement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#baseType()
	 */
	@Override
	public abstract Type baseType();

	protected boolean mustBeOverridden(Declaration member) {
		ObjectOrientedLanguage lang = language(ObjectOrientedLanguage.class);
		// ! CLASS ==> ! ABSTRACT
		return member.isTrue(lang.OVERRIDABLE()) && member.isTrue(lang.INSTANCE()) && member.isFalse(lang.DEFINED());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see chameleon.oo.type.Tajp#verifySelf()
	 */
	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		ObjectOrientedLanguage lang = language(ObjectOrientedLanguage.class);
		if (!isTrue(lang.ABSTRACT())) {
			List<Declaration> members = null;
			try {
				members = members();
			} catch (LookupException e) {
				result = result.and(new BasicProblem(this, "Cannot compute the members of this class"));
			}
			if (members != null) {
				Iterator<Declaration> iter = members.iterator();
				while (iter.hasNext()) {
					Declaration m = iter.next();
					if (!mustBeOverridden(m)) {
						iter.remove();
					} else {
						// DEBUG
						mustBeOverridden(m);
					}
				}
				if (!members.isEmpty()) {
					StringBuffer msg = new StringBuffer("This class must implement the following abstract members: ");
					int size = members.size();
					for (int i = 0; i < size; i++) {
						try {
							msg.append(members.get(i).name());
							if (i < size - 1) {
								msg.append(',');
							}
						} catch (NullPointerException exc) {
						}
					}
					result = result.and(new BasicProblem(this, msg.toString()));
				}
			}
		}
		return result;
	}

	// @Override
	// public <D extends Declaration> List<D>
	// membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws
	// LookupException {
	// List<D> result = Lists.create();
	// if(!selector.declaration().ancestors().contains(this)) {
	// result.addAll((List)members(selector));
	// } else {
	// for(InheritanceRelation relation:inheritanceRelations()) {
	// result.addAll(relation.membersDirectlyOverriddenBy(selector));
	// }
	// }
	// return result;
	// }
	//
	// @Override
	// public <D extends Declaration> List<D>
	// membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws
	// LookupException {
	// List<D> result = Lists.create();
	// for(InheritanceRelation relation:inheritanceRelations()) {
	// result.addAll(relation.membersDirectlyAliasedBy(selector));
	// }
	// return result;
	// }
	//
	// @Override
	// public <D extends Member> List<D>
	// membersDirectlyAliasing(MemberRelationSelector<D> selector) throws
	// LookupException {
	// return ImmutableList.of();
	// }

	// @Override
	// public HidesRelation<? extends Member> hidesRelation() {
	// return _hidesRelation;
	// }
	//
	private static HidesRelation<Type> _hidesRelation = new HidesRelation<Type>(Type.class);
}
