/*
 * Task Task.java.
 * 
 */

package hr.tvz.vi.orm;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.boot.devtools.restart.FailureHandler.Outcome;

import hr.tvz.vi.util.Constants.TaskOutcome;
import hr.tvz.vi.util.Constants.TaskType;
import lombok.Data;

/**
 * Instantiates a new task.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:44:38 PM Sep 19, 2021
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
  public String name;
  
  /** The type. */
  @Enumerated(EnumType.STRING)
  public TaskType type;
  
  /** The outcome. */
  @Enumerated(EnumType.STRING)
  public TaskOutcome outcome;
  
  /** The outcome message. */
  public String outcomeMessage;
  
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
