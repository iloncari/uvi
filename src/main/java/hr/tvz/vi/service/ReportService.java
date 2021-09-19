/*
 * ReportService ReportService.java.
 * 
 */

package hr.tvz.vi.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.vi.orm.AddressRepository;
import hr.tvz.vi.orm.EventDescription;
import hr.tvz.vi.orm.EventDescriptionRepository;
import hr.tvz.vi.orm.EventOrganization;
import hr.tvz.vi.orm.EventOrganizationPerson;
import hr.tvz.vi.orm.EventOrganizationPersonRepository;
import hr.tvz.vi.orm.EventOrganizationRepository;
import hr.tvz.vi.orm.EventOrganizationVechile;
import hr.tvz.vi.orm.EventOrganizationVechileRepository;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.ReportRepository;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.orm.TaskRepository;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventType;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.VechileCondition;
import hr.tvz.vi.util.Constants.VechileType;
import lombok.extern.slf4j.Slf4j;


/**
 * The Class ReportService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 9:46:37 PM Aug 21, 2021
 */
@Service
@Transactional
public class ReportService extends AbstractService<Report> {
	
	/** The address repository. */
	@Autowired
	AddressRepository addressRepository;
	
	/** The event organization repository. */
	@Autowired
	EventOrganizationRepository eventOrganizationRepository;
	
	/** The event organization vechile repository. */
	@Autowired
	EventOrganizationVechileRepository eventOrganizationVechileRepository;
	
	/** The event organization person repository. */
	@Autowired
	EventOrganizationPersonRepository eventOrganizationPersonRepository;
	
	/** The event authorization repository. */
	@Autowired
	EventDescriptionRepository eventDescriptionRepository;
	
	/** The task repository. */
	@Autowired
	TaskRepository taskRepository;
	/**
	 * Save event report.
	 *
	 * @param report    the report
	 * @param creatorId the creator id
	 * @return the report
	 */
	public Report saveEventReport(Report report, Long creatorId) {
		if(report == null || creatorId==null || report.getId()!=null) {
			return null;
		}
		report.setStatus(ReportStatus.NEW);
		report.setCreatorId(creatorId.toString());
		addressRepository.save(report.getEventAddress());
		Report saved =  ((ReportRepository)repository).save(report);
		report.getEventOrganizationList().forEach(eo -> eventOrganizationRepository.save(eo));
		LocalDate now = LocalDate.now();
		saved.setIdentificationNumber("RP-".concat(String.valueOf(now.getYear())).concat("/").concat(String.valueOf(now.getMonthValue()))
		  .concat("/").concat(String.valueOf(now.getDayOfMonth())).concat("/").concat(saved.getId().toString()));
		return ((ReportRepository)repository).save(saved);
	}
	
	/**
   * Save report task.
   *
   * @param task the task
   * @return the task
   */
	public Task saveReportTask(Task task) {
	  if(task == null) {
	    return null;
	  }
	  return taskRepository.save(task);
	}
	
	/**
	 * Gets the by id.
	 *
	 * @param reportId the report id
	 * @return the by id
	 */
	public Optional<Report> getById(Long reportId) {
	    if (reportId == null) {
	      return Optional.empty();
	    }
	    return repository.findById(reportId);
	  }
	
	/**
   * Gets the owning reports.
   *
   * @param owner the owner
   * @param filter the filter
   * @return the owning reports
   */
	public List<Report> getOwningReports(Organization owner, Map<String, List<String>> filter){
		if(owner==null) {
			return new ArrayList<Report>();
		}
		List<Report> reports =  ((ReportRepository)repository).findByCreatorId(owner.getId().toString());
		
		filterReports(filter, reports);
    
    return reports;
		
	}
	
	/**
	 * Gets the reports.
	 *
	 * @param status             the status
	 * @param eventOrganizations the event organizations
	 * @return the reports
	 */
	public List<Report> getReports( Organization org){
		if(org==null) {
			return new ArrayList<Report>();
		}
		return ((ReportRepository)repository).findByEventOrganizationList_Organization_Id(org.getId());
	}
	
	
	/**
   * Gets the reports.
   *
   * @param org the org
   * @param filter the filter
   * @return the reports
   */
	public List<Report> getReports( Organization org, Map<String, List<String>> filter){
    if(org==null) {
      return new ArrayList<Report>();
    }
    List<Report> reports  = ((ReportRepository)repository).findByEventOrganizationList_Organization_Id(org.getId());
  
    filterReports(filter, reports);
    
    return reports;
	}

