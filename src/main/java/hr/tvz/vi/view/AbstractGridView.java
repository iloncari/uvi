/*
 * AbstractGridView AbstractGridView.java.
 *
 */
package hr.tvz.vi.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.AdvancedSearch;
import hr.tvz.vi.components.SimpleSearch;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.FieldType;
import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Constants.VechileCondition;
import hr.tvz.vi.util.Constants.VechileType;
import hr.tvz.vi.util.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AbstractGridView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @param <T> the generic type
 * @since 6:10:55 PM Aug 14, 2021
 */
@EventSubscriber(scope = SubscriberScope.PUSH)
@Slf4j
public abstract class AbstractGridView<T> extends VVerticalLayout implements HasDynamicTitle, HasUrlParameter<String> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8662193361768278434L;

  /** The grid. */
  @Getter
  private final VGrid<T> grid = new VGrid<>();

  /** The current user. */
  @Getter
  private final CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
  
  /** The simple search. */
  private final boolean simpleSearch;
  
  /** The advanced search. */
  private final boolean advancedSearch;
  
  /** The query params. */
  @Getter
  private Map<String, List<String>> queryParams;
  
  /** The tags layout. */
  private VHorizontalLayout tagsLayout = new VHorizontalLayout();
  
  /** The address service. */
  @Autowired
  ServiceRef<AddressService> addressService;
  
  /** The organization service. */
  @Autowired
  ServiceRef<OrganizationService> organizationService;

  /**
   * Instantiates a new abstract grid view.
   *
   * @param simpleSearch the simple search
   * @param advancedSearch the advanced search
   * @param queryParams the query params
   */
  public AbstractGridView(boolean simpleSearch, boolean advancedSearch) {
    this.simpleSearch = simpleSearch;
    this.advancedSearch = advancedSearch;
    add(new VH3(getViewTitle()));
    getGrid().addThemeNames(ThemeAttribute.WRAP_CELL_CONTENT, ThemeAttribute.COMPACT, ThemeAttribute.TABLE);
    getGrid().getElement().setAttribute("style", "touch-action: none; border: none;");
    getGrid().setHeightByRows(true);
  }
  
  /**
   * Sets the query params.
   *
   * @param params the params
   */
  protected void setQueryParams(Map<String, List<String>> params) {
    this.queryParams = new HashMap<String, List<String>>(params);
  }
  
  
  /**
   * Sets the parameter.
   *
   * @param event the event
   * @param parameter the parameter
   */
  @Override
  public void setParameter(final BeforeEvent event, @OptionalParameter final String parameter) {
    final Location location = event.getLocation();
    final QueryParameters queryParameters = location
      .getQueryParameters();

    this.queryParams = new HashMap<String, List<String>>(queryParameters.getParameters());
    
    
    
    
  }

  /**
   * Gets the grid items.
   *
   * @return the grid items
   */
  public abstract List<T> getGridItems();
  
  /**
   * Gets the route.
   *
   * @return the route
   */
  public abstract String getRoute();

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public abstract String getPageTitle();

  /**
   * Gets the view title.
   *
   * @return the view title
   */
  protected abstract String getViewTitle();

  /**
   * Inits the bellow button layout.
   *
   * @return the v horizontal layout
   */
  protected abstract VHorizontalLayout initBellowButtonLayout();
  
  /**
   * Inits the above button layout.
   *
   * @return the v horizontal layout
   */
  protected abstract VHorizontalLayout initAboveLayout();

  /**
   * Inits the grid layout.
   */
  protected abstract void initGrid();

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    ChangeBroadcaster.registerToPushEvents(this);
    removeAll();
    if(simpleSearch || advancedSearch) {
      VHorizontalLayout searchLayout = new VHorizontalLayout();
      if(simpleSearch) {
        searchLayout.add(new SimpleSearch(queryParams, getRoute()));
      }
      if(advancedSearch) {
        AdvancedSearch<T> advancedSearch = new AdvancedSearch<T>(queryParams, getRoute());
        advancedSearch.setAddressService(addressService.get());
        advancedSearch.setOrganizationService(organizationService.get());
        searchLayout.add(advancedSearch.buildAdvancedSearch());
      }
      add(searchLayout);
      buildTagsLayout();
      add(tagsLayout);
    }
    
    VHorizontalLayout aboveGrid = initAboveLayout();
    if(aboveGrid != null) {
      add(aboveGrid);
    }
    initGrid();
    add(grid);
    grid.setItems(getGridItems());
    VHorizontalLayout buttons = initBellowButtonLayout();
    if(buttons != null) {
      add(buttons);
    }
  }

  /**
   * Builds the tags layout.
   *
   * @return the v horizontal layout
   */
  private void buildTagsLayout() {
    tagsLayout.removeAll();
    queryParams.forEach((key, values) -> {
      if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.STRING)) {
        values.forEach(value -> placeTag(key, value, value));
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.NUMBER)) {
        values.forEach(value -> placeTag(key, value, value.toString()));
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.GENDER)) {
        values.stream().map(g -> Gender.getGender(g)).filter(Objects::nonNull).collect(Collectors.toSet()).forEach(gender -> {
          placeTag(key, gender.getName(),getTranslation(gender.getGenderTranslationKey()));
        });
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.PROFESSION)) {
        values.stream().map(p -> Professions.getProfession(p)).filter(Objects::nonNull).collect(Collectors.toSet()).forEach(p -> {
          placeTag(key,p.getName(), getTranslation(p.getProfessionTranslationKey()));
        });
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.VEHICLE_CONDITION)) {
        values.stream().map(p -> VechileCondition.getVechileCondition(p)).filter(Objects::nonNull).collect(Collectors.toSet()).forEach(p -> {
          placeTag(key,p.getName(), getTranslation(p.getLabelKey()));
        });
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.VEHICLE_TYPE)) {
        values.stream().map(p -> VechileType.getVechileType(p)).filter(Objects::nonNull).collect(Collectors.toSet()).forEach(p -> {
          placeTag(key,p.getName(), getTranslation(p.getLabelKey()));
        });
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.DATE)) {
        values.forEach(value -> placeTag(key, value, value));
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.EVENT_TYPE)) {
        values.stream().map(p -> EventType.getEventType(p)).filter(Objects::nonNull).collect(Collectors.toSet()).forEach(p -> {
          placeTag(key,p.getName(), getTranslation(p.getEventTypeTranslationKey()));
        });
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.CITY)) {
        List<City> cities = addressService.get().getAllCities();
        values.forEach(cityId ->cities.stream().filter(c -> c.getId().toString().equals(cityId)).findFirst().ifPresent(c -> placeTag(key, c.getName(), c.getName())));
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.REPORT_STATUS)) {
        values.stream().map(p -> ReportStatus.getReportStatus(p)).filter(Objects::nonNull).collect(Collectors.toSet()).forEach(p -> {
          placeTag(key,p.getName(), getTranslation(p.getReportStatusTranslationKey()));
        });
      }else if(Utils.FIELD_TYPE.getOrDefault(key, FieldType.STRING).equals(FieldType.ORGANIZATION)) {
        List<Organization> organizations = organizationService.get().getOrganizationsByLevel(OrganizationLevel.OPERATIONAL_LEVEL);
        values.forEach(orgId ->organizations.stream().filter(o -> o.getId().toString().equals(orgId)).findFirst().ifPresent(c -> placeTag(key, c.getName(), c.getName())));
      }
        
    });
    
  }


  /**
   * Place tag.
   *
   * @param key the key
   * @param value the value
   */
  private void placeTag(String key, String value, String translatedValue) {
    VSpan tag = new VSpan(getTranslation("tag.".concat(key).concat(".label")).concat(":").concat(translatedValue));
    tag.addClickListener(e -> {
      tagsLayout.remove(tag);
      List<String> newValues = new ArrayList<>(queryParams.get(key));
      newValues.remove(value);
      queryParams.replace(key,  newValues);
      if(newValues.isEmpty()) {
        queryParams.remove(key);
      }
      UI.getCurrent().navigate(getRoute(), new QueryParameters(queryParams));
      UI.getCurrent().getPage().reload();
    });
    tagsLayout.add(tag);
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
   * Sets the grid items.
   */
  protected void setGridItems() {
    grid.setItems(getGridItems());
    grid.getDataProvider().refreshAll();
  }

}
