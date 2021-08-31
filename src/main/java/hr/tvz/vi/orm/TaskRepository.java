/*
 * VechileRepository VechileRepository.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface TaskRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:49:50 AM Aug 28, 2021
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

  /**
   * Find by organization id and assignee is null.
   *
   * @param organizationId the organization id
   * @return the list
   */
  List<Task> findByOrganizationAssignee_IdAndAssigneeIsNullAndExecutionDateTimeIsNull(Long organizationId);
  
  /**
   * Count by organization assignee id and assignee is null and execution date time is null.
   *
   * @param organizationId the organization id
   * @return the long
   */
  Long countByOrganizationAssignee_IdAndAssigneeIsNullAndExecutionDateTimeIsNull(Long organizationId);

  /**
   * Find by organization assignee and assignee id.
   *
   * @param organizationId the organization id
   * @param assigneeId the assignee id
   * @return the list
   */
  List<Task> findByOrganizationAssignee_IdAndAssignee_IdAndExecutionDateTimeIsNull(Long organizationId, Long assigneeId);
  
  /**
   * Count by organization assignee id and assignee id and execution date time is null.
   *
   * @param organizationId the organization id
   * @param assigneeId the assignee id
   * @return the long
   */
  Long countByOrganizationAssignee_IdAndAssignee_IdAndExecutionDateTimeIsNull(Long organizationId, Long assigneeId);
  
  /**
   * Find by organization assignee id and assignee id.
   *
   * @param organizationId the organization id
   * @param assigneeId the assignee id
   * @return the list
   */
  List<Task> findByOrganizationAssignee_IdAndAssignee_Id(Long organizationId, Long assigneeId);
  
  

  
  /**
   * Delete by report id.
   *
   * @param reportId the report id
   */
  List<Task> removeByReportId(Long reportId);

  /**
   * Find by report id.
   *
   * @param reportId the report id
   * @return the list
   */
  List<Task> findByReportId(Long reportId);

}
