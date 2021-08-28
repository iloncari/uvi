/*
 * Task Task.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.orm;

import java.time.LocalDateTime;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import hr.tvz.vi.util.Constants.EventActivity;
import hr.tvz.vi.util.Constants.TaskType;
import lombok.Data;

/**
 * Instantiates a new task.
 */
@Data
@Entity
@Table
public class Task {
  
  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The name. */
  private String name;
  
  /** The type. */
  @Enumerated(EnumType.STRING)
  private TaskType type;
  
  /** The assignee. */
  @OneToOne
  private Person assignee;
  
  /** The report id. */
  private Long reportId;
  
  /** The organization assignee. */
  @OneToOne
  private Organization organizationAssignee;
  
  /** The message. */
  private String message;
  
  /** The creation date time. */
  private LocalDateTime creationDateTime;
  
  /** The execution date time. */
  private LocalDateTime executionDateTime;

}
