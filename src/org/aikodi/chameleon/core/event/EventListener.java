package org.aikodi.chameleon.core.event;

public interface EventListener<C,S> {
  
  public void accept(Event<? extends C,? extends S> event);
}
