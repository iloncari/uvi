/*
 * CountyRepository CountyRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface CountyRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:23:09 PM Aug 21, 2021
 */
@Repository
public interface CountyRepository extends JpaRepository<County, Long> {

}
