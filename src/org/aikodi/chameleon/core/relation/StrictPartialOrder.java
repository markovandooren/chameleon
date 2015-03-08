package org.aikodi.chameleon.core.relation;

import java.util.Collection;

import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * A class of strict partial orders for Chameleon. It duplicates quite a bit of
 * functionality because Java does not support anchored exception declarations.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements in the order.
 */
public abstract class StrictPartialOrder<E> extends be.kuleuven.cs.distrinet.rejuse.logic.relation.StrictPartialOrder<E> {
  
  @Override
public abstract boolean contains(E first, E second) throws LookupException;

  @Override
public WeakPartialOrder<E> weakOrder() {
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
  public void removeBiggerElements(Collection<E> collection) throws LookupException {
    try{
      super.removeBiggerElements(collection);
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
  public void removeSmallerElements(Collection<E> collection) throws LookupException {
    try{
      super.removeSmallerElements(collection);
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
