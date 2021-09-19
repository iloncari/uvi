/*
 * ReportRepository ReportRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface ReportRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:43:48 PM Sep 19, 2021
 */
public interface ReportRepository extends JpaRepository<Report, Long> {

	/**
	 * Find by status and event organizations id in.
	 *
	 * @param status           the status
	 * @param organizationsIds the organizations ids
	 * @return the list
	 */
	List<Report> findByEventOrganizationList_Organization_Id(Long organizationsId);
	
	/**
   * Count by event organization list organization id.
   *
   * @param organizationId the organization id
   * @return the long
   */
	Long countByEventOrganizationList_Organization_Id(Long organizationId);
	
	/**
	 * Find by creator id.
	 *
	 * @param creatorId the creator id
	 * @return the list
	 */
	List<Report> findByCreatorId(String creatorId);
}
