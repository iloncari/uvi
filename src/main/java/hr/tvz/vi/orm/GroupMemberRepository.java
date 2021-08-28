/*
 * GroupMemberRepository GroupMemberRepository.java.
 * 
 */

package hr.tvz.vi.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.vi.util.Constants.GroupType;


/**
 * The Interface GroupMemberRepository.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 11:06:12 PM Aug 27, 2021
 */
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
  
  /**
   * Find by organization id and group id.
   *
   * @param organizationId the organization id
   * @param groupType the group type
   * @return the list
   */
  List<GroupMember> findByOrganizationIdAndGroupType(Long organizationId, GroupType groupType);

}
