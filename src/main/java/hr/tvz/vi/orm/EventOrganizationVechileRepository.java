/*
 * EventOrganizationVechileRepository EventOrganizationVechileRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The Interface EventOrganizationVechileRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:04:07 AM Aug 25, 2021
 */
@Repository
public interface EventOrganizationVechileRepository extends JpaRepository<EventOrganizationVechile, Long> {
  
  /**
   * Find by event organization id.
   *
   * @param eventOrganizationId the event organization id
   * @return the list
   */
  List<EventOrganizationVechile> findByEventOrganizationId(Long eventOrganizationId);


}
