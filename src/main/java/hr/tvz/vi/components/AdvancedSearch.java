/*
 * AdvancedSearch AdvancedSearch.java.
 * 
 */
package hr.tvz.vi.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.datepicker.VDatePicker;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VNumberField;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.gatanaso.MultiselectComboBox;

import com.github.appreciated.papermenubutton.PaperMenuButton;
import com.github.appreciated.papermenubutton.VerticalAlignment;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.QueryParameters;

import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventActivity;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Constants.VechileCondition;
import hr.tvz.vi.util.Constants.VechileType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AdvancedSearch.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @param <T> the generic type
 * @since 8:20:36 PM Sep 1, 2021
 */
public class AdvancedSearch<T> extends VDiv {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 6993798999382592250L;
  
  /** The query params. */
  private Map<String, List<String>> queryParams;

  /** The parent view route. */
  private final String parentViewRoute;
  
  /** The filter popup. */
  private PaperMenuButton filterPopup;
  
  /** The address service. */
  @Setter
  private AddressService addressService;
  
  /** The organization service. */
  @Setter
  private OrganizationService organizationService;
  
  /**
   * Instantiates a new advanced search.
   *
   * @param queryParams the query params
   * @param parentViewRoute the parent view route
   * @param addressService the address service
   */
  public AdvancedSearch(final  Map<String, List<String>> queryParams, final String parentViewRoute) {
    this.queryParams = new HashMap<String, List<String>>(queryParams);
    this.parentViewRoute = parentViewRoute;
  }
  
  /**
   * Builds the advanced search.
   *
   * @return the paper menu button
   */
  public PaperMenuButton buildAdvancedSearch() {
    final VButton triggerButton = new VButton(getTranslation("button.advancedSearch"));
    triggerButton.addThemeName(ThemeAttribute.BUTTON_BLUE);
    filterPopup = new PaperMenuButton(triggerButton, buildFilterForm());
    filterPopup.setVerticalAlignment(VerticalAlignment.TOP);
    filterPopup.setVerticalOffset(50);
    filterPopup.setDynamicAlign(true);
    return filterPopup;
  }
  

  /**
   * Builds the filter form.
   *
   * @return the component
   */
  private Component buildFilterForm() {
    VVerticalLayout layout = new VVerticalLayout();
    Utils.removeAllThemes(layout);
    layout.getThemeList().add(ThemeAttribute.PADDING);
    CustomFormLayout<T> formLayout = new CustomFormLayout<T>(null, null);
    layout.addClassName(StyleConstants.POPUP_FORM.getName());
    layout.setWidth("470px");
    VDiv headerDiv = new VDiv();
    headerDiv.add(new VSpan(getTranslation("advancedSearch.title")));
    headerDiv.getElement().getThemeList().add(ThemeAttribute.POPUP_HEADER);
    layout.add(headerDiv);
    if(StringUtils.equals(parentViewRoute, Routes.MEMBERS)) {
      initMembersFormFields(formLayout);
    }else if(StringUtils.equals(parentViewRoute, Routes.VECHILES)) {
      initVehicleFormFields(formLayout);
    }else if(StringUtils.equals(parentViewRoute, Routes.REPORTS)) {
      initReportFormFields(formLayout);
    }
    layout.add(formLayout);
    return layout;
  }

