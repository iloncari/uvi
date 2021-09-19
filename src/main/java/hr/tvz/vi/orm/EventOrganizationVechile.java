/*
 * EventOrganizationVechile EventOrganizationVechile.java.
 * 
 */
package hr.tvz.vi.orm;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 * The Class EventOrganizationVechile.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:36:50 PM Sep 19, 2021
 */
@Data
@Entity
@Table
public class EventOrganizationVechile {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The base departure date time. */
  private LocalDateTime baseDepartureDateTime;

  /** The field arrived date time. */
  private LocalDateTime fieldArrivedDateTime;

  /** The field departure date time. */
  private LocalDateTime fieldDepartureDateTime;

  /** The base arrived date time. */
  private LocalDateTime baseArrivedDateTime;

  /** The distance. */
  private int distance;

  /** The people number. */
  private int peopleNumber;

  /** The event organization id. */
  private Long eventOrganizationId;

  /** The vechile. */
  @OneToOne
  private Vechile vechile;

}
