package org.aikodi.chameleon.core.relation;

import org.aikodi.chameleon.core.lookup.LookupException;

import java.util.Collection;

/**
 * A class of strict partial orders for Chameleon. It duplicates quite a bit of
 * functionality because Java does not support anchored exception declarations.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements in the order.
 */
public interface StrictPartialOrder<E> extends org.aikodi.rejuse.logic.relation.StrictPartialOrder<E> {
  
  @Override
public abstract boolean contains(E first, E second) throws LookupException;

  @Override
default WeakPartialOrder<E> weakOrder() {
    return new WeakPartialOrder<E>() {

      @Override
      public boolean contains(E first, E second) throws LookupException {
        return StrictPartialOrder.this.contains(first,second) || equal(first,second);
      }

      @Override
      public boolean equal(E first, E second) throws LookupException {
        return StrictPartialOrder.this.equal(first,second);
      }
    };
  }

  @Override
  public abstract boolean equal(E first, E second) throws LookupException;

  @Override
  default void removeBiggerElements(Collection<E> collection) throws LookupException {
    try{
    	org.aikodi.rejuse.logic.relation.StrictPartialOrder.super.removeBiggerElements(collection);
    } catch(RuntimeException exc)  {
      throw exc;
    } catch(Error error) {
      throw error;
    } catch(LookupException exc) {
      throw exc;
    } catch(Exception exc) {
      // This should not happen
      throw new Error("A strict partial order from Chameleon throws a checked exception other than MetamodelException. Something is very wrong.");
    }
  }

  @Override
  default void removeSmallerElements(Collection<E> collection) throws LookupException {
    try{
    	org.aikodi.rejuse.logic.relation.StrictPartialOrder.super.removeSmallerElements(collection);
    } catch(RuntimeException exc)  {
      throw exc;
    } catch(Error error) {
      throw error;
    } catch(LookupException exc) {
      throw exc;
    } catch(Exception exc) {
      // This should not happen
      throw new Error("A strict partial order from Chameleon throws a checked exception other than MetamodelException. Something is very wrong.");
    }
  }

}
