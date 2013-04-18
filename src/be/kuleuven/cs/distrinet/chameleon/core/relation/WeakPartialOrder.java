package be.kuleuven.cs.distrinet.chameleon.core.relation;

import java.util.Collection;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public abstract class WeakPartialOrder<E> extends be.kuleuven.cs.distrinet.rejuse.logic.relation.WeakPartialOrder<E> {
  
  public abstract boolean contains(E first, E second) throws LookupException;

  public StrictPartialOrder<E> strictOrder() {
    return new StrictPartialOrder<E>() {

      @Override
      public boolean contains(E first, E second) throws LookupException {
        return WeakPartialOrder.this.contains(first,second) && (! contains(second,first));
      }

      @Override
      public boolean equal(E first, E second) throws LookupException {
        return WeakPartialOrder.this.equal(first,second);
      }
    };
  }

  @Override
  public boolean equal(E first, E second) throws LookupException {
    return contains(first, second) && contains(second, first);
  }

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
      throw new Error("A weak partial order from Chameleon throws a checked exception other than MetamodelException. Something is very wrong.");
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
      throw new Error("A weak partial order from Chameleon throws a checked exception other than MetamodelException. Something is very wrong.");
    }
  }

}
