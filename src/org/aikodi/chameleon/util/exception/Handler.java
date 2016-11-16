package org.aikodi.chameleon.util.exception;

import org.aikodi.rejuse.action.Nothing;

/**
 * An abstract class for exception handling strategies.
 * 
 * @author Marko van Dooren
 *
 * @param <I> The type of the incoming exception.
 * @param <O> The type of the propagated exception.
 */
public class Handler<O extends Exception> {

   private Class<O> _outgoing;
   
   /**
    * Create a new handler that propagates only the outgoing type of exceptions.
    * @param incoming
    * @param outgoing
    */
   public Handler(Class<O> outgoing) {
      _outgoing = outgoing;
   }
   
   public Class<O> outgoing() {
      return _outgoing;
   }
   
   public void handle(Exception exception) throws O {
      if(outgoing().isInstance(exception)) {
         throw (O) exception;
      }
   }
   
   public static Handler<Nothing> resume() {
      return new Handler<Nothing>(Nothing.class);
   }
   
   public static <T extends Exception> Handler<T> fail(Class<T> kind) {
      return new Handler<T>(kind);
   }
   
}
