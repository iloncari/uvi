/*
 * Organization Organization.java.
 * 
 */

package hr.tvz.vi.orm;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import hr.tvz.vi.util.Constants.OrganizationLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class Organization.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 3:54:12 PM Aug 20, 2021
 */
@Data
@Entity
@Table
@EqualsAndHashCode(exclude = "childs")
public class Organization {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The name. */
  private String name;

  /** The identification number. */
  private String identificationNumber;

  /** The establishment date. */
  private LocalDate establishmentDate;

  /** The iban. */
  private String iban;
  
  @OneToOne
  private Address address;

  /** The email. */
  private String email;

  /** The url. */
  private String url;

  /** The contact number. */
  private String contactNumber;
  
  /** The level. */
  @Enumerated(EnumType.STRING)
  private OrganizationLevel level;

  /** The parent id. */
  @Column(name = "parent_id", insertable = false, updatable = false)
  private Integer parentId;

  /** The childs. */
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id")
  private Set<Organization> childs;
  


}
