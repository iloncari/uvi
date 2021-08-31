/*
 * Constants Constants.java.
 * 
 */

package hr.tvz.vi.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


/**
 * The Class Constants.
 *
 * @author Igor Lončarić (iloncari2@optimit.hr)
 * @since 2:12:27 PM Aug 7, 2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

  /**
   * The Enum TaskType.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 10:04:19 PM Aug 27, 2021
   */
  public enum TaskType {
    
    /** The approve task. */
    APPROVE_TASK,
    
    /** The preparation task. */
    PREPARATION_TASK;
  }
  
  /**
   * The Enum GroupType.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 10:05:34 PM Aug 27, 2021
   */
  @RequiredArgsConstructor
  public enum GroupType{
    
    /** The preparers. */
    PREPARERS("preparers"),
    
    /** The approvers. */
    APPROVERS("approvers");
    
    /** The name. */
    private final String name;
    
    /**
     * Gets the group type localization key.
     *
     * @return the group type localization key
     */
    public String getGroupTypeLocalizationKey() {
      return "groupType."+name+".label";
    }
  }
  
  
  
  /**
   * The Enum NotificationType.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 6:35:11 PM Aug 29, 2021
   */
  public enum NotificationType{
    
    GROUP,
    
    TASK;
  }
  
 
	 /**
   * The Enum EventType.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 6:35:28 PM Aug 29, 2021
   */
 	@RequiredArgsConstructor
	  public enum EventType {
		 
		 /** The building fire. */
 		BUILDING_FIRE("buildingFire"),
		 
		 /** The industrial fire. */
 		INDUSTRIAL_FIRE("industrialFire"),
		 
		 /** The open space fire. */
 		OPEN_SPACE_FIRE("openSpaceFire"),
		 
		 /** The traffic fire. */
 		TRAFFIC_FIRE("trafficFire"),
		 
		 /** The building technical. */
 		BUILDING_TECHNICAL("buildingTechnical"),
		 
		 /** The idustrial technical. */
 		IDUSTRIAL_TECHNICAL("industrialTechnical"),
		 
		 /** The open space technical. */
 		OPEN_SPACE_TECHNICAL("openSpaceTechnical"),
		 
		 /** The traffic technical. */
 		TRAFFIC_TECHNICAL("trafficTechnical"),
		 
		 /** The dangerous material. */
 		DANGEROUS_MATERIAL("dangerousMaterial"),
		 
		 /** The false report. */
 		FALSE_REPORT("falseReport"),
		 
		 /** The stand by. */
 		STAND_BY("standBy"),
		 
		 /** The other. */
 		OTHER("other");
		 
   	    /** The name. */
  		private final String name;
		  
		  /**
		 * Gets the event type translation key.
		 *
		 * @return the event type translation key
		 */
  		public String getEventTypeTranslationKey() {
		      return "eventType.".concat(name).concat(".label");
		    } 
  		
  	/**
     * Checks if is intervention fire.
     *
     * @return true, if is intervention fire
     */
	  public boolean isInterventionFire() {
  	  return this.equals(BUILDING_FIRE) || this.equals(INDUSTRIAL_FIRE) || this.equals(OPEN_SPACE_FIRE) || this.equals(TRAFFIC_FIRE);
  	}
	  
	 }
 	
 	/**
    * The Enum BuildingType.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 6:15:14 PM Aug 26, 2021
    */
 	@RequiredArgsConstructor
  public enum BuildingType {
   
	   /** The hall. */
   	HALL("hall"),
	   
	   /** The two four floors building. */
   	TWO_FOUR_FLOORS_BUILDING("TwoFourFloorBuilding"),
	   
	   /** The wood building. */
   	WOOD_BUILDING("woodBuilding"),
	   
	   /** The container. */
   	CONTAINER("container"),
	   
	   /** The ground floor building. */
   	GROUND_FLOOR_BUILDING("groundFloorBuilding"),
	   
	   /** The multifloor building. */
   	MULTIFLOOR_BUILDING("multifloorBuilding"),
	   
	   /** The tent. */
   	TENT("tent"),
	   
	   /** The tall building. */
   	TALL_BUILDING("tallBuilding");
	  
      /** The name. */
    private final String name;
    
    /**
   * Gets the event type translation key.
   *
   * @return the event type translation key
   */
    public String getBuildingTypeTranslationKey() {
        return "buildingType.".concat(name).concat(".label");
      } 
 }
 	
 	/**
    * The Enum BuildingStatus.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 6:23:52 PM Aug 26, 2021
    */
 	@RequiredArgsConstructor
	 public enum BuildingStatus{
 	  
 	  /** The under construction. */
  	 UNDER_CONSTRUCTION("underConstruction"),
 	  
 	  /** The built rusty. */
  	 BUILT_RUSTY("builtAndRusty"),
 	  
 	  /** The built not used. */
  	 BUILT_NOT_USED("builtAndNotUsed"),
 	  
 	  /** The built in use. */
  	 BUILT_IN_USE("buildAndInUse");
 	  
 	  
 	  /** The name. */
  	 private final String name;
 	  
 	 /**
    * Gets the building status translation key.
    *
    * @return the building status translation key
    */
 	 public String getBuildingStatusTranslationKey() {
     return "buildingStatus.".concat(name).concat(".label");
   } 
 	  
 	}
 	
 	
 	/**
    * The Enum TrafficFireVechileType.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 6:51:31 PM Aug 26, 2021
    */
	 @RequiredArgsConstructor
  public enum TrafficFireVechileType{
   
   /** The without vechile. */
   WITHOUT_VECHILE("withoutVechile"),
   
   /** The vessels. */
   VESSELS("vessels"),
   
   /** The road passenger trasport. */
   ROAD_PASSENGER_TRASPORT("roadPassengerTransport"),
   
   /** The road goods transport. */
   ROAD_GOODS_TRANSPORT("roadGoodsTransport"),
   
   /** The road machinery. */
   ROAD_MACHINERY("roadMachinery"),
   
   /** The airplane. */
   AIRPLANE("airplane"),
   
   /** The railway vechile. */
   RAILWAY_VECHILE("railwayVechile");
   
   
   /** The name. */
    private final String name;
   

  /**
   * Gets the traffic fire vechile type translation key.
   *
   * @return the traffic fire vechile type translation key
   */
  public String getTrafficFireVechileTypeTranslationKey() {
    return "trafficFireVechileType.".concat(name).concat(".label");
  } 
   
 }
	 
   /**
    * The Enum IndustrialPlantType.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 6:34:44 PM Aug 26, 2021
    */
   @RequiredArgsConstructor
   public enum IndustrialPlantType{
    
    /** The production plant. */
    PRODUCTION_PLANT("productionPlant"),
    
    /** The storage space. */
    STORAGE_SPACE("storageSpace"),
    
    /** The administrative building. */
    ADMINISTRATIVE_BUILDING("administrative building");
    
    /** The name. */
     private final String name;
    
   /**
    * Gets the building status translation key.
    *
    * @return the building status translation key
    */
   public String getIndustrialPlantTypeTranslationKey() {
     return "industrialPlantType.".concat(name).concat(".label");
   } 
    
  }
   
   
   /**
    * The Enum OpenSpaceFireType.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 6:38:30 PM Aug 26, 2021
    */
   @RequiredArgsConstructor
   public enum OpenSpaceFireType{
    
    /** The combined fire. */
    COMBINED_FIRE("combinedFire"),
    
    /** The low surface fire. */
    LOW_SURFACE_FIRE("lowSurfaceFire"),
    
    /** The underground fire. */
    UNDERGROUND_FIRE("undergroundFire"),
    
    /** The high tree canopy fire. */
    HIGH_TREE_CANOPY_FIRE("highTreeCanopyFire");
    
    /** The name. */
     private final String name;
    
   /**
    * Gets the building status translation key.
    *
    * @return the building status translation key
    */
   public String getOpenSpaceFireTypeTranslationKey() {
     return "openSpaceFireType.".concat(name).concat(".label");
   } 
    
  }


  /**
   * Instantiates a new event activity.
   *
   * @author Igor LonÄ�ariÄ‡ (iloncari2@tvz.hr)
   * @since 6:20:37 PM Aug 26, 2021
   */
  @RequiredArgsConstructor
  public enum EventActivity {
    
    /** The wirhout extinguishing. */
    WIRHOUT_EXTINGUISHING("withoutExtinguishing"),
    
    /** The extinguishing. */
    EXTINGUISHING("extinguishing"),
    
    /** The evacuation. */
    EVACUATION("evacuation"),
    
    /** The wrecked building rescuing. */
    WRECKED_BUILDING_RESCUING("wreckedBuildingRescuing"),
    
    /** The peoples animals assets salvage. */
    PEOPLES_ANIMALS_ASSETS_SALVAGE("salvageOfPeoplesAnimalsAssets"),
    
    /** The height depth salvage. */
    HEIGHT_DEPTH_SALVAGE("salvageFromHeightOrDepth"),
    
    /** The animal removal. */
    ANIMAL_REMOVAL("animalRemoval");
   
    /** The name. */
    private final String name;
    
    /**
   * Gets the event type translation key.
   *
   * @return the event type translation key
   */
    public String getEventActivityTranslationKey() {
        return "eventActivity.".concat(name).concat(".label");
      } 
  }
  
  /**
   * The Enum FIRESIZE.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 11:46:12 PM Aug 25, 2021
   */
  @RequiredArgsConstructor
  public enum FireSize {
    
    /** The small. */
    SMALL("small"),
    
    /** The medium. */
    MEDIUM("medium"),
    
    /** The big. */
    BIG("big");
    
    /** The name. */
    private final String name;
    
    /**
   * Gets the event type translation key.
   *
   * @return the event type translation key
   */
    public String getFireSizeTranslationKey() {
        return "fireSize.".concat(name).concat(".label");
      }
    
  }
  
  
  /**
   * The Enum ItemOnFire.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 11:46:12 PM Aug 25, 2021
   */
  @RequiredArgsConstructor
  public enum ItemOnFire {
    
    /** The vechile part. */
    VECHILE_PART("vechilePart"),
    
    /** The electrical devices. */
    ELECTRICAL_DEVICES("electricalDevices"),
    
    /** The building elements. */
    BUILDING_ELEMENTS("buildingElements"),
    
    /** The apartment furniture. */
    APARTMENT_FURNITURE("apartmentFurniture"),
    
    /** The basic substances. */
    BASIC_SUBSTANCES("basicSubstances"),
    
    /** The undetermined. */
    UNDETERMINED("undetermined"),
    
    /** The other. */
    OTHER("other"),
    
    /** The machinery. */
    MACHINERY("machinery");
    
    /** The name. */
    private final String name;
    
    /**
   * Gets the event type translation key.
   *
   * @return the event type translation key
   */
    public String getItemOnFireTranslationKey() {
        return "itemOnFire.".concat(name).concat(".label");
      }
    
  }
 	
 	/**
    * The Enum EventCause.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 8:36:57 PM Aug 22, 2021
    */
	@RequiredArgsConstructor
 	public enum EventCause{
 	  
 	 /** The action of man. */
 	 ACTION_OF_MAN("actionOfMan"),
 	  
 	 /** The natural phenomena. */
 	 NATURAL_PHENOMENA("naturalPhenomena"),
 	 
 	 /** The undefined. */
 	 UNDEFINED("undefined"),
 	 
 	 /** The technological malfunction and technological processes. */
 	 TECHNOLOGICAL_MALFUNCTION_AND_TECHNOLOGICAL_PROCESSES("technologicalMalfunctionAndTechnologicalProcesses");
 	  
 	 /** The name. */
 	 private final String name;
 	  
 	 /**
    * Gets the event type translation key.
    *
    * @return the event type translation key
    */
 	 public String getEventCauseTranslationKey() {
     return "eventCause.".concat(name).concat(".label");
   } 
	}
 	 
 	/**
    * The Enum EventCausePerson.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 8:37:12 PM Aug 22, 2021
    */
 	@RequiredArgsConstructor
  public enum EventCausePerson{
	   
	  /** The without person act. */
  	WITHOUT_PERSON_ACT("withoutActOfPerson"),
	  
	  /** The child. */
  	CHILD("child"),
	  
	  /** The user. */
  	USER("user"),
	  
	  /** The owner. */
  	OWNER("owner"),
	  
	  /** The huner. */
  	HUNER("hunter"),
	  
	  /** The worker. */
  	WORKER("worker"),
	  
	  /** The guest. */
  	GUEST("guest"),
	  
	  /** The unknown. */
  	UNKNOWN("unknown"),
	  
	  /** The other. */
  	OTHER("other"),
	  
	  /** The farmer. */
  	FARMER("farmer"),
	  
	  /** The passerby. */
  	PASSERBY("passerby");
	   
   	/** The name. */
   	private final String name;
    
    /**
     * Gets the event cause person translation key.
     *
     * @return the event cause person translation key
     */
    public String getEventCausePersonTranslationKey() {
      return "eventCausePerson.".concat(name).concat(".label");
    } 
 	}
 	  
 	
	
  /**
	 * Instantiates a new duty.
	 *
	 * @author Igor LonÄ�ariÄ‡ (iloncari2@tvz.hr)
	 * @since 1:38:32 PM Aug 22, 2021
	 */
  @RequiredArgsConstructor
  public enum Duty {

    /** The president. */
    PRESIDENT("president"),

    /** The secretary. */
    SECRETARY("secretary"),

    /** The commander. */
    COMMANDER("commander"),

    /** The none. */
    NONE("none");

    /** The name. */
    @Getter
    private final String name;
    
    /**
	 * Gets the profession translation key.
	 *
	 * @return the profession translation key
	 */
    public String getProfessionTranslationKey() {
        return "duty.".concat(name).concat(".label");
      }
  }

  /**
   * The Enum EventAction.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 12:31:48 PM Aug 13, 2021
   */
  public enum EventAction {
    
    /** The added. */
    ADDED,

    /** The removed. */
    REMOVED,

    /** The modified. */
    MODIFIED;
  }


  /**
   * The Interface EventSubscriber.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 6:32:33 PM Aug 29, 2021
   */
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface EventSubscriber {

    /**
     * Scope.
     *
     * @return the subscriber scope
     */
    public SubscriberScope scope() default SubscriberScope.SESSION;
  }

  /**
   * The Enum Gender.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 5:41:51 PM Aug 12, 2021
   */
  @Getter
  @RequiredArgsConstructor
  public enum Gender {
    
    /** The male. */
    MALE("male"),

    /** The female. */
    FEMALE("female"),

    /** The other. */
    OTHER("other");

    /** The name. */
    private final String name;
  }

  /**
   * The Enum ImageConstants.
   *
   * @author Igor Lončarić (iloncari2@optimit.hr)
   * @since 3:12:45 PM Aug 7, 2021
   */
  @RequiredArgsConstructor
  public enum ImageConstants {

    /** The app logo. */
    APP_LOGO("app_logo.png"),
    
    /** The icon notification. */
    ICON_NOTIFICATION("icon-notifications.svg"),
    
    /** The icon organization. */
    ICON_ORGANIZATION("icon-organization.svg"),
    
    /** The icon log out. */
    ICON_LOG_OUT("icon-log-out.svg"),
    
    /** The icon close. */
    ICON_CLOSE("icon-cancel-red.svg");

    /** The src. */
    private final String name;

    /**
	 * Gets the path.
	 *
	 * @return the path
	 */
    public String getPath() {
      return "img/" + name;
    }
  }
  

  /**
	 * The Enum OrganizationLevel.
	 *
	 * @author Igor Lončarić (iloncari2@tvz.hr)
	 * @since 3:51:23 PM Aug 20, 2021
	 */
  @Getter
  @RequiredArgsConstructor
  public enum OrganizationLevel {
	  /** The country level. */
  	COUNTRY_LEVEL("countryLevel"),
	  
	  /** The regional level. */
  	REGIONAL_LEVEL("regionalLevel"),
	  
	  /** The city level. */
  	CITY_LEVEL("cityLevel"),
	  
  	/** The operational level. */
	  OPERATIONAL_LEVEL("operationalLevel");
	  
	  /** The name. */
  	private final String name;
  }
  
  /**
  * The Enum Professions.
  *
  * @author Igor Lončarić (iloncari2@tvz.hr)
  * @since 2:12:30 PM Aug 7, 2021
  */
  @Getter
  @RequiredArgsConstructor
  public enum Professions {

    /** The youth firefighter. */
    YOUTH_FIREFIGHTER("youth_firefighter"),

    /** The firefighter. */
    FIREFIGHTER("firefigter"),

    /** The first class firefighter. */
    FIRST_CLASS_FIREFIGHTER("first_class_firefigter"),

    /** The subofficer. */
    SUBOFFICER("subofficer"),

    /** The first class subofficer. */
    FIRST_CLASS_SUBOFFICER("first_class_subofficer"),

    /** The officer. */
    OFFICER("officer"),

    /** The first class officer. */
    FIRST_CLASS_OFFICER("first_class_officer"),

    /** The higher officer. */
    HIGHER_OFFICER("higher_officer"),

    /** The first class higher officer. */
    FIRST_CLASS_HIGHER_OFFICER("fitst_class_higher_officer"),

    /** The other. */
    OTHER("other");

    /** The name. */
    private final String name;

    /**
     * Gets the translation key.
     *
     * @return the translation key
     */
    public String getProfessionTranslationKey() {
      return "profession.".concat(name).concat(".label");
    }
  }

  /**
    * The Enum ReportStatus.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 11:49:01 AM Aug 9, 2021
    */
  @Getter
  @RequiredArgsConstructor
  public enum ReportStatus {
    
    /** The new. */
    NEW("new"),
    
    /** The prepared. */
    PREPARED("prepared"),

    /** The approved. */
    APPROVED("approved");

    /** The name. */
    private final String name;
    
    /**
	 * Gets the report status translation key.
	 *
	 * @return the report status translation key
	 */
    public String getReportStatusTranslationKey() {
        return "reportStatus.".concat(name).concat(".label");
      }
  }

  /**
   * The Class Routes.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 5:17:38 PM Aug 7, 2021
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  public static final class Routes {

    /** The Constant LOGIN. */
    public static final String LOGIN = "login";

    /** The Constant RESET_PASSWORD. */
    public static final String RESET_PASSWORD = "resetPassword";

    /** The Constant HOME. */
    public static final String HOME = "home";

    /** The Constant ORGANIZATION. */
    public static final String ORGANIZATION = "organization";

    /** The Constant MEMBERS. */
    public static final String MEMBERS = "members";

    /** The Constant MEMBER. */
    public static final String MEMBER = "member";

    /** The Constant ADD_MEMBER. */
    public static final String ADD_MEMBER = "member/add";

    /** The Constant ACCESS_DENIED. */
    public static final String ACCESS_DENIED = "accessDenied";

    /** The Constant NAVIGATION_ERROR. */
    public static final String NAVIGATION_ERROR = "navigationError";

    /** The Constant VECHILES. */
    public static final String VECHILES = "vechiles";

    /** The Constant NEW_VECHILE. */
    public static final String NEW_VECHILE = "vechile/add";

    /** The Constant VECHILE. */
    public static final String VECHILE = "vechile";
    
    /** The Constant REPORT_EVENT. */
    public static final String REPORT_EVENT= "reportEvent";
    
    /** The Constant REPORTS. */
    public static final String REPORTS="reports";
    
    /** The Constant REPORT. */
    public static final String REPORT="report";
    
    /** The Constant TASKS. */
    public static final String TASKS="tasks";
    

    /**
     * Gets the page title key.
     *
     * @param path the path
     * @return the page title key
     */
    public static final String getPageTitleKey(final String path) {
      return new StringBuilder("page.").append(path).append(".title").toString();
    }
  }

  /**
   * The Enum StyleConstants.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 2:12:30 PM Aug 7, 2021
   */
  @Getter
  @RequiredArgsConstructor
  public enum StyleConstants {

    /** The fire gradient. */
    FIRE_GRADIENT("fire-gradient"),

    /** The login content. */
    LOGIN_CONTENT("login_content"),

    /** The register content. */
    REGISTER_CONTENT("register_content"),

    /** The logo center. */
    LOGO_CENTER("logo_center"),

    /** The button blue. */
    BUTTON_BLUE("button_blue"),

    /** The width 100. */
    WIDTH_100("width_100"),

    /** The width 75. */
    WIDTH_75("width_75"),

    /** The width 25. */
    WIDTH_25("width_25"),

    /** The width 50. */
    WIDTH_50("width_50"),
    
    /** The width 33. */
    WIDTH_33("width_33"),
    
    /** The theme primary error. */
    THEME_PRIMARY_ERROR("primary error"),

    /** The theme primary success. */
    THEME_PRIMARY_SUCCESS("primary success"),

    /** The color red. */
    COLOR_RED("red_text"),
    

    /** The new notification. */
    NEW_NOTIFICATION("new-notification"),

    /** The notification content. */
    NOTIFICATION_CONTENT("notification-content"),

    /** The notification icon. */
    NOTIFICATION_ICON("notification-icon"),

    /** The document access item. */
    DOCUMENT_ACCESS_ITEM("document-access-item"),

    /** The notification item. */
    NOTIFICATION_ITEM("notification-item"),

    /** The notification items. */
    NOTIFICATION_ITEMS("notification-items"),

    /** The notification item content. */
    NOTIFICATION_ITEM_CONTENT("notification-item-content"),

    /** The notification number. */
    NOTIFICATION_NUMBER("notification-number"),

    /** The notification title. */
    NOTIFICATION_TITLE("notification-title"),
    
    ORGANIZATION_ICON("organization-icon");
    
    

    /** The name. */
    private final String name;
  }

  /**
	 * The Enum SubscriberScope.
	 *
	 * @author Igor Lončarić (iloncari2@tvz.hr)
	 * @since 3:51:03 PM Aug 20, 2021
	 */
  public enum SubscriberScope {
    /** The all. */
    ALL,
    /** The push. */
    PUSH,
    /** The session. */
    SESSION
  }

  /**
   * The Class ThemeAttribute.
   *
   * @author Igor Lončarić (iloncari2@optimit.hr)
   * @since 4:18:17 PM Aug 7, 2021
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public final class ThemeAttribute {

    /** The Constant BUTTON_BLUE. */
    public static final String BUTTON_BLUE = "button blue";
    
    /** The Constant HEADER_ITEMS. */
    public static final String HEADER_ITEMS = "header-items";
    
    /** The Constant ICON. */
    public static final String ICON = "icon";
    
    /** The Constant BUTTON_ICON. */
    public static final String BUTTON_ICON = "button-icon";
  }

  /**
	 * Instantiates a new user role.
	 *
	 * @author Igor Lončarić (iloncari2@tvz.hr)
	 * @since 1:38:32 PM Aug 22, 2021
	 */
  @RequiredArgsConstructor
  public enum UserRole {

    /** The manager. */
    MANAGER("manager"),

    /** The spectator. */
    SPECTATOR("spectator");

  
    /** The name. */
    @Getter
    private final String name;
  }

  /**
   * The Enum VechileCondition.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 8:49:31 PM Aug 7, 2021
   */
  @RequiredArgsConstructor
  public enum VechileCondition {

    /** The not usable. */
    NOT_USABLE("notUsable"),

    /** The usable. */
    USABLE("usable");

    /** The name. */
    private final String name;

    /**
     * Gets the label key.
     *
     * @return the label key
     */
    public String getLabelKey() {
      return "vechileCondition.".concat(name).concat(".label");
    }

  }
  
  
  /**
   * The Enum VechileType.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 6:38:17 PM Aug 29, 2021
   */
  @RequiredArgsConstructor
  public enum VechileType {

    /** The extinguishing and rescuing. */
    EXTINGUISHING_AND_RESCUING("extinguishingAndRescuing"),

    /** The heights rescuing. */
    HEIGHTS_RESCUING("rescuingFromHeight"),

    /** The technical and special equipment. */
    TECHNICAL_AND_SPECIAL_EQUIPMENT("technicalAndSpecialEquipment"),

    /** The ambulance. */
    AMBULANCE("ambulance"),

    /** The danngerous substances equipment. */
    DANNGEROUS_SUBSTANCES_EQUIPMENT("dangerousSubstancesEquipment"),

    /** The commanding. */
    COMMANDING("commanding"),

    /** The transfer firefighters. */
    TRANSFER_FIREFIGHTERS("transferFirefighters"),

    /** The supply. */
    SUPPLY("supply"),

    /** The special. */
    SPECIAL("special"),

    /** The drones. */
    DRONES("drones"),

    /** The vessels. */
    VESSELS("vessels");

    /** The name. */
    private final String name;

    /**
     * Gets the label key.
     *
     * @return the label key
     */
    public String getLabelKey() {
      return "vechileType.".concat(name).concat(".label");
    }
  }
}
