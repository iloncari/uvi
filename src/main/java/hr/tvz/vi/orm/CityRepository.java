/*
 * CityRepository CityRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface CityRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:23:09 PM Aug 21, 2021
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
	
	/**
	 * Find by county.
	 *
	 * @param county the county
	 * @return the list
	 */
	List<City> findByCounty(County county);

}
