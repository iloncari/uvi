/*
 * AppHeader AppHeader.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VFlexLayout;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.AccessControlFactory;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.orm.Notification;
import hr.tvz.vi.service.NotificationService;
import hr.tvz.vi.service.PersonService;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.ImageConstants;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.view.LoginView;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AppHeader.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 4:47:05 PM Aug 29, 2021
 */
@EventSubscriber(scope = SubscriberScope.ALL)
@Slf4j
public class AppHeader  extends VFlexLayout {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 4415377699158623761L;

  /** The user menu bar. */
  private final MenuBar menuBar = new MenuBar();

  /** The menu layout. */
  private final VHorizontalLayout menuLayout = new VHorizontalLayout();

  /** The badge indicator. */
  private final CustomBadge badgeIndicator = new CustomBadge();

  /** The notifications menu. */
  private SubMenu notificationsMenu;

  /** The notification content. */
  private VSpan notificationContent;

  /** The notification items. */
  private VDiv notificationItems;

  /** The current user. */
  private final transient CurrentUser currentUser;

  /** The person service. */
  @Setter
  private ServiceRef<PersonService> personService;
  
  /** The notification service. */
  @Setter
  private ServiceRef<NotificationService> notificationService;
  
  /**
   * Instantiates a new header.
   */
  public AppHeader() {
    currentUser = Utils.getCurrentUser(UI.getCurrent());
    menuBar.addThemeName(ThemeAttribute.HEADER_ITEMS);

    final Image notificationIcon = new Image(ImageConstants.ICON_NOTIFICATION.getPath(), "");
    notificationIcon.addClassName(StyleConstants.NOTIFICATION_ICON.getName());

    notificationContent = new VSpan(notificationIcon, badgeIndicator);
    notificationContent.addClassName(StyleConstants.NOTIFICATION_CONTENT.getName());
    notificationContent.addClickListener(event -> notificationContent.removeClassName(StyleConstants.NEW_NOTIFICATION.getName()));
    MenuItem notificationMenuItem= menuBar.addItem(notificationContent);
    notificationsMenu = notificationMenuItem.getSubMenu();
    notificationItems = new VDiv();
    notificationItems.setClassName(StyleConstants.NOTIFICATION_ITEMS.getName());
    
    add(menuLayout);
   
  }
  

  /**
   * Creates the logout.
   */
  private void createLogout() {
    menuBar.addItem(new VDiv(new Image(ImageConstants.ICON_LOG_OUT.getPath(), ""), new VSpan("Logout")).withClassName("logout-icon"), e -> { AccessControlFactory.of().getAccessControl(null).signOut();
    UI.getCurrent().navigate(LoginView.class);});
  }
  
  /**
   * Creates the organizations menu.
   *
   * @param siteMenuLayout the site menu layout
   */
  private void createOrganizationsMenu(final VHorizontalLayout siteMenuLayout) {
    
    final Image orgIcon = new Image(ImageConstants.ICON_ORGANIZATION.getPath(), "");
    orgIcon.addClassName(StyleConstants.ORGANIZATION_ICON.getName());
    if (currentUser.getActivePersonOrganizactions().size() > 1) {
      siteMenuLayout.add(orgIcon, new VSpan(currentUser.getActiveOrganizationObject().getName()));
      final MenuItem sitesMenuItem = menuBar.addItem(siteMenuLayout);
      final SubMenu sitesSubMenu = sitesMenuItem.getSubMenu();
      currentUser.getAppAvailablePersonOrganizactions().stream().filter(po -> !po.getOrganization().getId().equals(currentUser.getActiveOrganizationObject().getId())).forEach(org -> sitesSubMenu.addItem(org.getOrganization().getName(),
        event -> {
          currentUser.setActiveOrganization(org);
          currentUser.getPerson().setLastActiveOrganizationId(org.getOrganization().getId());
          personService.get().saveOrUpdatePerson(currentUser.getPerson());
          UI.getCurrent().getPage().reload();
        }));
    } else if (currentUser.getActivePersonOrganizactions().size() == 1) {
      siteMenuLayout.add(orgIcon,  new VSpan(currentUser.getActiveOrganizationObject().getName()));
      menuBar.addItem(siteMenuLayout);
    }
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    final VHorizontalLayout siteMenuLayout = new VHorizontalLayout();
    siteMenuLayout.getThemeList().set("spacing", false);
  
    createOrganizationsMenu(siteMenuLayout);
    
    createLogout();
    menuLayout.add(menuBar);
  }


  /**
   * Adds the notification card.
   *
   * @param notification the notification
   */
  private void addNotificationCard(final Notification notification) {
    final NotificationItem notificationCard = new NotificationItem(notification);
    notificationItems.addComponentAtIndex(NumberUtils.INTEGER_ZERO, notificationCard);
    notificationCard.addClickListener(event -> {
      badgeIndicator.decrease();
      notificationService.get().markNotificationAsRead(notificationCard.getNotification().getId(), currentUser.getPerson().getId());
      if (badgeIndicator.getCount() == NumberUtils.INTEGER_ZERO) {
        notificationItems.removeAll();
        badgeIndicator.setVisible(false);
      } else {
        badgeIndicator.setVisible(true);

      UI.getCurrent().getPage().executeJs("document.getElementById('" + notificationCard.getId().get() + "').outerHTML = '';");
        
      }
    });

  }


  /**
   * Gets the active notifications.
   *
   * @return the active notifications
   */
  public void setActiveNotifications(List<Notification> notifications) {
    if (currentUser == null) {
      return;
    }

    notificationsMenu.removeAll();
    notificationItems.removeAll();


    if (notifications.size() > NumberUtils.INTEGER_ZERO) {
      final Div titleDiv = new Div();
      titleDiv.setClassName(StyleConstants.NOTIFICATION_TITLE.getName());
      titleDiv.add(new Paragraph(getTranslation("notification.menu.title")));
      notificationsMenu.addComponentAtIndex(NumberUtils.INTEGER_ZERO, titleDiv);
      notificationsMenu.addComponentAtIndex(NumberUtils.INTEGER_ONE, notificationItems);
      notifications.stream().forEach(this::addNotificationCard);
    }

    badgeIndicator.setVisible(!notifications.isEmpty());
    badgeIndicator.setCount(notifications.size());
  }



}
