/*
 * GroupTasksTab GroupTasksTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.ListDataProvider;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.GroupChangeEvent;
import hr.tvz.vi.event.TaskChangeEvent;
import hr.tvz.vi.orm.GroupMember;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.view.AbstractGridView;
import hr.tvz.vi.view.ReportView;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class GroupTasksTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 1:11:00 AM Aug 28, 2021
 */
@Slf4j
public class GroupTasksTab extends AbstractGridView<Task>{
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -8807151173294870226L;

  /** The report service. */
  private ReportService reportService;

  /** The organization service. */
  private OrganizationService organizationService;
  
  /**
   * Instantiates a new g tasks tab.
   *
   * @param reportService the report service
   * @param organizationService the organization service
   */
  public GroupTasksTab(ReportService reportService, OrganizationService organizationService) {
   this.reportService = reportService;
   this.organizationService = organizationService;
  }

  /**
   * Gets the grid items.
   *
   * @return the grid items
   */
  @Override
  public List<Task> getGridItems() {
    return reportService.getGroupTasks(getCurrentUser().getActiveOrganization().getOrganization().getId());
  }

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public String getPageTitle() {
    return getTranslation(Routes.getPageTitleKey(Routes.TASKS));
  }

  /**
   * Gets the view title.
   *
   * @return the view title
   */
  @Override
  protected String getViewTitle() {
    return StringUtils.EMPTY;
  }

  /**
   * Inits the bellow button layout.
   *
   * @return the v horizontal layout
   */
  @Override
  protected VHorizontalLayout initBellowButtonLayout() {
    return null;
  }

  /**
   * Inits the above layout.
   *
   * @return the v horizontal layout
   */
  @Override
  protected VHorizontalLayout initAboveLayout() {
    return null;
  }
  
  /**
   * Group changed.
   *
   * @param event the event
   */
  @Subscribe
  public void groupChanged(GroupChangeEvent event) {
    if(getCurrentUser().getActiveOrganizationObject().getId().equals(event.getGroupMember().getOrganizationId())
        && getCurrentUser().getPerson().getId().equals(event.getGroupMember().getPerson().getId())) {
      getUI().ifPresent(ui -> ui.access(() -> {
        initGrid();
        getGrid().getDataProvider().refreshAll();
      }));
    }
  }
  
  /**
   * Task changed.
   *
   * @param event the event
   */
  @SuppressWarnings("unchecked")
  @Subscribe
  public void taskChanged(TaskChangeEvent event) {
    if(getCurrentUser().getActiveOrganizationObject().getId().equals(event.getTask().getOrganizationAssignee().getId())) {
      getUI().ifPresent(ui -> ui.access(() -> {
        if(EventAction.ADDED.equals(event.getAction())) {
          //new report & task
          ((ListDataProvider<Task>)getGrid().getDataProvider()).getItems().add(event.getTask());
        }else if(EventAction.MODIFIED.equals(event.getAction())) {
          //assigne changed
         if(event.getTask().getAssignee()==null) {
           ((ListDataProvider<Task>)getGrid().getDataProvider()).getItems().add(event.getTask());
         }else {
           ((ListDataProvider<Task>)getGrid().getDataProvider()).getItems().removeIf(task -> task.getId().equals(event.getTask().getId()));
         }
        }
        getGrid().getDataProvider().refreshAll();
      }));
    }
  }

  /**
   * Inits the grid.
   */
  @Override
  protected void initGrid() {
    List<Long> preparers = organizationService.getOrganizationGroupMembers(GroupType.PREPARERS, getCurrentUser().getActiveOrganization().getOrganization().getId()).stream().map(groupMember -> groupMember.getPerson().getId()).collect(Collectors.toList());
    List<Long> approvers =  organizationService.getOrganizationGroupMembers(GroupType.APPROVERS, getCurrentUser().getActiveOrganization().getOrganization().getId()).stream().map(groupMember -> groupMember.getPerson().getId()).collect(Collectors.toList());
    getGrid().removeAllColumns();
    getGrid().addColumn(task -> task.getCreationDateTime()).setHeader(getTranslation("myTasksTab.grid.creationDate"));
    getGrid().addColumn(task -> task.getName()).setHeader(getTranslation("myTasksTab.grid.name"));
    getGrid().addColumn(task -> {
      return TaskType.PREPARATION_TASK.equals(task.getType()) ? getTranslation(GroupType.PREPARERS.getGroupTypeLocalizationKey())  : getTranslation(GroupType.APPROVERS.getGroupTypeLocalizationKey());
    }).setHeader(getTranslation("myTasksTab.grid.executorsGroup"));
    getGrid().addComponentColumn(task -> {
      if( (TaskType.PREPARATION_TASK.equals(task.getType()) && preparers.stream().anyMatch(prep -> prep.equals(getCurrentUser().getPerson().getId()))) 
        || (TaskType.APPROVE_TASK.equals(task.getType()) && approvers.stream().anyMatch(app -> app.equals(getCurrentUser().getPerson().getId())))   ) {
        @SuppressWarnings("unchecked")
        VButton assign = new VButton(getTranslation("groupTasksTab.grid.button.assign")).withClickListener(e -> {
          task.setAssignee(getCurrentUser().getPerson());
          reportService.saveReportTask(task);
          ChangeBroadcaster.firePushEvent(new TaskChangeEvent(this, task, EventAction.MODIFIED));
        });
        return assign;
      }
      return new VSpan();
      
    });
    getGrid().addItemClickListener(e -> {
      UI.getCurrent().navigate(ReportView.class, e.getItem().getReportId().toString());
    });
    
  }

}
