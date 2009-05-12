package chameleon.core.method;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.java.collections.Visitor;
import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.accessibility.AccessibilityDomain;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Definition;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.member.MemberImpl;
import chameleon.core.method.exception.ExceptionClause;
import chameleon.core.method.exception.TypeExceptionDeclaration;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierContainer;
import chameleon.core.statement.Block;
import chameleon.core.statement.ExceptionPair;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.core.variable.FormalParameter;
import chameleon.util.Util;


public abstract class Method<E extends Method<E,H,S>, H extends MethodHeader<H, E, S>, S extends MethodSignature> extends MemberImpl<E,Type,S,Method> implements Definition<E,Type,S>, ModifierContainer<E,Type> {

	public Method(H header) {
		setHeader(header);
	}
	
	public boolean complete() {
	  return getImplementation() != null;
	}

	public List<FormalParameter> getParameters() {
	  return header().getParameters();
	}
	
	public S signature() {
		return header().signature();
	}
	
	 public void setHeader(H header) {
	    if(header != null) {
	      _header.connectTo(header.parentLink());
	    } else {
	      _header.connectTo(null);
	    }
	  }
	  
	  /**
	   * Return the signature of this member.
	   */
	  public H header() {
	    return _header.getOtherEnd();
	  }
	  
	  private Reference<E, H> _header = new Reference<E, H>((E) this);
	
	/******************
	 * IMPLEMENTATION *
	 ******************/
	
	public abstract Implementation getImplementation();

//	public Reference<Method,Implementation> getImplementationlink() {
//		return _implementationLink;
//	}



	public abstract void setImplementation(Implementation implementation);



	/****/

	/*@
	 @ also public behavior
	 @
	 @ // A method introduces itself as a method.
	 @ post \result.contains(this);
	 @ post \result.size() == 1;
	 @*/
	public Set<Member> getIntroducedMembers() {
		Set result = new HashSet<Member>();
		result.add(this);
		return result;
	}



	/********
	 * MISC *
	 ********/

	/**
	 * Return the exception types that this method throw in a worst case scenario. Subtypes
	 * are not necessarily included.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result.equals(getExceptionClause().getWorstCaseExceptions());
	 @*/
	public Set getWorstCaseExceptions() throws MetamodelException {
		return getExceptionClause().getWorstCaseExceptions();
	}

	/**
	 * Return the checked exceptions that can be thrown by the
	 * <b>body</b> of this method if exceptions anchors are <b>not</b> taken into account.
	 * If this method has no body, the result will be empty.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @ post getBody() == null ==> \result.isEmpty();
	 @*/
	public Set getAbsoluteThrownCheckedExceptions() throws MetamodelException {
		throw new Error("Implements exception anchors again");
//		Set excs = new HashSet();
//		Block body = getBody();
//		if(body != null) {
//			Collection declarations = body.getAbsCEL().getDeclarations();
//			final ExceptionClause exceptionClause = new ExceptionClause();
//			new Visitor() {
//				public void visit(Object element) {
//					exceptionClause.add((ExceptionDeclaration)element);
//				}
//			}.applyTo(declarations);
//
//			StubExceptionClauseContainer stub = new StubExceptionClauseContainer(this, exceptionClause, lexicalContext());
//			excs = exceptionClause.getWorstCaseExceptions();
//		}
//		return excs;
	}

	/*@
	  @ also public behavior
	  @
	  @ post \result == getType();
	  @*/
	public Type getNearestType() {
		return parent();
	}

