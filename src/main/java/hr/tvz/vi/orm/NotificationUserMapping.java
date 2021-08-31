/*
 * NotificationUserMapping NotificationUserMapping.java.
 * 
 */
package hr.tvz.vi.orm;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import hr.tvz.vi.util.Constants.NotificationType;
import lombok.Data;

/**
 * The Class NotificationUserMapping.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 10:00:41 PM Aug 29, 2021
 */
@Data
@Entity
@Table
public class NotificationUserMapping {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /** The read at. */
  private LocalDateTime readAt;
  
  /** The user id. */
  private Long userId;
  
  /** The notification id. */
  private Long notificationId;
  
}