  /**
   * Inits the report form fields.
   *
   * @param formLayout the form layout
   */
  private void initReportFormFields(CustomFormLayout<T> formLayout) {
    VTextField identificationNumber = new VTextField();
    identificationNumber.setValue(queryParams.getOrDefault(Report.Fields.identificationNumber, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(identificationNumber, "reportsView.reportsGrid.identificationNumber");
    
    VDatePicker eventDate = new VDatePicker();
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    try {
      LocalDate date = LocalDate.parse(queryParams.getOrDefault("eventDate", Arrays.asList(StringUtils.EMPTY)).get(0), formatter);
      eventDate.setValue(date);
    }catch (DateTimeParseException e) {
      eventDate.setValue(null);
    }
    formLayout.addFormItem(eventDate, "reportsView.reportsGrid.eventDateTime");
    
    MultiselectComboBox<EventType> eventType = new MultiselectComboBox<EventType>();
    eventType.setItemLabelGenerator(c -> getTranslation(c.getEventTypeTranslationKey()));
    eventType.setItems(Arrays.asList(EventType.values()));
    eventType.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    eventType.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    eventType.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    eventType.setValue(queryParams.getOrDefault(Report.Fields.eventType, Arrays.asList(StringUtils.EMPTY)).stream().map(c -> EventType.getEventType(c)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(eventType, "vechileForm.field.condition");
    
    MultiselectComboBox<ReportStatus> status = new MultiselectComboBox<ReportStatus>();
    status.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    status.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    status.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    status.setItemLabelGenerator(c -> getTranslation(c.getReportStatusTranslationKey()));
    status.setItems(Arrays.asList(ReportStatus.values()));
    status.setValue(queryParams.getOrDefault(Report.Fields.status, Arrays.asList(StringUtils.EMPTY)).stream().map(c -> ReportStatus.getReportStatus(c)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(status, "reportsView.reportsGrid.status");
   
    VSelect<City> city = new VSelect<City>();
    List<County> counties = addressService.getAllCounties();
    VSelect<County> county = new VSelect<County>();
    county.setItemLabelGenerator(c -> c.getName());
    county.setItems(counties);
    county.addValueChangeListener(e -> city.setItems(addressService.getCities(e.getValue())));
    formLayout.addFormItem(county, "reportEventView.form.county");
    
    city.setItemLabelGenerator(c -> c.getName());
    formLayout.addFormItem(city, "reportEventView.form.city");
    String cityName = queryParams.getOrDefault("eventCity", Arrays.asList(StringUtils.EMPTY)).get(0);
    if(StringUtils.isNotBlank(cityName)) {
      Optional<City> cityOpt = addressService.getAllCities().stream().filter(c -> cityName.equals(c.getName())).findFirst();
      if(cityOpt.isPresent()) {
        county.setValue(cityOpt.get().getCounty());
        city.setValue(cityOpt.get());
      }
    }
    
    MultiselectComboBox<Organization> eventOrganizations = new MultiselectComboBox<Organization>();
    eventOrganizations.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    eventOrganizations.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    eventOrganizations.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    eventOrganizations.setItemLabelGenerator(o -> o.getName());
    List<Organization> organizations = organizationService.getOrganizationsByLevel(OrganizationLevel.OPERATIONAL_LEVEL);
    eventOrganizations.setItems(organizations);
    List<String> organizationIds = queryParams.getOrDefault("eventOrganization", Arrays.asList(StringUtils.EMPTY)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    if(!organizationIds.isEmpty()) {
     eventOrganizations.setValue(organizations.stream().filter(o -> organizationIds.contains(o.getId().toString())).collect(Collectors.toSet()));
    }
    formLayout.addFormItem(eventOrganizations, "reportEventView.form.eventOrganizationList");
    

    VTextField reporter = new VTextField();
    reporter.setValue(queryParams.getOrDefault(Report.Fields.reporter, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(reporter, "reportEventView.form.reporter");
    

    
    VButton filterButton = formLayout.addButton("advancedSearch.button.filter", e -> {
      setStringParamValue(Report.Fields.identificationNumber, identificationNumber.getValue());
      setStringParamValue("eventDate", eventDate.getValue() != null ? eventDate.getValue().format(formatter) : null);
      setStringParamValue(Report.Fields.reporter, reporter.getValue());
      
      removeQueryParams(Report.Fields.eventType);
      List<String> values = eventType.getValue().stream().map(g -> g.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put(Report.Fields.eventType, values);
      }
      
      removeQueryParams(Report.Fields.status);
      values = status.getValue().stream().map(p -> p.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put(Report.Fields.status, values);
      }
      
      setStringParamValue("eventCity", city.getValue() == null ? null : city.getValue().getId().toString());
      
      removeQueryParams("eventOrganization");
      values = eventOrganizations.getValue().stream().map(o -> o.getId().toString()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put("eventOrganization", values);
      }
      
      applyFilter();
    });
    filterButton.getElement().getThemeList().add(ThemeAttribute.BUTTON_BLUE);
  }

  /**
   * Inits the vehicle form fields.
   *
   * @param formLayout the form layout
   */
  private void initVehicleFormFields(CustomFormLayout<T> formLayout) {
    VTextField make = new VTextField();
    make.setValue(queryParams.getOrDefault(Vechile.Fields.make, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(make, "vechileForm.field.make");
    
    VTextField model = new VTextField();
    model.setValue(queryParams.getOrDefault(Vechile.Fields.model, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(model, "vechileForm.field.model");
    
    VTextField vechileNumber = new VTextField();
    vechileNumber.setValue(queryParams.getOrDefault(Vechile.Fields.vechileNumber, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(vechileNumber, "vechileForm.field.vechileNumber");
    
    VTextField licencePlateNumber = new VTextField();
    licencePlateNumber.setValue(queryParams.getOrDefault(Vechile.Fields.licencePlateNumber, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(licencePlateNumber, "vechileForm.field.licencePlateNumber");
    
    MultiselectComboBox<VechileCondition> condition = new MultiselectComboBox<VechileCondition>();
    condition.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    condition.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    condition.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    condition.setItemLabelGenerator(c -> getTranslation(c.getLabelKey()));
    condition.setItems(Arrays.asList(VechileCondition.values()));
    condition.setValue(queryParams.getOrDefault(Vechile.Fields.condition, Arrays.asList(StringUtils.EMPTY)).stream().map(c -> VechileCondition.getVechileCondition(c)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(condition, "vechileForm.field.condition");

    
    MultiselectComboBox<VechileType> vechileType = new MultiselectComboBox<VechileType>();
    vechileType.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    vechileType.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    vechileType.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    vechileType.setItemLabelGenerator(c -> getTranslation(c.getLabelKey()));
    vechileType.setItems(Arrays.asList(VechileType.values()));
    vechileType.setValue(queryParams.getOrDefault(Vechile.Fields.type, Arrays.asList(StringUtils.EMPTY)).stream().map(c -> VechileType.getVechileType(c)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(vechileType, "vechileForm.field.type");
    
    VButton filterButton = formLayout.addButton("advancedSearch.button.filter", e -> {
      setStringParamValue(Vechile.Fields.make, make.getValue());
      setStringParamValue(Vechile.Fields.model, model.getValue());
      setStringParamValue(Vechile.Fields.vechileNumber,vechileNumber.getValue());
      setStringParamValue(Vechile.Fields.licencePlateNumber, licencePlateNumber.getValue());
      removeQueryParams(Vechile.Fields.condition);
      List<String> values = condition.getValue().stream().map(g -> g.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put(Vechile.Fields.condition, values);
      }
      
      removeQueryParams(Vechile.Fields.type);
      values = vechileType.getValue().stream().map(p -> p.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put(Vechile.Fields.type, values);
      }
      applyFilter();
    });
    filterButton.getElement().getThemeList().add(ThemeAttribute.BUTTON_BLUE);
  }

  /**
   * Inits the members form fields.
   *
   * @param formLayout the form layout
   */
  private void initMembersFormFields(CustomFormLayout<T> formLayout) {
    VTextField name = new VTextField();
    name.setValue(queryParams.getOrDefault(Person.Fields.name, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(name, "memberForm.field.name");
    
    VTextField lastname = new VTextField();
    lastname.setValue(queryParams.getOrDefault(Person.Fields.lastname, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(lastname, "memberForm.field.lastname");
    
    VTextField identificationNumber = new VTextField();
    lastname.setValue(queryParams.getOrDefault(Person.Fields.identificationNumber, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(identificationNumber, "memberForm.field.identificationNumber");
    
    VTextField email = new VTextField();
    lastname.setValue(queryParams.getOrDefault(Person.Fields.email, Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(email, "memberForm.field.email");
    
   
    VNumberField minbirthYear = new VNumberField();
    String min = queryParams.getOrDefault("minBirthYear", Arrays.asList(StringUtils.EMPTY)).get(0);
    if(NumberUtils.isParsable(min)) {
      minbirthYear.setValue(NumberUtils.createDouble(min));
    }
    formLayout.setLabel(minbirthYear, "advancedSearch.member.minBirthYear");
    
    VNumberField maxBirthYear = new VNumberField(); 
    String max = queryParams.getOrDefault("maxBirthYear", Arrays.asList(StringUtils.EMPTY)).get(0);
    if(NumberUtils.isParsable(max)) {
      maxBirthYear.setValue(NumberUtils.createDouble(max));
    }
    formLayout.setLabel(maxBirthYear, "advancedSearch.member.maxBirthYear");
    formLayout.addTwoColumnItemsLayout(minbirthYear, maxBirthYear);
    
    MultiselectComboBox<Gender> gender = new MultiselectComboBox<Gender>();
    gender.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    gender.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    gender.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    gender.setItemLabelGenerator(g -> getTranslation(g.getGenderTranslationKey()));
    gender.setItems(Arrays.asList(Gender.values()));
    gender.setValue(queryParams.getOrDefault(Person.Fields.gender, Arrays.asList(StringUtils.EMPTY)).stream().map(g -> Gender.getGender(g)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(gender, "memberForm.field.gender");

    
    MultiselectComboBox<Professions> profession = new MultiselectComboBox<Professions>();
    profession.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    profession.setPlaceholder(getTranslation("placeholder.combobox.selected", 0));
    profession.addValueChangeListener(e -> e.getSource().setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())));
    profession.setItemLabelGenerator(p -> getTranslation(p.getProfessionTranslationKey()));
    profession.setItems(Arrays.asList(Professions.values()));
    profession.setValue(queryParams.getOrDefault(Person.Fields.profession, Arrays.asList(StringUtils.EMPTY)).stream().map(p -> Professions.getProfession(p)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(profession, "memberForm.field.profession");
    
    VButton filterButton = formLayout.addButton("advancedSearch.button.filter", e -> {
      setStringParamValue(Person.Fields.name, name.getValue());
      setStringParamValue(Person.Fields.lastname, lastname.getValue());
      setStringParamValue(Person.Fields.identificationNumber, identificationNumber.getValue());
      setStringParamValue(Person.Fields.email, email.getValue());
      setStringParamValue("minBirthYear", ObjectUtils.defaultIfNull(minbirthYear.getValue(), StringUtils.EMPTY).toString());
      setStringParamValue("maxBirthYear", ObjectUtils.defaultIfNull(maxBirthYear.getValue(), StringUtils.EMPTY).toString());
      removeQueryParams(Person.Fields.gender);
      List<String> values = gender.getValue().stream().map(g -> g.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put(Person.Fields.gender, values);
      }
      
      removeQueryParams(Person.Fields.profession);
      values = profession.getValue().stream().map(p -> p.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put(Person.Fields.profession, values);
      }
      applyFilter();
    });
    filterButton.getElement().getThemeList().add(ThemeAttribute.BUTTON_BLUE);
  }

  /**
   * Sets the string param value.
   *
   * @param key the key
   * @param name the name
   */
  private void setStringParamValue(String key, String value) {
    removeQueryParams(key);
    if(StringUtils.isNotBlank(value)) {
      queryParams.put(key, Arrays.asList(value));
    }
  }
  
  
  /**
   * Removes the query params.
   *
   * @param key the key
   */
  private void removeQueryParams(String key) {
    if(queryParams.containsKey(key)) {
      queryParams.remove(key);
    }
  }
  
  /**
   * Apply filter.
   */
  private void applyFilter() {
    UI.getCurrent().navigate(parentViewRoute, new QueryParameters(queryParams));
    UI.getCurrent().getPage().reload();
  }


 
  
  

}
