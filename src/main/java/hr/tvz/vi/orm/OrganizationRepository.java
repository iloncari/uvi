/*
 * OrganizationRepository OrganizationRepository.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.orm;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.tvz.vi.util.Constants.OrganizationLevel;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

  /**
   * Find by identification number.
   *
   * @param organizationIdentificationNumber the organization identification number
   * @return the optional
   */
  Optional<Organization> findByIdentificationNumber(String organizationIdentificationNumber);
  
  /**
	 * Find by level.
	 *
	 * @param level the level
	 * @return the list
	 */
  List<Organization> findByLevel(OrganizationLevel level);
  
  /**
	 * Find by level and parent id.
	 *
	 * @param level    the level
	 * @param parentId the parent id
	 * @return the list
	 */
  List<Organization> findByLevelAndParentId(OrganizationLevel level, Integer parentId);
  
  /**
	 * Find by level and parent id in.
	 *
	 * @param level     the level
	 * @param parentIds the parent ids
	 * @return the list
	 */
  List<Organization> findByLevelAndParentIdIn(OrganizationLevel level, List<Integer> parentIds);
  
  

}
