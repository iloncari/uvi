/*
 * ReportEventView ReportEventView.java.
 * 
 */
package hr.tvz.vi.view;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.datetimepicker.VDateTimePicker;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VTextArea;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.gatanaso.MultiselectComboBox;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.CustomFormLayout;
import hr.tvz.vi.components.GroupTasksTab;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.NotificationEvent;
import hr.tvz.vi.event.TaskChangeEvent;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.EventOrganization;
import hr.tvz.vi.orm.Notification;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.NotificationService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.NotificationType;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportEventView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 4:05:32 PM Aug 20, 2021
 */
@Slf4j
@Route(value = Routes.REPORT_EVENT, layout = MainAppLayout.class)
public class ReportEventView extends VVerticalLayout implements HasDynamicTitle{

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1437177004196617167L;
	
	/** The report event layout. */
	private CustomFormLayout<Report> reportEventLayout;
	
	/** The event report. */
	private Report eventReport = new Report();
	
	/** The county. */
	private VSelect<County> county;
	
	/** The event organizations. */
	private MultiselectComboBox<Organization> eventOrganizations;
	
	/** The current user. */
	private final CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());

	/** The address service ref. */
	@Autowired
	private ServiceRef<AddressService> addressServiceRef;
	
	/** The organization service ref. */
	@Autowired
	private ServiceRef<OrganizationService> organizationServiceRef;
	
	/** The report service ref. */
	@Autowired
	private ServiceRef<ReportService> reportServiceRef;
	
	/** The notification service ref. */
	@Autowired
	private ServiceRef<NotificationService> notificationServiceRef;
	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {
		return getTranslation(Routes.getPageTitleKey(Routes.REPORT_EVENT));
	}
	
	/**
	 * Instantiates a new report event view.
	 */
	public ReportEventView() {
		
		reportEventLayout = new CustomFormLayout<Report>(new Binder<>(Report.class), eventReport);
		reportEventLayout.setFormTitle(Routes.getPageTitleKey(Routes.REPORT_EVENT));
		
		VDateTimePicker eventDateTime = new VDateTimePicker(LocalDateTime.now());
		reportEventLayout.setLabel(eventDateTime, "reportEventView.form.eventDateTime");
		reportEventLayout.processBinder(eventDateTime, null, null, true, "eventDateTime");
		VSelect<EventType> eventType = new VSelect<EventType>();
		eventType.setItems(Arrays.asList(EventType.values()));
		eventType.setItemLabelGenerator(type -> getTranslation(type.getEventTypeTranslationKey()));
		reportEventLayout.setLabel(eventType, "reportEventView.form.eventType");
		reportEventLayout.processBinder(eventType, null, null, true, "eventType");
		reportEventLayout.addTwoColumnItemsLayout(eventDateTime, eventType);
		
		county = new VSelect<County>();
		county.setItemLabelGenerator(c -> c.getName());
		reportEventLayout.setLabel(county, "reportEventView.form.county");
	    VSelect<City> city = new VSelect<City>();
		city.setItemLabelGenerator(c -> c.getName());
		county.addValueChangeListener(e -> city.setItems(addressServiceRef.get().getCities(e.getValue())));
		reportEventLayout.setLabel(city, "reportEventView.form.city");
		reportEventLayout.processBinder(city, null, null, true, "eventAddress.city");
		reportEventLayout.addTwoColumnItemsLayout(county, city);
		
		VTextField street = new VTextField();
		reportEventLayout.setLabel(street, "reportEventView.form.street");
		reportEventLayout.processBinder(street, null, null, false, "eventAddress.street");
		VTextField streetNum = new VTextField();
		reportEventLayout.setLabel(streetNum, "reportEventView.form.streetNumber");
		reportEventLayout.processBinder(streetNum, null, null, false, "eventAddress.streetNumber");
		reportEventLayout.addTwoColumnItemsLayout(street, StyleConstants.WIDTH_75, streetNum, StyleConstants.WIDTH_25);
		
		VTextField reporter = new VTextField();
		reportEventLayout.setLabel(reporter, "reportEventView.form.reporter");
		reportEventLayout.processBinder(reporter, null, null, false, "reporter");
		VTextArea eventDescription= new VTextArea();
		reportEventLayout.setLabel(eventDescription, "reportEventView.form.eventDescription");
		reportEventLayout.processBinder(eventDescription, null, null, false, "eventDescription");
		reportEventLayout.addTwoColumnItemsLayout(reporter,  eventDescription);
		
		eventOrganizations = new MultiselectComboBox<Organization>();
		eventOrganizations.setItemLabelGenerator(org -> org.getName());
		eventOrganizations.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
		eventOrganizations.setPlaceholder(getTranslation("placeholder.combobox.selected", eventOrganizations.getValue().size()));
		eventOrganizations.addValueChangeListener(e -> eventOrganizations.setPlaceholder(getTranslation("placeholder.combobox.selected", e.getValue().size())) );
		reportEventLayout.setLabel(eventOrganizations, "reportEventView.form.eventOrganizationList");
		reportEventLayout.processBinder(eventOrganizations, null, null, true, report -> {
			if(report.getEventOrganizationList()==null) {
				return null;
			}
			 return report.getEventOrganizationList().stream().map(EventOrganization::getOrganization).collect(Collectors.toSet());
		}, (report, organizations) -> {
			if(!organizations.isEmpty()) {
				Set<EventOrganization> eOrg = new HashSet<EventOrganization>();
				organizations.forEach(o -> {
						EventOrganization eO = new EventOrganization();
						eO.setReport(report);
						eO.setOrganization(o);
						eOrg.add(eO);
				});
				report.setEventOrganizationList(eOrg);
			}
		});
		reportEventLayout.addTwoColumnItemsLayout(eventOrganizations, null);
		
		reportEventLayout.addButton("reportEventView.form.button.reportEvent", e -> {
			if(reportEventLayout.writeBean()){
			reportServiceRef.get().saveEventReport(eventReport, currentUser.getActiveOrganization().getOrganization().getId());
			eventReport.getEventOrganizationList().forEach(eventOrganization -> {
			  Task preparationTask = new Task();
			  preparationTask.setCreationDateTime(LocalDateTime.now());
			  preparationTask.setName(getTranslation("task.preparation.label", eventReport.getIdentificationNumber()));
			  preparationTask.setReportId(eventReport.getId());
			  preparationTask.setType(TaskType.PREPARATION_TASK);
			  preparationTask.setOrganizationAssignee(eventOrganization.getOrganization());
			  reportServiceRef.get().saveReportTask(preparationTask);
			  ChangeBroadcaster.firePushEvent(new TaskChangeEvent(this, preparationTask, EventAction.ADDED));
			  
			  Notification notification = new Notification();
			  notification.setCreationDateTime(LocalDateTime.now());
			  notification.setMessage(getTranslation("task.preparation.label", eventReport.getIdentificationNumber()));
			  notification.setOrganizationId(eventOrganization.getOrganization().getId());
			  notification.setRecipientId(null);
			  notification.setSourceId(eventReport.getId());
			  notification.setTitle("Novi zadatak");
			  notification.setType(NotificationType.TASK);
			  notificationServiceRef.get().saveOrUpdateNotification(notification);
			  organizationServiceRef.get().getOrganizationGroupMembers(GroupType.PREPARERS, eventOrganization.getOrganization().getId()).forEach(gm -> 
			    notificationServiceRef.get().mapNotificationToUser(notification.getId(), gm.getPerson().getId()));
			  ChangeBroadcaster.firePushEvent(new NotificationEvent(this, notification));
			});
			UI.getCurrent().navigate(HomeView.class);
			}
		}).getElement().getThemeList().add(ThemeAttribute.BUTTON_BLUE);
		
		add(reportEventLayout);
	}

	/**
	 * On attach.
	 *
	 * @param attachEvent the attach event
	 */
	@Override
    protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		county.setItems(addressServiceRef.get().getAllCounties());
		eventOrganizations.setItems(organizationServiceRef.get().getReportSelectableChildsPerLevel(currentUser.getActiveOrganization().getOrganization()));
		reportEventLayout.readBean();
	}

}
