package chameleon.oo.method;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.Target;
import chameleon.oo.member.DeclarationWithParametersHeader;
import chameleon.oo.member.DeclarationWithParametersSignature;
import chameleon.oo.member.Member;
import chameleon.oo.member.MemberImpl;
import chameleon.oo.type.DeclarationWithType;
import chameleon.oo.type.Type;
import chameleon.oo.variable.FormalParameter;
import chameleon.util.Util;
import chameleon.util.association.Single;

public abstract class DeclarationWithParameters extends MemberImpl implements DeclarationContainer, Target, DeclarationWithType {

	/**
	 * Initialize a new method with the given header.
	 * @param header
	 */
 /*@
   @ public behavior
   @
   @ post header() == header;
   @*/ 
	public DeclarationWithParameters(MethodHeader header) {
		setHeader(header);
	}
	
	protected DeclarationWithParameters() {
		
	}
	
	/**
	 * Set the header of this method.
	 * @param header
	 */
	public void setHeader(MethodHeader header) {
	  set(_header,header);
	}
	  
	  /**
	   * Return the signature of this member.
	   */
	  public MethodHeader header() {
	    return _header.getOtherEnd();
	  }
	  
	  private Single<MethodHeader> _header = new Single<MethodHeader>(this,true);
	
		public DeclarationWithParametersSignature signature() {
			return header().signature();
		}
		
		public void setSignature(Signature signature) {
			setHeader((MethodHeader) header().createFromSignature(signature));
		}
		
		public void setName(String name) {
			header().setName(name);
		}
		
		/**
		 * Return a string representation for the name of the method. This is just a convenience method.
		 * DO NOT USE IT TO IDENTIFY ELEMENTS! The signature is used for identification, not just the name.
		 * @return
		 */
		public String name() {
			DeclarationWithParametersHeader header = header();
			if(header != null) {
			  return header.name();
			} else {
				return null;
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
		
		public List<FormalParameter> formalParameters() {
			DeclarationWithParametersHeader header = header();
		  if(header != null) {
			  return header.formalParameters();
		  } else {
		  	return new ArrayList<FormalParameter>();
		  }
		}
		
		public List<Type> formalParameterTypes() throws LookupException {
			DeclarationWithParametersHeader header = header();
		  if(header != null) {
			  return header.formalParameterTypes();
		  } else {
		  	return new ArrayList<Type>();
		  }
		}
		
	 /*@
		 @ also public behavior
		 @
		 @ // A method introduces itself as a method.
		 @ post \result.contains(this);
		 @ post \result.size() == 1;
		 @*/
		public List<Member> getIntroducedMembers() {
			return (List)Util.createSingletonList(this);
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
	  
	  public LookupStrategy localStrategy() {
	  	return localLookupStrategy();
	  }
	  
	  private LookupStrategy _local;
	  
	  private LookupStrategy _lexical;
	  
		public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
			return declarations();
		}

	  public List<? extends Declaration> declarations() {
	  	return header().declarations();
	  }
	  
	  public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
	  	return header().declarations(selector);
	  }

		public Declaration declarator() {
			return this;
		}
		

}
