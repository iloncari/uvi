/*
 * VechileReminderRepository VechileReminderRepository.java.
 * 
 */
package hr.tvz.vi.orm;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface VechileReminderRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 7:00:11 PM Sep 1, 2021
 */
public interface VechileReminderRepository extends JpaRepository<VechileRegistrationReminder, Long> {
  
  /**
   * Find by organization id and checked date.
   *
   * @param organizationId the organization id
   * @param checkedDate the checked date
   * @return the vechile registration reminder
   */
  VechileRegistrationReminder findByOrganizationIdAndCheckedDate(Long organizationId, LocalDate checkedDate);
}
