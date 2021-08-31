/*
 * ReportForcesTab ReportForcesTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.swing.Renderer;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.format.datetime.DateFormatter;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.datetimepicker.VDateTimePicker;
import org.vaadin.firitin.components.dialog.VDialog;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.html.VH5;
import org.vaadin.firitin.components.html.VParagaph;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VNumberField;
import org.vaadin.firitin.components.textfield.VTextField;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.converter.DoubleToIntegerConverter;
import hr.tvz.vi.orm.EventOrganization;
import hr.tvz.vi.orm.EventOrganizationPerson;
import hr.tvz.vi.orm.EventOrganizationVechile;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.service.VechileService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportForcesTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 9:56:06 PM Aug 22, 2021
 */

/** The Constant log. */
@Slf4j
public class ReportForcesTab extends VVerticalLayout{
  
  /** The report. */
  private Report report;
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -2451807794983342377L;
  
  /** The organization repository. */
  private OrganizationService organizationService;
  
  /** The vechile service. */
  private VechileService vechileService;
  
  /** The report service. */
  private ReportService reportService;
  
  /** The current user. */
  private CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
  
  /** The removed event vechiles. */
  private Set<EventOrganizationVechile> removedEventVechiles;
  
  /** The event vechiles. */
  private Set<EventOrganizationVechile> eventVechiles;
  
  /** The removed event persons. */
  private Set<EventOrganizationPerson> removedEventPersons;
  
  /** The event persons. */
  private Set<EventOrganizationPerson> eventPersons;
  
  /** The edit rights. */
  private final boolean editRight;
  
  
  /**
   * Instantiates a new report forces tab.
   *
   * @param report the report
   * @param tabComponentMap the tab component map
   * @param organizatonService the organizaton service
   * @param vechileService the vechile service
   * @param reportService the report service
   */
  public ReportForcesTab(Report report, boolean editRights, OrganizationService organizatonService, VechileService vechileService, ReportService reportService) {
    this.report = report;
    this.organizationService = organizatonService;
    this.vechileService = vechileService;
    this.reportService = reportService;
    this.editRight = editRights;
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    removeAll();
    if(OrganizationLevel.OPERATIONAL_LEVEL.equals(currentUser.getActiveOrganization().getOrganization().getLevel())) {
      report.getEventOrganizationList().stream().filter(eventOrg -> eventOrg.getOrganization().getId().equals(currentUser.getActiveOrganization().getOrganization().getId())).findFirst().ifPresent(eventOrg -> initForceLayout(eventOrg));
    }else {
      report.getEventOrganizationList().stream().forEach(eventOrg -> initForceLayout(eventOrg));
    }
  }


  /**
   * Inits the force form.
   *
   * @param eventOrganization the event organization
   */
  @SuppressWarnings("unchecked")
  private void initForceLayout(EventOrganization eventOrganization) {
    
    
    VVerticalLayout forceLayout = new VVerticalLayout();
    VH3 organizationName = new VH3(eventOrganization.getOrganization().getName());
    
    eventVechiles = new HashSet<EventOrganizationVechile>(reportService.getVechilesByEventOrganization(eventOrganization));
    removedEventVechiles = new HashSet<EventOrganizationVechile>();
    //Intervention times
    
    VVerticalLayout timesLayout = new VVerticalLayout();
    VH5 timesTitle = new VH5(getTranslation("reportView.reportForceTab.forceLayout.times.label"));
    timesLayout.add(timesTitle);
    
    CustomFormLayout<EventOrganization> timesCustomForm =  new CustomFormLayout<EventOrganization>(new Binder<>(EventOrganization.class), eventOrganization);
    VDateTimePicker alarmedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.alarmedDateTime")).withReadOnly(!editRight);
    timesCustomForm.processBinder(alarmedTime, null, null, true, "alarmedDateTime");
   
    VDateTimePicker baseDepartureTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.baseDepartureDateTime")).withReadOnly(true);

    VDateTimePicker fieldArrivedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.fieldArrivedDateTime")).withReadOnly(true);
  
    timesCustomForm.addThreeColumnItemsLayout(alarmedTime, baseDepartureTime, fieldArrivedTime);
    
    VDateTimePicker workFinishedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.workFinishedDateTime")).withReadOnly(!editRight);
    timesCustomForm.processBinder(workFinishedTime, null, null, false, "workFinishedDateTime");
    VDateTimePicker fieldDepartureTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.fieldDepartureDateTime")).withReadOnly(true);
   
    timesCustomForm.addThreeColumnItemsLayout(workFinishedTime, fieldDepartureTime, null);
    
    VDateTimePicker baseReturnDateTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.baseArrivedDateTime")).withReadOnly(true);
    eventVechiles.stream().map(EventOrganizationVechile::getBaseArrivedDateTime).filter(Objects::nonNull).max(Comparator.naturalOrder()).ifPresent(maxBaseArrived -> baseReturnDateTime.setValue(maxBaseArrived));
    VDateTimePicker interventionFinishedDateTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.interventionFinishedDateTime")).withReadOnly(!editRight);
    timesCustomForm.processBinder(interventionFinishedDateTime, null, null, false, "interventionFinishedDateTime");
    timesCustomForm.addThreeColumnItemsLayout(baseReturnDateTime, interventionFinishedDateTime, null);
   
    timesCustomForm.setBean();
    timesLayout.add(timesCustomForm);
    
    
    //Intervention vechiles
    VVerticalLayout vechilesLayout = new VVerticalLayout();
    VH5 vechileTitle = new VH5(getTranslation("reportView.reportForceTab.forceLayout.vechiles.title"));
    vechilesLayout.add(vechileTitle);
    
    VGrid<EventOrganizationVechile> eventVechilesGrid = new VGrid<EventOrganizationVechile>();
    VGrid<EventOrganizationPerson> eventFightersGrid = new VGrid<EventOrganizationPerson>();
    eventVechilesGrid.setHeightByRows(true);
    eventVechilesGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_NO_BORDER);
   
