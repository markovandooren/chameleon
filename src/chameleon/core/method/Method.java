package chameleon.core.method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;
import org.rejuse.java.collections.Visitor;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.AbstractPredicate;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.DeclarationWithParametersHeader;
import chameleon.core.declaration.DeclarationWithParametersSignature;
import chameleon.core.declaration.Definition;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.Target;
import chameleon.core.member.Member;
import chameleon.core.member.MemberImpl;
import chameleon.core.method.exception.ExceptionClause;
import chameleon.core.method.exception.TypeExceptionDeclaration;
import chameleon.core.modifier.Modifier;
import chameleon.core.statement.Block;
import chameleon.core.statement.ExceptionTuple;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.core.variable.FormalParameter;
import chameleon.oo.type.DeclarationWithType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.util.Util;

/**
 * A class of methods.
 * 
 * A method has a header, which contains its return type, throws clause, name, arguments, ... 
 * A method can also have an implementation, which can be a native implementation, or a regular implementation.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <H>
 * @param <S>
 * @param <M>
 */
public abstract class Method<E extends Method<E,H,S,M>, H extends DeclarationWithParametersHeader<H, S>, S extends DeclarationWithParametersSignature, M extends Method> extends MemberImpl<E,S,M> implements Definition<E,S,M>, DeclarationContainer<E>, Target<E>, DeclarationWithType<E,S,M> {

	/**
	 * Initialize a new method with the given header.
	 * @param header
	 */
 /*@
   @ public behavior
   @
   @ post header() == header;
   @*/ 
	public Method(H header) {
		setHeader(header);
	}
	
	public Type declarationType() throws LookupException {
		return returnType();
	}
	
	public Ternary complete() {
	  return (body() == null ? Ternary.FALSE : Ternary.TRUE);
	}

	public List<FormalParameter> formalParameters() {
	  H header = header();
	  if(header != null) {
		  return header.formalParameters();
	  } else {
	  	return new ArrayList<FormalParameter>();
	  }
	}
	
	/**
	 * The index starts at 1.
	 */
	public FormalParameter formalParameter(int index) {
		return header().formalParameter(index);
	}
	
	public FormalParameter lastFormalParameter() {
		int nbFormalParameters = nbFormalParameters();
		if(nbFormalParameters > 0) {
		  return formalParameter(nbFormalParameters);
		} else {
			return null;
		}
	}
	
	public int nbFormalParameters() {
		return header().nbFormalParameters();
	}
	
	public List<TypeParameter> typeParameters() {
	  return header().typeParameters();
	}
	
	/**
	 * Return the index-th type parameter. Indices start at 1.
	 */
	public TypeParameter typeParameter(int index) {
		return header().typeParameter(index);
	}

	/**
	 * Return a string representation for the name of the method. This is just a convenience method.
	 * DO NOT USE IT TO IDENTIFY METHODS! The signature identifies a method, not just its name.
	 * @return
	 */
	public String name() {
		H header = header();
		if(header != null) {
		  return header.name();
		} else {
			return null;
		}
	}
	
	public S signature() {
		return header().signature();
	}
	
	public void setSignature(Signature signature) {
		setHeader(header().createFromSignature(signature));
	}
	
	public void setName(String name) {
		header().setName(name);
	}
	
	/**
	 * Set the header of this method.
	 * @param header
	 */
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
	  
	  private SingleAssociation<E, H> _header = new SingleAssociation<E, H>((E) this);
	
	/******************
	 * IMPLEMENTATION *
	 ******************/
	
	public abstract Implementation implementation();

	public abstract void setImplementation(Implementation implementation);

	/****/

