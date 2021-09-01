/*
 * VechileRegistrationReminder VechileRegistrationReminder.java.
 * 
 */
package hr.tvz.vi.orm;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * The Class VechileRegistrationReminder.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:57:56 PM Sep 1, 2021
 */
@Table
@Entity
@Data
public class VechileRegistrationReminder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private LocalDate checkedDate;
  
  private Long organizationId;
}
