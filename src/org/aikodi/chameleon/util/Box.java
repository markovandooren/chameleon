package org.aikodi.chameleon.util;

/**
 * A class to store objects such that they can be set from
 * within lambda's. The makes it easier to extract intermediate
 * results from a builder.
 *  
 * @author Marko van Dooren
 *
 * @param <T> The type of the element in the box.
 */
public class Box<T> {
   private T _object;
   
   /**
    * Set the element in the box.
    * 
    * @param object The new element in the box.
    */
  /*@
    @ public behavior
    @
    @ post get() == object;
    @*/
   public void set(T object) {
      _object = object;
   }
   
   /**
    * @return The element in the box.
    */
   public T get() {
      return _object;
   }
}