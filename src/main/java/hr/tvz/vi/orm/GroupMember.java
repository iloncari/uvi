/*
 * GroupMember GroupMember.java.
 * 
 */
package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import hr.tvz.vi.util.Constants.GroupType;
import lombok.Data;

/**
 * The Class GroupMember.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 11:05:10 PM Aug 27, 2021
 */
@Data
@Entity
@Table
public class GroupMember {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /** The person. */
  @OneToOne
  private Person person;
  
  /** The organizationid. */
  private Long organizationId;
  
  /** The group id. */
  @Enumerated(EnumType.STRING)
  private GroupType groupType;
}
