package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.rejuse.property.PropertySet;

public abstract class TypeIndirection extends ClassImpl {

	public TypeIndirection(String name, Type aliasedType) {
		super(name);
		_aliasedType = aliasedType;
		if(aliasedType != null) {
		  setUniParent(aliasedType.lexical().parent());
		}
	}
	
	// @EXTENSIBILITY : change names of constructors?

	protected Type indirectionTarget() {
		return _aliasedType;
	}
	
	protected void setAliasedType(Type type) {
		_aliasedType = type;
	}
	
	private Type _aliasedType;

	@Override
	public void add(Declarator element) {
		throw new ChameleonProgrammerException("Trying to add an element to a type alias.");
	}

	@Override
	public void remove(Declarator element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove an element from a type alias.");
	}

	@Override
	public List<Declaration> localMembers() throws LookupException {
		return indirectionTarget().localMembers();
	}
	
	@Override
	public <D extends Declaration> List<? extends SelectionResult> localMembers(DeclarationSelector<D> selector) throws LookupException {
		return indirectionTarget().localMembers(selector);
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
		return indirectionTarget().inheritanceRelations();
	}
	
	@Override
	public List<InheritanceRelation> nonMemberInheritanceRelations() {
		return indirectionTarget().nonMemberInheritanceRelations();
	}
	
	@Override
	public PropertySet<Element, ChameleonProperty> properties() {
		return indirectionTarget().properties();
	}

  @Override
  public void replace(Declarator oldElement, Declarator newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a type alias.");
  }
  
	@Override
	public Type baseType() {
		return indirectionTarget().baseType();
	}

	@Override
   public <P extends Parameter> List<P> parameters(Class<P> kind) {
		return indirectionTarget().parameters(kind);
	}
	
	@Override
   public <P extends Parameter> P parameter(Class<P> kind, int index) {
		return indirectionTarget().parameter(kind, index);
	}

	@Override
   public <P extends Parameter> int nbTypeParameters(Class<P> kind) {
		return indirectionTarget().nbTypeParameters(kind);
	}

	@Override
   public <P extends Parameter> void replaceParameter(Class<P> kind,P oldParameter, P newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a type alias.");
	}

	@Override
   public <P extends Parameter> void replaceAllParameters(Class<P> kind,List<P> newParameters) {
		throw new ChameleonProgrammerException("Trying to replace type parameters in a type alias.");
	}

	@Override
   public <P extends Parameter> void addParameter(Class<P> kind,P parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a type alias.");
	}
  
	@Override
	public List<? extends Declarator> directlyDeclaredElements() {
		return indirectionTarget().directlyDeclaredElements();
	}

	@Override
   public void addParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to add a type parameter block to a type alias.");
	}


	@Override
   public List<ParameterBlock> parameterBlocks() {
		return indirectionTarget().parameterBlocks();
	}


	@Override
   public void removeParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to remove a type parameter block from a type alias.");
	}

	@Override
   public <P extends Parameter> ParameterBlock<P> parameterBlock(Class<P> kind) {
		return indirectionTarget().parameterBlock(kind);
	}

	@Override
	public List<InheritanceRelation> explicitNonMemberInheritanceRelations() {
		return indirectionTarget().explicitNonMemberInheritanceRelations();
	}

	@Override
	public List<InheritanceRelation> implicitNonMemberInheritanceRelations() {
		return indirectionTarget().implicitNonMemberInheritanceRelations();
	}

}
