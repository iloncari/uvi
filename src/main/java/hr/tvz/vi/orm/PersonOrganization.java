/*
 * PersonOrganization PersonOrganization.java.
 * 
 */

package hr.tvz.vi.orm;

import hr.tvz.vi.util.Constants.Duty;
import hr.tvz.vi.util.Constants.UserRole;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * The Class PersonOrganization.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:40:38 PM Sep 19, 2021
 */
@Data
@Entity
@Table
@EqualsAndHashCode(exclude = "person")
public class PersonOrganization {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The organization. */
  @JoinColumn
  @OneToOne
  private Organization organization;

  /** The request date. */
  private LocalDate requestDate;

  /** The join date. */
  private LocalDate joinDate;

  /** The exit date. */
  private LocalDate exitDate;

  /** The app rights. */
  private boolean appRights;

  /** The duty. */
  @Enumerated(EnumType.STRING)
  private Duty duty;

  /** The role. */
  @Enumerated(EnumType.STRING)
  private UserRole role;

  /** The person. */
  @ManyToOne
  @ToString.Exclude
  @JoinColumn(name = "personId")
  private Person person;
}
