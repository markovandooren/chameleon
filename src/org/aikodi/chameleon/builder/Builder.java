package org.aikodi.chameleon.builder;

import java.util.function.Consumer;

/**
 * <p>An abstract class for nested builders. By keeping track of the
 * parent builder and keeping its type in type parameter {@link P}, a method
 * that finished the work of the current builder can return a reference to
 * the parent builder such that the chaining of method calls can continue.</p>
 * 
 * <code>
 * parent
 *   .methodThatReturnsNestedBuilder()
 *     .build()
 *     .moreBuild()
 *     .finishBuilding()
 *   .buildMethodFromParent()
 * </code>
 * 
 * @author Marko van Dooren
 *
 * @param <P> The type of the parent of the builder.
 * @param <R> The type of the objects created by the builder.
 */
public class Builder<P,R> {

   private Consumer<R> _consumer;
   private P _parent;

   /**
    * Create a new builder with the given parent builder and consumer.
    * The objects that are created will be passed to the consumer.
    * 
    * @param parent The parent builder.
    * @param consumer A consumer that will process the created objects.
    */
   public Builder(P parent, Consumer<R> consumer) {
      _parent = parent;
      _consumer = consumer;
   }

   /**
    * @return The parent builder of this builder.
    */
   public P parent() {
      return _parent;
   }

   /**
    * @return the consumer that will process the created objects.
    */
   public Consumer<R> consumer() {
      return _consumer;
   }

}