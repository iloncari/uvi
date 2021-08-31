/*
 * NotificationEvent NotificationEvent.java.
 * 
 */
package hr.tvz.vi.event;

import java.util.EventObject;

import hr.tvz.vi.orm.Notification;
import lombok.Getter;

/**
 * The Class NotificationEvent.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:32:51 PM Aug 29, 2021
 */
public class NotificationEvent extends EventObject{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -339384440302542539L;
  
  /** The notification. */
  @Getter
  private final Notification notification;

  /**
   * Instantiates a new notification event.
   *
   * @param source the source
   * @param notification the notification
   */
  public NotificationEvent(Object source, Notification notification) {
    super(source);
    this.notification = notification;
  }

}
