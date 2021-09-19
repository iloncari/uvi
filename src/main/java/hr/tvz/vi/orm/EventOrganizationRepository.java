/*
 * EventOrganizationRepository EventOrganizationRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface EventOrganizationRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 3:08:53 PM Aug 22, 2021
 */
@Repository
public interface EventOrganizationRepository extends JpaRepository<EventOrganization, Long> {


}
