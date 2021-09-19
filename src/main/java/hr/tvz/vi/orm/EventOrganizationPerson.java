/*
 * EventOrganizationPerson EventOrganizationPerson.java.
 * 
 */
package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * The Class EventOrganizationPerson.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:36:14 PM Sep 19, 2021
 */
@Data
@Entity
@Table
public class EventOrganizationPerson {
  
  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /** The event organization id. */
  private Long eventOrganizationId;
  
  /** The vechile. */
  @OneToOne
  private Vechile vechile;
  
  /** The person. */
  @OneToOne
  private Person person;
  
  /** The time in seconds. */
  long timeInSeconds;
}
