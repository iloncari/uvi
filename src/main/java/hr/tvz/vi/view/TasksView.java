/*
 * TasksView TasksView.java.
 * 
 */
package hr.tvz.vi.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.VTabSheet;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.components.GroupTasksTab;
import hr.tvz.vi.components.MyTasksTab;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Constants.Routes;

/**
 * The Class TasksView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:41:26 AM Aug 28, 2021
 */
@Route(value = Routes.TASKS, layout = MainAppLayout.class)
public class TasksView extends VVerticalLayout implements HasDynamicTitle{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 810944106440458173L;
  
  /** The report service ref. */
  @Autowired
  private ServiceRef<ReportService> reportServiceRef;
  
  /** The organization service ref. */
  @Autowired
  private ServiceRef<OrganizationService> oganizationServiceRef;

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    VTabSheet tabs = new VTabSheet();
    tabs.addTab(getTranslation("tasksView.myTaskTab.label"), new MyTasksTab(reportServiceRef.get()));
    tabs.addTab(getTranslation("tasksView.groupTaskTab.label"), new GroupTasksTab(reportServiceRef.get(), oganizationServiceRef.get()));
    add(tabs);
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
  
  

}
