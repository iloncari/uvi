/*
 * EventDescriptionRepository EventDescriptionRepository.java.
 * 
 */
package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface EventDescriptionRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 1:57:37 PM Aug 28, 2021
 */
@Repository
public interface EventDescriptionRepository extends JpaRepository<EventDescription, Long> {
  
  /**
   * Find by event organization id.
   *
   * @param reportId the report id
   * @return the list
   */
  List<EventDescription> findByReportId(Long reportId);


}
