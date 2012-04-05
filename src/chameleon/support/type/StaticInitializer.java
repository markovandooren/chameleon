package chameleon.support.type;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.member.Member;
import chameleon.oo.statement.Block;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.ExceptionSource;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElementImpl;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class StaticInitializer extends TypeElementImpl implements ExceptionSource {

  public StaticInitializer(Block block) {
      setBlock(block);
  }

  public Type getNearestType() {
    return getType();
  }

  public Type getType() {
  	return nearestAncestor(Type.class);
  }

  /*********
   * BLOCK *
   *********/

  public SingleAssociation getBlockLink() {
    return _blockLink;
  }

  public Block getBlock() {
    return _blockLink.getOtherEnd();
  }


  private SingleAssociation<StaticInitializer,Block> _blockLink = new SingleAssociation<StaticInitializer,Block>(this);

  public void setBlock(Block block) {
    setAsParent(_blockLink,block);
  }

  /**
   * @return
   */
  public StaticInitializer clone() {
  	Block block = getBlock();
  	Block clone = (block == null ? null : block.clone());
    return new StaticInitializer(clone);
  }

 /*@
   @ also public behavior
   @
   @ post \result == getBlock().getCEL();
   @*/
  public CheckedExceptionList getCEL() throws LookupException {
    return getBlock().getCEL();
  }

 /*@
   @ also public behavior
   @
   @ post \result == getBlock().getAbsCEL();
   @*/
  public CheckedExceptionList getAbsCEL() throws LookupException {
    return getBlock().getAbsCEL();
  }

  /**
   * A static initializer does not add members to a type.
   */
  public List<Member> getIntroducedMembers() {
    return new ArrayList<Member>();
  }

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
