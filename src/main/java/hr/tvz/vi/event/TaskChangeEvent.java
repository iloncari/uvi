/*
 * TaskChangeEvent TaskChangeEvent.java.
 * 
 */
package hr.tvz.vi.event;

import java.util.EventObject;

import hr.tvz.vi.orm.Task;
import hr.tvz.vi.util.Constants.EventAction;
import lombok.Getter;


/**
 * The Class TaskChangeEvent.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 9:17:17 PM Aug 29, 2021
 */
public class TaskChangeEvent extends EventObject{
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -2798760558389822909L;

  /** The task. */
  @Getter
  private final Task task;
  
  /** The action. */
  @Getter
  private final EventAction action;
  
  /**
   * Instantiates a new group change event.
   *
   * @param source the source
   * @param task the task
   * @param action the action
   */
  public TaskChangeEvent(Object source, Task task, EventAction action) {
    super(source);
    this.task=task;
    this.action=action;
  }

}
