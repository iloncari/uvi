/*
 * FireEventTypeDataTab FireEventTypeDataTab.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.select.VSelect;
import org.vaadin.firitin.components.textfield.VNumberField;
import org.vaadin.gatanaso.MultiselectComboBox;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBooleanConverter;

import hr.tvz.vi.converter.DoubleToIntegerConverter;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.util.Constants.BuildingStatus;
import hr.tvz.vi.util.Constants.BuildingType;
import hr.tvz.vi.util.Constants.EventActivity;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.FireSize;
import hr.tvz.vi.util.Constants.IndustrialPlantType;
import hr.tvz.vi.util.Constants.ItemOnFire;
import hr.tvz.vi.util.Constants.OpenSpaceFireType;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Constants.TrafficFireVechileType;

/**
 * The Class FireEventTypeDataTab.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 11:22:05 PM Aug 25, 2021
 */
public class FireEventTypeDataTab extends VVerticalLayout{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 4629673292194250868L;
  
  /** The report. */
  private Report report;
  
  private boolean editRight;
  
  /** The event actions. */
  private MultiselectComboBox<EventActivity> eventActions = new MultiselectComboBox<EventActivity>();
  
  /** The items on fire. */
  private MultiselectComboBox<ItemOnFire> itemsOnFire = new MultiselectComboBox<ItemOnFire>();
  
  /** The explosion. */
  private VSelect<String> explosion = new VSelect<String>();
  
  /** The fire repeated. */
  private VSelect<String> fireRepeated = new VSelect<String>();
  
  /** The building type. */
  private VSelect<BuildingType> buildingType = new VSelect<BuildingType>();
  
  /** The height. */
  private VNumberField height = new VNumberField();
  
  /** The floor. */
  private VNumberField floor = new VNumberField();
  
  /** The building status. */
  private VSelect<BuildingStatus> buildingStatus = new VSelect<BuildingStatus>();
  
  /** The fire size. */
  private VSelect<FireSize> fireSize = new VSelect<FireSize>();
  
  /** The industrial plant type. */
  private VSelect<IndustrialPlantType> industrialPlantType = new VSelect<IndustrialPlantType>();
  
  /** The open space fire type. */
  private VSelect<OpenSpaceFireType> openSpaceFireType = new VSelect<OpenSpaceFireType>();
  
  /** The width. */
  private VNumberField width = new VNumberField();

  /** The lenght. */
  private VNumberField lenght = new VNumberField();
  
  /** The number. */
  private VNumberField number = new VNumberField();
  
  /** The traffic fire vechile type. */
  private VSelect<TrafficFireVechileType> trafficFireVechileType = new VSelect<TrafficFireVechileType>();
  
  /** The binder. */
  private Binder<Report> binder;
  
  /** The tab component map. */
  private  Map<Component, Integer> tabComponentMap;
  
