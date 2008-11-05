package chameleon.core.type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.type.inheritance.InheritanceRelation;

public class RegularType extends Type {

	public RegularType(SimpleNameSignature sig) {
		super(sig);
	}

	@Override
	public RegularType clone() {
		RegularType result = cloneThis();
		result.copyContents(this);
		return result;
	}

	protected RegularType cloneThis() {
		return new RegularType(signature().clone());
	}

	private OrderedReferenceSet<Type, TypeElement> _elements = new OrderedReferenceSet<Type, TypeElement>(this);

	public void add(TypeElement element) {
	  if(element != null) {
	    _elements.add(element.getParentLink());
	  }
	}

  /**
   * Return the members directly declared by this type.
   * @return
   * @throws MetamodelException 
   */
  public Set<TypeElement> directlyDeclaredElements() {
     Set<TypeElement> result = new HashSet<TypeElement>();
     for(TypeElement m: _elements.getOtherEnds()) {
       result.addAll(m.getIntroducedMembers());
     }
     return result;
  }

	private OrderedReferenceSet<Type, InheritanceRelation> _inheritanceRelations = new OrderedReferenceSet<Type, InheritanceRelation>(this);

	public void removeInheritanceRelation(InheritanceRelation relation) {
		_inheritanceRelations.remove(relation.getParentLink());
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() {
		return _inheritanceRelations.getOtherEnds();
	}
	
	@Override
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		_inheritanceRelations.add(relation.getParentLink());
	}

}
