/*
 * MemberForm MemberForm.java.
 *
 */
package hr.tvz.vi.components;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.checkbox.VCheckBox;
import org.vaadin.firitin.components.datepicker.VDatePicker;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VEmailField;
import org.vaadin.firitin.components.textfield.VPasswordField;
import org.vaadin.firitin.components.textfield.VTextField;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import hr.tvz.vi.orm.Address;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.PersonOrganization;
import hr.tvz.vi.service.AbstractService;
import hr.tvz.vi.service.AddressService;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.PersonService;
import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.view.MembersView;

/**
 * The Class MemberForm.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 7:18:32 PM Aug 11, 2021
 */
public class MemberForm extends AbstractForm<Person> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 252125676081010394L;
  
  /** The county. */
  private VSelect<County> county;

   /** The contact layout. */
  private CustomFormLayout<Person> contactLayout;
   
  /** The organization service. */
  private final OrganizationService organizationService;
  
  /** The address service ref. */
  private final AddressService addressService;

  /**
   * Instantiates a new member form.
   *
   * @param person the person
   * @param personService the person service
   * @param organizationService the organization service
   */
  public MemberForm(Person person, AbstractService<Person> personService, AbstractService<Organization> organizationService, AbstractService<Address> addressService, boolean navigateToParentsView) {
    super(person, personService, navigateToParentsView);
    this.organizationService = (OrganizationService) organizationService;
    this.addressService = (AddressService)addressService;
  }

  /**
   * Inits the form.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void initForm() {
	  Binder<Person> b = new Binder<>(Person.class);
    final CustomFormLayout<Person> basicDataLayout = (CustomFormLayout<Person>) new CustomFormLayout<>(b, formEntity)
      .withClassName(StyleConstants.WIDTH_50.getName());
    basicDataLayout.setFormTitle("memberForm.basicData.label");
    final VTextField nameField = new VTextField();
    basicDataLayout.setLabel(nameField, "memberForm.field.name");
    basicDataLayout.processBinder(nameField, null, null, false, "name");
    final VTextField lastnameField = new VTextField();
    basicDataLayout.setLabel(lastnameField, "memberForm.field.lastname");
    basicDataLayout.processBinder(lastnameField, null, null, false, "lastname");
    basicDataLayout.addTwoColumnItemsLayout(nameField, lastnameField);

    final VTextField idNumberField = new VTextField().withReadOnly(true);
    basicDataLayout.setLabel(idNumberField, "memberForm.field.identificationNumber");
    basicDataLayout.processBinder(idNumberField, null, null, false, "identificationNumber");
    final VDatePicker birthDateField = new VDatePicker();
    basicDataLayout.setLabel(birthDateField, "memberForm.field.birthDate");
    basicDataLayout.processBinder(birthDateField, null, null, false, "birthDate");
    basicDataLayout.addTwoColumnItemsLayout(idNumberField, birthDateField);

    final VSelect<Gender> genderSelect = new VSelect<>();
    basicDataLayout.setLabel(genderSelect, "memberForm.field.gender");
    genderSelect.setItemLabelGenerator(gender -> getTranslation("gender." + gender.getName() + ".label"));
    genderSelect.setItems(Arrays.asList(Gender.values()));
    basicDataLayout.processBinder(genderSelect, null, null, false, "gender");
    final VSelect<Professions> professionSelect = new VSelect<>();
    basicDataLayout.setLabel(professionSelect, "memberForm.field.profession");
    professionSelect.setItemLabelGenerator(profession -> getTranslation("profession." + profession.getName() + ".label"));
    professionSelect.setItems(Arrays.asList(Professions.values()));
    basicDataLayout.processBinder(professionSelect, null, null, false, "profession");
    basicDataLayout.addTwoColumnItemsLayout(genderSelect, professionSelect);

    basicDataLayout.readBean();

    contactLayout = new CustomFormLayout<>(new Binder<>(Person.class), formEntity);
    contactLayout.setFormTitle("memberForm.contactData.label");
    final VEmailField emailField = new VEmailField();
    contactLayout.setLabel(emailField, "memberForm.field.email");
    contactLayout.processBinder(emailField, null, null, false, "email");
    final VTextField phoneNumberField = new VTextField();
    contactLayout.setLabel(phoneNumberField, "memberForm.field.phoneNumber");
    contactLayout.processBinder(phoneNumberField, null, null, false, "phoneNumber");
    contactLayout.addTwoColumnItemsLayout(emailField, phoneNumberField);

    county = new VSelect<County>();
	county.setItemLabelGenerator(c -> c.getName());
	contactLayout.setLabel(county, "memberForm.field.residenceCounty");
    VSelect<City> city = new VSelect<City>();
	city.setItemLabelGenerator(c -> c.getName());
	county.addValueChangeListener(e -> city.setItems(addressService.getCities(e.getValue())));
	contactLayout.setLabel(city, "memberForm.field.residenceAddress.city");
	contactLayout.processBinder(city, null, null, false, "residenceAddress.city");
	contactLayout.addTwoColumnItemsLayout(county, city);
   
    final VTextField streetField = new VTextField();
    contactLayout.setLabel(streetField, "memberForm.field.residenceStreet");
   contactLayout.processBinder(streetField, null, null, false, "residenceAddress.street");
    final VTextField steetNumberField = new VTextField();
    contactLayout.setLabel(steetNumberField, "memberForm.field.residenceStreetNumber");
    contactLayout.processBinder(steetNumberField, null, null, false, "residenceAddress.streetNumber");
    contactLayout.addTwoColumnItemsLayout(streetField, steetNumberField);

    final CustomFormLayout<Person> appDataLayout = (CustomFormLayout<Person>) new CustomFormLayout<>(new Binder<>(Person.class), formEntity)
      .withClassName(StyleConstants.WIDTH_50.getName());
    appDataLayout.setFormTitle("memberForm.appData.label");

    final boolean changePasswordEnabled = currentUser.getPerson().getIdentificationNumber().equals(formEntity.getIdentificationNumber())
    	      || formEntity.getId()==null;
    final VTextField usernameField = new VTextField().withEnabled(changePasswordEnabled);
    appDataLayout.setLabel(usernameField, "memberForm.field.username");
    appDataLayout.processBinder(usernameField, null, null, false, "username");
    
    final VCheckBox accessRight = new VCheckBox().withEnabled(changePasswordEnabled || currentUser.hasManagerRole());
    accessRight.setLabelAsHtml(getTranslation("memberForm.field.accessRights"));
    if(formEntity.getOrgList()!=null) {
    Optional<PersonOrganization> currentPO = formEntity.getOrgList().stream().filter(po -> po.getOrganization().getId().equals(currentUser.getActiveOrganization().getId())).findFirst();
    if(currentPO.isPresent()) {
    	accessRight.setValue(currentPO.get().isAppRights());
    }
    }
    accessRight.setVisible(false);
    appDataLayout.addTwoColumnItemsLayout(usernameField, accessRight);

   
   StringBuilder builder = new StringBuilder();
  for(int i = 0; i < formEntity.getPasswordLength(); i++) {
	  builder.append("*");
  }
  final String value = builder.toString();
  boolean passCleared=false || value.isBlank();
    final VPasswordField passwordField = new VPasswordField().withEnabled(changePasswordEnabled).withValue(value);
    passwordField.setRevealButtonVisible(passCleared);
    
    appDataLayout.setLabel(passwordField, "memberForm.field.password");
    final VPasswordField repeatPasswordField = new VPasswordField().withEnabled(changePasswordEnabled).withValue(value);
    repeatPasswordField.setRevealButtonVisible(passCleared);
    appDataLayout.setLabel(repeatPasswordField, "memberForm.field.repeatPassword");
    appDataLayout.addTwoColumnItemsLayout(passwordField, repeatPasswordField);
    accessRight.setEnabled(currentUser.hasManagerRole() && StringUtils.isNoneEmpty(passwordField.getValue(), repeatPasswordField.getValue()));
    appDataLayout.readBean();

    final VButton saveButton = new VButton(getTranslation("button.save"));
    saveButton.addClickListener(e -> { 	
      boolean succes = true;
      succes = basicDataLayout.writeBean();
      succes = appDataLayout.writeBean();
      succes = contactLayout.writeBean();
      if (succes) {
    	  if(changePasswordEnabled && !StringUtils.equals(passwordField.getValue(), value) && StringUtils.isNotBlank(repeatPasswordField.getValue())) {
    		  formEntity
              .setHashedPassword(BCrypt.hashpw(repeatPasswordField.getValue(), BCrypt.gensalt()));
    	  }
        
        addressService.saveOrUpdateAddress(formEntity.getResidenceAddress());
        ((PersonService) entityService).saveOrUpdatePerson(formEntity);
       //formEntity.getOrgList().stream().filter(po -> po.getOrganization().getId().equals(currentUser.getActiveOrganization().getId())).findFirst()
      //    .ifPresent(po -> po.setAppRights(accessRight.getValue()));

        if (!organizationService.isPersonOrganizationMember(formEntity, currentUser.getActiveOrganization().getOrganization())) {
          organizationService.joinOrganization(formEntity, currentUser.getActiveOrganization().getOrganization(), true);
        }
        Utils.showSuccessNotification(2000, Position.TOP_CENTER, "memberForm.notification.memberSaved");
        if (navigateToParentsView) {
          UI.getCurrent().navigate(MembersView.class);
        }
      }
    });
    saveButton.getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_BLUE);

    if (changePasswordEnabled) {
      passwordField.setValueChangeMode(ValueChangeMode.EAGER);
      passwordField.setMinLength(5);
      passwordField.setErrorMessage(getTranslation("memberForm.field.password.minLength.error", passwordField.getMinLength()));
      passwordField.addValueChangeListener(e -> {
        passwordField.setInvalid(false);
        if (e.getValue().length() < e.getSource().getMinLength()) {
          passwordField.setInvalid(true);
          passwordField.setErrorMessage(getTranslation("memberForm.field.password.minLength.error", passwordField.getMinLength()));
        } else if (!StringUtils.equals(passwordField.getValue(), repeatPasswordField.getValue()) && StringUtils.isNotBlank(repeatPasswordField.getValue())) {
          passwordField.setInvalid(true);
          repeatPasswordField.setErrorMessage(getTranslation("memberForm.field.password.notMatch.error"));
        }
        
        if(!StringUtils.equals(value, e.getValue()) && StringUtils.equals(value, repeatPasswordField.getValue())) {
        	repeatPasswordField.setValue(null);
        }
        
        accessRight.setEnabled(currentUser.hasManagerRole() && StringUtils.isNoneEmpty(passwordField.getValue(), repeatPasswordField.getValue())
          && !passwordField.isInvalid() && !repeatPasswordField.isInvalid());
        saveButton.setEnabled(!passwordField.isInvalid() && !repeatPasswordField.isInvalid());
        passwordField.setRevealButtonVisible(passCleared);
      });

      repeatPasswordField.setValueChangeMode(ValueChangeMode.EAGER);
      repeatPasswordField.setErrorMessage(getTranslation("memberForm.field.password.notMatch.error"));
      repeatPasswordField.addValueChangeListener(e -> {
        repeatPasswordField.setInvalid(false);
        if (!StringUtils.equals(passwordField.getValue(), repeatPasswordField.getValue()) && StringUtils.isNotBlank(passwordField.getValue())) {
          repeatPasswordField.setErrorMessage(getTranslation("memberForm.field.password.notMatch.error"));
          repeatPasswordField.setInvalid(true);
        }
        if(!StringUtils.equals(value, e.getValue()) && StringUtils.equals(value, passwordField.getValue())) {
        	passwordField.setValue(null);
        }
        repeatPasswordField.setRevealButtonVisible(passCleared);

        accessRight.setEnabled(currentUser.hasManagerRole() && StringUtils.isNoneEmpty(passwordField.getValue(), repeatPasswordField.getValue())
          && !passwordField.isInvalid() && !repeatPasswordField.isInvalid());
        saveButton.setEnabled(!passwordField.isInvalid() && !repeatPasswordField.isInvalid());

      });
    }

    final VHorizontalLayout userDataAppDataLayout = new VHorizontalLayout().withClassName(StyleConstants.WIDTH_100.getName());
    userDataAppDataLayout.add(basicDataLayout, appDataLayout);
    add(userDataAppDataLayout, contactLayout, saveButton);
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
	  super.onAttach(attachEvent);
	 county.setItems(addressService.getAllCounties());
	 county.setValue(formEntity.getResidenceAddress().getCity().getCounty());
	 contactLayout.readBean();
	 
  }
}
