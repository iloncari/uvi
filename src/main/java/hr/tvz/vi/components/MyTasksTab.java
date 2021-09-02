/*
 * MyTasksTab MyTasksTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.GroupChangeEvent;
import hr.tvz.vi.event.TaskChangeEvent;
import hr.tvz.vi.orm.GroupMember;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.view.AbstractGridView;
import hr.tvz.vi.view.ReportView;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class MyTasksTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:45:31 AM Aug 28, 2021
 */
@Slf4j
public class MyTasksTab extends AbstractGridView<Task>{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /** The report service. */
  private ReportService reportService;
  
  
  /**
   * Instantiates a new my tasks tab.
   *
   * @param reportService the report service
   */
  public  MyTasksTab(ReportService reportService) {
   super(false, false);
   this.reportService = reportService;
  }
  
  /**
   * Gets the grid items.
   *
   * @return the grid items
   */
  @Override
  public List<Task> getGridItems() {
    return reportService.getActiveAssignedTasks(getCurrentUser().getActiveOrganization().getOrganization().getId(), getCurrentUser().getPerson().getId());
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
   * Group changed.
   *
   * @param event the event
   */
  @SuppressWarnings("unchecked")
  @Subscribe
  public void groupChanged(GroupChangeEvent event) {
    if(getCurrentUser().getActiveOrganizationObject().getId().equals(event.getGroupMember().getOrganizationId())
      && getCurrentUser().getPerson().getId().equals(event.getGroupMember().getPerson().getId())) {
      getUI().ifPresent(ui -> ui.access(() -> {
        if(EventAction.REMOVED.equals(event.getAction())) {
           ((ListDataProvider<Task>)getGrid().getDataProvider()).getItems().forEach(task -> {
             task.setAssignee(null);
             reportService.saveReportTask(task);
           });
           getGrid().setItems(getGridItems());
           getGrid().getDataProvider().refreshAll();
        }
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
       if(EventAction.MODIFIED.equals(event.getAction())) {
          //assigne changed
         if(event.getTask().getAssignee()==null) {
           ((ListDataProvider<Task>)getGrid().getDataProvider()).getItems().remove(event.getTask());
           getGrid().getDataProvider().refreshAll();
         }
        }
      }));
    }
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
   * Inits the grid.
   */
  @Override
  protected void initGrid() {
    getGrid().removeAllColumns();
    getGrid().addColumn(task -> task.getCreationDateTime()).setHeader(getTranslation("myTasksTab.grid.creationDate"));
    getGrid().addColumn(task -> task.getName()).setHeader(getTranslation("myTasksTab.grid.name"));
    getGrid().addComponentColumn(task -> {
        @SuppressWarnings("unchecked")
        VButton toGroup = new VButton(getTranslation("myTasksTab.grid.button.returnToGrup")).withClickListener(e -> {
          task.setAssignee(null);
          reportService.saveReportTask(task);
          ChangeBroadcaster.firePushEvent(new TaskChangeEvent(this, task, EventAction.MODIFIED));
        });
        return toGroup;
    });
    getGrid().addItemClickListener(e -> {
      UI.getCurrent().navigate(ReportView.class, e.getItem().getReportId().toString());
    });
    
  }

  /**
   * Gets the route.
   *
   * @return the route
   */
  @Override
  public String getRoute() {
    return Routes.TASKS;
  }

}
