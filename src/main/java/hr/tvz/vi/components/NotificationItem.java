/*
 * NotificationItem NotificationItem.java.
 * 
 */
package hr.tvz.vi.components;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.shared.Registration;

import hr.tvz.vi.orm.Notification;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.ImageConstants;
import hr.tvz.vi.util.Constants.NotificationType;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.view.OrganizationView;
import hr.tvz.vi.view.ReportView;
import hr.tvz.vi.view.VechileView;
import lombok.Getter;

/**
 * The Class NotificationItem.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 9:37:24 PM Aug 31, 2021
 */
public class NotificationItem extends VHorizontalLayout{
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -5431444942772039197L;

  /** The close button. */
  private final VButton closeButton = new VButton(new Image(ImageConstants.ICON_CLOSE.getPath(), StringUtils.EMPTY));

  /** The notification. */
  @Getter
  private transient Notification notification;

  /** The click registration. */
  private final Registration clickRegistration;

  /**
   * Instantiates a new notification card.
   *
   * @param notification the notification
   */
  public NotificationItem(final Notification notification) {
    Utils.removeAllThemes(this);
    addClassName(StyleConstants.NOTIFICATION_ITEM.getName());

    setId("NOTIFICATION_" + notification.getId());

    this.notification = notification;

    final VDiv notificationCardContent = new VDiv();
    notificationCardContent.addClassName(StyleConstants.NOTIFICATION_ITEM_CONTENT.getName());

    notificationCardContent.add(new Paragraph(notification.getTitle()), new Span(notification.getMessage()), new Paragraph(notification.getCreationDateTime().toString()));
    add(notificationCardContent, closeButton);

    clickRegistration = addClickListener(event -> {
      if(NotificationType.GROUP.equals(notification.getType())) {
        UI.getCurrent().navigate(OrganizationView.class);
      }else if(NotificationType.TASK.equals(notification.getType())){
        UI.getCurrent().navigate(ReportView.class, notification.getSourceId().toString());
      }else if(NotificationType.VECHILE.equals(notification.getType())){
        UI.getCurrent().navigate(VechileView.class, notification.getSourceId().toString());
      }
    });

    closeButton.addClickListener(() -> NotificationItem.this.clickRegistration.remove());
    closeButton.removeThemeName(ThemeAttribute.ICON);
    closeButton.addThemeName(ThemeAttribute.BUTTON_ICON);
  }

}
