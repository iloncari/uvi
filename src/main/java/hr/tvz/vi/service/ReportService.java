/*
 * ReportService ReportService.java.
 * 
 */

package hr.tvz.vi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.vi.orm.AddressRepository;
import hr.tvz.vi.orm.EventOrganizationRepository;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.orm.ReportRepository;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.util.Constants.ReportStatus;
import jdk.internal.jline.internal.Log;
import lombok.extern.slf4j.Slf4j;


/**
 * The Class ReportService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 9:46:37 PM Aug 21, 2021
 */
@Service
@Slf4j
public class ReportService extends AbstractService<Report> {
	
	/** The address repository. */
	@Autowired
	AddressRepository addressRepository;
	
	/** The event organization repository. */
	@Autowired
	EventOrganizationRepository eventOrganizationRepository;
	
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
	 * @return the owning reports
	 */
	public List<Report> getOwningReports(Organization owner){
		if(owner==null) {
			return new ArrayList<Report>();
		}
		return ((ReportRepository)repository).findByCreatorId(owner.getId().toString());
		
	}
	
	/**
	 * Gets the reports.
	 *
	 * @param status             the status
	 * @param eventOrganizations the event organizations
	 * @return the reports
	 */
	public List<Report> getReports(ReportStatus status, Organization org){
		if(status==null || org==null) {
			return new ArrayList<Report>();
		}
		return ((ReportRepository)repository).findByStatusAndEventOrganizationList_Organization_Id(status, org.getId());
	}

	/**
	 * Delete report.
	 *
	 * @param repotForDelete the repot for delete
	 */
	public void deleteReport(Report repotForDelete) {
		if(repotForDelete==null) {
			return;
		}
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
	

}
