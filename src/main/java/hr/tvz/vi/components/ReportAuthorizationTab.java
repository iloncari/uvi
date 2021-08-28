/*
 * ReportAuthorizationTab ReportAuthorizationTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.dialog.VDialog;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.textfield.VTextArea;
import org.vaadin.firitin.layouts.VTabSheet;

import com.sun.source.util.TaskListener;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.orm.EventDescription;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.view.ReportsView;
import hr.tvz.vi.util.Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportAuthorizationTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 7:54:47 PM Aug 26, 2021
 */
@Slf4j
public class ReportAuthorizationTab extends VVerticalLayout{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8252589639504891782L;
  
  /** The report. */
  private Report report;
  
  /** The binder. */
  private Binder<Report> binder;
  
  /** The organization service. */
  private OrganizationService organizationService;
  
  /** The report service. */
  private ReportService reportService;
  
  /** The current user. */
  private CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
  
  /** The all descriptions field. */
  private VTextArea allDescriptionsField;
  
  /** The add description. */
  private VButton addDescription = new VButton();
  
  /** The tab component map. */
  private  Map<Component, Integer> tabComponentMap;
  
  private  boolean descEditRight;
  
  private boolean taskExecutionRight;
  
  VTabSheet tabs;
  
  /**
   * Instantiates a new report authorization tab.
   *
   * @param report the report
   * @param binder the binder
   * @param organizationService the organization service
   * @param reportService the report service
   */
  public ReportAuthorizationTab(Report report, Binder<Report> binder, boolean descEditRight, boolean taskExecutionRight, VTabSheet tabs, Map<Component, Integer> tabComponentMap, OrganizationService organizationService, ReportService reportService) {
    this.report = report;
    this.organizationService = organizationService;
    this.binder = binder;
    this.reportService = reportService;
    this.tabComponentMap = tabComponentMap;
    this.tabs = tabs;
    this.descEditRight = descEditRight;
    this.taskExecutionRight = taskExecutionRight;
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
    initDesctiprionForm();
    initAuthorizationGrid();
  }



  /**
   * Inits the desctiprion form.
   */
  private void initDesctiprionForm() {
    VVerticalLayout descriptionsLayout = new VVerticalLayout();
    allDescriptionsField = new VTextArea().withLabel(getTranslation("reportView.reportAuthorizationTab.description")).withReadOnly(true).withSizeFull();
   
    List<EventDescription> reportDescriptions = reportService.getReportDescriptions(report);
    refreshAllDescriptions(reportDescriptions);
      addDescription = new VButton(getTranslation("reportView.reportAuthorizationTab.button.addDescription")).withClickListener(e -> {
      VDialog addDescDialog = new VDialog();
      VVerticalLayout dialogLayout = new VVerticalLayout();
      VTextArea desc = new VTextArea(getTranslation("reportView.reportAuthorizationTab.description")).withSizeFull();
      Optional<EventDescription> oldAuth = reportDescriptions.stream().filter(auth -> auth.getPerson().getId().equals(currentUser.getPerson().getId())).findFirst();
      if(oldAuth.isPresent()) {
        desc.setValue(oldAuth.get().getDescription());
      }
      VButton addMyDesc = new VButton(getTranslation("reportView.reportAuthorizationTab.button.addDescription")).withClickListener(addMyDescEvent -> {
        
        if(StringUtils.isBlank(desc.getValue())) {
          desc.setInvalid(true);
          return;
        }
        
        if(oldAuth.isPresent()) {
          oldAuth.get().setDescription(desc.getValue());
          reportService.saveEventDesctiption(oldAuth.get());
          reportDescriptions.remove(oldAuth.get());
          reportDescriptions.add(oldAuth.get());
        }else {
          EventDescription newAuth = new EventDescription();
          newAuth.setDescription(desc.getValue());
          newAuth.setOrganization(currentUser.getActiveOrganization().getOrganization());
          newAuth.setPerson(currentUser.getPerson());
          newAuth.setReportId(report.getId());
          reportService.saveEventDesctiption(newAuth);
          reportDescriptions.add(newAuth);
        }
        refreshAllDescriptions(reportDescriptions);
        
        addDescDialog.close();
      });
      VButton cancel = new VButton(getTranslation("button.cancel")).withClickListener(cancelEvent -> addDescDialog.close());
      VHorizontalLayout buttons = new VHorizontalLayout(addMyDesc, cancel);
      dialogLayout.add(desc, buttons);
      addDescDialog.add(dialogLayout);
      addDescDialog.open();
    });
      
    addDescription.setEnabled(descEditRight);
    

    
    descriptionsLayout.add(allDescriptionsField, addDescription);
  
    add(descriptionsLayout);
  }

  
  /**
   * Refresh all descriptions.
   *
   * @param items the items
   */
  private void refreshAllDescriptions(List<EventDescription> items) {
    StringBuilder descriptionsBuilder = new StringBuilder();
    items.forEach(auth -> {
      descriptionsBuilder.append("\n").append(auth.getPerson().getName() + " " + auth.getPerson().getLastname());
      descriptionsBuilder.append("\n").append(auth.getOrganization().getName());
      descriptionsBuilder.append("\n").append(auth.getDescription()).append("\n");
    });
    allDescriptionsField.setValue(descriptionsBuilder.toString());
  }
  
