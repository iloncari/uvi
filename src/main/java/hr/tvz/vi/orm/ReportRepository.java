/*
 * VechileRepository VechileRepository.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.tvz.vi.util.Constants.ReportStatus;

public interface ReportRepository extends JpaRepository<Report, Long> {

	/**
	 * Find by status and event organizations id in.
	 *
	 * @param status           the status
	 * @param organizationsIds the organizations ids
	 * @return the list
	 */
	List<Report> findByStatusAndEventOrganizationList_Organization_Id(ReportStatus status, Long organizationsId);
	
	/**
	 * Find by creator id.
	 *
	 * @param creatorId the creator id
	 * @return the list
	 */
	List<Report> findByCreatorId(String creatorId);
}
