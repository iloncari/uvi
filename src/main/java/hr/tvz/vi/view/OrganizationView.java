/*
 * OrganizationView OrganizationView.java.
 *
 */

package hr.tvz.vi.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.VTabSheet;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.GroupMembersTab;
import hr.tvz.vi.components.OrganizationDetailTab;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.NotificationService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Utils;

/**
 * The Class OrganizationView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 10:21:47 PM Aug 10, 2021
 */
@Route(value = Routes.ORGANIZATION, layout = MainAppLayout.class)
public class OrganizationView extends VVerticalLayout implements HasDynamicTitle {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -3567099719758726806L;
  
  /** The current user. */
  private CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());

  /** The organization service ref. */
  @Autowired
  private ServiceRef<OrganizationService> organizationServiceRef;
  
  /** The address service ref. */
  @Autowired
  private ServiceRef<AddressService> addressServiceRef;
  
  /** The notification service ref. */
  @Autowired
  private ServiceRef<NotificationService> notificationServiceRef;


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
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    VTabSheet tabs = new VTabSheet();
    tabs.addTab(getTranslation("organizationView.organizationDetailTab.label"), new OrganizationDetailTab(organizationServiceRef.get(), addressServiceRef.get()));
    if(OrganizationLevel.OPERATIONAL_LEVEL.equals(currentUser.getActiveOrganizationObject().getLevel()) || OrganizationLevel.CITY_LEVEL.equals(currentUser.getActiveOrganizationObject().getLevel())) {
      tabs.addTab(getTranslation("organizationView.groupMembersTab.label"), new GroupMembersTab(organizationServiceRef.get(), notificationServiceRef.get()));
    }
    add(tabs);
  }
}
