/*
 * VechileServiceChangedChangedEvent VechileServiceChangedChangedEvent.java.
 * 
 */

package hr.tvz.vi.event;

import hr.tvz.vi.orm.Service;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.service.VechileService;
import hr.tvz.vi.util.Constants.EventAction;

import java.util.EventObject;

import lombok.Getter;


/**
 * The Class VechileServiceChangedChangedEvent.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 1:30:12 PM Aug 20, 2021
 */
public class VechileServiceChangedChangedEvent extends EventObject {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8173909444661694456L;

  /** The vechile. */
  @Getter
  private final Vechile vechile;
  
  /** The service. */
  @Getter
  private final Service service;
  
  /** The action. */
  @Getter
  private EventAction action;

  /**
   * Instantiates a new vechile changed changed event.
   *
   * @param source  the source
   * @param vechile the vechile
   * @param service2 the service
   * @param action  the action
   */
  public VechileServiceChangedChangedEvent(Object source, Vechile vechile, Service service, EventAction action) {
    super(source);
    this.vechile = vechile;
    this.service = service;
    this.action = action;
  }

}