	/**********
	 * ACCESS *
	 **********/

//	public boolean sameSignatureAs(Method other) throws MetamodelException {
//		boolean result = other.getName().equals(getName());
//		int nbParams = getNbParameters();
//		result = result && (other.getNbParameters() == nbParams);
//		if (result) {
//			List mine = getParameters();
//			List others = other.getParameters();
//			for (int i = 0; result && (i < nbParams); i++) {
//				if (!((FormalParameter)mine.get(i)).getType().equals(((FormalParameter)others.get(i)).getType())) {
//					result = false;
//				}
//			}
//		}
//		return result;
//	}

//	/*@
//	 @ also public behavior
//	 @
//	 @ post \result == ! isAbstract() &&
//	 @                 (
//	 @                   equals(method) ||
//	 @                   (
//	 @                     (method != null) &&
//	 @                     method.isAbstract() &&
//	 @                     !is(Static.PROTOTYPE) &&
//	 @                     !method.is(Static.PROTOTYPE) &&
//	 @                     sameSignatureAs(method)
//	 @                   )
//	 @                 );
//	 @*/
//	public final boolean canImplement(Method method) throws MetamodelException {
//		return  (equals(method) || ((method != null) && !is(new Static()) && (! method.is(new Static())) && method.complete() && sameSignatureAs(method)));
//	}
	
	/***************
	 * RETURN TYPE *
	 ***************/

	public abstract TypeReference getReturnTypeReference();

	/**
	 * Return the type of this method.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == getReturnTypeReference().getType();
	 @*/
	public Type getType() throws MetamodelException {
		return getReturnTypeReference().getType();
	}


	public E clone() {
		final E result = cloneThis();
		// MODIFIERS
		new Visitor<Modifier>() {
			public void visit(Modifier element) {
				result.addModifier(element.clone());
			}
		}.applyTo(modifiers());
		// EXCEPTION CLAUSE
		result.setExceptionClause(getExceptionClause().clone());
		// HEADER
		result.setHeader((H)header().clone());
		// IMPLEMENTATION
		if(getImplementation() != null) {
			result.setImplementation(getImplementation().clone());
		}

		return result;
	}

	protected abstract E cloneThis();

	/**
	 * Check whether or not the implementation of this method is compatible with the exception clause
	 * of this method.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == (getImplementation() == null) || getImplementation().compatible();
	 @*/
	public boolean hasCompatibleImplementation() throws MetamodelException {
		return (getImplementation() == null) || getImplementation().compatible();
	}

	/**
	 * Check whether or not all checked exceptions caught in catch blocks can actually be thrown.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == (getImplementation() == null) || getImplementation().hasValidCatchClauses();
	 @*/
	public boolean hasValidCatchClauses() throws MetamodelException {
		return (getImplementation() == null) || getImplementation().hasValidCatchClauses();
	}

  public abstract ExceptionClause getExceptionClause();

  public abstract void setExceptionClause(ExceptionClause clause);

	/**
	 * Check whether or not all elements of the exception are at least as visible as this method.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == getExceptionClause().hasValidAccessibility();
	 @*/
	public boolean hasValidExceptionClauseAccessibility() throws MetamodelException {
		return getExceptionClause().hasValidAccessibility();
	}

	/**
	 * Check whether or not this method has an acyclic exception graph.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == hasAcyclicExceptionGraph(new HashSet());
	 @*/
	public boolean hasAcyclicExceptionGraph() throws MetamodelException {
		return hasAcyclicExceptionGraph(new HashSet());
	}

	/**
	 * @param done
	 * @return
	 */
	public boolean hasAcyclicExceptionGraph(Set done) throws MetamodelException {
		if(done.contains(this)) {
			return false;
		}
		else {
			Set newDone = new HashSet(done);
			newDone.add(this);
			return getExceptionClause().isAcyclic(newDone);
		}
	}

