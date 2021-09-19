/*
 * NotificationService NotificationService.java.
 * 
 */

package hr.tvz.vi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.firitin.components.html.VSpan;

import hr.tvz.vi.orm.Notification;
import hr.tvz.vi.orm.NotificationRepository;
import hr.tvz.vi.orm.NotificationUserMapping;
import hr.tvz.vi.orm.NotificationUserMappingRepository;
import hr.tvz.vi.orm.PersonRepository;
import hr.tvz.vi.orm.VechileRegistrationReminder;
import hr.tvz.vi.orm.VechileReminderRepository;
import hr.tvz.vi.orm.VechileRepository;
import hr.tvz.vi.util.Constants.NotificationType;
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
	
  /** The vechile reminder repository. */
  @Autowired
  VechileReminderRepository vechileReminderRepository;
  
  /** The organization repository. */
  @Autowired
  PersonRepository personRepository;
  
  /** The vechile repository. */
  @Autowired
  VechileRepository vechileRepository;
  
	/**
   * Find all active notifications.
   *
   * @param organizationId the organization id
   * @param userId the user id
   * @return the list
   */
	public List<Notification> findAllActiveNotifications(Long organizationId, Long userId){
	  List<Notification> notifications = new ArrayList<Notification>();
	  
	  notifications.addAll(((NotificationRepository)repository).findByRecipientIdAndOrganizationId(userId, organizationId));
	  notifications.addAll(((NotificationRepository)repository).findByRecipientIdIsNullAndOrganizationId(organizationId));
	 
	  List<NotificationUserMapping> mapping = notificationUserMappingRepository.findAll();

	  return notifications.stream().filter(n -> mapping.stream().anyMatch(m -> m.getNotificationId().equals(n.getId()) && m.getUserId().equals(userId) && m.getReadAt()==null)).collect(Collectors.toList());
	  
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
	    mapping.setReadAt(LocalDateTime.now());
	    notificationUserMappingRepository.save(mapping);
	  }
	}
	
	/**
   * Send registration expiring notifications if needed.
   *
   * @param organizationId the organization id
   */
	public void sendRegistrationExpiringNotificationsIfNeeded(Long organizationId) {
	  if(vechileReminderRepository.findByOrganizationIdAndCheckedDate(organizationId, LocalDate.now()) == null) {
	    vechileRepository.findByOrganizationIdAndActiveTrue(organizationId).forEach(vechile -> {
	      VSpan span = new VSpan();
	      Notification notification = new Notification();
	      notification.setCreationDateTime(LocalDateTime.now());
	      notification.setOrganizationId(organizationId);
	      notification.setSourceId(vechile.getId());
	      notification.setType(NotificationType.VECHILE);
	      notification.setTitle(span.getTranslation("notification.vechile.reminder"));
	      if(vechile.getRegistrationValidUntil() != null && vechile.getRegistrationValidUntil().isBefore(LocalDate.now().plusDays(4))) {
          notification.setMessage(span.getTranslation("notification.vechile.reminder.message", vechile.getVechileNumber(), 4));
          saveOrUpdateNotification(notification);
          personRepository.findByOrgList_ExitDateIsNullAndOrgList_OrganizationId(organizationId).forEach(member -> {
            mapNotificationToUser(notification.getId(), member.getId());
          });
        }else if(vechile.getRegistrationValidUntil() != null && vechile.getRegistrationValidUntil().isBefore(LocalDate.now().plusDays(10))) {
	        notification.setMessage(span.getTranslation("notification.vechile.reminder.message", vechile.getVechileNumber(), 10));
	        saveOrUpdateNotification(notification);
	        personRepository.findByOrgList_ExitDateIsNullAndOrgList_OrganizationId(organizationId).forEach(member -> {
	          mapNotificationToUser(notification.getId(), member.getId());
	        });
	      }
	      
	    });
	    VechileRegistrationReminder vechilesChecked = new VechileRegistrationReminder();
	    vechilesChecked.setCheckedDate(LocalDate.now());
	    vechilesChecked.setOrganizationId(organizationId);
	    vechileReminderRepository.save(vechilesChecked);
	  }
	}


}
