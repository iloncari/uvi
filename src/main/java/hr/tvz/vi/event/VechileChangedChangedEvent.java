/*
 * VechileChangedChangedEvent VechileChangedChangedEvent.java.
 * 
 */

package hr.tvz.vi.event;

import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.util.Constants.EventAction;

import java.util.EventObject;

import lombok.Getter;

/**
 * The Class VechileChangedChangedEvent.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 1:30:12 PM Aug 20, 2021
 */
public class VechileChangedChangedEvent extends EventObject {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8173909444661694456L;

  /** The vechile. */
  @Getter
  private final Vechile vechile;
  
  /** The action. */
  @Getter
  private EventAction action;

  /**
	 * Instantiates a new vechile changed changed event.
	 *
	 * @param source  the source
	 * @param vechile the vechile
	 * @param action  the action
	 */
  public VechileChangedChangedEvent(Object source, Vechile vechile, EventAction action) {
    super(source);
    this.vechile = vechile;
    this.action = action;
  }

}
