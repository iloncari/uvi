/*
 * Service Service.java.
 * 
 */

package hr.tvz.vi.orm;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 * Instantiates a new service.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:44:04 PM Sep 19, 2021
 */
@Data
@Entity
@Table
public class Service {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The service date. */
  private LocalDate serviceDate;

  /** The service name. */
  private String serviceName;

  /** The service description. */
  private String serviceDescription;

  /** The price. */
  private Double price;

  /** The service vechile. */
  @ManyToOne
  @ToString.Exclude
  @JoinColumn(name = "vechileId")
  private Vechile serviceVechile;

}