	/**
   * Filter reports.
   *
   * @param filter the filter
   * @param reports the reports
   */
	private void filterReports(Map<String, List<String>> filter, List<Report> reports) {
	  if(filter.containsKey("simpleSearch")) {
      String searchValue = filter.get("simpleSearch").get(0);
      List<Report> filtered = reports.stream().filter(report -> {
        AtomicBoolean passes = new AtomicBoolean(false);
        Utils.getSearchableFields(report).forEach(fieldName -> {
          try {
            Object fieldValue =  Report.class.getDeclaredField(fieldName).get(report);
            if(fieldValue instanceof String && !passes.get()) {
              String value = (String)fieldValue;
              passes.set(StringUtils.equalsIgnoreCase(value, searchValue));
            }
          } catch (IllegalAccessException | NoSuchFieldException e) {
          }
        });
        return passes.get();
      }).collect(Collectors.toList());
      reports.clear();
      reports.addAll(filtered);
    }
    
    
    filter.forEach((fieldKey, values) -> {
      if(fieldKey.equals("simpleSearch") || values.isEmpty()){
        return;
      } 
      List<Report> filtered = reports.stream().filter(report -> {
      try {
         Object fieldValue =  Report.class.getDeclaredField(fieldKey).get(report);
         if(fieldValue instanceof String) {
           String value = (String)fieldValue;
           return StringUtils.equalsIgnoreCase(value, values.get(0));
         }else if(fieldValue instanceof EventType) {
           return values.stream().map(value -> EventType.getEventType(value)).filter(Objects::nonNull).anyMatch(type -> type.equals((EventType)fieldValue));
         }else if(fieldValue instanceof ReportStatus) {
           return values.stream().map(value -> ReportStatus.getReportStatus(value)).filter(Objects::nonNull).anyMatch(status -> status.equals((ReportStatus)fieldValue));
         }
      } catch (IllegalAccessException | NoSuchFieldException e) {
        if("eventDate".equals(fieldKey)) {
          DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
          LocalDate date;
          try {
            date = LocalDate.parse(values.get(0), formatter);
          }catch (DateTimeParseException ex) {
            return true;
          }
          return report.getEventDateTime() != null && report.getEventDateTime().getDayOfMonth() == date.getDayOfMonth() && 
            report.getEventDateTime().getMonthValue() == date.getMonthValue() && report.getEventDateTime().getYear() == date.getYear();
        }else if("eventCity".equals(fieldKey)) {
          return report.getEventAddress() != null && report.getEventAddress().getCity() != null && report.getEventAddress().getCity().getId().toString().equals(values.get(0));
        }else if("eventOrganization".equals(fieldKey)) {
          return report.getEventOrganizationList() != null && report.getEventOrganizationList().stream().anyMatch(o -> values.contains(o.getId().toString()));
        }
      }
       
       return true;
     }).collect(Collectors.toList());
      reports.clear();
      reports.addAll(filtered);
    });
    
    
  }

  /**
	 * Delete report.
	 *
	 * @param repotForDelete the repot for delete
	 */
	
	@Transactional
	public void deleteReport(Report repotForDelete) {
		if(repotForDelete==null) {
			return;
		}
		taskRepository.removeByReportId(repotForDelete.getId());
		eventOrganizationRepository.deleteAll(repotForDelete.getEventOrganizationList());
		repository.deleteById(repotForDelete.getId());
		addressRepository.deleteById(repotForDelete.getEventAddress().getId());
		
	}

  /**
   * Update report.
   *
   * @param report the report
   */
  public void updateReport(Report report) {
    if(report == null) {
      return;
    }
    if(report.getEventAddress() != null) {
      addressRepository.save(report.getEventAddress());
    }
    repository.save(report);
    
  }
  
  
  /**
   * Save event organization person.
   *
   * @param eventPerson the event person
   * @return the event organization person
   */
  public EventOrganizationPerson saveEventOrganizationPerson(EventOrganizationPerson eventPerson) {
    if(eventPerson==null) {
      return null;
    }
    return eventOrganizationPersonRepository.save(eventPerson);
    
  }
  
 
  /**
   * Delete event organization person.
   *
   * @param eventPerson the event person
   */
  public void deleteEventOrganizationPerson(EventOrganizationPerson eventPerson) {
    if(eventPerson==null) {
      return;
    }
     eventOrganizationPersonRepository.deleteById(eventPerson.getId());
  }

