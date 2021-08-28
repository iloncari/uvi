/*
 * EventOrganizationPersonRepository EventOrganizationPersonRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The Interface EventOrganizationPersonRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:04:07 AM Aug 25, 2021
 */
@Repository
public interface EventOrganizationPersonRepository extends JpaRepository<EventOrganizationPerson, Long> {
  
  /**
   * Find by event organization id.
   *
   * @param eventOrganizationId the event organization id
   * @return the list
   */
  List<EventOrganizationPerson> findByEventOrganizationId(Long eventOrganizationId);


}
