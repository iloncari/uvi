/*
 * Constants Constants.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
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

/**
 * Instantiates a new constants.
 */

/**
 * Instantiates a new constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	 /**
	 * Instantiates a new event type.
	 *
	 * @param name the name
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
   * Subscribe to push or session events. Defaults to session scope.
   *
   * @author Goran Petanjek (goran.petanjek@optimit.hr)
   * @since 13:32:01 16. tra 2020.
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
  
  /**
	 * Gets the name.
	 *
	 * @return the name
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
  }

  /**
   * The Enum ImageConstants.
   *
   * @author Igor Lončarić (iloncari2@optimit.hr)
   * @since 3:12:45 PM Aug 7, 2021
   */
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  @Getter
  
  /**
	 * Instantiates a new image constants.
	 *
	 * @param name the name
	 */
  
  /**
	 * Instantiates a new image constants.
	 *
	 * @param name the name
	 */
  @RequiredArgsConstructor
  public enum ImageConstants {

    /** The app logo. */
    APP_LOGO("app_logo.png");

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
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  @Getter
  
  /**
	 * Instantiates a new organization level.
	 *
	 * @param name the name
	 */
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
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  @Getter
  
  /**
	 * Instantiates a new professions.
	 *
	 * @param name the name
	 */
  
  /**
	 * Instantiates a new professions.
	 *
	 * @param name the name
	 */
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
  
  /**
	 * Gets the name.
	 *
	 * @return the name
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
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  
  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
  @Getter
  
  /**
	 * Instantiates a new style constants.
	 *
	 * @param name the name
	 */
  
  /**
	 * Instantiates a new style constants.
	 *
	 * @param name the name
	 */
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
    COLOR_RED("red_text");

    /** The name. */
    private final String name;
  }

  /**
	 * The Enum SubscriberScope.
	 *
	 * @author Igor LonÄ�ariÄ‡ (iloncari2@tvz.hr)
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
  
  /**
	 * Instantiates a new theme attribute.
	 */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public final class ThemeAttribute {

    /** The Constant BUTTON_BLUE. */
    public static final String BUTTON_BLUE = "button blue";
  }

  /**
	 * Instantiates a new user role.
	 *
	 * @author Igor LonÄ�ariÄ‡ (iloncari2@tvz.hr)
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

    /**
	 * Gets the name.
	 *
	 * @return the name
	 */
    
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

  /**
   * The Enum VechileType.
   *
   * @author Igor Lončarić (iloncari2@tvz.hr)
   * @since 9:19:37 PM Aug 16, 2021
   */
  
  /**
	 * Instantiates a new vechile type.
	 *
	 * @param name the name
	 */
  
  /**
	 * Instantiates a new vechile type.
	 *
	 * @param name the name
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
