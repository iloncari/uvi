/*
 * EventTypeChangedObserver EventTypeChangedObserver.java.
 * 
 */
package hr.tvz.vi.event;

import hr.tvz.vi.util.Constants.EventType;

/**
 * An asynchronous update interface for receiving notifications
 * about EventTypeChanged information as the EventTypeChanged is constructed.
 */
public interface EventTypeChangedObserver {
  
  /**
   * This method is called when information about an EventTypeChanged
   * which was previously requested using an asynchronous
   * interface becomes available.
   *
   * @param eventType the event type
   */
  public abstract void eventTypeChanged(EventType eventType);

}
