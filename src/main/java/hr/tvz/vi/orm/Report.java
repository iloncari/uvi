/*
 * Report Report.java.
 * 
 */

package hr.tvz.vi.orm;

import hr.tvz.vi.util.Constants.BuildingStatus;
import hr.tvz.vi.util.Constants.BuildingType;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.EventActivity;
import hr.tvz.vi.util.Constants.EventCause;
import hr.tvz.vi.util.Constants.EventCausePerson;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.FireSize;
import hr.tvz.vi.util.Constants.IndustrialPlantType;
import hr.tvz.vi.util.Constants.ItemOnFire;
import hr.tvz.vi.util.Constants.OpenSpaceFireType;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.Searchable;
import hr.tvz.vi.util.Constants.TrafficFireVechileType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

/**
 * The Class Report.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:41:46 PM Sep 19, 2021
 */
@Data
@Entity
@Table
@FieldNameConstants
public class Report {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /** The locked. */
  private boolean locked;
  
  /** The lock owner. */
  @OneToOne
  private Person lockOwner;
  
  /** The identification number. */
  @Searchable
  public String identificationNumber;
  
  /** The event date time. */
  public LocalDateTime eventDateTime;
  
  /** The event type. */
  @Enumerated(EnumType.STRING)
  public EventType eventType;
  
  /** The event address. */
  @OneToOne
  private Address eventAddress;
  
  /** The reporter. */
  @Searchable
  public String reporter;
  
  /** The event description. */
  private String eventDescription;
  
  /** The event organization list. */
  @ToString.Exclude
  @OneToMany(mappedBy = "report", fetch = FetchType.EAGER)
  public Set<EventOrganization> eventOrganizationList;

  /** The title. */
  private String title;

  /** The status. */
  @Enumerated(EnumType.STRING)
  public ReportStatus status;

  /** The creator id. */
  private String creatorId;
  
  /** The event cause. */
  @Enumerated(EnumType.STRING)
  private EventCause eventCause;
  
  /** The event cause person. */
  @Enumerated(EnumType.STRING)
  private EventCausePerson eventCausePerson;
  
  /** The event activities. */
  @Column
  @Enumerated
  @ElementCollection(targetClass = EventActivity.class, fetch = FetchType.EAGER)
  private Set<EventActivity> eventActivities;
  
  /** The items on fire. */
  @Column
  @Enumerated
  @ElementCollection(targetClass = ItemOnFire.class, fetch = FetchType.EAGER)
  private Set<ItemOnFire> itemsOnFire;
  
  /** The fire size. */
  @Enumerated(EnumType.STRING)
  public FireSize fireSize;
  
  /** The explosion. */
  private Boolean explosion;
  
  /** The fire repeated. */
  private Boolean fireRepeated;
  
  /** The building type. */
  @Enumerated(EnumType.STRING)
  private BuildingType buildingType;
 
  /** The building status. */
  @Enumerated(EnumType.STRING)
  private BuildingStatus buildingStatus;
  
  /** The height. */
  private Integer height;
  
  /** The floor. */
  private Integer floor;
 
  /** The industrial plant type. */
  @Enumerated(EnumType.STRING)
  private IndustrialPlantType industrialPlantType;
  
  /** The open space fire type. */
  @Enumerated(EnumType.STRING)
  private OpenSpaceFireType openSpaceFireType;
  
  /** The width. */
  private Double width;
  
  /** The lenght. */
  private Double lenght;
  
  /** The number of vechiles. */
  private Integer numberOfVechiles;
  
  /** The traffic fire vechile type. */
  @Enumerated(EnumType.STRING)
  private TrafficFireVechileType trafficFireVechileType;

  /**
	 * Instantiates a new report.
	 */
  public Report() {
	  eventDateTime = LocalDateTime.now();
  }
 
  /**
	 * Gets the event address.
	 *
	 * @return the event address
	 */
  public Address getEventAddress() {
	  if(eventAddress==null) {
		  eventAddress = new Address();
	  }
	  return eventAddress;
  }
  
}
