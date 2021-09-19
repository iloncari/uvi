/*
 * VechileRepository VechileRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface VechileRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:46:07 PM Sep 19, 2021
 */
public interface VechileRepository extends JpaRepository<Vechile, Long> {

  /**
   * Find by organization id and active true.
   *
   * @param organizationId the organization id
   * @return the list
   */
  List<Vechile> findByOrganizationIdAndActiveTrue(Long organizationId);

}
