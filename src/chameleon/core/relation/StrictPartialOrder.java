package chameleon.core.relation;

import java.util.Collection;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

/**
 * A class of strict partial orders for Chameleon. It duplicates quite a bit of
 * functionality because Java does not support anchored exception declarations.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements in the order.
 */
public abstract class StrictPartialOrder<E> extends org.rejuse.logic.relation.StrictPartialOrder<E> {
  
  public abstract boolean contains(E first, E second) throws MetamodelException;

  public WeakPartialOrder<E> weakOrder() {
    return new WeakPartialOrder<E>() {

      @Override
      public boolean contains(E first, E second) throws MetamodelException {
        return contains(first,second) || equal(first,second);
      }

      @Override
      public boolean equal(E first, E second) throws MetamodelException {
        return equal(first,second);
      }
    };
  }

  @Override
  public abstract boolean equal(E first, E second) throws MetamodelException;

  @Override
  public void removeBiggerElements(Collection<E> collection) throws MetamodelException {
    try{
      super.removeBiggerElements(collection);
    } catch(RuntimeException exc)  {
      throw exc;
    } catch(Error error) {
      throw error;
    } catch(MetamodelException exc) {
      throw exc;
    } catch(Exception exc) {
      // This should not happen
      throw new Error("A strict partial order from Chameleon throws a checked exception other than MetamodelException. Something is very wrong.");
    }
  }

  @Override
  public void removeSmallerElements(Collection<E> collection) throws MetamodelException {
    try{
      super.removeSmallerElements(collection);
    } catch(RuntimeException exc)  {
      throw exc;
    } catch(Error error) {
      throw error;
    } catch(MetamodelException exc) {
      throw exc;
    } catch(Exception exc) {
      // This should not happen
      throw new Error("A strict partial order from Chameleon throws a checked exception other than MetamodelException. Something is very wrong.");
    }
  }

}
