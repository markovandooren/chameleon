package org.aikodi.chameleon.support.type;

import com.google.common.collect.ImmutableList;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.statement.ExceptionSource;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.rejuse.association.SingleAssociation;

import java.util.List;

/**
 * @author Marko van Dooren
 */
public class StaticInitializer extends ElementWithModifiersImpl implements Declarator, ExceptionSource {

  public StaticInitializer(Block block) {
    setBlock(block);
  }

  /*********
   * BLOCK *
   *********/

  public SingleAssociation getBlockLink() {
    return _block;
  }

  public Block getBlock() {
    return _block.getOtherEnd();
  }

  private Single<Block> _block = new Single<Block>(this, "block");

  public void setBlock(Block block) {
    set(_block, block);
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
