/*
 * NotificationUserMappingRepository NotificationUserMappingRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The Interface NotificationUserMappingRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 10:01:54 PM Aug 29, 2021
 */
@Repository
public interface NotificationUserMappingRepository extends JpaRepository<NotificationUserMapping, Long> {

  /**
   * Find by user id and notification id.
   *
   * @param userId the user id
   * @param notificaitonId the notificaiton id
   * @return the notification user mapping
   */
  NotificationUserMapping findByUserIdAndNotificationId(Long userId, Long notificaitonId);
}
