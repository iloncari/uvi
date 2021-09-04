/*
 * NotificationRepository NotificationRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface NotificationRepository.
 *
 * @author Igor Lončarić(iloncari2@tvz.hr)
 * @since 12:33:41 PM Aug 29, 2021
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  /**
   * Find by recipient id and organization id and read date time is null.
   *
   * @param receipientId the receipient id
   * @param orgId the org id
   * @return the list
   */
  List<Notification> findByRecipientIdAndOrganizationId(Long receipientId, Long orgId);
  
  /**
   * Find by recipient id is null and organization id.
   *
   * @param orgId the org id
   * @return the list
   */
  List<Notification> findByRecipientIdIsNullAndOrganizationId(Long orgId);
}
