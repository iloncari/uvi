/*
 * GroupMembersTab GroupMembersTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.List;

import org.vaadin.firitin.components.button.DeleteButton;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.select.VSelect;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.data.provider.ListDataProvider;

import hr.tvz.vi.orm.GroupMember;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Routes;
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
  
  /** The members select. */
  private VSelect<Person> membersSelect;
  
  /** The members. */
  private List<Person> members;
  
  
  /**
   * Instantiates a new group members tab.
   *
   * @param organizationService the organization service
   */
  public GroupMembersTab(OrganizationService organizationService) {
    super();
    this.organizationService = organizationService;
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
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
      getGrid().addColumn(groupMember -> groupMember.getPerson().getIdentificationNumber()).setHeader("groupMembersTab.grid.personIdentificationNumber");
      getGrid().addColumn(groupMember -> groupMember.getPerson().getName() +" " + groupMember.getPerson().getLastname()).setHeader("groupMembersTab.grid.perosonNameLastname");
      getGrid().addComponentColumn(groupMember -> {
        @SuppressWarnings("unchecked")
        DeleteButton delete = new DeleteButton().withText(getTranslation("button.delete"))
                                                .withRejectText(getTranslation("button.cancel"))
                                                .withHeaderText(getTranslation("groupMembersTab.deleteMember.label"))
                                                .withPromptText(getTranslation("areYouSure.label"))
                                                .withConfirmText(getTranslation("button.confirm"))
                                                .withConfirmHandler(() -> {
                                                  organizationService.deleteGroupMember(groupMember);
                                                  ((ListDataProvider<GroupMember>)getGrid().getDataProvider()).getItems().remove(groupMember);
                                                  getGrid().getDataProvider().refreshAll();
                                                  members.add(groupMember.getPerson());
                                                  membersSelect.setItems(members);
                                                });
        return delete;
      });
  }

  /**
   * Inits the above layout.
   *
   * @return the v horizontal layout
   */
  @SuppressWarnings("unchecked")
  @Override
  protected VHorizontalLayout initAboveLayout() {
    membersSelect = new VSelect<Person>();
    membersSelect.setLabel(getTranslation("groupMembersTab.field.members"));
    membersSelect.setItemLabelGenerator(m -> m.getName() + " " + m.getLastname());
    members= organizationService.getOrganizationMembers(getCurrentUser().getActiveOrganization().getOrganization());
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
      ((ListDataProvider<GroupMember>)getGrid().getDataProvider()).getItems().add(newMember);
      getGrid().getDataProvider().refreshAll();
      members.remove(e.getValue());
      membersSelect.setItems(members);
    });

    return new VHorizontalLayout(membersSelect);
  }
  
  
}