	/**
	 * Check whether or not the exception clause of this method is compatible with the
	 * exception clauses of all super methods.
	 * @return
	 * @throws MetamodelException
	 */
	public boolean hasValidOverridingExceptionClause() throws MetamodelException {
		try {
			Set methods = directlyOverriddenMembers();
			return new PrimitivePredicate() {
				public boolean eval(Object o) throws MetamodelException {
					Method method = (Method)o;
					return getExceptionClause().compatibleWith(method.getExceptionClause());
				}
			}.forAll(methods);
		}
		catch (MetamodelException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
	}

//	/**
//	 * @return
//	 */
//	public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//		return getParent().getTypeAccessibilityDomain().intersect(getAccessModifier().getAccessibilityDomain(getParent()));
//	}


	/*@
	 @ public behavior
	 @
	 @ post \result.containsAll(getParameters());
	 @ post \result.contains(getExceptionClause());
	 @ post getImplementation() != null ==> \result.contains(getImplementation());
	 @*/
	public List<? extends Element> children() {
		List<Element> result = Util.createNonNullList(getImplementation());
		result.add(signature());
		result.add(getExceptionClause());
		return result;
	}

	/**
	 * Return the body of this method.
	 */
	public Block getBody() {
		if(getImplementation() == null) {
			return null;
		}
		else {
			return getImplementation().getBody();
		}
	}

	public Set getDirectlyThrownExceptions() throws MetamodelException {
		Block block = getBody();
		Set result = new HashSet();
		if (block != null) {
			List pairs = block.getCEL().getPairs();
			Iterator iter = pairs.iterator();
			while (iter.hasNext()) {
				ExceptionPair pair = (ExceptionPair)iter.next();
				if (pair.getDeclaration() instanceof TypeExceptionDeclaration) {
					result.add(pair.getException());
				}
			}
		}
		return result;
	}


// /*@
//	 @ public behavior
//	 @
//	 @ post (other == null) || (! is(Static.PROTOTYPE)) || (!other.is(Static.PROTOTYPE)) || (other.is(Final.PROTOTYPE)) || ! getContainingType().subTypeOf(other.getContainingType()) || ! sameSignatureAs(other)
//	 @        ==> \result == false;
//	 @*/
//	public final boolean overrides(Member other) throws MetamodelException {
//		boolean result;
//	  if(other instanceof Method) {
//	    assert other != null;
//	    Method<? extends Method> method = (Method<? extends Method>) other;
//	    Ternary temp = method.is(language().OVERRIDABLE);
//	    boolean overridable;
//	    if(temp == Ternary.TRUE) {
//	      overridable = true;
//	    } else if (temp == Ternary.FALSE) {
//	      overridable = false;
//	    } else {
//	      throw new MetamodelException("The overridability of the other method could not be determined.");
//	    }
//	    result = overridable && sameSignatureAs(method) && getParent().subTypeOf(method.getParent()) && sameKind(method);
//	  } else {
//	    result = false;
//	  }
//	  return result; 
//		
//		//(other != null) && (! is(new Static())) && (!other.is(new Static())) && (!other.is(new Final())) && getParent().subTypeOf(other.getParent()) && sameSignatureAs(other) && sameKind(other);
//	}

	public abstract boolean sameKind(Method other);

	//TODO dees mag teruggezet worden als de metamodelfactory dat correct doet
//	@Override
//	public void reParse(ChameleonDocument doc, IMetaModelFactory factory) {
//		//Via codewriter doen.!!
//		String language = doc.getLanguage();
//		Syntax writer;
//		try {
//			String path = LanguageMgt.getInstance().getWriterPath(language);
//			writer =  (Syntax)((Class) Class.forName(path)).newInstance();
////			java.lang.reflect.Method methode = writer.getMethod("toCode",new Class[]{ElementImpl.class});
////			String methodeCode = (String)methode.invoke(this,language);
////			factory.replaceMethod(doc,methodeCode,this);
//			factory.replaceMethod(doc,writer.toCode(this),this);
//		} catch (RecognitionException e) {
//
//			e.printStackTrace();
//		} catch (TokenStreamException e) {
//
//			e.printStackTrace();
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		} catch (MetamodelException e) {
//
//			e.printStackTrace();
//		} catch (InstantiationException e1) {
//
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//
//			e1.printStackTrace();
//		} catch (ClassNotFoundException e1) {
//
//			e1.printStackTrace();
//		}catch (SecurityException e) {
//
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//
//			e.printStackTrace();
//		}
//
//	}

	public Method alias(MethodHeader sig) {
		return new MethodAlias(sig,this);
	}
	
}
