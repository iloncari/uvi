/*
 * OrganizationView OrganizationView.java.
 *
 */

package hr.tvz.vi.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.CustomFormLayout;
import hr.tvz.vi.orm.Address;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import jdk.internal.jline.internal.Log;
import hr.tvz.vi.util.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.datepicker.VDatePicker;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VTextField;

import de.codecamp.vaadin.serviceref.ServiceRef;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class OrganizationView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 10:21:47 PM Aug 10, 2021
 */
@Slf4j
@Route(value = Routes.ORGANIZATION, layout = MainAppLayout.class)
public class OrganizationView extends VVerticalLayout implements HasDynamicTitle {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -3567099719758726806L;

  /** The current user. */
  private final CurrentUser currentUser;
  
  /** The organization form layout. */
  private final CustomFormLayout<Organization> organizationFormLayout;

  /** The active organization. */
  private final Organization activeOrganization;
  
  /** The county. */
  private VSelect<County> county;
	
  /** The city. */
  private VSelect<City> city;

  /** The organization service ref. */
  @Autowired
  private ServiceRef<OrganizationService> organizationServiceRef;
  
  /** The address service ref. */
  @Autowired
  private ServiceRef<AddressService> addressServiceRef;

  /**
   * Instantiates a new organization view.
   */
  public OrganizationView() {
	  
	  
    currentUser = Utils.getCurrentUser(UI.getCurrent());
    activeOrganization = currentUser.getActiveOrganization().getOrganization();
    
    organizationFormLayout = new CustomFormLayout<>(new Binder<>(Organization.class), activeOrganization);
    organizationFormLayout.setFormTitle("organizationView.activeOrganization.title");

    final VTextField activeOrgName = new VTextField();
    organizationFormLayout.setLabel(activeOrgName, "organizationView.form.field.name");
    organizationFormLayout.processBinder(activeOrgName, null, null, true, "name");
    organizationFormLayout.addTwoColumnItemsLayout(activeOrgName, StyleConstants.WIDTH_75, null, null);
    
    county = new VSelect<County>();
	county.setItemLabelGenerator(c -> c.getName());
	organizationFormLayout.setLabel(county, "organizationView.form.field.county");
	city = new VSelect<City>();
	city.setItemLabelGenerator(c -> c.getName());
	county.addValueChangeListener(e -> city.setItems(addressServiceRef.get().getCities(e.getValue())));
	organizationFormLayout.setLabel(city, "organizationView.form.field.city");
	organizationFormLayout.processBinder(city, null, null, true, org -> {
		if(org.getAddress()==null) {
			return null;
		}
		return org.getAddress().getCity();
	}, (org, city) -> {
		if(city==null) {
			return;
		}
		Address address = org.getAddress();
		if(org.getAddress()==null) {
			address = new Address();
		}
		address.setCity(city);
		org.setAddress(address);
	});
	organizationFormLayout.addTwoColumnItemsLayout(county, city);
	
    final VTextField activeOrgStreet = new VTextField();
    organizationFormLayout.setLabel(activeOrgStreet, "organizationView.form.field.street");
    organizationFormLayout.processBinder(activeOrgStreet, null, null, true, org -> {
		if(org.getAddress()==null) {
			return null;
		}
		return org.getAddress().getStreet();
	}, (org, street) -> {
		Address address = org.getAddress();
		if(org.getAddress()==null) {
			address = new Address();
		}
		address.setStreet(street);
		org.setAddress(address);
	});
   
    final VTextField activeOrgStreetNumber = new VTextField();
    organizationFormLayout.setLabel(activeOrgStreetNumber, "organizationView.form.field.streetNumber");
    organizationFormLayout.processBinder(activeOrgStreetNumber, null, null, true, org -> {
		if(org.getAddress()==null) {
			return null;
		}
		return org.getAddress().getStreetNumber();
	}, (org, streetNo) -> {
		Address address = org.getAddress();
		if(org.getAddress()==null) {
			address = new Address();
		}
		address.setStreetNumber(streetNo);
		org.setAddress(address);
	});
    organizationFormLayout.addTwoColumnItemsLayout(activeOrgStreet, StyleConstants.WIDTH_75, activeOrgStreetNumber, StyleConstants.WIDTH_25);

    final VTextField activeOrgIDNumber = new VTextField();
    organizationFormLayout.setLabel(activeOrgIDNumber, "organizationView.form.field.identificationNumber");
    organizationFormLayout.processBinder(activeOrgIDNumber, null, null, true, "identificationNumber");
    final VTextField activeOrgIban = new VTextField();
    organizationFormLayout.setLabel(activeOrgIban, "organizationView.form.field.iban");
    organizationFormLayout.processBinder(activeOrgIban, null, null, false, "iban");
    organizationFormLayout.addTwoColumnItemsLayout(activeOrgIDNumber, activeOrgIban);

    final VDatePicker activeOrgEstablishmentDate = new VDatePicker();
    organizationFormLayout.setLabel(activeOrgEstablishmentDate, "organizationView.form.field.establishmentDate");
    organizationFormLayout.processBinder(activeOrgEstablishmentDate, null, null, false, "establishmentDate");
    organizationFormLayout.addTwoColumnItemsLayout(activeOrgEstablishmentDate, null);

    organizationFormLayout.addSaveBeanButton(e -> {
      if (organizationFormLayout.writeBean()) {
    	  addressServiceRef.get().saveOrUpdateAddress(activeOrganization.getAddress());
        organizationServiceRef.get().saveOrUpdateOrganization(activeOrganization);
        Utils.showSuccessNotification(2000, Position.TOP_CENTER, "organizationView.notification.saveSuccess");
      }
    });

    organizationFormLayout.readBean();
    add(organizationFormLayout);
  }

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public String getPageTitle() {
    return getTranslation(Routes.getPageTitleKey(Routes.ORGANIZATION));
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
    if(activeOrganization.getAddress() != null ) {
	 county.setValue(activeOrganization.getAddress().getCity().getCounty());
    }
	 organizationFormLayout.readBean();
  }
}
