/*
 * MainAppLayout MainAppLayout.java.
 *
 */

package hr.tvz.vi.view;

import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.select.VSelect;

import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts.LeftResponsiveHybrid;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.entity.Section;
import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.AppHeader;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.NotificationEvent;
import hr.tvz.vi.event.PersonOrganizationChangedEvent;
import hr.tvz.vi.event.TaskChangeEvent;
import hr.tvz.vi.orm.PersonOrganization;
import hr.tvz.vi.service.NotificationService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.PersonService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.ImageConstants;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.SubscriberScope;
import lombok.extern.slf4j.Slf4j;
import hr.tvz.vi.util.Utils;

/**
 * The Class MainAppLayout.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:24:01 PM Aug 7, 2021
 */
@CssImport("./styles/custom-colors.css")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/custom-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport(value = "./styles/custom-text-area-styles.css", themeFor = "vaadin-text-area")
@CssImport(value = "./styles/cusrom-multiselect-combo-box-styles.css", themeFor = "multiselect-combo-box")
@CssImport(value = "./styles/custom-button-style.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/custom-vaadin-grid-styles.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/custom-dialog-style.css", themeFor = "vaadin-dialog-overlay")
@CssImport(value = "./styles/custom-menu-bar-styles.css", themeFor = "vaadin-menu-bar")
@CssImport(value = "./styles/custom-multiselect-combobox-input-styles.css", themeFor = "multiselect-combo-box-input")
@Push
@Slf4j
@EventSubscriber(scope = SubscriberScope.ALL)
public class MainAppLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsiveHybrid> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1575520009397533185L;

  /** The current user. */
  private final CurrentUser currentUser;

  /** The members item badge. */
  private DefaultBadgeHolder membersItemBadge;

  
  /** The tasks item badge. */
  private DefaultBadgeHolder tasksItemBadge;
  
  
  /** The report event item. */
  private LeftNavigationItem reportEventItem;
  
  /** The reports item. */
  private LeftNavigationItem reportsItem;

  /** The person organization select. */
  private VSelect<PersonOrganization> personOrganizationSelect;

  
  /** The app header. */
  private AppHeader appHeader;

  /** The auth service ref. */
  @Autowired
  private ServiceRef<NotificationService> notificationServiceRef;

  /** The organization service ref. */
  @Autowired
  private ServiceRef<OrganizationService> organizationServiceRef;
  
  /** The person service ref. */
  @Autowired
  private ServiceRef<PersonService> personServiceRef;
  
  /** The reports service ref. */
  @Autowired
  private ServiceRef<ReportService> reportServiceRef;
  
  LeftResponsiveHybrid leftMenu;

  /**
   * Instantiates a new main app layout.
   */
  public MainAppLayout() {
    appHeader = new AppHeader();
    currentUser = Utils.getCurrentUser(UI.getCurrent());
     leftMenu =AppLayoutBuilder.get(LeftLayouts.LeftResponsiveHybrid.class)
      .withIcon(ImageConstants.APP_LOGO.getPath())
      .withAppBar(appHeader)
      .withAppMenu(buildLeftMenu())
      .build();
    
    leftMenu.getElement().removeAttribute("class");

    init(leftMenu);

  }
  
  /**
   * Notification received.
   *
   * @param event the event
   */
  @Subscribe
  public void notificationReceived(NotificationEvent event) {
   if(currentUser.getActiveOrganizationObject().getId().equals(event.getNotification().getOrganizationId())) {
     getUI().ifPresent(ui -> ui.access(() -> 
       appHeader.setActiveNotifications(notificationServiceRef.get().findAllActiveNotifications(currentUser.getActiveOrganizationObject().getId(), currentUser.getPerson().getId()))));
   }
  }

  /**
   * Builds the left menu.
   *
   * @return the component
   */
  private Component buildLeftMenu() {
    final LeftAppMenuBuilder leftMenuBuilder = LeftAppMenuBuilder.get();

    final LeftNavigationItem homeItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.HOME)), VaadinIcon.HOME.create(), HomeView.class);

    final LeftNavigationItem organizationItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.ORGANIZATION)), VaadinIcon.BUILDING.create(),
      OrganizationView.class);
    

    final LeftNavigationItem membersItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.MEMBERS)), VaadinIcon.GROUP.create(),
      MembersView.class);
    membersItemBadge = new DefaultBadgeHolder();
    membersItemBadge.bind(membersItem.getBadge());

    final LeftNavigationItem vechilesItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.VECHILES)), VaadinIcon.CAR.create(),
      VechilesView.class);
    
    
    reportEventItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.REPORT_EVENT)), VaadinIcon.PLUS_CIRCLE.create(),
    	      ReportEventView.class);
    
    reportsItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.REPORTS)), VaadinIcon.FILE.create(),
    	      ReportsView.class);

    final LeftNavigationItem tasksItem = new LeftNavigationItem(getTranslation(Routes.getPageTitleKey(Routes.TASKS)), VaadinIcon.TASKS.create(),
      TasksView.class);
    tasksItemBadge = new DefaultBadgeHolder();
    tasksItemBadge.bind(tasksItem.getBadge());
    
    leftMenuBuilder.addToSection(Section.DEFAULT, homeItem, organizationItem, membersItem, vechilesItem, reportEventItem, reportsItem, tasksItem);
   
    
    return leftMenuBuilder.build();
  }

  /**
   * Inits the organizations select.
   */
  private void initOrganizationsSelect() {
    personOrganizationSelect.setItems(currentUser.getAppAvailablePersonOrganizactions());
    personOrganizationSelect.setItemLabelGenerator(personOrg -> personOrg.getOrganization().getName());
    personOrganizationSelect.setValue(currentUser.getActiveOrganization());
    personOrganizationSelect.addValueChangeListener(e -> {
      currentUser.setActiveOrganization(e.getValue());
      currentUser.getPerson().setLastActiveOrganizationId(e.getValue().getOrganization().getId());
      personServiceRef.get().saveOrUpdatePerson(currentUser.getPerson());
      UI.getCurrent().getPage().reload();
    });
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    
    leftMenu.getChildren().forEach(C -> {if("application-content".equals(C.getElement().getAttribute("slot"))) {
      C.getElement().removeAttribute("style");
    }});
    leftMenu.addClassName(StyleConstants.SIDEBAR_HEADER.getName());
    
    
   
    ChangeBroadcaster.registerToPushEvents(this);
    setActiveOrganizationMembersBadge();
    setActiveOrganizationTasksBadge();
    reportEventItem.setVisible(currentUser.hasManagerRole());
    notificationServiceRef.get().sendRegistrationExpiringNotificationsIfNeeded(currentUser.getActiveOrganizationObject().getId());
    appHeader.setPersonService(personServiceRef);
    appHeader.setNotificationService(notificationServiceRef);
    appHeader.setActiveNotifications(notificationServiceRef.get().findAllActiveNotifications(currentUser.getActiveOrganizationObject().getId(), currentUser.getPerson().getId()));
  }

  /**
   * On detach.
   *
   * @param detachEvent the detach event
   */
  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
    ChangeBroadcaster.unregisterFromPushEvents(this);
  }

  /**
   * Person organization changed.
   *
   * @param event the event
   */
  @Subscribe
  public void personOrganizationChanged(PersonOrganizationChangedEvent event) {
    if (event.getPersonOrganization().getPerson().getId().equals(currentUser.getPerson().getId())) {
      if (EventAction.ADDED.equals(event.getAction())) {
        currentUser.getPerson().getOrgList().add(event.getPersonOrganization());
        initOrganizationsSelect();
      } else if (EventAction.MODIFIED.equals(event.getAction())) {
        final Optional<PersonOrganization> modifiedOrg = currentUser.getPerson().getOrgList().stream()
          .filter(po -> po.getId().equals(event.getPersonOrganization().getId())).findAny();
        if (modifiedOrg.isPresent()) {
          modifiedOrg.get().setRole(event.getPersonOrganization().getRole());
          modifiedOrg.get().setDuty(event.getPersonOrganization().getDuty());
          if (null != event.getPersonOrganization().getExitDate()
            || true != event.getPersonOrganization().isAppRights()) {
          }
        }
      }
    }

    if (event.getPersonOrganization().getOrganization().getId().equals(currentUser.getActiveOrganization().getOrganization().getId())) {
      getUI().ifPresent(ui -> ui.access(() -> setActiveOrganizationMembersBadge()));
    }
  }
  
  @Subscribe
  public void tasksChanged(TaskChangeEvent event) {
    if(event.getTask().getOrganizationAssignee().getId().equals(currentUser.getActiveOrganizationObject().getId())) {
      getUI().ifPresent(ui -> ui.access(() -> setActiveOrganizationTasksBadge()));
    }
  }

  /**
   * Sets the active organization members badge.
   */
  private void setActiveOrganizationMembersBadge() {
    final int membersCount = organizationServiceRef.get().getOrganizationMembers(currentUser.getActiveOrganization().getOrganization()).size();
    membersItemBadge.setCount(membersCount);
  }
  

  /**
   * Sets the active organization tasks badge.
   */
  private void setActiveOrganizationTasksBadge() {
    final Long myTasksCount = reportServiceRef.get().getUserTaskNumber(currentUser.getActiveOrganization().getOrganization().getId(),
      currentUser.getPerson().getId());
    final Long groupTasksCount = reportServiceRef.get().getGroupTaskNumber(currentUser.getActiveOrganization().getOrganization().getId());
    tasksItemBadge.setCount(NumberUtils.toInt(myTasksCount.toString()) + NumberUtils.toInt(groupTasksCount.toString()));
  }

}
