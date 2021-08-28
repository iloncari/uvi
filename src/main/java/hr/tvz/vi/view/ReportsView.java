/*
 * ReportsView ReportsView.java.
 * 
 */
package hr.tvz.vi.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.button.DeleteButton;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.VechileChangedChangedEvent;
import hr.tvz.vi.orm.EventOrganization;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.Routes;

/**
 * The Class ReportsView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 2:00:11 PM Aug 22, 2021
 */
@Route(value = Routes.REPORTS, layout = MainAppLayout.class)
public class ReportsView extends AbstractGridView<Report>{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1372381528372139683L;
	
	/** The delete button. */
	private VButton deleteButton;
	
	
	/** The report service ref. */
	@Autowired
	private ServiceRef<ReportService> reportServiceRef;

	/**
	 * Gets the grid items.
	 *
	 * @return the grid items
	 */
	@Override
	public List<Report> getGridItems() {
		if(OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel())) {
			return reportServiceRef.get().getReports(getCurrentUser().getActiveOrganization().getOrganization());
		}else {
			return reportServiceRef.get().getOwningReports(getCurrentUser().getActiveOrganization().getOrganization());
		}
	}

	/**
	 * On attach.
	 *
	 * @param attachEvent the attach event
	 */
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		deleteButton.setVisible(!OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel()));
	}

	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {
		return getTranslation(Routes.getPageTitleKey(Routes.REPORTS));
	}

	/**
	 * Gets the view title.
	 *
	 * @return the view title
	 */
	@Override
	protected String getViewTitle() {
		return getPageTitle();
	}

	/**
	 * Inits the bellow button layout.
	 *
	 * @return the v horizontal layout
	 */
	@SuppressWarnings("unchecked")
  @Override
	protected VHorizontalLayout initBellowButtonLayout() {
		final VHorizontalLayout buttonsLayout = new VHorizontalLayout();
	    //moze brisati samo kreator
	    deleteButton = new VButton(getTranslation("reportsView.button.delete.label")).withVisible(!OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel())).withEnabled(false);
	    deleteButton.addClickListener(e -> getGrid().getSelectedItems().forEach(repotForDelete -> {
	    	reportServiceRef.get().deleteReport(repotForDelete);
	    	((ListDataProvider<Report>)getGrid().getDataProvider()).getItems().remove(repotForDelete);	
	    	getGrid().getDataProvider().refreshAll();
	    	//event
	    }));
	    buttonsLayout.add(deleteButton);


	    return buttonsLayout;
	}

	/**
	 * Inits the grid.
	 */
	@Override
	protected void initGrid() {
		getGrid().removeAllColumns();
	    getGrid().setSelectionMode(SelectionMode.SINGLE);
	    getGrid().addSelectionListener(e ->  deleteButton.setEnabled(!e.getFirstSelectedItem().isEmpty()) );

	    getGrid().addComponentColumn(report -> new RouterLink(report.getIdentificationNumber(), ReportView.class, report.getId().toString()))
	      .setHeader(getTranslation("reportsView.reportsGrid.identificationNumber"));
	    getGrid().addColumn(report -> report.getEventDateTime()).setHeader(getTranslation("reportsView.reportsGrid.eventDateTime"));
	    getGrid().addColumn(report -> getTranslation(report.getEventType().getEventTypeTranslationKey())).setHeader(getTranslation("reportsView.reportsGrid.eventType"));
	    getGrid().addColumn(report -> getTranslation(report.getStatus().getReportStatusTranslationKey())).setHeader(getTranslation("reportsView.reportsGrid.status"));
	    
	    getGrid().addItemClickListener(e -> {
	    	if(e.getClickCount() > 1) {
	    		UI.getCurrent().navigate(ReportView.class, e.getItem().getId().toString());
	    	}
	    });
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



}
