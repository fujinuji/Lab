package scs.ubb.map.utils.observers;

import scs.ubb.map.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}