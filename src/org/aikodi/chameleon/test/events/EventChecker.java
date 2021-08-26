package org.aikodi.chameleon.test.events;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;

/**
 * A helper class to check whether certain events have been sent.
 * 
 * @author Marko van Dooren
 *
 * @param <C>
 * @param <S>
 */
public class EventChecker<C,S> implements EventListener<C, S> {

  private List<Event<? extends C,? extends S>> _received = new ArrayList<>();
  private List<Predicate<Event<? extends C, ? extends S>>> _conditions = new ArrayList<>();
  private int _checked = -1;
  
  public void expect(Class<?> eventType,Object sender) {
    expect(e -> eventType.isInstance(e.change()) && sender == e.source());
  }
  
  public void expect(Predicate<Event<? extends C, ? extends S>> condition) {
    if(condition == null) {
      throw new IllegalArgumentException("The predicate cannot be null.");
    }
    _conditions.add(condition);
  }
  
  public void check() {
    assertEquals(_conditions.size(), _received.size());
    for(int i = _checked+1; i< _conditions.size();i++) {
      assertTrue(_conditions.get(i).test(_received.get(i)));
    }
    _checked = _conditions.size()-1;
    assertEquals(_checked, _received.size() - 1);
  }

  @Override
  public void accept(Event<? extends C, ? extends S> event) {
    _received.add(event);
  }
  

}