  /**
   * Save report vechile.
   *
   * @param eventVechile the event vechile
   */
  public EventOrganizationVechile saveEventOrganizationVechile(EventOrganizationVechile eventVechile) {
    if(eventVechile==null) {
      return null;
    }
    return eventOrganizationVechileRepository.save(eventVechile);
    
  }

  
  /**
   * Delete event organization vechile.
   *
   * @param eventVechile the event vechile
   */
  public void deleteEventOrganizationVechile(EventOrganizationVechile eventVechile) {
    if(eventVechile==null) {
      return;
    }
     eventOrganizationVechileRepository.deleteById(eventVechile.getId());
  }

  /**
   * Gets the vechiles by event organization.
   *
   * @param eventOrganization the event organization
   * @return the vechiles by event organization
   */
  public List<EventOrganizationVechile> getVechilesByEventOrganization(EventOrganization eventOrganization) {
    if(eventOrganization==null) {
      return null;
    }
    
   return eventOrganizationVechileRepository.findByEventOrganizationId(eventOrganization.getId());
  }
  
  /**
   * Gets the persons by event organization.
   *
   * @param eventOrganization the event organization
   * @return the persons by event organization
   */
  public List<EventOrganizationPerson> getPersonsByEventOrganization(EventOrganization eventOrganization) {
    if(eventOrganization==null) {
      return null;
    }
    
   return eventOrganizationPersonRepository.findByEventOrganizationId(eventOrganization.getId());
  }
  
  /**
   * Gets the report authrizations.
   *
   * @param report the report
   * @return the report authrizations
   */
  public List<EventDescription> getReportDescriptions(Report report) {
    if(report==null) {
      return null;
    }
    
   return eventDescriptionRepository.findByReportId(report.getId());
  }

  /**
   * Save event authorization.
   *
   * @param auth the auth
   */
  public void saveEventDesctiption(EventDescription desc) {
    if(desc == null) {
      return;
    }
    eventDescriptionRepository.save(desc);
  }

  /**
   * Gets the group tasks.
   *
   * @param organizationId the organization id
   * @return the group tasks
   */
  public List<Task> getGroupTasks(Long organizationId) {
    if(organizationId == null) {
      return new  ArrayList<Task>();
    }
    return taskRepository.findByOrganizationAssignee_IdAndAssigneeIsNullAndExecutionDateTimeIsNull(organizationId);
  }
  
  /**
   * Gets the assigned tasks.
   *
   * @param organizationId the organization id
   * @param assigneeId the assignee id
   * @return the assigned tasks
   */
  public List<Task> getActiveAssignedTasks(Long organizationId, Long assigneeId) {
    if(organizationId == null || assigneeId==null) {
      return new  ArrayList<Task>();
    }
    return taskRepository.findByOrganizationAssignee_IdAndAssignee_IdAndExecutionDateTimeIsNull(organizationId, assigneeId);
  }
  
  /**
   * Gets the user task number.
   *
   * @param organizationId the organization id
   * @param assigneeId the assignee id
   * @return the user task number
   */
  public Long getUserTaskNumber(Long organizationId, Long assigneeId) {
    if(organizationId == null || assigneeId==null) {
      return 0L;
    }
    return taskRepository.countByOrganizationAssignee_IdAndAssignee_IdAndExecutionDateTimeIsNull(organizationId, assigneeId);
  }
  
  /**
   * Gets the group task number.
   *
   * @param organizationId the organization id
   * @return the group task number
   */
  public Long getGroupTaskNumber(Long organizationId) {
    if(organizationId == null) {
      return 0L;
    }
    return taskRepository.countByOrganizationAssignee_IdAndAssigneeIsNullAndExecutionDateTimeIsNull(organizationId);
  }
  
  
  /**
   * Gets the reports number.
   *
   * @param organizationId the organization id
   * @return the reports number
   */
  public Long getReportsNumber(Long organizationId) {
    if(organizationId == null) {
      return 0L;
    }
    return ((ReportRepository)repository).countByEventOrganizationList_Organization_Id(organizationId);
  }
  
  /**
   * Gets the all assigned tasks.
   *
   * @param organizationId the organization id
   * @param assigneeId the assignee id
   * @return the all assigned tasks
   */
  public List<Task> getAllAssignedTasks(Long organizationId, Long assigneeId) {
    if(organizationId == null || assigneeId==null) {
      return new  ArrayList<Task>();
    }
    return taskRepository.findByOrganizationAssignee_IdAndAssignee_Id(organizationId, assigneeId);
  }

  /**
   * Gets the report tasks.
   *
   * @param reportId the report id
   * @return the report tasks
   */
  public List<Task> getReportTasks(Long reportId) {
    if(reportId == null) {
      return new  ArrayList<Task>();
    }
    return taskRepository.findByReportId(reportId);
  }
  
  
	

}
