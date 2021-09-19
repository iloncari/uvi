/*
 * PersonOrganizationRepository PersonOrganizationRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface PersonOrganizationRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:41:15 PM Sep 19, 2021
 */
@Repository
public interface PersonOrganizationRepository extends JpaRepository<PersonOrganization, Long> {

  /**
   * Find by join date is null.
   *
   * @return the list
   */
  List<PersonOrganization> findByJoinDateIsNull();

  /**
   * Find by organization id and join date is null.
   *
   * @param id the id
   * @return the list
   */
  List<PersonOrganization> findByOrganizationIdAndJoinDateIsNull(Long id);

  /**
   * Find by request date is null.
   *
   * @return the list
   */
  List<PersonOrganization> findByRequestDateIsNull();

}
