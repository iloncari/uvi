/*
 * GroupMembersTab GroupMembersTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.vaadin.firitin.components.button.DeleteButton;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.select.VSelect;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.ListDataProvider;

import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.GroupChangeEvent;
import hr.tvz.vi.event.NotificationEvent;
import hr.tvz.vi.orm.GroupMember;
import hr.tvz.vi.orm.Notification;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.service.NotificationService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.NotificationType;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.view.AbstractGridView;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class GroupMembersTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 11:29:55 PM Aug 27, 2021
 */
@Slf4j
public class GroupMembersTab extends AbstractGridView<GroupMember>{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1919043495982070794L;
  
  /** The organization service. */
  private OrganizationService organizationService;
  
  /** The notification service. */
  private NotificationService notificationService;
  
  /** The members select. */
  private VSelect<Person> membersSelect;
  
  /** The members. */
  private List<Person> members;
  
  
  /**
   * Instantiates a new group members tab.
   *
   * @param organizationService the organization service
   */
  public GroupMembersTab(OrganizationService organizationService, NotificationService notificationService) {
    super(false, false);
    this.organizationService = organizationService;
    this.notificationService = notificationService;
  }
  
  /**
   * Group changed.
   *
   * @param event the event
   */
  @SuppressWarnings("unchecked")
  @Subscribe
  public void groupChanged(GroupChangeEvent event) {
    if(getCurrentUser().getActiveOrganizationObject().getId().equals(event.getGroupMember().getOrganizationId())) {
      getUI().ifPresent(ui -> ui.access(() -> {
        Notification notification = new Notification();
        notification.setCreationDateTime(LocalDateTime.now());
        notification.setOrganizationId(getCurrentUser().getActiveOrganizationObject().getId());
        notification.setRecipientId(event.getGroupMember().getPerson().getId());
        notification.setTitle("Grupa");
        notification.setType(NotificationType.GROUP);
      if(EventAction.ADDED.equals(event.getAction())) {
        ((ListDataProvider<GroupMember>)getGrid().getDataProvider()).getItems().add(event.getGroupMember());
        members.remove(event.getGroupMember().getPerson());
        membersSelect.setItems(members);
        notification.setMessage("Dodani ste u grupu");
      }else if(EventAction.REMOVED.equals(event.getAction())) {
        ((ListDataProvider<GroupMember>)getGrid().getDataProvider()).getItems().remove(event.getGroupMember());
        members.add(event.getGroupMember().getPerson());
        membersSelect.setItems(members);
        notification.setMessage("Uklonjeni ste iz grupu");
      }
      notificationService.saveOrUpdateNotification(notification);
      notificationService.mapNotificationToUser(notification.getId(), event.getGroupMember().getPerson().getId());
      ChangeBroadcaster.firePushEvent(new NotificationEvent(this, notification));
      getGrid().getDataProvider().refreshAll();
      }));
    }
  }
  
  


  /**
   * Gets the grid items.
   *
   * @return the grid items
   */
  @Override
  public List<GroupMember> getGridItems() {
    return organizationService.getOrganizationGroupMembers(getGroupType(), getCurrentUser().getActiveOrganization().getOrganization().getId());
  }

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public String getPageTitle() {
    return getTranslation(Routes.getPageTitleKey(Routes.ORGANIZATION));
  }

  /**
   * Gets the view title.
   *
   * @return the view title
   */
  @Override
  protected String getViewTitle() {
    return getTranslation(getGroupType().getGroupTypeLocalizationKey());
  }
  
  /**
   * Gets the group type.
   *
   * @return the group type
   */
  private GroupType getGroupType() {
    if(OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel())) {
      return GroupType.PREPARERS;
    }else {
      return GroupType.APPROVERS;
    }
    
  }

  /**
   * Inits the bellow button layout.
   *
   * @return the v horizontal layout
   */
  @Override
  protected VHorizontalLayout initBellowButtonLayout() {
    //no buttons
    return null;
  }

  /**
   * Inits the grid.
   */
  @Override
  protected void initGrid() {
      getGrid().removeAllColumns();
      getGrid().addColumn(groupMember -> groupMember.getPerson().getIdentificationNumber()).setHeader(getTranslation("groupMembersTab.grid.personIdentificationNumber"));
      getGrid().addColumn(groupMember -> groupMember.getPerson().getName() +" " + groupMember.getPerson().getLastname()).setHeader(getTranslation("groupMembersTab.grid.perosonNameLastname"));
      getGrid().addComponentColumn(groupMember -> {
        DeleteButton delete = new DeleteButton().withText(getTranslation("button.delete"))
                                                .withRejectText(getTranslation("button.cancel"))
                                                .withHeaderText(getTranslation("groupMembersTab.deleteMember.label"))
                                                .withPromptText(getTranslation("areYouSure.label"))
                                                .withConfirmText(getTranslation("button.confirm"))
                                                .withConfirmHandler(() -> {
                                                  organizationService.deleteGroupMember(groupMember);
                                                  ChangeBroadcaster.firePushEvent(new GroupChangeEvent(this, groupMember, EventAction.REMOVED));
                                                });
        delete.getElement().getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_RED);
        return delete;
      });
  }

  /**
   * Inits the above layout.
   *
   * @return the v horizontal layout
   */
  @Override
  protected VHorizontalLayout initAboveLayout() {
    membersSelect = new VSelect<Person>();
    membersSelect.setLabel(getTranslation("groupMembersTab.field.members"));
    membersSelect.setItemLabelGenerator(m -> m.getName() + " " + m.getLastname());
    members=organizationService.getOrganizationMembers(getCurrentUser().getActiveOrganization().getOrganization());
    List<GroupMember> groupMembers = getGridItems();
    members.removeIf(member -> groupMembers.stream().map(GroupMember::getPerson).anyMatch(groupMember -> groupMember.getId().equals(member.getId())));
    membersSelect.setItems(members);
    membersSelect.addValueChangeListener(e -> {
      if(e.getValue()==null) {
        return;
      }
      GroupMember newMember = new GroupMember();
      newMember.setGroupType(getGroupType());
      newMember.setOrganizationId(getCurrentUser().getActiveOrganization().getOrganization().getId());
      newMember.setPerson(e.getValue());
      organizationService.saveGroupMember(newMember);
      ChangeBroadcaster.firePushEvent(new GroupChangeEvent(this, newMember, EventAction.ADDED));
    });

    return new VHorizontalLayout(membersSelect);
  }

  /**
   * Gets the route.
   *
   * @return the route
   */
  @Override
  public String getRoute() {
    return Routes.ORGANIZATION;
  }
  
  
}
