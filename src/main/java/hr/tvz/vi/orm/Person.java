/*
 * User User.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.orm;

import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.Searchable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Entity
@Table
@FieldNameConstants
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Searchable
  public String name;

  @Searchable
  public String lastname;

  @Searchable
  public LocalDate birthDate;

  @Searchable
  public String identificationNumber;

  public String email;

  private String hashedPassword;
  
  private int passwordLength;

  private String username;
  
  private Long lastActiveOrganizationId;

  @Enumerated(EnumType.STRING)
  public Gender gender;

  @Searchable
  public String phoneNumber;

  @OneToOne
  private Address residenceAddress;

  @Enumerated(EnumType.STRING)
  public Professions profession;

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
