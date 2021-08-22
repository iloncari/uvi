/*
 * ReportView ReportView.java.
 * 
 */
package hr.tvz.vi.view;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VH4;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.VTabSheet;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.ReportBasicDataTab;
import hr.tvz.vi.components.ReportForcesTab;
import hr.tvz.vi.components.VechileForm;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.service.VechileService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.SubscriberScope;
import jdk.internal.jline.internal.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value = Routes.REPORT, layout = MainAppLayout.class)
@EventSubscriber(scope = SubscriberScope.PUSH)
public class ReportView extends VVerticalLayout implements HasDynamicTitle, HasUrlParameter<String>, BeforeLeaveObserver{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1544518913913978114L;
	
	/** The report. */
	private Report report;
	
	/** The current user. */
	private final CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
	
	/** The report service ref. */
	@Autowired
	private ServiceRef<ReportService> reportServiceRef;
	
	/** The address service ref. */
	@Autowired
  private ServiceRef<AddressService> addressServiceRef;
	
	private Binder<Report> binder = new Binder<>(Report.class);

	/**
	 * Sets the parameter.
	 *
	 * @param event     the event
	 * @param parameter the parameter
	 */
	@Override
	public void setParameter(BeforeEvent event, String reportId) {
		if (StringUtils.isBlank(reportId) || !NumberUtils.isParsable(reportId)) {
		      // navigate to NavigationErrorPage
	        throw new NotFoundException();
		}
		CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
		this.report = reportServiceRef.get().getById(Long.valueOf(reportId)).orElse(null);
		if (report == null || !report.getEventOrganizationList().stream().anyMatch(eventOrg -> eventOrg.getOrganization().getId().equals(currentUser.getActiveOrganization().getOrganization().getId()))) {
		    throw new AccessDeniedException("Access Denied");
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
    add(initTabLayout());
  }

  /**
   * Inits the tab layout.
   *
   * @return the v tab sheet
   */
  private VTabSheet initTabLayout() {
    VTabSheet tabs = new VTabSheet();
   tabs.addTab(getTranslation("reportView.tab.basicData.label"), new ReportBasicDataTab(report, addressServiceRef.get()));
    Tab forcesTab = tabs.addTab(getTranslation("reportView.tab.forcesData.label"), new ReportForcesTab(report));
    
   

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
    reportServiceRef.get().updateReport(report);
  }

}
