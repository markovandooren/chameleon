package org.aikodi.chameleon.core.element;

public interface EventListener<C,S> {
  
  public void accept(Event<? extends C,? extends S> event);
}
