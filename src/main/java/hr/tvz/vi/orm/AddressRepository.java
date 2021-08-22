/*
 * AddressRepository AddressRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface AddressRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:23:09 PM Aug 21, 2021
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