  /**
   * Instantiates a new fire event type data tab.
   *
   * @param report the report
   */
  public FireEventTypeDataTab(Report report,  boolean editRight, Binder<Report> binder, Map<Component, Integer> tabComponentMap) {
    this.report = report; 
    this.binder = binder;
    this.editRight = editRight;
    this.tabComponentMap = tabComponentMap;
    explosion.setItems(Arrays.asList("true", "false"));
    fireRepeated.setItems(Arrays.asList("true", "false"));
    explosion.setReadOnly(!editRight);
    fireRepeated.setReadOnly(!editRight);
    buildingType.setReadOnly(!editRight);
    height.setReadOnly(!editRight);
    floor.setReadOnly(!editRight);
    buildingStatus.setReadOnly(!editRight);
    fireSize.setReadOnly(!editRight);
    industrialPlantType.setReadOnly(!editRight);
    openSpaceFireType.setReadOnly(!editRight);
    width.setReadOnly(!editRight);
    lenght.setReadOnly(!editRight);
    number.setReadOnly(!editRight);
    trafficFireVechileType.setReadOnly(!editRight);
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);   
    initFireEventDataForm();
  }

  /**
   * Inits the fire event data form.
   */
  @SuppressWarnings("unchecked")
  public void initFireEventDataForm() {
    
    binder.removeBinding(floor);
    binder.removeBinding(height);
    binder.removeBinding(buildingStatus);
    binder.removeBinding(buildingType);
    binder.removeBinding(industrialPlantType);
    binder.removeBinding(openSpaceFireType);
    binder.removeBinding(width);
    binder.removeBinding(lenght);
    binder.removeBinding(number);
    binder.removeBinding(trafficFireVechileType);
    binder.removeBinding(eventActions);
    binder.removeBinding(itemsOnFire);
    binder.removeBinding(fireSize);
    binder.removeBinding(explosion);
    binder.removeBinding(fireRepeated);
    
    new HashSet<Component>(tabComponentMap.keySet()).forEach(component -> {
      if(tabComponentMap.get(component) == 2) {
        tabComponentMap.remove(component);
      }
    });
   
    removeAll();
    CustomFormLayout<Report> reportLayoutTop = (CustomFormLayout<Report>)new CustomFormLayout<Report>(binder, report).withClassName(StyleConstants.WIDTH_50.getName());
    
    eventActions.setItems(Arrays.asList(EventActivity.values()));
    eventActions.setEnabled(editRight);
    eventActions.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    eventActions.setItemLabelGenerator(eventAction -> getTranslation(eventAction.getEventActivityTranslationKey()));
    eventActions.setPlaceholder(getTranslation("placeholder.combobox.selected", eventActions.getValue().size()));
    eventActions.addValueChangeListener(e ->  eventActions.setPlaceholder(getTranslation("placeholder.combobox.selected", eventActions.getValue().size())));
    reportLayoutTop.setLabel(eventActions, true, "reportView.fireEventTab.field.eventAction");
    reportLayoutTop.processBinder(eventActions, null, null, true, "eventActivities");
  
    itemsOnFire.setItemLabelGenerator(item -> getTranslation(item.getItemOnFireTranslationKey()));
    itemsOnFire.setEnabled(editRight);
    itemsOnFire.getElement().getThemeList().add(ThemeAttribute.DROPDOWN_WHITE);
    itemsOnFire.setPlaceholder(getTranslation("placeholder.combobox.selected", eventActions.getValue().size()));
    itemsOnFire.addValueChangeListener(e ->  itemsOnFire.setPlaceholder(getTranslation("placeholder.combobox.selected", itemsOnFire.getValue().size())));
    itemsOnFire.setItems(Arrays.asList(ItemOnFire.values()));
    reportLayoutTop.setLabel(itemsOnFire, true, "reportView.fireEventTab.field.itemsOnFire");
    reportLayoutTop.processBinder(itemsOnFire, null, null, true, "itemsOnFire");
 
    fireSize.setItems(Arrays.asList(FireSize.values()));
    fireSize.setItemLabelGenerator(size -> getTranslation(size.getFireSizeTranslationKey()));
    reportLayoutTop.processBinder(fireSize, null, null, true, "fireSize");
    reportLayoutTop.setLabel(fireSize, true,"reportView.fireEventTab.field.fireSize");
    reportLayoutTop.addThreeColumnItemsLayout(eventActions, itemsOnFire, fireSize);
    tabComponentMap.put(fireSize, 2);
    tabComponentMap.put(eventActions, 2);
    tabComponentMap.put(itemsOnFire, 2);
 
    
  
    
    explosion.setItemLabelGenerator(value -> getTranslation("label.".concat(value)));
    reportLayoutTop.processBinder(explosion, new StringToBooleanConverter(""), null, false, "explosion");
    reportLayoutTop.setLabel(explosion, false, "reportView.fireEventTab.field.explosion");
    

    fireRepeated.setItemLabelGenerator(value -> getTranslation("label.".concat(value)));
    reportLayoutTop.processBinder(fireRepeated, new StringToBooleanConverter(""), null, false, "fireRepeated");
    reportLayoutTop.setLabel(fireRepeated, false, "reportView.fireEventTab.field.fireRepeated");
    reportLayoutTop.addThreeColumnItemsLayout(explosion, fireRepeated, null);
    tabComponentMap.put(explosion, 2);
    tabComponentMap.put(fireRepeated, 2);
    
   
    buildingType.setItems(Arrays.asList(BuildingType.values()));
    buildingType.setItemLabelGenerator(type -> getTranslation(type.getBuildingTypeTranslationKey()));
    reportLayoutTop.setLabel(buildingType, true, "reportView.fireEventTab.field.buildingType");
   
    
   
    reportLayoutTop.setLabel(floor, true, "reportView.fireEventTab.field.floor"); 
    reportLayoutTop.setLabel(height, true, "reportView.fireEventTab.field.height");
    
    if(EventType.BUILDING_FIRE.equals(report.getEventType())) {
     
      buildingStatus.setItems(Arrays.asList(BuildingStatus.values()));
      buildingStatus.setItemLabelGenerator(status -> getTranslation(status.getBuildingStatusTranslationKey()));
      reportLayoutTop.processBinder(buildingStatus, null, null, true, "buildingStatus");
      reportLayoutTop.setLabel(buildingStatus, true, "reportView.fireEventTab.field.buildingStatus");
      
      reportLayoutTop.processBinder(buildingType, null, null, true, "buildingType");
      reportLayoutTop.processBinder(height,  new DoubleToIntegerConverter(), null, true, "height");
      reportLayoutTop.processBinder(floor, new DoubleToIntegerConverter(), null, true, "floor");
      tabComponentMap.put(buildingStatus, 2);
      tabComponentMap.put(buildingType,2);
      tabComponentMap.put(height, 2);
      tabComponentMap.put(floor, 2);
      reportLayoutTop.addThreeColumnItemsLayout(buildingType, buildingStatus, null);
      reportLayoutTop.addThreeColumnItemsLayout(height, floor, null);
    }else if(EventType.INDUSTRIAL_FIRE.equals(report.getEventType())) {
    
      industrialPlantType.setItems(Arrays.asList(IndustrialPlantType.values()));
      industrialPlantType.setItemLabelGenerator(type -> getTranslation(type.getIndustrialPlantTypeTranslationKey()));
      reportLayoutTop.processBinder(industrialPlantType, null, null, true, "industrialPlantType");
      reportLayoutTop.setLabel(industrialPlantType,true, "reportView.fireEventTab.field.industrialPlantType");
      
      
      reportLayoutTop.processBinder(buildingType, null, null, true, "buildingType");
      reportLayoutTop.processBinder(height,  new DoubleToIntegerConverter(), null, true, "height");
      reportLayoutTop.processBinder(floor, new DoubleToIntegerConverter(), null, true, "floor");
      reportLayoutTop.addThreeColumnItemsLayout(buildingType, industrialPlantType, null);
      reportLayoutTop.addThreeColumnItemsLayout(height, floor, null);
      tabComponentMap.put(industrialPlantType, 2);
      tabComponentMap.put(buildingType,2);
      tabComponentMap.put(height, 2);
      tabComponentMap.put(floor, 2);
    }else if(EventType.OPEN_SPACE_FIRE.equals(report.getEventType())) {
     
      openSpaceFireType.setItems(Arrays.asList(OpenSpaceFireType.values()));
      openSpaceFireType.setItemLabelGenerator(type -> getTranslation(type.getOpenSpaceFireTypeTranslationKey()));
      reportLayoutTop.processBinder(openSpaceFireType, null, null, true, "openSpaceFireType");
      reportLayoutTop.setLabel(openSpaceFireType, true, "reportView.fireEventTab.field.openSpaceFireType");
      
      
      reportLayoutTop.processBinder(width, null, null, true, "width");
      reportLayoutTop.setLabel(width, true, "reportView.fireEventTab.field.width");
      
      reportLayoutTop.processBinder(lenght,null , null, true, "lenght");
      reportLayoutTop.setLabel(lenght, true, "reportView.fireEventTab.field.lenght");
      
      reportLayoutTop.addThreeColumnItemsLayout(openSpaceFireType, null, null);
      reportLayoutTop.addThreeColumnItemsLayout(width, lenght, null);
      tabComponentMap.put(openSpaceFireType, 2);
      tabComponentMap.put(width, 2);
      tabComponentMap.put(lenght, 2);
    }else if(EventType.TRAFFIC_FIRE.equals(report.getEventType())) {
      
      trafficFireVechileType.setItems(Arrays.asList(TrafficFireVechileType.values()));
      trafficFireVechileType.setItemLabelGenerator(type -> getTranslation(type.getTrafficFireVechileTypeTranslationKey()));
      reportLayoutTop.processBinder(trafficFireVechileType, null, null, true, "trafficFireVechileType");
      reportLayoutTop.setLabel(trafficFireVechileType, true, "reportView.fireEventTab.field.trafficFireVechileType");
      tabComponentMap.put(trafficFireVechileType, 2);
      
      reportLayoutTop.processBinder(number, new DoubleToIntegerConverter(), null, true, "numberOfVechiles");
      reportLayoutTop.setLabel(number, true, "reportView.fireEventTab.field.number");
      tabComponentMap.put(number, 2);
      
      reportLayoutTop.addThreeColumnItemsLayout(trafficFireVechileType, number, null);
    }

    reportLayoutTop.setBean();
    add(reportLayoutTop);
  }

}
