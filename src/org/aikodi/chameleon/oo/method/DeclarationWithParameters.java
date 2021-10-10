package org.aikodi.chameleon.oo.method;

import com.google.common.collect.ImmutableList;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationImpl;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.*;
import org.aikodi.chameleon.oo.member.DeclarationWithParametersHeader;
import org.aikodi.chameleon.oo.member.SignatureWithParameters;
import org.aikodi.chameleon.oo.type.DeclarationWithType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.chameleon.util.association.Single;

import java.util.List;

public abstract class DeclarationWithParameters extends DeclarationImpl implements Target,
    DeclarationWithType {

  /**
   * Initialize a new method with the given header.
   * 
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
  
  @Override
  public LocalLookupContext<?> targetContext() throws LookupException {
  	return DeclarationWithType.super.targetContext();
  }

  /**
   * Set the header of this method.
   * 
   * @param header
   */
  public void setHeader(MethodHeader header) {
    set(_header, header);
  }

  /**
   * Return the signature of this member.
   */
  public MethodHeader header() {
    return _header.getOtherEnd();
  }

  private Single<MethodHeader> _header = new Single<MethodHeader>(this, true, "header");

  public SignatureWithParameters signature() {
    MethodHeader header = header();
    return header == null ? null : header.signature();
  }

  @Override
  public void setSignature(Signature signature) {
    setHeader((MethodHeader) header().createFromSignature(signature));
  }

  @Override
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
   * Return a string representation for the name of the method. This is just a
   * convenience method. DO NOT USE IT TO IDENTIFY ELEMENTS! The signature is
   * used for identification, not just the name.
   * 
   * @return
   */
  @Override
  public String name() {
    DeclarationWithParametersHeader header = header();
    if (header != null) {
      return header.name();
    } else {
      return null;
    }
  }

  /**
	 */
  public FormalParameter formalParameter(int index) {
    return header().formalParameter(index);
  }

  public FormalParameter lastFormalParameter() {
    int nbFormalParameters = nbFormalParameters();
    if (nbFormalParameters > 0) {
      return formalParameter(nbFormalParameters - 1);
    } else {
      return null;
    }
  }

  public int nbFormalParameters() {
    return header().nbFormalParameters();
  }

  public List<FormalParameter> formalParameters() {
    DeclarationWithParametersHeader header = header();
    if (header != null) {
      return header.formalParameters();
    } else {
      return ImmutableList.<FormalParameter> of();
    }
  }

  public List<Type> formalParameterTypes() throws LookupException {
    DeclarationWithParametersHeader header = header();
    if (header != null) {
      return header.formalParameterTypes();
    } else {
      return ImmutableList.<Type> of();
    }
  }

//  /*
//   * @
//   * @ also public behavior
//   * @
//   * @ // A method introduces itself as a method.
//   * @ post \result.contains(this);
//   * @ post \result.size() == 1;
//   * @
//   */
//  @Override
//  public List<Declaration> getIntroducedMembers() {
//    return (List) Util.createSingletonList(this);
//  }

  @Override
  public LookupContext lookupContext(Element element) throws LookupException {
    if (element == header()) {
      return parent().lookupContext(this);
    } else {
      if (_lexical == null) {
        _lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(), this);
      }
      return _lexical;
    }
  }

  public LookupContext localLookupStrategy() {
    if (_local == null) {
      _local = language().lookupFactory().createTargetLookupStrategy(this);
    }
    return _local;
  }

  @Override
  public LookupContext localContext() {
    return localLookupStrategy();
  }

  private LookupContext _local;

  private LookupContext _lexical;

  @Override
  public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
    return declarations();
  }

  @Override
  public List<? extends Declaration> declarations() {
    return header().declarations();
  }

  @Override
  public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector)
      throws LookupException {
    return header().declarations(selector);
  }

}
