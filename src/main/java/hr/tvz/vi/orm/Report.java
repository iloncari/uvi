/*
 * Report Report.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
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

@Data
@Entity
@Table
@FieldNameConstants
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private boolean locked;
  
  @OneToOne
  private Person lockOwner;
  
  @Searchable
  public String identificationNumber;
  
  public LocalDateTime eventDateTime;
  
  @Enumerated(EnumType.STRING)
  public EventType eventType;
  
  @OneToOne
  private Address eventAddress;
  
  @Searchable
  public String reporter;
  
  private String eventDescription;
  
  @ToString.Exclude
  @OneToMany(mappedBy = "report", fetch = FetchType.EAGER)
  public Set<EventOrganization> eventOrganizationList;

  private String title;

  @Enumerated(EnumType.STRING)
  public ReportStatus status;


  private String creatorId;
  
  @Enumerated(EnumType.STRING)
  private EventCause eventCause;
  
  @Enumerated(EnumType.STRING)
  private EventCausePerson eventCausePerson;
  
  @Column
  @Enumerated
  @ElementCollection(targetClass = EventActivity.class, fetch = FetchType.EAGER)
  private Set<EventActivity> eventActivities;
  
  @Column
  @Enumerated
  @ElementCollection(targetClass = ItemOnFire.class, fetch = FetchType.EAGER)
  private Set<ItemOnFire> itemsOnFire;
  
  @Enumerated(EnumType.STRING)
  public FireSize fireSize;
  
  private Boolean explosion;
  
  private Boolean fireRepeated;
  
  @Enumerated(EnumType.STRING)
  private BuildingType buildingType;
 
  @Enumerated(EnumType.STRING)
  private BuildingStatus buildingStatus;
  
  private Integer height;
  
  private Integer floor;
 
  
  @Enumerated(EnumType.STRING)
  private IndustrialPlantType industrialPlantType;
  
  @Enumerated(EnumType.STRING)
  private OpenSpaceFireType openSpaceFireType;
  
  private Double width;
  
  private Double lenght;
  
  private Integer numberOfVechiles;
  
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
