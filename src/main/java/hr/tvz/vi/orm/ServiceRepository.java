/*
 * ServiceRepository ServiceRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface ServiceRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:44:19 PM Sep 19, 2021
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

}
