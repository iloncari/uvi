/*
 * ReportBasicDataTab ReportBasicDataTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import org.vaadin.firitin.components.datetimepicker.VDateTimePicker;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VTextArea;
import org.vaadin.firitin.components.textfield.VTextField;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

import hr.tvz.vi.event.EventTypeChangedObserver;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.util.Constants.EventCause;
import hr.tvz.vi.util.Constants.EventCausePerson;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.StyleConstants;

/**
 * The Class ReportBasicDataTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 7:37:31 PM Aug 22, 2021
 */
public class ReportBasicDataTab extends VVerticalLayout{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -8100160983095189096L;
  
  /** The report. */
  private Report report;
  
  /** The report layout. */
  private CustomFormLayout<Report> reportLayoutLeft;
  
  /** The report layout right. */
  private CustomFormLayout<Report> reportLayoutRight;
  
  /** The county. */
  private VSelect<County> county;
  
  /** The city. */
  private VSelect<City> city;
   
  /** The address service. */
  private AddressService addressService;
  
  /** The event type changed observer. */
  private EventTypeChangedObserver eventTypeChangedObserver;
  
  /** The binder. */
  private  Binder<Report> binder;
  
  /** The tab component map. */
  private  Map<Component, Integer> tabComponentMap;
  
  /** The edit right. */
  private final boolean editRight;
  
  /**
   * Instantiates a new report basic data tab.
   *
   * @param report the report
   * @param binder the binder
   * @param tabComponentMap the tab component map
   * @param addressService the address service
   * @param eventTypeChangedObserver the event type changed observer
   */
  public ReportBasicDataTab(Report report,  Binder<Report> binder, boolean editRights, Map<Component, Integer> tabComponentMap,  AddressService addressService, EventTypeChangedObserver eventTypeChangedObserver) {
    this.report = report;
    this.addressService=addressService;
    this.eventTypeChangedObserver = eventTypeChangedObserver;
    this.binder = binder;
    this.tabComponentMap = tabComponentMap;
    this.editRight = editRights;
    initBasicDataForm();
  }

  /**
   * Inits the basic data form.
   */
  @SuppressWarnings("unchecked")
  private void initBasicDataForm() {
    reportLayoutLeft = (CustomFormLayout<Report>)new CustomFormLayout<Report>(binder, report).withClassName(StyleConstants.WIDTH_50.getName());
   
    VDateTimePicker eventDateTime = new VDateTimePicker(LocalDateTime.now()).withReadOnly(!editRight);
    reportLayoutLeft.setLabel(eventDateTime, true, "reportEventView.form.eventDateTime");
    reportLayoutLeft.processBinder(eventDateTime, null, null, true, "eventDateTime");
    VTextField interventionNumber = new VTextField().withReadOnly(!editRight);
    reportLayoutLeft.setLabel(interventionNumber, "reportView.basicDataTab.form.identificationNumber");
    reportLayoutLeft.processBinder(interventionNumber, null, null, true, "identificationNumber");
    reportLayoutLeft.addTwoColumnItemsLayout(eventDateTime, interventionNumber);
    tabComponentMap.put(eventDateTime, 0);
    tabComponentMap.put(interventionNumber, 0);
    
    county = new VSelect<County>().withReadOnly(!editRight);
    county.setItemLabelGenerator(c -> c.getName());
    reportLayoutLeft.setLabel(county, true, "reportEventView.form.county");
    city = new VSelect<City>().withReadOnly(!editRight);
    city.setItemLabelGenerator(c -> c.getName());
    county.addValueChangeListener(e -> city.setItems(addressService.getCities(e.getValue())));
    reportLayoutLeft.setLabel(city, true, "reportEventView.form.city");
    reportLayoutLeft.processBinder(city, null, null, true, "eventAddress.city");
    reportLayoutLeft.addTwoColumnItemsLayout(county, city);
    tabComponentMap.put(county, 0);
    tabComponentMap.put(city, 0);
    
    VTextField street = new VTextField().withReadOnly(!editRight);
    reportLayoutLeft.setLabel(street, "reportEventView.form.street");
    reportLayoutLeft.processBinder(street, null, null, false, "eventAddress.street");
    VTextField streetNum = new VTextField().withReadOnly(!editRight);
    reportLayoutLeft.setLabel(streetNum, "reportEventView.form.streetNumber");
    reportLayoutLeft.processBinder(streetNum, null, null, false, "eventAddress.streetNumber");
    reportLayoutLeft.addTwoColumnItemsLayout(street, StyleConstants.WIDTH_75, streetNum, StyleConstants.WIDTH_25);
    
    VTextField reporter = new VTextField().withReadOnly(!editRight);
    reportLayoutLeft.setLabel(reporter, "reportEventView.form.reporter");
    reportLayoutLeft.processBinder(reporter, null, null, false, "reporter");
    VTextArea eventDescription= new VTextArea().withReadOnly(!editRight);
    reportLayoutLeft.setLabel(eventDescription, "reportEventView.form.eventDescription");
    reportLayoutLeft.processBinder(eventDescription, null, null, false, "eventDescription");
    reportLayoutLeft.addTwoColumnItemsLayout(reporter,  eventDescription);
    
    
    reportLayoutRight = (CustomFormLayout<Report>)new CustomFormLayout<Report>(binder, report).withClassName(StyleConstants.WIDTH_50.getName());
    
    VSelect<EventType> eventType = new VSelect<EventType>().withReadOnly(!editRight);
    eventType.setItems(Arrays.asList(EventType.values()));
    eventType.setItemLabelGenerator(type -> getTranslation(type.getEventTypeTranslationKey()));
    eventType.setValue(report.getEventType());
    eventType.addValueChangeListener(e ->eventTypeChangedObserver.eventTypeChanged(e.getValue()));
    reportLayoutRight.addFormItem(eventType, "reportEventView.form.eventType");
    
    VSelect<EventCause> eventCause = new VSelect<EventCause>().withReadOnly(!editRight);
    eventCause.setItems(Arrays.asList(EventCause.values()));
    eventCause.setItemLabelGenerator(cause -> getTranslation(cause.getEventCauseTranslationKey()));
    reportLayoutRight.processBinder(eventCause, null, null, true, "eventCause");
    reportLayoutRight.addFormItem(eventCause,"reportView.basicDataTab.form.eventCause");
    tabComponentMap.put(eventCause, 0);
    
    VSelect<EventCausePerson> eventCausePerson = new VSelect<EventCausePerson>().withReadOnly(!editRight);
    eventCausePerson.setItems(Arrays.asList(EventCausePerson.values()));
    eventCausePerson.setItemLabelGenerator(causePerson -> getTranslation(causePerson.getEventCausePersonTranslationKey()));
    reportLayoutRight.processBinder(eventCausePerson, null, null, true, "eventCausePerson");
    reportLayoutRight.addFormItem(eventCausePerson,"reportView.basicDataTab.form.eventCausePerson");
    tabComponentMap.put(eventCausePerson, 0);
    
    VHorizontalLayout formsLayout = new VHorizontalLayout();
    formsLayout.add(reportLayoutLeft, reportLayoutRight);
    
    add(formsLayout);
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    county.setItems(addressService.getAllCounties());
    reportLayoutLeft.setBean();
    reportLayoutRight.setBean();
    if(report.getEventAddress() != null) {
      county.setValue(report.getEventAddress().getCity().getCounty());
      city.setValue(report.getEventAddress().getCity());
      
    }
  }
  
  

}
