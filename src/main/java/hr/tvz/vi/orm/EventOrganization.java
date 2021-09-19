/*
 * EventOrganization EventOrganization.java.
 * 
 */
package hr.tvz.vi.orm;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The Class EventOrganization.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:35:47 PM Sep 19, 2021
 */
@Data
@Entity
@Table
@EqualsAndHashCode(exclude = "report")
public class EventOrganization {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The organization. */
  @JoinColumn
  @OneToOne
  private Organization organization;

  /** The report. */
  @ManyToOne
  @ToString.Exclude
  @JoinColumn(name = "reportId")
  private Report report;

  /** The alarmed date time. */
  private LocalDateTime alarmedDateTime;

  /** The work finished date time. */
  private LocalDateTime workFinishedDateTime;

  /** The intervention finished date time. */
  private LocalDateTime interventionFinishedDateTime;
}
