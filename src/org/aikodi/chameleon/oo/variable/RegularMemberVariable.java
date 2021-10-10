package org.aikodi.chameleon.oo.variable;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.member.DeclarationComparator;
import org.aikodi.chameleon.oo.member.HidesRelation;
import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class RegularMemberVariable extends RegularVariable {

   /**
    * @param name
    */
   public RegularMemberVariable(String name, TypeReference type) {
      super(name, type, null);
   }

   /**
    * @param name
    */
   public RegularMemberVariable(String name, TypeReference type, Expression initCode) {
      super(name, type, initCode);
   }

   @Override
   public boolean uniSameAs(Element other) throws LookupException {
      if (other instanceof RegularMemberVariable) {
         RegularMemberVariable var = (RegularMemberVariable) other;
         Element parent = parent();
         Element otherParent = other.lexical().parent();
         return (parent != null && otherParent != null && otherParent.equals(parent) && sameSignatureAs(var));
      } else {
         return false;
      }
   }

   /**********
    * ACCESS *
    **********/

   @Override
   protected RegularMemberVariable cloneSelf() {
      return new RegularMemberVariable(name(), null, null);
   }

//   @Override
//   public List<Declaration> getIntroducedMembers() {
//      return Util.<Declaration> createSingletonList(this);
//   }
//
//   @Override
//   public List<Declaration> declaredMembers() {
//      return Util.<Declaration> createSingletonList(this);
//   }
//
//   @Override
//   public List<? extends Member> directlyAliasedMembers() throws LookupException {
//      return nearestAncestor(Type.class).membersDirectlyAliasedBy(aliasSelector());
//   }
//
//   @Override
//   public List<? extends Member> directlyAliasingMembers() throws LookupException {
//      return nearestAncestor(Type.class).membersDirectlyAliasing(aliasSelector());
//   }

//   @Override
//   public boolean canImplement(Member other) throws LookupException {
//      StrictPartialOrder<Member> implementsRelation = language(ObjectOrientedLanguage.class).implementsRelation();
//      return implementsRelation.contains(this, other);
//   }

   public HidesRelation<? extends Declaration> hidesSelector() {
      return _hidesSelector;
   }

   private static HidesRelation<RegularMemberVariable> _hidesSelector = new HidesRelation<RegularMemberVariable>(
         RegularMemberVariable.class);

//   @Override
//   public MemberRelationSelector<Member> aliasSelector() {
//      return new MemberRelationSelector<Member>(Member.class, this, _aliasSelector);
//   }

   private static DeclarationComparator<Declaration> _aliasSelector = new DeclarationComparator<>(Declaration.class);

}
