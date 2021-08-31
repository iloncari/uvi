/*
 * GroupChangeEvent GroupChangeEvent.java.
 * 
 */
package hr.tvz.vi.event;

import java.util.EventObject;

import hr.tvz.vi.orm.GroupMember;
import hr.tvz.vi.util.Constants.EventAction;
import lombok.Getter;

/**
 * The Class GroupChangeEvent.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 8:17:51 PM Aug 29, 2021
 */
public class GroupChangeEvent extends EventObject{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 7617041948883070577L;
  
  /** The group member. */
  @Getter
  private final GroupMember groupMember;
  
  @Getter
  private final EventAction action;
  
  /**
   * Instantiates a new group change event.
   *
   * @param source the source
   * @param groupMember the group member
   * @param action the action
   */
  public GroupChangeEvent(Object source, GroupMember groupMember, EventAction action) {
    super(source);
    this.groupMember=groupMember;
    this.action=action;
  }

}
