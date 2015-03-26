package org.aikodi.chameleon.core.element;

public interface EventStream<C, S> {

  public void call(EventListener<? super C,? super S> listener);

  public void stopCalling(EventListener<? super C,? super S> listener);
  
  public default <T extends C> EventStream<T,S> about(Class<T> changeType) {
    return new ChangeFilter<T, S>(this, changeType);
  }

  public default <T extends S> EventStream<C,T> from(Class<T> sourceType) {
    return new SourceFilter<C,T>(this, sourceType);
  }
  
  public default EventStream<C,S> union(EventStream<? extends C, ? extends S> other) {
    return new UnionStream<C, S>(this,other);
  }
}
