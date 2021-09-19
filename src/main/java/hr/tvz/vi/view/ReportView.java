/*
 * ReportView ReportView.java.
 * 
 */
package hr.tvz.vi.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.vaadin.firitin.components.html.VH4;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.VTabSheet;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.FireEventTypeDataTab;
import hr.tvz.vi.components.ReportAuthorizationTab;
import hr.tvz.vi.components.ReportBasicDataTab;
import hr.tvz.vi.components.ReportForcesTab;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.EventTypeChangedObserver;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.NotificationService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.service.VechileService;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;

@Route(value = Routes.REPORT, layout = MainAppLayout.class)
@EventSubscriber(scope = SubscriberScope.PUSH)
public class ReportView extends VVerticalLayout implements HasDynamicTitle, HasUrlParameter<String>, BeforeLeaveObserver, EventTypeChangedObserver{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1544518913913978114L;
	
	/** The report. */
	private Report report;
	
	/** The fire event tab. */
	private Tab fireEventTab;
	
	
	/** The report service ref. */
	@Autowired
	private ServiceRef<ReportService> reportServiceRef;
	
	/** The address service ref. */
	@Autowired
  private ServiceRef<AddressService> addressServiceRef;
	
	/** The organization service ref. */
	@Autowired
  private ServiceRef<OrganizationService> organizationServiceRef;
	
	/** The vechile service ref. */
	@Autowired
  private ServiceRef<VechileService> vechileServiceRef;
	
	/** The notification service ref. */
	@Autowired
  private ServiceRef<NotificationService> notificationServiceRef;
	
	/** The fire event type component. */
	private FireEventTypeDataTab fireEventTypeComponent;
	
	/** The is organization creator. */
	private boolean organizationIsCreator;
	
	/** The user need to prepare. */
	private boolean userNeedToPrepare;
	
	/** The user need to approve. */
	private boolean userNeedToApprove;
	

	/**
   * Sets the parameter.
   *
   * @param event     the event
   * @param reportId the report id
   */
	@Override
	public void setParameter(BeforeEvent event, String reportId) {
		if (StringUtils.isBlank(reportId) || !NumberUtils.isParsable(reportId)) {
		      // navigate to NavigationErrorPage
	        throw new NotFoundException();
		}
		CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
		this.report = reportServiceRef.get().getById(Long.valueOf(reportId)).orElse(null);
		
		if (report == null) {
		  throw new NotFoundException();
		}
	  boolean organizationIsPartOfEvent = report.getEventOrganizationList().stream().anyMatch(eventOrg -> eventOrg.getOrganization().getId().equals(currentUser.getActiveOrganization().getOrganization().getId()));
	  
	  organizationIsCreator = currentUser.getActiveOrganization().getOrganization().getId().toString().equals(report.getCreatorId());
	  //List<Task> assignedReportTasks = reportServiceRef.get().getReportTasks(report.getId());
	  List<Task> assignedReportTasks = reportServiceRef.get().getAllAssignedTasks(currentUser.getActiveOrganization().getOrganization().getId(), currentUser.getPerson().getId());
	   boolean organizationIsPartOfTask = assignedReportTasks.stream().anyMatch(task -> task.getOrganizationAssignee().getId().equals(currentUser.getActiveOrganization().getOrganization().getId()));
	   userNeedToPrepare = assignedReportTasks.stream().anyMatch(task ->task.getExecutionDateTime()==null  && task.getReportId().equals(report.getId()) && task.getType().equals(TaskType.PREPARATION_TASK));
	   userNeedToApprove = assignedReportTasks.stream().anyMatch(task ->task.getExecutionDateTime()==null && task.getReportId().equals(report.getId()) && task.getType().equals(TaskType.APPROVE_TASK));
	   List<Long> preparerIds = organizationServiceRef.get().getOrganizationGroupMembers(GroupType.PREPARERS, currentUser.getActiveOrganizationObject().getId())
	     .stream().map(gm -> gm.getPerson().getId()).collect(Collectors.toList());
	   boolean userCanPrepare = preparerIds.contains(currentUser.getPerson().getId());
	  if(!organizationIsPartOfTask && !organizationIsPartOfEvent && !organizationIsCreator && !userNeedToApprove && !userNeedToPrepare) {
	    throw new AccessDeniedException("Access Denied");
	  }
	 
   if((organizationIsCreator || userNeedToPrepare || userNeedToApprove) && !report.isLocked() ) {
	    report.setLocked(true);
	    report.setLockOwner(currentUser.getPerson());
	    reportServiceRef.get().updateReport(report);
	 }
	 
	 
	  
	}

	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {	
		return getTranslation(Routes.getPageTitleKey(Routes.REPORT));
	}
	
	
  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    ChangeBroadcaster.registerToPushEvents(this);
    add(new VH4(getPageTitle()));
    add(initLockInformationLayout());
    add(initTabLayout());
  }
  

  /**
   * Inits the lock information layout.
   *
   * @return the v horizontal layout
   */
  private VHorizontalLayout initLockInformationLayout() {
    VHorizontalLayout layout = new VHorizontalLayout();
    if(report.isLocked()) {
      Utils.removeAllThemes(layout);
      layout.getThemeList().add(ThemeAttribute.LOCKED);
      layout.add(getTranslation("reportView.reportLocked.label", report.getLockOwner().getName()  +  " " +report.getLockOwner().getLastname()));
    }
    return layout;
  }

  /**
   * Inits the tab layout.
   *
   * @return the v tab sheet
   */
  private VTabSheet initTabLayout() {
    VTabSheet tabs = new VTabSheet();
    Map<Component, Integer> tabComponentMap = new HashMap<Component, Integer>();
    Binder<Report> binder = new Binder<>(Report.class);
    fireEventTypeComponent = new FireEventTypeDataTab(report,userNeedToPrepare, binder, tabComponentMap);
    boolean basicEditRight = !report.getStatus().equals(ReportStatus.APPROVED) && (organizationIsCreator  || userNeedToPrepare);
    tabs.addTab(getTranslation("reportView.tab.basicData.label"), new ReportBasicDataTab(report, binder, basicEditRight, tabComponentMap, addressServiceRef.get(), this));
    tabs.addTab(getTranslation("reportView.tab.forcesData.label"), new ReportForcesTab(report, userNeedToPrepare, organizationServiceRef.get(), vechileServiceRef.get(), reportServiceRef.get()));
    fireEventTab = tabs.addTab(getTranslation("reportView.tab.fireEventData.label"), fireEventTypeComponent);
    tabs.addTab(getTranslation("reportView.tab.authorizationData.label"), new ReportAuthorizationTab(report, binder, userNeedToPrepare, (userNeedToPrepare || userNeedToApprove), tabs, tabComponentMap, organizationServiceRef.get(), reportServiceRef.get(), notificationServiceRef.get()));
    return tabs;
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
   * Before leave.
   *
   * @param event the event
   */
  @Override
  public void beforeLeave(BeforeLeaveEvent event) {
    report.setLocked(false);
    report.setLockOwner(null);
    reportServiceRef.get().updateReport(report);
  }

  /**
   * Event type changed.
   *
   * @param eventType the event type
   */
  @Override
  public void eventTypeChanged(EventType eventType) {
    if(eventType == null) {
      return;
    }
    report.setEventType(eventType);
    fireEventTab.setVisible(eventType.isInterventionFire());
    fireEventTypeComponent.initFireEventDataForm();
  }

}