    VSelect<Vechile> vechilesSelect = new VSelect<Vechile>().withLabel(getTranslation("reportView.reportForceTab.forceLayout.vechiles.field.organizationVechiles")).withReadOnly(!editRight);
    vechilesSelect.setItemLabelGenerator(vechile -> vechile.getMake().concat(StringUtils.isNotBlank(vechile.getMake()) ? " ".concat(vechile.getModel()) : vechile.getVechileNumber()));  
    final List<Vechile> vechileSelectItems = new ArrayList<Vechile>(vechileService.getActiveByOrganization(eventOrganization.getOrganization().getId()));
    vechileSelectItems.removeIf(vechile -> eventVechiles.stream().map(EventOrganizationVechile::getVechile).map(Vechile::getId).anyMatch(eventVechile -> eventVechile.equals(vechile.getId())));
    vechilesSelect.setItems(vechileSelectItems);
    vechilesSelect.addValueChangeListener(vechileSelectedEvent -> {
      if(vechileSelectedEvent.getValue() == null) {
        return;
      }
      EventOrganizationVechile eventOrganizationVechile = new EventOrganizationVechile();
      eventOrganizationVechile.setEventOrganizationId(eventOrganization.getId());
      eventOrganizationVechile.setVechile(vechileSelectedEvent.getValue());      
      eventOrganizationVechile = reportService.saveEventOrganizationVechile(eventOrganizationVechile);   
      
      ((ListDataProvider<EventOrganizationVechile>)eventVechilesGrid.getDataProvider()).getItems().add(eventOrganizationVechile);
      eventVechilesGrid.getDataProvider().refreshAll();
      eventFightersGrid.getDataProvider().refreshAll();
      vechileSelectItems.remove(vechileSelectedEvent.getValue());
      vechilesSelect.setItems(vechileSelectItems);
      openEditEventOrganizationVechileDialog(eventOrganizationVechile, eventVechilesGrid, baseDepartureTime, fieldArrivedTime, fieldDepartureTime, baseReturnDateTime);
    });
    
    vechilesLayout.add(vechilesSelect);
    
    eventVechilesGrid.addColumn(eventVechile -> getTranslation(eventVechile.getVechile().getType().getLabelKey())).setHeader(getTranslation("vechilesView.vechilesGrid.type"));
    
    eventVechilesGrid.addColumn(eventVechile -> eventVechile.getVechile().getVechileNumber())
    .setHeader(getTranslation("vechilesView.vechilesGrid.vechileNumber"));
    
