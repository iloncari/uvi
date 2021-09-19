/*
 * FuelConsuptionRepository FuelConsuptionRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface FuelConsuptionRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:38:16 PM Sep 19, 2021
 */
public interface FuelConsuptionRepository extends JpaRepository<FuelConsuption, Long> {

}
