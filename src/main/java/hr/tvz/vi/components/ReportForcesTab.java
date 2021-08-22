/*
 * ReportForcesTab ReportForcesTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.HashSet;
import java.util.Set;

import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.datetimepicker.VDateTimePicker;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.html.VH5;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.data.binder.Binder;

import hr.tvz.vi.orm.EventOrganization;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.util.Constants.EventType;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportForcesTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 9:56:06 PM Aug 22, 2021
 */
@Slf4j
public class ReportForcesTab extends VVerticalLayout{
  
  /** The report. */
  private Report report;
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -2451807794983342377L;
  
  /**
   * Instantiates a new report forces tab.
   *
   * @param report the report
   */
  public ReportForcesTab(Report report) {
    this.report = report;
    
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
    report.getEventOrganizationList().forEach(eventOrganization -> initForceLayout(eventOrganization));
  }


  /**
   * Inits the force form.
   *
   * @param eventOrganization the event organization
   */
  private void initForceLayout(EventOrganization eventOrganization) {
    VVerticalLayout forceLayout = new VVerticalLayout();
    
    VHorizontalLayout organizationLayout = new VHorizontalLayout();
    VH3 organizationName = new VH3(eventOrganization.getOrganization().getName());
    organizationLayout.add(organizationName);
    VButton removeButton = new VButton(getTranslation("reportView.reportForceTab.forceLayout.button.remove")).withClickListener(e -> {
      //neki update organizacija
      remove(forceLayout);
    });
    organizationLayout.add(removeButton);
    
    CustomFormLayout<EventOrganization> eventOrganizationFormLayout =  new CustomFormLayout<EventOrganization>(new Binder<>(EventOrganization.class), eventOrganization);
    VDateTimePicker alarmedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.alarmedDateTime"));
    eventOrganizationFormLayout.processBinder(alarmedTime, null, null, true, "alarmedDateTime");
    VDateTimePicker baseDepartureTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.baseDepartureDateTime"));
    eventOrganizationFormLayout.processBinder(baseDepartureTime, null, null, true, "baseDepartureDateTime");
    VDateTimePicker fieldArrivedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.fieldArrivedDateTime"));
    eventOrganizationFormLayout.processBinder(fieldArrivedTime, null, null, true, "fieldArrivedDateTime");
    eventOrganizationFormLayout.addThreeColumnItemsLayout(alarmedTime, baseDepartureTime, fieldArrivedTime);
    
    VDateTimePicker workFinishedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.workFinishedDateTime"));
    eventOrganizationFormLayout.processBinder(workFinishedTime, null, null, false, "workFinishedDateTime");
    VDateTimePicker baseReturnDateTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.baseReturnDateTime"));
    eventOrganizationFormLayout.processBinder(baseReturnDateTime, null, null, false, "baseReturnDateTime");
    VDateTimePicker interventionFinishedDateTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.interventionFinishedDateTime"));
    eventOrganizationFormLayout.processBinder(interventionFinishedDateTime, null, null, false, "interventionFinishedDateTime");
    
    log.info("is fire " +report.getEventType() + " " + report.getEventType().isInterventionFire());
    if(report.getEventType() != null && report.getEventType().isInterventionFire()) {
      VDateTimePicker fireUnderSurveillanceTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.fireUnderSurveillanceTime"));
      eventOrganizationFormLayout.processBinder(fireUnderSurveillanceTime, null, null, false, "fireUnderSurveillanceDateTime");
      VDateTimePicker fireLocalizedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.fireLocalizedDateTime"));
      eventOrganizationFormLayout.processBinder(fireLocalizedTime, null, null, false, "fireExtinguishedDateTime");
      VDateTimePicker fireExtinguishedTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.fireExtinguishedDateTime"));
      eventOrganizationFormLayout.processBinder(fireExtinguishedTime, null, null, false, "fieldArrivedDateTime");
      eventOrganizationFormLayout.addThreeColumnItemsLayout(fireUnderSurveillanceTime, fireLocalizedTime,fireExtinguishedTime);
   
      VDateTimePicker insuranceStartDateTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.insuranceStartDateTime"));
      eventOrganizationFormLayout.processBinder(insuranceStartDateTime, null, null, false, "insuranceStartDateTime");
      VDateTimePicker insuranceEndDateTime = new VDateTimePicker(getTranslation("reportView.reportForceTab.forceLayout.field.insuranceEndDateTime"));
      eventOrganizationFormLayout.processBinder(insuranceEndDateTime, null, null, false, "insuranceEndDateTime");
      eventOrganizationFormLayout.addThreeColumnItemsLayout(workFinishedTime, insuranceStartDateTime, insuranceEndDateTime);
      eventOrganizationFormLayout.addThreeColumnItemsLayout(baseReturnDateTime, interventionFinishedDateTime, null);
    }else {
      eventOrganizationFormLayout.addThreeColumnItemsLayout(workFinishedTime, baseReturnDateTime, interventionFinishedDateTime);
    }
   
    forceLayout.add(organizationLayout, eventOrganizationFormLayout);
    eventOrganizationFormLayout.setBean();
    add(forceLayout);
  }

 

}