  /**
   * Inits the authorization grid.
   */
  private void  initAuthorizationGrid() {
    VVerticalLayout authorizationLayout = new VVerticalLayout();
    List<Task> tasksList = reportService.getReportTasks(report.getId());
    
    VGrid<Task> authorizationTasksGrid = new VGrid<Task>();
    authorizationTasksGrid.addColumn(task -> task.getOrganizationAssignee().getName()).setHeader(getTranslation("reportView.reportAuthorizationTab.grid.organization"));
    authorizationTasksGrid.addColumn(task -> { 
      if(task.getAssignee()==null) {
        return task.getType().equals(TaskType.PREPARATION_TASK) ? getTranslation(GroupType.PREPARERS.getGroupTypeLocalizationKey()) : getTranslation(GroupType.APPROVERS.getGroupTypeLocalizationKey());
      }
      return task.getAssignee().getName() +  " " + task.getAssignee().getLastname();
    }).setHeader(getTranslation("reportView.reportAuthorizationTab.grid.assignee"));
    
    authorizationTasksGrid.addComponentColumn(task -> {
      if(task.getExecutionDateTime()!=null) {
        return VaadinIcon.CHECK.create();
      }else if(task.getAssignee() != null && task.getAssignee().getId().equals(currentUser.getPerson().getId())) {
        VHorizontalLayout buttons = new VHorizontalLayout();
        VButton authorize = new VButton(getTranslation("reportView.reportAuthorizationTab.button.authorize")).withClickListener(authEvent -> {
          task.setExecutionDateTime(LocalDateTime.now());
          reportService.saveReportTask(task);
          tasksList.removeIf(t -> t.getId().equals(task.getId()));
          tasksList.add(task);
          authorizationTasksGrid.getDataProvider().refreshAll();
          
          if(!tasksList.stream().anyMatch(t -> t.getExecutionDateTime()==null) && OrganizationLevel.OPERATIONAL_LEVEL.equals(currentUser.getActiveOrganization().getOrganization().getLevel())) {
            Task approveTask = new Task();
            approveTask.setCreationDateTime(LocalDateTime.now());
            approveTask.setName(getTranslation("task.approve.label", report.getIdentificationNumber()));
            approveTask.setOrganizationAssignee(currentUser.getParentOrganization());
            approveTask.setReportId(report.getId());
            approveTask.setType(TaskType.APPROVE_TASK);
            reportService.saveReportTask(approveTask);
            
            report.setStatus(ReportStatus.PREPARED);
            reportService.updateReport(report);
          }
          
          if(!OrganizationLevel.OPERATIONAL_LEVEL.equals(currentUser.getActiveOrganization().getOrganization().getLevel())) {
            report.setStatus(ReportStatus.APPROVED);
            reportService.updateReport(report);
          }
          UI.getCurrent().getPage().reload();
        });
        authorize.setEnabled(taskExecutionRight);
        buttons.add(authorize);
        if(task.getType().equals(TaskType.APPROVE_TASK)) {
          VButton reject = new VButton(getTranslation("reportView.reportAuthorizationTab.button.reject")).withClickListener(authEvent -> {
            task.setExecutionDateTime(LocalDateTime.now());
            reportService.saveReportTask(task);
            tasksList.removeIf(t -> t.getId().equals(task.getId()));
            tasksList.add(task);
            authorizationTasksGrid.getDataProvider().refreshAll();
            currentUser.getActiveOrganization().getOrganization().getChilds().stream().filter(org -> report.getEventOrganizationList().stream().anyMatch(eo -> eo.getOrganization().getId().equals(org.getId()))).forEach(child -> {
              Task preparationTask = new Task();
              preparationTask.setCreationDateTime(LocalDateTime.now());
              preparationTask.setName(getTranslation("task.preparation.label", report.getIdentificationNumber()));
              preparationTask.setOrganizationAssignee(child);
              preparationTask.setReportId(report.getId());
              preparationTask.setType(TaskType.PREPARATION_TASK);
              reportService.saveReportTask(preparationTask);
            });
            
            report.setStatus(ReportStatus.NEW);
            reportService.updateReport(report);
            UI.getCurrent().getPage().reload();
             
          });
          reject.setEnabled(taskExecutionRight);
          buttons.add(reject);
        }
        return buttons;
      }
      return new VSpan();
    });
    authorizationTasksGrid.setItems(tasksList);
 
    
    authorizationLayout.add(authorizationTasksGrid);
    add(authorizationLayout);
  }
  
  

}
