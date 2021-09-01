/*
 * AdvancedSearch AdvancedSearch.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VNumberField;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.gatanaso.MultiselectComboBox;

import com.github.appreciated.papermenubutton.PaperMenuButton;
import com.github.appreciated.papermenubutton.VerticalAlignment;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.QueryParameters;

import hr.tvz.vi.orm.Person;
import hr.tvz.vi.util.Constants.EventActivity;
import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.ThemeAttribute;

/**
 * The Class AdvancedSearch.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @param <T> the generic type
 * @since 8:20:36 PM Sep 1, 2021
 */
public class AdvancedSearch<T> extends VHorizontalLayout {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 6993798999382592250L;
  
  private Map<String, List<String>> queryParams;

  /** The parent view route. */
  private final String parentViewRoute;
  
  /**
   * Instantiates a new advanced search.
   *
   * @param queryParams the query params
   * @param parentViewRoute the parent view route
   */
  public AdvancedSearch(final  Map<String, List<String>> queryParams, final String parentViewRoute) {
    this.queryParams = new HashMap<String, List<String>>(queryParams);
    this.parentViewRoute = parentViewRoute;
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    
    final VButton triggerButton = new VButton(getTranslation("button.advancedSearch"));
    triggerButton.addThemeName(ThemeAttribute.BUTTON_BLUE);

    PaperMenuButton filterPopup = new PaperMenuButton(triggerButton, buildFilterForm());
    filterPopup.setVerticalAlignment(VerticalAlignment.TOP);
    filterPopup.setVerticalOffset(50);
    filterPopup.setDynamicAlign(true);
    add(filterPopup);
  }

  /**
   * Builds the filter form.
   *
   * @return the component
   */
  private Component buildFilterForm() {
    CustomFormLayout<T> formLayout = new CustomFormLayout<T>(null, null);
    formLayout.setFormTitle("advancedSearch.title");
    if(StringUtils.equals(parentViewRoute, Routes.MEMBERS)) {
      initMembersFormFields(formLayout);
    }else if(StringUtils.equals(parentViewRoute, Routes.VECHILES)) {
      
    }else if(StringUtils.equals(parentViewRoute, Routes.REPORTS)) {
      
    }else if(StringUtils.equals(parentViewRoute, Routes.TASKS)) {
      
    }
    return formLayout;
  }

  /**
   * Inits the members form fields.
   *
   * @param formLayout the form layout
   */
  private void initMembersFormFields(CustomFormLayout<T> formLayout) {
    VTextField name = new VTextField();
    name.setValue(queryParams.getOrDefault("name", Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(name, "memberForm.field.name");
    
    VTextField lastname = new VTextField();
    lastname.setValue(queryParams.getOrDefault("lastname", Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(lastname, "memberForm.field.lastname");
    
    VTextField identificationNumber = new VTextField();
    lastname.setValue(queryParams.getOrDefault("identificationNumber", Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(identificationNumber, "memberForm.field.identificationNumber");
    
    VTextField email = new VTextField();
    lastname.setValue(queryParams.getOrDefault("email", Arrays.asList(StringUtils.EMPTY)).get(0));
    formLayout.addFormItem(email, "memberForm.field.email");
    
    List<String> birthDateList = queryParams.getOrDefault("birthDate", Arrays.asList(StringUtils.EMPTY, StringUtils.EMPTY));
    VNumberField minbirthYear = new VNumberField();
    if(NumberUtils.isParsable(birthDateList.get(0))) {
      minbirthYear.setValue(NumberUtils.createDouble(birthDateList.get(0)));
    }
    formLayout.setLabel(minbirthYear, "advancedSearch.member.minBirthYear");
    
    VNumberField maxBirthYear = new VNumberField(); 
    if(birthDateList.size() > 1 && NumberUtils.isParsable(birthDateList.get(1))) {
      maxBirthYear.setValue(NumberUtils.createDouble(birthDateList.get(1)));
    }
    formLayout.setLabel(maxBirthYear, "advancedSearch.member.maxBirthYear");
    formLayout.addTwoColumnItemsLayout(minbirthYear, maxBirthYear);
    
    MultiselectComboBox<Gender> gender = new MultiselectComboBox<Gender>();
    gender.setItemLabelGenerator(g -> getTranslation(g.getGenderTranslationKey()));
    gender.setItems(Arrays.asList(Gender.values()));
    gender.setValue(queryParams.getOrDefault("gender", Arrays.asList(StringUtils.EMPTY)).stream().map(g -> Gender.getGender(g)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(gender, "advancedSearch.member.gender");

    
    MultiselectComboBox<Professions> profession = new MultiselectComboBox<Professions>();
    profession.setItemLabelGenerator(p -> getTranslation(p.getProfessionTranslationKey()));
    profession.setItems(Arrays.asList(Professions.values()));
    profession.setValue(queryParams.getOrDefault("profession", Arrays.asList(StringUtils.EMPTY)).stream().map(p -> Professions.getProfession(p)).filter(Objects::nonNull).collect(Collectors.toSet()));
    formLayout.addFormItem(profession, "advancedSearch.member.profession");
    
    formLayout.addButton("advancedSearch.button.filter", e -> {
      setStringParamValue("name", name);
      setStringParamValue("lastname", lastname);
      setStringParamValue("identificationNumber", identificationNumber);
      setStringParamValue("email", email);
      setRangeParamValue("birthDate", minbirthYear.getValue(), maxBirthYear.getValue());
      removeQueryParams("gender");
      List<String> values = gender.getValue().stream().map(g -> g.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put("gender", values);
      }
      removeQueryParams("profession");
      values = profession.getValue().stream().map(p -> p.getName()).collect(Collectors.toList());
      if(!values.isEmpty()) {
        queryParams.put("profession", values);
      }
      applyFilter();
    });

  }

  /**
   * Sets the string param value.
   *
   * @param key the key
   * @param name the name
   */
  private void setStringParamValue(String key, VTextField name) {
    removeQueryParams(key);
    if(StringUtils.isNotBlank(name.getValue())) {
      queryParams.put(key, Arrays.asList(name.getValue()));
    }
  }
  
  /**
   * Sets the string param value.
   *
   * @param key the key
   * @param min the min
   * @param max the max
   */
  private void setRangeParamValue(String key, Object min, Object max) {
    removeQueryParams(key);
    if(ObjectUtils.anyNotNull(min, max)) {
      queryParams.put(key, Arrays.asList(ObjectUtils.defaultIfNull(min, StringUtils.EMPTY).toString(), ObjectUtils.defaultIfNull(max, StringUtils.EMPTY).toString())); 
    }
  }
  
  /**
   * Removes the query params.
   *
   * @param key the key
   */
  private void removeQueryParams(String key) {
    if(queryParams.containsKey(key)) {
      queryParams.remove(key);
    }
  }
  
  /**
   * Apply filter.
   */
  private void applyFilter() {
    UI.getCurrent().navigate(parentViewRoute, new QueryParameters(queryParams));
    UI.getCurrent().getPage().reload();
  }


 
  
  

}
