/*
 * MyTasksTab MyTasksTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.ListDataProvider;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.view.AbstractGridView;
import hr.tvz.vi.view.ReportView;

/**
 * The Class MyTasksTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:45:31 AM Aug 28, 2021
 */
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
          ((ListDataProvider<Task>)getGrid().getDataProvider()).getItems().remove(task);
          getGrid().getDataProvider().refreshAll();
        });
        return toGroup;
    });
    getGrid().addItemClickListener(e -> {
      UI.getCurrent().navigate(ReportView.class, e.getItem().getReportId().toString());
    });
    
  }

}
