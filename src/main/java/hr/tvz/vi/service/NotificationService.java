/*
 * NotificationService NotificationService.java.
 * 
 */

package hr.tvz.vi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.vi.orm.Address;
import hr.tvz.vi.orm.AddressRepository;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.CityRepository;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.CountyRepository;
import hr.tvz.vi.orm.Notification;
import hr.tvz.vi.orm.NotificationRepository;
import hr.tvz.vi.orm.NotificationUserMapping;
import hr.tvz.vi.orm.NotificationUserMappingRepository;
import lombok.extern.slf4j.Slf4j;


/**
 * The Class NotificationService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:34:44 PM Aug 29, 2021
 */
@Slf4j
@Service
public class NotificationService extends AbstractService<Notification> {
  
  /** The notification user mapping repository. */
  @Autowired
  NotificationUserMappingRepository notificationUserMappingRepository;
	
	/**
   * Find all active notifications.
   *
   * @param organizationId the organization id
   * @param userId the user id
   * @return the list
   */
	public List<Notification> findAllActiveNotifications(Long organizationId, Long userId){
	  List<Notification> notifications = new ArrayList<Notification>();
	  
	  notifications.addAll(((NotificationRepository)repository).findByRecipientIdAndOrganizationIdAndReadDateTimeIsNull(userId, organizationId));
	  
	  notifications.addAll(((NotificationRepository)repository).findByRecipientIdAndOrganizationIdAndReadDateTimeIsNull(null, organizationId));
	  
	  List<NotificationUserMapping> mapping = notificationUserMappingRepository.findAll();
	  
	 return  notifications.stream().filter(n -> mapping.stream().anyMatch(m -> m.getNotificationId().equals(n.getId()) && m.getReadAt()==null)).collect(Collectors.toList());
	 
	  
	}
	
	/**
   * Save or update notification.
   *
   * @param notification the notification
   * @return the notification
   */
	public Notification saveOrUpdateNotification(Notification notification) {
	  if(notification==null) {
	    return null;
	  }
	 
	  return repository.save(notification);
	 
	}
	
	
	/**
   * Map notification to user.
   *
   * @param notificationId the notification id
   * @param userId the user id
   * @return the notification user mapping
   */
	public NotificationUserMapping mapNotificationToUser(Long notificationId, Long userId) {
	  NotificationUserMapping mapping = new NotificationUserMapping();
	  mapping.setNotificationId(notificationId);
	  mapping.setUserId(userId);
	  return notificationUserMappingRepository.save(mapping);
	}
	
	
	/**
   * Mark notification as read.
   *
   * @param notificationId the notification id
   * @param userId the user id
   */
	public void markNotificationAsRead(Long notificationId, Long userId) {
	  NotificationUserMapping mapping = notificationUserMappingRepository.findByUserIdAndNotificationId(userId, notificationId);
	  if(mapping != null) {
	    log.info("setting on read");
	    mapping.setReadAt(LocalDateTime.now());
	    notificationUserMappingRepository.save(mapping);
	  }
	}


}
