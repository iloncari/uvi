/*
 * Report Report.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.orm;

import hr.tvz.vi.util.Constants.EventCause;
import hr.tvz.vi.util.Constants.EventCausePerson;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.ReportStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String identificationNumber;
  
  private LocalDateTime eventDateTime;
  
  @Enumerated(EnumType.STRING)
  EventType eventType;
  
  @OneToOne
  private Address eventAddress;
  
  private String reporter;
  
  private String eventDescription;
  
  @ToString.Exclude
  @OneToMany(mappedBy = "report", fetch = FetchType.EAGER)
  private Set<EventOrganization> eventOrganizationList;

  private String title;

  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  private LocalDate creationDate;

  private LocalDate updateDate;

  private String creatorId;
  
  @Enumerated(EnumType.STRING)
  private EventCause eventCause;
  
  @Enumerated(EnumType.STRING)
  private EventCausePerson eventCausePerson;

  @ToString.Exclude
  @OneToMany(mappedBy = "report", fetch = FetchType.EAGER)
  private List<hr.tvz.vi.orm.Task> tasks;
  
  

  
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
