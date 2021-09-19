/*
 * PersonRepository PersonRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface PersonRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:41:32 PM Sep 19, 2021
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

  /**
   * Find by identification number.
   *
   * @param identificationNumber the identification number
   * @return the person
   */
  Person findByIdentificationNumber(String identificationNumber);

  /**
   * Find by org list organization id.
   *
   * @param organizationId the organization id
   * @return the list
   */
  List<Person> findByOrgList_ExitDateIsNullAndOrgList_OrganizationId(Long organizationId);
  
  /**
   * Count by org list exit date is null and org list organization id.
   *
   * @param organizationId the organization id
   * @return the long
   */
  Long countByOrgList_ExitDateIsNullAndOrgList_OrganizationId(Long organizationId);

  /**
   * Find by username.
   *
   * @param username the username
   * @return the person
   */
  Optional<Person> findByUsername(String username);

  /**
   * Find by username is not null and org list organization id in.
   *
   * @param childIds the child ids
   * @return the list
   */
  List<Person> findByUsernameIsNotNullAndOrgList_ExitDateIsNullAndOrgList_JoinDateIsNotNullAndOrgList_AppRightsTrueAndOrgList_OrganizationIdIn(
    List<Long> childIds);

}
