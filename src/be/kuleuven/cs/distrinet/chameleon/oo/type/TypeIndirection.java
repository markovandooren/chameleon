package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.type.inheritance.InheritanceRelation;

public abstract class TypeIndirection extends ClassImpl {

	public TypeIndirection(SimpleNameSignature sig, Type aliasedType) {
		super(sig);
		_aliasedType = aliasedType;
		if(aliasedType != null) {
		  setUniParent(aliasedType.parent());
		}
	}
	
	// @EXTENSIBILITY : change names of constructors?

	public Type aliasedType() {
		return _aliasedType;
	}
	
	protected void setAliasedType(Type type) {
		_aliasedType = type;
	}
	
	private Type _aliasedType;

	@Override
	public void add(TypeElement element) {
		throw new ChameleonProgrammerException("Trying to add an element to a type alias.");
	}

	@Override
	public void remove(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove an element from a type alias.");
	}

	@Override
	public List<Member> localMembers() throws LookupException {
		return aliasedType().localMembers();
	}
	
	@Override
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException {
		return aliasedType().localMembers(selector);
	}


	@Override
	public void removeNonMemberInheritanceRelation(InheritanceRelation relation) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a type alias.");
	}



	@Override
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add a super type to a type alias.");
	}



	@Override
	public List<InheritanceRelation> inheritanceRelations() throws LookupException {
		return aliasedType().inheritanceRelations();
	}
	
	@Override
	public List<InheritanceRelation> nonMemberInheritanceRelations() {
		return aliasedType().nonMemberInheritanceRelations();
	}
	
	//TODO I am not sure if these definitions are appropriate for a constructed type.
  public PropertySet<Element,ChameleonProperty> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedType().defaultProperties());
  }
	
	/**
	 * Return the default properties for this element.
	 * @return
	 */
	protected PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this);
	}

	protected PropertySet<Element,ChameleonProperty> myInherentProperties() {
		return new PropertySet<Element,ChameleonProperty>();
	}

  public PropertySet<Element,ChameleonProperty> inherentProperties() {
    return filterProperties(myInherentProperties(), aliasedType().inherentProperties());
  }

  public PropertySet<Element,ChameleonProperty> declaredProperties() {
    return filterProperties(myDeclaredProperties(), aliasedType().declaredProperties());
  }
  
  public PropertySet<Element,ChameleonProperty> myDeclaredProperties() {
  	return super.declaredProperties();
  }

  public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a type alias.");
  }
  
	@Override
	public Type baseType() {
		return aliasedType().baseType();
	}

//	public <P extends Parameter> List<P> parameters(Class<P> kind) {
//		return new ArrayList<P>();
//	}
//	
//	public <P extends Parameter> P parameter(Class<P> kind, int index) {
//		return null;
//	}
//
//	public <P extends Parameter> int nbTypeParameters(Class<P> kind) {
//		return 0;
//	}

	public <P extends Parameter> List<P> parameters(Class<P> kind) {
		return aliasedType().parameters(kind);
	}
	
	public <P extends Parameter> P parameter(Class<P> kind, int index) {
		return aliasedType().parameter(kind, index);
	}

	public <P extends Parameter> int nbTypeParameters(Class<P> kind) {
		return aliasedType().nbTypeParameters(kind);
	}

	public <P extends Parameter> void replaceParameter(Class<P> kind,P oldParameter, P newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a type alias.");
	}

	public <P extends Parameter> void replaceAllParameters(Class<P> kind,List<P> newParameters) {
		throw new ChameleonProgrammerException("Trying to replace type parameters in a type alias.");
	}

	public <P extends Parameter> void addParameter(Class<P> kind,P parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a type alias.");
	}
  
	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		return aliasedType().directlyDeclaredElements();
	}

	public void addParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to add a type parameter block to a type alias.");
	}


	public Class<? extends Parameter> kindOf(ParameterBlock block) throws LookupException {
		return aliasedType().kindOf(block);
	}


	public List<ParameterBlock> parameterBlocks() {
		return aliasedType().parameterBlocks();
	}


	public void removeParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to remove a type parameter block from a type alias.");
	}

	public <P extends Parameter> ParameterBlock<P> parameterBlock(Class<P> kind) {
		return aliasedType().parameterBlock(kind);
	}

	@Override
	public List<InheritanceRelation> explicitNonMemberInheritanceRelations() {
		return aliasedType().explicitNonMemberInheritanceRelations();
	}

	@Override
	public List<InheritanceRelation> implicitNonMemberInheritanceRelations() {
		return aliasedType().implicitNonMemberInheritanceRelations();
	}


}
