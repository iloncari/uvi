/*
 * Utils Utils.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.util;

import com.google.common.eventbus.AsyncEventBus;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

import hr.tvz.vi.auth.AccessControlImpl;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.util.Constants.FieldType;
import hr.tvz.vi.util.Constants.Searchable;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.UserRole;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public final class Utils {
  
  /** The Constant FIELD_TYPE. */
  public static final Map<String, FieldType> FIELD_TYPE = new  HashMap<String, FieldType>();
  
  static {
    FIELD_TYPE.put(Person.Fields.name, FieldType.STRING);
    FIELD_TYPE.put(Person.Fields.lastname, FieldType.STRING);
    FIELD_TYPE.put(Person.Fields.identificationNumber, FieldType.STRING);
    FIELD_TYPE.put(Person.Fields.email, FieldType.STRING);
    FIELD_TYPE.put(Person.Fields.gender, FieldType.GENDER);
    FIELD_TYPE.put(Person.Fields.profession, FieldType.PROFESSION);
    FIELD_TYPE.put("minBirthYear", FieldType.NUMBER);
    FIELD_TYPE.put("maxBirthYear", FieldType.NUMBER);
    
    FIELD_TYPE.put(Vechile.Fields.make, FieldType.STRING);
    FIELD_TYPE.put(Vechile.Fields.model, FieldType.STRING);
    FIELD_TYPE.put(Vechile.Fields.licencePlateNumber, FieldType.STRING);
    FIELD_TYPE.put(Vechile.Fields.vechileNumber, FieldType.STRING);
    FIELD_TYPE.put(Vechile.Fields.condition, FieldType.VEHICLE_CONDITION);
    FIELD_TYPE.put(Vechile.Fields.type, FieldType.VEHICLE_TYPE);
    
    FIELD_TYPE.put(Report.Fields.identificationNumber, FieldType.STRING);
    FIELD_TYPE.put("eventDate", FieldType.DATE);
    FIELD_TYPE.put(Report.Fields.eventType, FieldType.EVENT_TYPE);
    FIELD_TYPE.put("eventCity", FieldType.CITY);
    FIELD_TYPE.put(Report.Fields.reporter, FieldType.STRING);
    FIELD_TYPE.put("eventOrganization", FieldType.ORGANIZATION);
    FIELD_TYPE.put(Report.Fields.status, FieldType.REPORT_STATUS);
  }

  /**
   * Gets the current person.
   *
   * @param ui the ui
   * @return the current person
   */
  public static CurrentUser getCurrentUser(final UI ui) {
    Person person = null;
    if (ui != null) {
      person = ComponentUtil.getData(ui, Person.class);
    }
    if (person == null && VaadinService.getCurrentRequest() != null) {
      return (CurrentUser) VaadinService.getCurrentRequest().getWrappedSession()
        .getAttribute(AccessControlImpl.CURRENT_USER_SESSION_ATTRIBUTE_KEY);
    }
    return null;
  }
  
  /**
   * Removes the all themes.
   *
   * @param component the component
   */
  public static void removeAllThemes(final Component component) {
    removeAllThemes(component.getElement());
  }

  /**
   * Removes the all themes.
   *
   * @param element the element
   */
  public static void removeAllThemes(final Element element) {
    element.getThemeList().removeAll(element.getThemeList());
  }

  /**
   * Gets the session event bus.
   *
   * @return the session event bus
   */
  public static AsyncEventBus getSessionEventBus() {
    final VaadinRequest request = VaadinService.getCurrentRequest();
    if (request == null) {
      throw new IllegalStateException("No request bound to current thread.");
    }

    AsyncEventBus eventBus = (AsyncEventBus) request.getWrappedSession().getAttribute(ChangeBroadcaster.class.getCanonicalName());
    if (null == eventBus) {
      eventBus = new AsyncEventBus("vi-session-event-bus", Executors.newCachedThreadPool());
      request.getWrappedSession().setAttribute(ChangeBroadcaster.class.getCanonicalName(), eventBus);
    }

    return eventBus;
  }

  /**
   * Checks if is user has edit rights.
   *
   * @param currentUser the current user
   * @param memberForEdit the member for edit
   * @return true, if is user has edit rights
   */
  public static boolean isUserHasEditRights(CurrentUser currentUser, Person memberForEdit) {
    final boolean editYourself = memberForEdit != null && currentUser.getPerson().getId().equals(Long.valueOf(memberForEdit.getId()));
    return editYourself || UserRole.MANAGER.equals(currentUser.getActiveOrganization().getRole());
  }

  /**
   * Show error notification.
   *
   * @param duration the duration
   * @param position the position
   * @param translationKey the translation key
   * @param translationParams the translation params
   */

  public static void showErrorNotification(final int duration, final Position position, final String translationKey, final Object... translationParams) {
    final Notification notification = new Notification(StringUtils.EMPTY, duration, position);
    notification.setText(notification.getTranslation(translationKey, translationParams));
    notification.addThemeName(StyleConstants.THEME_PRIMARY_ERROR.getName());
    notification.open();
  }

  /**
   * Show success notification.
   *
   * @param duration the duration
   * @param position the position
   * @param translationKey the translation key
   * @param translationParams the translation params
   */
  public static void showSuccessNotification(final int duration, final Position position, final String translationKey, final Object... translationParams) {
    final Notification notification = new Notification(StringUtils.EMPTY, duration, position);
    notification.setText(notification.getTranslation(translationKey, translationParams));
    notification.addThemeName(StyleConstants.THEME_PRIMARY_SUCCESS.getName());
    notification.open();
  }
  
  /**
   * Gets the searchable fields.
   *
   * @param classType the class type
   * @return the searchable fields
   */
  public static List<String> getSearchableFields(Object obj){
    return Arrays.asList(obj.getClass().getDeclaredFields()).stream(). 
      filter(field -> field.getAnnotation(Searchable.class)!=null).map(field -> field.getName())
     .collect(Collectors.toList());
  }

}
