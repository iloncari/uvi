/*
 * FuelConsuption FuelConsuption.java.
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
 * The Class FuelConsuption.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:37:47 PM Sep 19, 2021
 */
@Data
@Entity
@Table
public class FuelConsuption {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The filling date. */
  private LocalDate fillingDate;

  /** The fuel amount. */
  private int fuelAmount;

  /** The price. */
  private Long price;

  /** The fuel vechile. */
  @ManyToOne
  @ToString.Exclude
  @JoinColumn(name = "vechileId")
  private Vechile fuelVechile;
}
