package chameleon.core.type;

import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.member.Member;
import chameleon.core.type.inheritance.InheritanceRelation;

public class RegularType extends Type {

	public RegularType(SimpleNameSignature sig) {
		super(sig);
		_body.connectTo(new ClassBody().parentLink());
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

	private Reference<Type, ClassBody> _body = new Reference<Type, ClassBody>(this);

	public ClassBody body() {
		return _body.getOtherEnd();
	}
	
	public void setBody(ClassBody body) {
		if(body == null) {
			throw new ChameleonProgrammerException("Body passed to setBody is null.");
		} else {
			_body.connectTo(body.parentLink());
		}
	}
	
	public void add(TypeElement element) {
	  body().add(element);
	}

  /**
   * Return the members directly declared by this type.
   * @return
   */
  public Set<Member> directlyDeclaredElements() {
     return body().elements();
  }

	private OrderedReferenceSet<Type, InheritanceRelation> _inheritanceRelations = new OrderedReferenceSet<Type, InheritanceRelation>(this);

	public void removeInheritanceRelation(InheritanceRelation relation) {
		_inheritanceRelations.remove(relation.parentLink());
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() {
		return _inheritanceRelations.getOtherEnds();
	}
	
	@Override
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		_inheritanceRelations.add(relation.parentLink());
	}

}
