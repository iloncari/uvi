/*
 * Vechile Vechile.java.
 * 
 */

package hr.tvz.vi.orm;

import hr.tvz.vi.util.Constants.Searchable;
import hr.tvz.vi.util.Constants.VechileCondition;
import hr.tvz.vi.util.Constants.VechileType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

/**
 * The Class Vechile.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:45:08 PM Sep 19, 2021
 */
@Data
@Entity
@Table
@FieldNameConstants
@EqualsAndHashCode(exclude = "services")
public class Vechile {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The make. */
  @Searchable
  public String make;
  
  /** The model. */
  @Searchable
  public String model;

  /** The model year. */
  private int modelYear;

  /** The licence plate number. */
  @Searchable
  public String licencePlateNumber;

  /** The registration valid until. */
  private LocalDate registrationValidUntil;

  /** The vechile number. */
  @Searchable
  public String vechileNumber;

  /** The first registration date. */
  private LocalDate firstRegistrationDate;

  /** The description. */
  private String description;

  /** The condition. */
  @Enumerated(EnumType.STRING)
  public VechileCondition condition;

  /** The type. */
  @Enumerated(EnumType.STRING)
  public VechileType type;

  /** The organization. */
  @ManyToOne
  private Organization organization;
  
  /** The active. */
  private Boolean active;

  /** The services. */
  @ToString.Exclude
  @OneToMany(mappedBy = "serviceVechile", fetch = FetchType.EAGER)
  private Set<Service> services;

  /** The fuel consuptions. */
  @ToString.Exclude
  @OneToMany(mappedBy = "fuelVechile", fetch = FetchType.LAZY)
  private List<FuelConsuption> fuelConsuptions;
}
