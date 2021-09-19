/*
 * EventDescription EventDescription.java.
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
 * The Class EventDescription.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 1:55:15 PM Aug 28, 2021
 */
@Data
@Entity
@Table
public class EventDescription {
  
  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /** The report id. */
  private Long reportId;

  /** The person. */
  @OneToOne
  private Person person;
  
  /** The organization. */
  @OneToOne
  private Organization organization;
  
  /** The description. */
  private String description;

}
