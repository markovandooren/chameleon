package be.kuleuven.cs.distrinet.chameleon.oo.method;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.Target;
import be.kuleuven.cs.distrinet.chameleon.oo.member.DeclarationWithParametersHeader;
import be.kuleuven.cs.distrinet.chameleon.oo.member.DeclarationWithParametersSignature;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.member.MemberImpl;
import be.kuleuven.cs.distrinet.chameleon.oo.type.DeclarationWithType;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.TypeParameter;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
		
		public List<TypeParameter> typeParameters() {
		  return header().typeParameters();
		}
		
		public int nbTypeParameters() {
			return header().nbTypeParameters();
		}
		
		/**
		 * Return the index-th type parameter. Indices start at 1.
		 */
		public TypeParameter typeParameter(int index) {
			return header().typeParameter(index);
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

	  public LookupContext lookupContext(Element element) throws LookupException {
	  	if(element == header()) {
	  		return parent().lookupContext(this);
	  	} else {
	  	  if(_lexical == null) {
		      _lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(), this);
	  	  }
	  	  return _lexical;
	  	}
	  }
	  
	  public LookupContext localLookupStrategy() {
	  	if(_local == null) {
	  		_local = language().lookupFactory().createTargetLookupStrategy(this);
	  	}
	  	return _local;
	  }
	  
	  public LookupContext localContext() {
	  	return localLookupStrategy();
	  }
	  
	  private LookupContext _local;
	  
	  private LookupContext _lexical;
	  
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
