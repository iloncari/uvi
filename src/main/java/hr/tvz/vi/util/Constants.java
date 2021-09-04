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
import java.util.Arrays;
import java.util.Optional;

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

/**
 * Instantiates a new constants.
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
  * The Enum FieldType.
  *
  * @author Igor LonÄ�ariÄ‡ (iloncari2@tvz.hr)
  * @since 9:20:15 AM Sep 2, 2021
  */
 public enum FieldType {
    
    /** The string. */
    STRING,
    
    /** The gender. */
    GENDER,
    
    /** The profession. */
    PROFESSION,
    
    /** The number. */
    NUMBER,
    
    /** The vehicle type. */
    VEHICLE_TYPE,
    
    /** The vehicle condition. */
    VEHICLE_CONDITION,
    
    /** The organization. */
    ORGANIZATION,
    
    /** The report status. */
    REPORT_STATUS,
    
    /** The city. */
    CITY,
    
    /** The event type. */
    EVENT_TYPE,
    
    /** The date. */
    DATE;
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
    
    /** The group. */
    GROUP,
    
    /** The task. */
    TASK,
    
    /** The vechile. */
    VECHILE;
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
	    @Getter
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
	  
	  /**
     * Gets the event type.
     *
     * @param eventTypeName the event type name
     * @return the event type
     */
  	public static EventType getEventType(String eventTypeName) {
      Optional<EventType> eventTypeOpt = Arrays.asList(values()).stream().filter(eventType -> eventType.name.equals(eventTypeName)).findFirst();
      return eventTypeOpt.isPresent() ? eventTypeOpt.get() : null;
    }
	  
	 }
 	
 	/**
    * The Enum BuildingType.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 6:15:14 PM Aug 26, 2021
    */
 	
	 /**
    * Instantiates a new building type.
    *
    * @param name the name
    */
	 @RequiredArgsConstructor
  public enum BuildingType {
   
	   /** The hall. */
   	HALL("hall"),
	   
	   /** The two four floors building. */
   	TWO_FOUR_FLOORS_BUILDING("twoFourFloorBuilding"),
	   
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
 	
	 /**
    * Instantiates a new building status.
    *
    * @param name the name
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
	 
 	/**
   * Instantiates a new traffic fire vechile type.
   *
   * @param name the name
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
   
   /**
    * Instantiates a new industrial plant type.
    *
    * @param name the name
    */
   @RequiredArgsConstructor
   public enum IndustrialPlantType{
    
    /** The production plant. */
    PRODUCTION_PLANT("productionPlant"),
    
    /** The storage space. */
    STORAGE_SPACE("storageSpace"),
    
    /** The administrative building. */
    ADMINISTRATIVE_BUILDING("administrativeBuilding");
    
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
   
   /**
    * Instantiates a new open space fire type.
    *
    * @param name the name
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
  
  /**
   * Instantiates a new event activity.
   *
   * @param name the name
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
  
  /**
   * Instantiates a new fire size.
   *
   * @param name the name
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
  
  /**
   * Instantiates a new item on fire.
   *
   * @param name the name
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
	
	/**
   * Instantiates a new event cause.
   *
   * @param name the name
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
 	
	 /**
    * Instantiates a new event cause person.
    *
    * @param name the name
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
  
  /**
   * Instantiates a new duty.
   *
   * @param name the name
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
    
    /**
     * Gets the name.
     *
     * @return the name
     */
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
   * The Interface Searchable.
   *
   * @author Igor LonÄ�ariÄ‡ (iloncari2@tvz.hr)
   * @since 7:09:10 PM Sep 1, 2021
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  public @interface Searchable {
    
  }

  /**
   * The Enum Gender.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 5:41:51 PM Aug 12, 2021
   */
  
  /**
   * Gets the name.
   *
   * @return the name
   */
  @Getter
  
  /**
   * Instantiates a new gender.
   *
   * @param name the name
   */
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
    
    /**
     * Gets the value.
     *
     * @param genderName the gender name
     * @return the value
     */
    public static Gender getGender(String genderName) {
      Optional<Gender> genderOpt = Arrays.asList(values()).stream().filter(gender -> gender.getName().equals(genderName)).findFirst();
      return genderOpt.isPresent() ? genderOpt.get() : null;
    }
    
    /**
     * Gets the gender translation key.
     *
     * @return the gender translation key
     */
    public String getGenderTranslationKey() {
      return "gender.".concat(name).concat(".label");
    }
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
  	COUNTRY_LEVEL,
	  
	  /** The regional level. */
  	REGIONAL_LEVEL,
	  
	  /** The city level. */
  	CITY_LEVEL,
	  
  	/** The operational level. */
	  OPERATIONAL_LEVEL;
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
    
    /**
     * Gets the professions.
     *
     * @param professionsName the professions name
     * @return the professions
     */
    public static Professions getProfession(String professionsName) {
      Optional<Professions> professionOpt = Arrays.asList(values()).stream().filter(profession -> profession.getName().equals(professionsName)).findFirst();
      return professionOpt.isPresent() ? professionOpt.get() : null;
    }
  }

  /**
    * The Enum ReportStatus.
    *
    * @author Igor Lončarić (iloncari2@tvz.hr)
    * @since 11:49:01 AM Aug 9, 2021
    */
  
  /**
   * Gets the name.
   *
   * @return the name
   */
  @Getter
  
  /**
   * Instantiates a new report status.
   *
   * @param name the name
   */
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
    
    /**
     * Gets the event type.
     *
     * @param reportStatusName the report status name
     * @return the event type
     */
    public static ReportStatus getReportStatus(String reportStatusName) {
      Optional<ReportStatus> reportStatusOpt = Arrays.asList(values()).stream().filter(reportStatus -> reportStatus.name.equals(reportStatusName)).findFirst();
      return reportStatusOpt.isPresent() ? reportStatusOpt.get() : null;
    }
  }

  /**
   * The Class Routes.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 5:17:38 PM Aug 7, 2021
   */
  
  /**
   * Instantiates a new routes.
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
    public static final String VECHILES = "vehicles";

    /** The Constant NEW_VECHILE. */
    public static final String NEW_VECHILE = "vehicle/add";

    /** The Constant VECHILE. */
    public static final String VECHILE = "vehicle";
    
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
    
    /** The widget number. */
    WIDGET_NUMBER("widget-number"),

    /** The widget title. */
    WIDGET_TITLE("widget-title"),
    
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
    
    /** The organization icon. */
    ORGANIZATION_ICON("organization-icon"),
    
    /** The sidebar header. */
    SIDEBAR_HEADER("sidebar-header"),
    
    /** The error page. */
    ERROR_CONTENT("error-content"),
    
    /** The popup form. */
    POPUP_FORM("popup-form");
    
    

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
  
  /**
   * Instantiates a new theme attribute.
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
    
    /** The Constant CONTENT. */
    public static final String CONTENT="content";
    
    /** The Constant PAGE_TITLE. */
    public static final String PAGE_TITLE ="page-title";
    
    /** The Constant CARD. */
    public static final String CARD ="card";
    
    /** The Constant CARD_SPACING. */
    public static final String CARD_SPACING ="card-spacing";
    
    /** The Constant CARD_FULL_BLOCK. */
    public static final String  CARD_FULL_BLOCK = "card-full-block";
  
    /** The Constant SPACING. */
    public static final String  SPACING = "spacing";
    
    /** The Constant PADDING. */
    public static final String  PADDING = "padding";
    
    /** The Constant WIDGET_CARD. */
    public static final String  WIDGET_CARD = "widget-card";
    
    /** The Constant WIDGET_CARDS. */
    public static final String  WIDGET_CARDS = "widget-cards";
    
    public static final String DROPDOWN_WHITE = "dropdown white";
    
    public static final String DROPDOWN_BLUE = "dropdown blue";
    
    /** The Constant LOCKED. */
    public static final String LOCKED="locked";
    
    
    /*inherit*/
    
    /** The Constant ABOVE_TABLE_BLOCK. */
    public static final String ABOVE_TABLE_BLOCK = "above-table-block";

    /** The Constant SEARCH_TABLE_BLOCK. */
    public static final String SEARCH_TABLE_BLOCK = "search-table-block";

    /** The Constant ATTACHMENT. */
    public static final String ATTACHMENT = "attachment";

    /** The Constant BACKGROUND_TITLE. */
    public static final String BACKGROUND_TITLE = "background-title";

    /** The Constant BLUE. */
    public static final String BLUE = "blue";

    /** The Constant BROADCAST_MODAL. */
    public static final String BROADCAST_MODAL = "brodcast-modal";


    /** The Constant BUTTON_ICON_BLUE. */
    public static final String BUTTON_ICON_BLUE = "icon blue button-small-icon";

    /** The Constant BUTTON_OUTLINE_BLUE. */
    public static final String BUTTON_OUTLINE_BLUE = "button outline-blue";

    /** The Constant BUTTON_OUTLINE_GREEN. */
    public static final String BUTTON_OUTLINE_GREEN = "button outline-green";

    /** The Constant BUTTON_OUTLINE_RED. */
    public static final String BUTTON_OUTLINE_RED = "button outline-red";

    /** The Constant BUTTON_OUTLINE_VIOLET. */
    public static final String BUTTON_OUTLINE_VIOLET = "button outline-violet";

    /** The Constant BUTTON_PLUS_BLUE. */
    public static final String BUTTON_PLUS_BLUE = "button-plus blue";

    /** The Constant BUTTON_RESET. */
    public static final String BUTTON_RESET = "button-reset";

    /** The Constant BUTTON_SMALL_EGG_BLUE. */
    public static final String BUTTON_SMALL_EGG_BLUE = "button-small egg-blue";

    /** The Constant BUTTON_SMALL_OUTLINE_EGG_BLUE. */
    public static final String BUTTON_SMALL_OUTLINE_EGG_BLUE = "button-small outline-egg-blue";

    /** The Constant BUTTON_ULTRA_SMALL. */
    public static final String BUTTON_ULTRA_SMALL = "button-ultra-small";

    /** The Constant BUTTONS. */
    public static final String BUTTONS = "buttons";

    /** The Constant BUTTONS_10. */
    public static final String BUTTONS_10 = "buttons-10";

    /** The Constant BUTTONS_15. */
    public static final String BUTTONS_15 = "buttons-15";


    /** The Constant CARD_CHARTS. */
    public static final String CARD_CHARTS = "card-charts";



    /** The Constant CARD_HEADING. */
    public static final String CARD_HEADING = "card-heading";

  

    /** The Constant CODEBOOK_CONTENT. */
    public static final String CODEBOOK_CONTENT = "codebook-content";

    /** The Constant COMBO. */
    public static final String COMBO = "combo";

    /** The Constant COMPACT. */
    public static final String COMPACT = "compact";

    /** The Constant CONTAINER_PREVIEW. */
    public static final String CONTAINER_PREVIEW = "container-preview";

    /** The Constant CONTAINER_VERTICAL_TABS. */
    public static final String CONTAINER_VERTICAL_TABS = "container-vertical-tabs";



    /** The Constant CONTENT_VERTICAL_TABS. */
    public static final String CONTENT_VERTICAL_TABS = "content-vertical-tabs";

    /** The Constant DATE_PICKER. */
    public static final String DATE_PICKER = "date-picker";

    /** The Constant DISABLED. */
    public static final String DISABLED = "disabled";

    /** The Constant DOCUMENT_LIST. */
    public static final String DOCUMENT_LIST = "document-list";



    /** The Constant EMPTY_TABLE. */
    public static final String EMPTY_TABLE = "empty-table";

    /** The Constant ERROR. */
    public static final String ERROR = "error";

    /** The Constant ERROR_TEXT. */
    public static final String ERROR_TEXT = "error-text";

    /** The Constant FIXED_LABEL. */
    public static final String FIXED_LABEL = "fixed-label";

    /** The Constant FORM. */
    public static final String FORM = "form";

    /** The Constant FORM_600. */
    public static final String FORM_600 = "form-600";

    /** The Constant FORM_CENTER. */
    public static final String FORM_CENTER = "form-center";

    /** The Constant FORM_TABS. */
    public static final String FORM_TABS = "form-tabs";

    /** The Constant GREEN. */
    public static final String GREEN = "green";

    /** The Constant HALF. */
    public static final String HALF_75 = "half-75";



    /** The Constant HORIZONTAL_TABS. */
    public static final String HORIZONTAL_TABS = "horizontal-tabs";


    /** The Constant IMG_404. */
    public static final String IMG_404 = "img-404";

    /** The Constant IMG_MAINTENANCE. */
    public static final String IMG_MAINTENANCE = "img-maintenance";

    /** The Constant LIGHT_BLUE. */
    public static final String LIGHT_BLUE = "light-blue";

    /** The Constant LOGIN. */
    public static final String LOGIN = "login";

    /** The Constant LOGIN_FOOTER. */
    public static final String LOGIN_FOOTER = "login-footer";

    /** The Constant MESSAGE_ITEM. */
    public static final String MESSAGE_ITEM = "message-item";

    /** The Constant NO_BORDER. */
    public static final String NO_BORDER = "no-border";

    /** The Constant NO_SELECTED_VALUE. */
    public static final String NO_SELECTED_VALUE = "no-selected-value";


    /** The Constant PAGE_404. */
    public static final String PAGE_404 = "page-404";

    /** The Constant PAGE_MAINTENANCE. */
    public static final String PAGE_MAINTENANCE = "page-maintenance";


    /** The Constant POPUP_HEADER. */
    public static final String POPUP_HEADER = "popup-header";

    /** The Constant PURPLE. */
    public static final String PURPLE = "purple";

    /** The Constant REVERSE_CARD. */
    public static final String REVERSE_CARD = "reverse-card";

    /** The Constant RIGHT. */
    public static final String RIGHT = "right";

    /** The Constant ROW_STRIPES. */
    public static final String ROW_STRIPES = "row-stripes";

    /** The Constant SAVED_SEARCHES_ELEMENTS. */
    public static final String SAVED_SEARCHES_ELEMENTS = "saved-searches-elements";

    /** The Constant SAVED_SEARCHES_NAME. */
    public static final String SAVED_SEARCHES_NAME = "saved-searches-name";

    /** The Constant BUTTON_SAVE. */
    public static final String BUTTON_SAVE = "button-save";

    /** The Constant SEARCH_BLOCK. */
    public static final String SEARCH_BLOCK = "search-block";

    /** The Constant SEARCH_FIELD. */
    public static final String SEARCH_FIELD = "search-field";

    /** The Constant SEARCH_TAGS. */
    public static final String SEARCH_TAGS = "search-tags";

    /** The Constant SEARCH_TAGS_BLOCK. */
    public static final String SEARCH_TAGS_BLOCK = "search-tags-block";

    /** The Constant SIDEBAR_UPLOAD. */
    public static final String SIDEBAR_UPLOAD = "sidebar-upload";

    /** The Constant SMALL_TABS. */
    public static final String SMALL_TABS = "small-tabs";

    /** The Constant TAB_TABLE. */
    public static final String TAB_TABLE = "tab-table";

    /** The Constant TABLE. */
    public static final String TABLE = "table";

    /** The Constant TABLE_ICON. */
    public static final String TABLE_ICON = "table-icon";

    /** The Constant TAGS_BLOCK. */
    public static final String TAGS_BLOCK = "tags-block";

    /** The Constant TITLE_BLOCK. */
    public static final String TITLE_BLOCK = "title-block";

    /** The Constant TITLE_TAB. */
    public static final String TITLE_TAB = "title-tab";

    /** The Constant TITLE_TAB_BLUE. */
    public static final String TITLE_TAB_BLUE = "title-tab-blue";

    /** The Constant TITLE_TAB_VIOLET. */
    public static final String TITLE_TAB_VIOLET = "title-tab-violet";

    /** The Constant VERTICAL_VIOLET_TABS. */
    public static final String VERTICAL_BLUE_TABS = "vertical-blue-tabs";

    /** The Constant VERTICAL_ICONS_TABS. */
    public static final String VERTICAL_ICONS_TABS = "vertical-icons-tabs";

    /** The Constant VERTICAL_VIOLET_TABS. */
    public static final String VERTICAL_VIOLET_TABS = "vertical-violet-tabs";

    /** The Constant VIOLET_TABS. */
    public static final String VIOLET_TABS = "violet-tabs";

    /** The Constant WHITE. */
    public static final String WHITE = "white";
    
    /** The Constant AQUA. */
    public static final String AQUA = "aqua";

    /** The Constant WIDGET_UPLOAD_ELEMENT. */
    public static final String WIDGET_UPLOAD_ELEMENT = "widget-upload-element";

    /** The Constant WIDTH_100. */
    public static final String WIDTH_100 = "width-100";

    /** The Constant WIDTH_50. */
    public static final String WIDTH_50 = "width-50";

    /** The Constant WIDTH_75. */
    public static final String WIDTH_75 = "width-75";

    /** The Constant WRAP_CELL_CONTENT. */
    public static final String WRAP_CELL_CONTENT = "wrap-cell-content";

    /** The Constant CARD_VIOLET. */
    public static final String CARD_VIOLET = "card-violet";

    /** The Constant FORM_ITEM_50. */
    public static final String FORM_ITEM_50 = "form-item-50";

    /** The Constant BUTTON_NAV. */
    public static final String BUTTON_NAV = "button-nav";

    /** The Constant BUTTON_PREVIOUS. */
    public static final String BUTTON_PREVIOUS = "button-previous";

    /** The Constant BUTTON_NEXT. */
    public static final String BUTTON_NEXT = "button-next";

    /** The Constant NO_VISIBILITY. */
    public static final String NO_VISIBILITY = "no-visibility";

    /** The Constant TYPE_DROPDOWN. */
    public static final String TYPE_DROPDOWN = "type-dropdown";

    
    
    
    /*end*/

  }

  /**
	 * Instantiates a new user role.
	 *
	 * @author Igor Lončarić (iloncari2@tvz.hr)
	 * @since 1:38:32 PM Aug 22, 2021
	 */
  
  /**
   * Instantiates a new user role.
   *
   * @param name the name
   */
  @RequiredArgsConstructor
  public enum UserRole {

    /** The manager. */
    MANAGER("manager"),

    /** The spectator. */
    SPECTATOR("spectator");

  
    /** The name. */
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    @Getter
    private final String name;
  }

  /**
   * The Enum VechileCondition.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 8:49:31 PM Aug 7, 2021
   */
  
  /**
   * Instantiates a new vechile condition.
   *
   * @param name the name
   */
  @RequiredArgsConstructor
  public enum VechileCondition {

    /** The not usable. */
    NOT_USABLE("notUsable"),

    /** The usable. */
    USABLE("usable");

    /** The name. */
    @Getter
    private final String name;

    /**
     * Gets the label key.
     *
     * @return the label key
     */
    public String getLabelKey() {
      return "vechileCondition.".concat(name).concat(".label");
    }
    
    /**
     * Gets the vechile condition.
     *
     * @param vechileConditionName the vechile condition name
     * @return the vechile condition
     */
    public static VechileCondition getVechileCondition(String vechileConditionName) {
      Optional<VechileCondition> vechileConditionOpt = Arrays.asList(values()).stream().filter(vechileCondition -> vechileCondition.name.equals(vechileConditionName)).findFirst();
      return vechileConditionOpt.isPresent() ? vechileConditionOpt.get() : null;
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
    @Getter
    private final String name;

    /**
     * Gets the label key.
     *
     * @return the label key
     */
    public String getLabelKey() {
      return "vechileType.".concat(name).concat(".label");
    }
    
    /**
     * Gets the vechile type.
     *
     * @param vechileTypeName the vechile type name
     * @return the vechile type
     */
    public static VechileType getVechileType(String vechileTypeName) {
      Optional<VechileType> vechileTypeOpt = Arrays.asList(values()).stream().filter(vechileType -> vechileType.name.equals(vechileTypeName)).findFirst();
      return vechileTypeOpt.isPresent() ? vechileTypeOpt.get() : null;
    }
  }
}
