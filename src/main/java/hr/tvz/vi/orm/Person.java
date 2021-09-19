/*
 * Person Person.java.
 * 
 */

package hr.tvz.vi.orm;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.Searchable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

/**
 * The Class Person.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:39:48 PM Sep 19, 2021
 */
@Data
@Entity
@Table
@FieldNameConstants
public class Person {
  
  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The name. */
  @Searchable
  public String name;

  /** The lastname. */
  @Searchable
  public String lastname;

  /** The birth date. */
  @Searchable
  public LocalDate birthDate;

  /** The identification number. */
  @Searchable
  public String identificationNumber;

  /** The email. */
  public String email;

  /** The hashed password. */
  private String hashedPassword;
  
  /** The password length. */
  private int passwordLength;

  /** The username. */
  private String username;
  
  /** The last active organization id. */
  private Long lastActiveOrganizationId;

  /** The gender. */
  @Enumerated(EnumType.STRING)
  public Gender gender;

  /** The phone number. */
  @Searchable
  public String phoneNumber;

  /** The residence address. */
  @OneToOne
  private Address residenceAddress;

  /** The profession. */
  @Enumerated(EnumType.STRING)
  public Professions profession;

  /** The org list. */
  @ToString.Exclude
  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
  private Set<PersonOrganization> orgList;
  
  /**
	 * Gets the residence address.
	 *
	 * @return the residence address
	 */
  public Address getResidenceAddress() {
	  if(residenceAddress==null) {
		  residenceAddress = new Address();
	  }
	  return residenceAddress;
  }
  
 

}