	/*@
	 @ also public behavior
	 @
	 @ // A method introduces itself as a method.
	 @ post \result.contains(this);
	 @ post \result.size() == 1;
	 @*/
	public List<Member> getIntroducedMembers() {
		List<Member> result = new ArrayList<Member>();
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
	public Set getWorstCaseExceptions() throws LookupException {
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
//	public Set getAbsoluteThrownCheckedExceptions() throws MetamodelException {
//		throw new Error("Implements exception anchors again");
////		Set excs = new HashSet();
////		Block body = getBody();
////		if(body != null) {
////			Collection declarations = body.getAbsCEL().getDeclarations();
////			final ExceptionClause exceptionClause = new ExceptionClause();
////			new Visitor() {
////				public void visit(Object element) {
////					exceptionClause.add((ExceptionDeclaration)element);
////				}
////			}.applyTo(declarations);
////
////			StubExceptionClauseContainer stub = new StubExceptionClauseContainer(this, exceptionClause, lexicalContext());
////			excs = exceptionClause.getWorstCaseExceptions();
////		}
////		return excs;
//	}

//	/*@
//	  @ also public behavior
//	  @
//	  @ post \result == getType();
//	  @*/
//	public Type getNearestType() {
//		return nearestAncestor(Type.class);
//	}

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

	public abstract TypeReference returnTypeReference();
	
	public abstract void setReturnTypeReference(TypeReference type);

	/**
	 * Return the type of this method.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == getReturnTypeReference().getType();
	 @*/
	public Type returnType() throws LookupException {
		if(returnTypeReference() != null) {
		  return returnTypeReference().getType();
		} else {
			throw new LookupException("Return type reference of method is null");
		}
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
		if(implementation() != null) {
			result.setImplementation(implementation().clone());
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
	public boolean hasCompatibleImplementation() throws LookupException {
		return (implementation() == null) || implementation().compatible();
	}

	/**
	 * Check whether or not all checked exceptions caught in catch blocks can actually be thrown.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result == (getImplementation() == null) || getImplementation().hasValidCatchClauses();
	 @*/
	public boolean hasValidCatchClauses() throws LookupException {
		return (implementation() == null) || implementation().hasValidCatchClauses();
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
	public boolean hasValidExceptionClauseAccessibility() throws LookupException {
		return getExceptionClause().hasValidAccessibility();
	}

//	/**
//	 * Check whether or not this method has an acyclic exception graph.
//	 */
//	/*@
//	 @ public behavior
//	 @
//	 @ post \result == hasAcyclicExceptionGraph(new HashSet());
//	 @*/
//	public boolean hasAcyclicExceptionGraph() throws LookupException {
//		return hasAcyclicExceptionGraph(new HashSet());
//	}
//
//	/**
//	 * @param done
//	 * @return
//	 */
//	public boolean hasAcyclicExceptionGraph(Set done) throws LookupException {
//		if(done.contains(this)) {
//			return false;
//		}
//		else {
//			Set newDone = new HashSet(done);
//			newDone.add(this);
//			return getExceptionClause().isAcyclic(newDone);
//		}
//	}

	/**
	 * Check whether or not the exception clause of this method is compatible with the
	 * exception clauses of all super methods.
	 * @return
	 * @throws LookupException
	 */
	public boolean hasValidOverridingExceptionClause() throws LookupException {
		try {
			List methods = directlyOverriddenMembers();
			return new AbstractPredicate() {
				public boolean eval(Object o) throws LookupException {
					Method method = (Method)o;
					return getExceptionClause().compatibleWith(method.getExceptionClause());
				}
			}.forAll(methods);
		}
		catch (LookupException e) {
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
	 @ post \result.containsAll(modifiers());
	 @ post implementation() != null ==> \result.contains(implementation());
	 @ post header() != null ==> \result.contains(header());
	 @ post getExceptionClause() != null ==> \result.contains(getExceptionClause());
	 @ post getReturnTypeReference() != null ==> \result.contains(getReturnTypeReference());
	 @*/
	public List<Element> children() {
		List<Element> result = super.children();
		Util.addNonNull(implementation(),result);
		Util.addNonNull(header(),result);
		Util.addNonNull(getExceptionClause(), result);
		Util.addNonNull(returnTypeReference(), result);
		return result;
	}

	/**
	 * Return the body of this method.
	 */
	public Block body() {
		if(implementation() == null) {
			return null;
		}
		else {
			return implementation().getBody();
		}
	}

	public Set getDirectlyThrownExceptions() throws LookupException {
		Block block = body();
		Set result = new HashSet();
		if (block != null) {
			List pairs = block.getCEL().getPairs();
			Iterator iter = pairs.iterator();
			while (iter.hasNext()) {
				ExceptionTuple pair = (ExceptionTuple)iter.next();
				if (pair.getDeclaration() instanceof TypeExceptionDeclaration) {
					result.add(pair.getException());
				}
			}
		}
		return result;
	}

  public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
  	if(element == header()) {
  		return parent().lexicalLookupStrategy(this);
  	} else {
  	  if(_lexical == null) {
	      _lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(), this);
  	  }
  	  return _lexical;
  	}
  }
  
  public LookupStrategy localLookupStrategy() {
  	if(_local == null) {
  		_local = language().lookupFactory().createTargetLookupStrategy(this);
  	}
  	return _local;
  }
  
  private LookupStrategy _local;
  
  private LookupStrategy _lexical;
  
  public LookupStrategy targetContext() throws LookupException {
  	return returnType().lexicalLookupStrategy();
  }
  
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

  public List<? extends Declaration> declarations() {
  	return header().declarations();
  }
  
  public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
  	return header().declarations(selector);
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

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(header() == null) {
			result = result.and(new BasicProblem(this, "This method has no header"));
		}
		return result;
	}
	
	//TODO dees mag teruggezet worden als de metamodelfactory dat correct doet
	//     NO IT CANNOT BE RESTORED! MOVE TO TOOL EXTENSION FOR EDITOR.
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

//	public Method alias(S sig) {
//		return new MethodAlias(sig,this);
//	}
	
	public Declaration declarator() {
		return this;
	}
	
	/**
	 * For debugging purposes because Eclipse detail formatters simply don't work.
	 */
	public String toString() {
		Type container = nearestAncestor(Type.class);
		return (container == null ? "" : container.getFullyQualifiedName() +".")+signature().toString();
	}

}