    eventVechilesGrid.addColumn(eventVechile -> eventVechile.getVechile().getMake().concat(StringUtils.isNotBlank(eventVechile.getVechile().getMake()) ? " ".concat(eventVechile.getVechile().getModel()) : ""))
    .setHeader(getTranslation("vechilesView.vechilesGrid.name"));
  

    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM).withLocale(getLocale());
    eventVechilesGrid.setDetailsVisibleOnClick(true);
    eventVechilesGrid.setItemDetailsRenderer(new ComponentRenderer<>(eventVechile ->  {
      return new VParagaph(getTranslation("reportView.reportForceTab.forceLayout.field.vechileGrid.summary", eventVechile.getBaseDepartureDateTime()==null ? "-" :eventVechile.getBaseDepartureDateTime().format(formatter)
        , eventVechile.getFieldArrivedDateTime()==null ? "-" :eventVechile.getFieldArrivedDateTime().format(formatter)
        , eventVechile.getFieldDepartureDateTime()==null ? "-" :eventVechile.getFieldDepartureDateTime().format(formatter)
        , eventVechile.getBaseArrivedDateTime()==null ? "-" :eventVechile.getBaseArrivedDateTime().format(formatter)
        , eventVechile.getDistance()
        , eventVechile.getPeopleNumber()
        ));
    }));
    
    
    eventVechilesGrid.addComponentColumn(eventVechile -> {
      Icon editButton = VaadinIcon.EDIT.create();
      editButton.addClickListener(deleteEvent ->{ 
        if(!editRight) {
          return;
         }
         openEditEventOrganizationVechileDialog(eventVechile, eventVechilesGrid, baseDepartureTime, fieldArrivedTime, fieldDepartureTime, baseReturnDateTime);
      });
      return editButton;
   });

    
    
    eventVechilesGrid.addComponentColumn(eventVechile -> {
      Icon delete = VaadinIcon.TRASH.create();
    
      delete.addClickListener(deleteEvent ->{ 
        if(!editRight) {
          return;
         }

         reportService.deleteEventOrganizationVechile(eventVechile);
         ((ListDataProvider<EventOrganizationVechile>)eventVechilesGrid.getDataProvider()).getItems().remove(eventVechile);
         eventVechilesGrid.getDataProvider().refreshAll();
         eventFightersGrid.getDataProvider().refreshAll();
         vechileSelectItems.add(eventVechile.getVechile());
         vechilesSelect.setItems(vechileSelectItems);
      });
      return delete;
   });
    
    eventVechilesGrid.setItems(eventVechiles);
    setOrganizationTimes(eventVechilesGrid, baseDepartureTime, fieldArrivedTime, fieldDepartureTime, baseReturnDateTime);
    
    vechilesLayout.add(eventVechilesGrid);
    
    
    //Persons layout
    
    
    VVerticalLayout personsLayout = new VVerticalLayout();
    VH5 personsTitle = new VH5(getTranslation("reportView.reportForceTab.forceLayout.firefighters.title"));
    personsLayout.add(personsTitle);
    
    eventPersons = new HashSet<EventOrganizationPerson>(reportService.getPersonsByEventOrganization(eventOrganization));
    removedEventPersons = new HashSet<EventOrganizationPerson>();
    
   
    eventFightersGrid.setHeightByRows(true);
    eventFightersGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_NO_BORDER);
   
    VSelect<Person> personsSelect = new VSelect<Person>().withLabel(getTranslation("reportView.reportForceTab.forceLayout.firefighters.field.organizationMembers")).withReadOnly(!editRight);
    personsSelect.setItemLabelGenerator(p -> p.getName() + " " + p.getLastname()); 
    final List<Person> personSelectItems = new ArrayList<Person>(organizationService.getOrganizationMembers(currentUser.getActiveOrganization().getOrganization()));
    personSelectItems.removeIf(person -> eventPersons.stream().map(EventOrganizationPerson::getPerson).map(Person::getId).anyMatch(eventPerson -> eventPerson.equals(person.getId())));
    personsSelect.setItems(personSelectItems);
    personsSelect.addValueChangeListener(personSelectedEvent -> {
      if(personSelectedEvent.getValue() == null) {
        return;
      }
      EventOrganizationPerson eventOrganizationPerson = new EventOrganizationPerson();
      eventOrganizationPerson.setEventOrganizationId(eventOrganization.getId());
      eventOrganizationPerson.setPerson(personSelectedEvent.getValue());   
     
      reportService.saveEventOrganizationPerson(eventOrganizationPerson);
      ((ListDataProvider<EventOrganizationPerson>)eventFightersGrid.getDataProvider()).getItems().add(eventOrganizationPerson);
      eventFightersGrid.getDataProvider().refreshAll();
        
      personSelectItems.remove(personSelectedEvent.getValue());
      personsSelect.setItems(personSelectItems);
    });
    
    personsLayout.add(personsSelect);
    
    eventFightersGrid.addColumn(eventPerson -> eventPerson.getPerson().getName() + " " +eventPerson.getPerson().getLastname()).setHeader(getTranslation("membersView.membersGrid.nameAndLastname"));
    
    eventFightersGrid.addComponentColumn(eventPerson -> {
      log.info("component co " + eventPerson.getPerson().getId()+ " " + eventPerson.getTimeInSeconds() + " " + eventPerson.getVechile());
      VSelect<Vechile> personVechileSelect = new VSelect<Vechile>().withReadOnly(!editRight);
      personVechileSelect.setItems(eventVechiles.stream().filter(eventVec -> ObjectUtils.allNotNull(eventVec.getBaseDepartureDateTime(), eventVec.getFieldArrivedDateTime(), eventVec.getFieldDepartureDateTime(), eventVec.getBaseArrivedDateTime())).map(EventOrganizationVechile::getVechile).collect(Collectors.toList()));
      personVechileSelect.setValue(eventPerson.getVechile());
      personVechileSelect.addValueChangeListener(vechileSelected ->{ 
        log.info("changed " + vechileSelected.getValue().getId());
        eventPerson.setVechile(vechileSelected.getValue());
        eventVechiles.stream().filter(eventVechile -> eventVechile.getVechile().getId().equals(eventPerson.getVechile().getId())).findFirst().ifPresent(eventVechile -> {
        log.info("stream nasao " + eventVechile.getVechile().getId());
        long durationSecond =  Math.abs(eventVechile.getBaseArrivedDateTime().toEpochSecond(ZoneOffset.UTC) - eventVechile.getBaseDepartureDateTime().toEpochSecond(ZoneOffset.UTC));
        eventPerson.setTimeInSeconds(durationSecond);
        log.info("duration " + durationSecond);
        eventFightersGrid.getDataProvider().refreshAll();
        });
      });
      return personVechileSelect;
    }).setHeader("reportView.reportForceTab.forceLayout.firefightersGrid.vechile");
    
    eventFightersGrid.addColumn(eventPerson -> {
     return generateDurationLabel(eventPerson.getTimeInSeconds());
    });
    
    eventFightersGrid.addComponentColumn(eventPerson -> {
      Icon delete = VaadinIcon.TRASH.create();
      delete.addClickListener(deleteEvent ->{ 
         if(!editRight) {
           return;
         }
         reportService.deleteEventOrganizationPerson(eventPerson);
         ((ListDataProvider<EventOrganizationPerson>)eventFightersGrid.getDataProvider()).getItems().remove(eventPerson);
         eventFightersGrid.getDataProvider().refreshAll();
         personSelectItems.add(eventPerson.getPerson());
         personsSelect.setItems(personSelectItems);
      });
      return delete;
   });
    
    eventFightersGrid.setItems(eventPersons);
   
    personsLayout.add(eventFightersGrid);
    
    forceLayout.add(organizationName, timesLayout, vechilesLayout,personsLayout);

    add(forceLayout);
  }
  
  /**
   * Generate duration label.
   *
   * @param seconds the seconds
   * @return the string
   */
  private String generateDurationLabel(long seconds) {
    if(seconds == 0) {
      return "-";
    }
    int hours = (int) seconds / 3600;
    int remainder = (int) seconds - hours * 3600;
    int mins = remainder / 60;
    
    return hours + ":"+mins;
  }


  /**
   * Open edit event organization vechile dialog.
   *
   * @param eventVechile the event vechile
   * @param vechilesGrid the vechiles grid
   * @param baseDepartureTime the base departure time
   * @param fieldArrivedTime the field arrived time
   * @param fieldDepartureTime the field departure time
   * @param baseReturnDateTime the base return date time
   */
  private void openEditEventOrganizationVechileDialog(EventOrganizationVechile eventVechile, VGrid<EventOrganizationVechile> vechilesGrid,  VDateTimePicker baseDepartureTime, VDateTimePicker fieldArrivedTime, VDateTimePicker fieldDepartureTime, VDateTimePicker baseReturnDateTime) {
    VDialog editEventVechileDialog = new VDialog();
    VVerticalLayout dialogLayout = new VVerticalLayout();
    
    CustomFormLayout<EventOrganizationVechile> eventVechileForm = new CustomFormLayout<EventOrganizationVechile>(new Binder<>(EventOrganizationVechile.class), eventVechile);
    VDateTimePicker baseDeparture = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.vechileGrid.vechile.baseDepartureDateTime"));
    eventVechileForm.processBinder(baseDeparture, null, null, true, "baseDepartureDateTime");
    VDateTimePicker fieldArrived = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.vechileGrid.vechile.fieldArrivedDateTime"));
    eventVechileForm.processBinder(fieldArrived, null, null, true, "fieldArrivedDateTime");
    eventVechileForm.addTwoColumnItemsLayout(baseDeparture, fieldArrived);
    VDateTimePicker fieldDeparture = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.vechileGrid.vechile.fieldDepartureDateTime"));
    eventVechileForm.processBinder(fieldDeparture, null, null, true, "fieldDepartureDateTime");
    VDateTimePicker baseArrived = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.vechileGrid.vechile.baseArrivedDateTime"));
    eventVechileForm.processBinder(baseArrived, null, null, true, "baseArrivedDateTime");
    eventVechileForm.addTwoColumnItemsLayout(fieldDeparture, baseArrived);
    
    VNumberField numberOfFirefighters = new VNumberField();
    eventVechileForm.setLabel(numberOfFirefighters, "reportView.reportForceTab.forceLayout.vechiles.vechilesGrid.numberOfFirefighters");
    eventVechileForm.processBinder(numberOfFirefighters, new DoubleToIntegerConverter(), null, true, "peopleNumber");
    VNumberField distance = new VNumberField();
    eventVechileForm.setLabel(distance, "reportView.reportForceTab.forceLayout.vechiles.vechilesGrid.distance");
    eventVechileForm.processBinder(distance, new DoubleToIntegerConverter(), null, true, "distance");
    eventVechileForm.addTwoColumnItemsLayout(distance, numberOfFirefighters);

    eventVechileForm.setBean();
    
    dialogLayout.add(eventVechileForm);
    
    VHorizontalLayout buttons = new VHorizontalLayout();
    @SuppressWarnings("unchecked")
    VButton save = new VButton(getTranslation("button.save")).withClickListener(saveEvent -> {
      if(eventVechileForm.validate()) {
       ((ListDataProvider<EventOrganizationVechile>)vechilesGrid.getDataProvider()).getItems().removeIf(eventV -> eventV.getVechile().getId().equals(eventVechile.getVechile().getId()));
       ((ListDataProvider<EventOrganizationVechile>)vechilesGrid.getDataProvider()).getItems().add(  reportService.saveEventOrganizationVechile(eventVechile));
        vechilesGrid.getDataProvider().refreshAll();
        setOrganizationTimes(vechilesGrid, baseDepartureTime, fieldArrivedTime, fieldDepartureTime, baseReturnDateTime);
        editEventVechileDialog.close();
      }
    });
    VButton close = new VButton(getTranslation("button.close")).withClickListener(saveEvent -> {
      editEventVechileDialog.close();
    });
    buttons.add(save, close);
    
    dialogLayout.add(buttons);
  
    editEventVechileDialog.add(dialogLayout);
    editEventVechileDialog.open();
    
  }
  
  /**
   * Sets the organization times.
   *
   * @param grid the grid
   * @param baseDepartureTime the base departure time
   * @param fieldArrivedTime the field arrived time
   * @param fieldDepartureTime the field departure time
   * @param baseReturnDateTime the base return date time
   */
  private void setOrganizationTimes(VGrid<EventOrganizationVechile> grid, VDateTimePicker baseDepartureTime, VDateTimePicker fieldArrivedTime, VDateTimePicker fieldDepartureTime, VDateTimePicker baseReturnDateTime) {
    List<EventOrganizationVechile> items =((ListDataProvider<EventOrganizationVechile>)grid.getDataProvider()).getItems().stream().collect(Collectors.toList());
    items.stream().map(EventOrganizationVechile::getBaseDepartureDateTime).filter(Objects::nonNull).min(Comparator.naturalOrder()).ifPresent(minBaseDeparture -> baseDepartureTime.setValue(minBaseDeparture));
  
    items.stream().map(EventOrganizationVechile::getFieldArrivedDateTime).filter(Objects::nonNull).min(Comparator.naturalOrder()).ifPresent(minFieldArrived -> fieldArrivedTime.setValue(minFieldArrived));
  
    items.stream().map(EventOrganizationVechile::getFieldDepartureDateTime).filter(Objects::nonNull).max(Comparator.naturalOrder()).ifPresent(maxFeildDeparture -> fieldDepartureTime.setValue(maxFeildDeparture));
    
    items.stream().map(EventOrganizationVechile::getBaseArrivedDateTime).filter(Objects::nonNull).max(Comparator.naturalOrder()).ifPresent(maxBaseArrived -> baseReturnDateTime.setValue(maxBaseArrived));
  }
}
