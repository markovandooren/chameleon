package org.aikodi.chameleon.support.type;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.statement.ExceptionSource;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

import com.google.common.collect.ImmutableList;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * @author Marko van Dooren
 */
public class StaticInitializer extends ElementWithModifiersImpl implements Declarator, ExceptionSource {

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
    set(_blockLink, block);
  }

  /**
   * @return
   */
  @Override
  protected StaticInitializer cloneSelf() {
    return new StaticInitializer(null);
  }

  /**
   * A static initializer does not add members to a type.
   */
  @Override
  public List<Declaration> declaredDeclarations() {
    return ImmutableList.of();
  }

  @Override
  public Verification verifySelf() {
    return Valid.create();
  }

}
