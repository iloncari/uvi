/*
 * Notification Notification.java.
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
import javax.persistence.Table;

import hr.tvz.vi.util.Constants.NotificationType;
import lombok.Data;

/**
 * The Class Notification.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:26:44 PM Aug 29, 2021
 */
@Data
@Entity
@Table
public class Notification {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /** The creation date time. */
  private LocalDateTime creationDateTime;
  
  //izbrisi
  private LocalDateTime readDateTime;
  
  /** The title. */
  private String title;
  
  /** The message. */
  private String message;
  
  private Long sourceId;
  
  /** The type. */
  @Enumerated(EnumType.STRING)
  private NotificationType type;
  
  /** The recipient id. */
  private Long recipientId;
  
  /** The organization id. */
  private Long organizationId;
}
