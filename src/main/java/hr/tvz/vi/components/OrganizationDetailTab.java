/*
 * OrganizationDetailTab OrganizationDetailTab.java.
 * 
 */
package hr.tvz.vi.components;

import org.vaadin.firitin.components.datepicker.VDatePicker;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VTextField;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.data.binder.Binder;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.orm.Address;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;

/**
 * The Class OrganizationDetailTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 11:19:49 PM Aug 27, 2021
 */
public class OrganizationDetailTab extends VVerticalLayout{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 7993145854788145576L;
  
  /** The current user. */
  private final CurrentUser currentUser;
  
  /** The organization form layout. */
  private  CustomFormLayout<Organization> organizationFormLayout;

  /** The active organization. */
  private Organization activeOrganization;
  
  /** The county. */
  private VSelect<County> county;
  
  /** The city. */
  private VSelect<City> city;

  /** The organization service. */
  private OrganizationService organizationService;
  
  /** The address service. */
  private AddressService addressService;
  
  /**
   * Instantiates a new organization detail tab.
   *
   * @param organizationService the organization service
   * @param addressService the address service
   */
  public OrganizationDetailTab(OrganizationService organizationService, AddressService addressService) {
    this.organizationService = organizationService;
    this.addressService = addressService;
    this.currentUser = Utils.getCurrentUser(UI.getCurrent());
    this.activeOrganization = currentUser.getActiveOrganization().getOrganization();
    initOrganizationForm();
  }
  
  /**
   * Inits the organization form.
   */
  private void initOrganizationForm() {
    organizationFormLayout = new CustomFormLayout<>(new Binder<>(Organization.class), activeOrganization);
    organizationFormLayout.setFormTitle("organizationView.activeOrganization.title");

    final VTextField activeOrgName = new VTextField();
    organizationFormLayout.setLabel(activeOrgName, "organizationView.form.field.name");
    organizationFormLayout.processBinder(activeOrgName, null, null, true, "name");
    organizationFormLayout.addTwoColumnItemsLayout(activeOrgName, StyleConstants.WIDTH_75, null, null);
    
    county = new VSelect<County>();
    county.setItems(addressService.getAllCounties());
    county.setItemLabelGenerator(c -> c.getName());
    organizationFormLayout.setLabel(county, "organizationView.form.field.county");
    city = new VSelect<City>();
    city.setItemLabelGenerator(c -> c.getName());
    county.addValueChangeListener(e -> city.setItems(addressService.getCities(e.getValue())));
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
        addressService.saveOrUpdateAddress(activeOrganization.getAddress());
        organizationService.saveOrUpdateOrganization(activeOrganization);
        Utils.showSuccessNotification(2000, Position.TOP_CENTER, "organizationView.notification.saveSuccess");
      }
    }).getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_BLUE);

    add(organizationFormLayout);
    if(activeOrganization.getAddress() != null ) {
        county.setValue(activeOrganization.getAddress().getCity().getCounty());
    }
   organizationFormLayout.readBean();
  }

}
