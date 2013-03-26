package be.kuleuven.cs.distrinet.chameleon.support.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionSource;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeElementImpl;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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


  private Single<Block> _blockLink = new Single<Block>(this);

  public void setBlock(Block block) {
    set(_blockLink,block);
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
