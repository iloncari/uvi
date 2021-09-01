/*
 * OrganizationService OrganizationService.java.
 *
 */
package hr.tvz.vi.service;

import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.PersonOrganizationChangedEvent;
import hr.tvz.vi.orm.GroupMember;
import hr.tvz.vi.orm.GroupMemberRepository;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.OrganizationRepository;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.PersonOrganization;
import hr.tvz.vi.orm.PersonOrganizationRepository;
import hr.tvz.vi.orm.PersonRepository;
import hr.tvz.vi.util.Constants.Duty;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.GroupType;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.UserRole;
import lombok.extern.slf4j.Slf4j;
import hr.tvz.vi.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class OrganizationService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 10:13:29 PM Aug 10, 2021
 */
@Slf4j
@Service
public class OrganizationService extends AbstractService<Organization> {

  /** The person organization repository. */
  @Autowired
  private PersonOrganizationRepository personOrganizationRepository;

  /** The person repository. */
  @Autowired
  private PersonRepository personRepository;
  
  /** The group member repository. */
  @Autowired
  private GroupMemberRepository groupMemberRepository;

  /**
   * Save or update organization for person.
   *
   * @param personOrganization the person organization
   * @return the person organization
   */
  public PersonOrganization addOrUpdateOrganizationForPerson(PersonOrganization personOrganization) {
    if (personOrganization == null) {
      return null;
    }
    return personOrganizationRepository.save(personOrganization);
  }

  /**
   * Gets the child organization members.
   *
   * @param parentOrganization the parent organization
   * @return the child organization members
   */
  public List<Person> getChildOrganizationMembers(Organization parentOrganization) {
    if (parentOrganization == null || parentOrganization.getChilds().isEmpty()) {
      return new ArrayList<>();
    }

    return personRepository
      .findByUsernameIsNotNullAndOrgList_ExitDateIsNullAndOrgList_JoinDateIsNotNullAndOrgList_AppRightsTrueAndOrgList_OrganizationIdIn(
        parentOrganization.getChilds().stream().map(Organization::getId).collect(Collectors.toList()));
  }
  
  /**
	 * Gets the report selectable childs per level.
	 *
	 * @param organization the organization
	 * @return the report selectable childs per level
	 */
  public List<Organization> getReportSelectableChildsPerLevel(Organization organization){
	 
	  if(organization == null || organization.getLevel() == null) {
		  return new ArrayList<Organization>();
	  }

	  if(OrganizationLevel.COUNTRY_LEVEL.equals(organization.getLevel() )) {
		  return ((OrganizationRepository)repository).findByLevel(OrganizationLevel.OPERATIONAL_LEVEL);
	  }else if(OrganizationLevel.REGIONAL_LEVEL.equals(organization.getLevel())){
		  List<Integer> parentIds = new ArrayList<Integer>();
		  organization.getChilds().forEach(cityLevelOrg -> parentIds.add(cityLevelOrg.getId().intValue()));
		  return ((OrganizationRepository)repository).findByLevelAndParentIdIn(OrganizationLevel.OPERATIONAL_LEVEL, parentIds);
	  }else if(OrganizationLevel.CITY_LEVEL.equals(organization.getLevel() )) {
		  return ((OrganizationRepository)repository).findByLevelAndParentId(OrganizationLevel.OPERATIONAL_LEVEL, organization.getId().intValue());
	  }
	  return Arrays.asList(organization);
  }

  /**
   * Gets the organization by identification number.
   *
   * @param organizationIdentificationNumber the organization identification number
   * @return the organization by identification number
   */
  public Optional<Organization> getOrganizationByIdentificationNumber(String organizationIdentificationNumber) {
    if (StringUtils.isBlank(organizationIdentificationNumber)) {
      return Optional.empty();
    }
    return ((OrganizationRepository) repository).findByIdentificationNumber(organizationIdentificationNumber);
  }

  /**
   * Gets the organization members.
   *
   * @param organization the organization
   * @return the organization members
   */
  public List<Person> getOrganizationMembers(Organization organization) {
    if (organization == null) {
      return new ArrayList<>();
    }

    List<Person> members = personRepository.findByOrgList_ExitDateIsNullAndOrgList_OrganizationId(organization.getId());
    
 
    
    return members;
  }
  
  
  /**
   * Gets the organization members.
   *
   * @param organization the organization
   * @return the organization members
   */
  public List<Person> getOrganizationMembers(Organization organization, Map<String, List<String>> filter) {
    if (organization == null) {
      return new ArrayList<>();
    }
    
    final List<Person> members = personRepository.findByOrgList_ExitDateIsNullAndOrgList_OrganizationId(organization.getId());
    
    
    if(filter.containsKey("simpleSearch")) {
      String searchValue = filter.get("simpleSearch").get(0);
      List<Person> filtered = members.stream().filter(member -> {
        AtomicBoolean passes = new AtomicBoolean(false);
        Utils.getSearchableFields(member).forEach(fieldName -> {
          try {
            Object fieldValue =  Person.class.getDeclaredField(fieldName).get(member);
            if(fieldValue instanceof String && !passes.get()) {
              String value = (String)fieldValue;
              passes.set(StringUtils.equalsIgnoreCase(value, searchValue));
            }
          } catch (IllegalAccessException | NoSuchFieldException e) {
          }
        });
        return passes.get();
      }).collect(Collectors.toList());
      members.clear();
      members.addAll(filtered);
    }
    filter.forEach((fieldKey, values) -> {
      if(fieldKey.equals("simpleSearch") || values.isEmpty()){
        return;
      } 
      List<Person> filtered = members.stream().filter(member -> {
      try {
         Object fieldValue =  Person.class.getDeclaredField(fieldKey).get(member);
         if(fieldValue instanceof String) {
           String value = (String)fieldValue;
           return StringUtils.equalsIgnoreCase(value, values.get(0));
         }else if(fieldValue instanceof Gender) {
           return values.stream().map(value -> Gender.getGender(value)).filter(Objects::nonNull).anyMatch(gender -> gender.equals((Gender)fieldValue));
         }else if(fieldValue instanceof Professions) {
           return values.stream().map(value -> Professions.getProfession(value)).filter(Objects::nonNull).anyMatch(profession -> profession.equals((Professions)fieldValue));
         }else if(fieldValue instanceof LocalDate && values.size()>1) {
           LocalDate date = (LocalDate)fieldValue;
           return date != null && NumberUtils.isParsable(values.get(0)) && NumberUtils.isParsable(values.get(1))
             && date.getYear() >= NumberUtils.createDouble(values.get(0)) && date.getYear() <= NumberUtils.createDouble(values.get(1));
         }
      } catch (IllegalAccessException | NoSuchFieldException e) {
      }
       
       return true;
     }).collect(Collectors.toList());
      members.clear();
      members.addAll(filtered);
    });
    
    return members;
  }
  
  /**
   * Gets the organization members number.
   *
   * @param organization the organization
   * @return the organization members number
   */
  public Long getOrganizationMembersNumber(Organization organization) {
    if (organization == null) {
      return 0L;
    }

    return personRepository.countByOrgList_ExitDateIsNullAndOrgList_OrganizationId(organization.getId());
  }

  /**
   * Gets the person organization if member.
   *
   * @param person the person
   * @param organization the organization
   * @return the person organization if member
   */
  public Optional<PersonOrganization> getPersonOrganization(Person person, Organization organization) {
    if (person == null || person == null) {
      return null;
    }
    return person.getOrgList().stream()
      .filter(po -> po.getOrganization().getId().equals(organization.getId())).findFirst();
  }

  /**
   * Gets the selectable organizations.
   *
   * @return the selectable organizations
   */
  public List<Organization> getSelectableOrganizations() {
    return ((OrganizationRepository) repository).findAll().stream().filter(organization -> organization.getChilds().isEmpty()).collect(Collectors.toList());
  }

  /**
   * Checks if is person organization member.
   *
   * @param person the person
   * @param organization the organization
   * @return true, if is person organization member
   */
  public boolean isPersonOrganizationMember(Person person, Organization organization) {
    if (person == null || organization == null) {
      return false;
    }
    return person.getOrgList().stream().filter(perOrg -> perOrg.getExitDate() == null)
      .anyMatch(perOrg -> perOrg.getOrganization().getId().equals(organization.getId()));

  }

  /**
   * Join organization.
   *
   * @param person the person
   * @param organization the organization
   * @param appAccess the app access
   * @return the person organization
   */
  public PersonOrganization joinOrganization(Person person, Organization organization, boolean appAccess) {
    if (person == null || organization == null) {
      return null;
    }

    final PersonOrganization personOrganization = new PersonOrganization();
    personOrganization.setPerson(person);
    personOrganization.setAppRights(appAccess);
    personOrganization.setDuty(Duty.NONE);
    personOrganization.setOrganization(organization);
    personOrganization.setRequestDate(LocalDate.now());
    personOrganization.setJoinDate(LocalDate.now());
    personOrganization.setRole(UserRole.SPECTATOR);
    final PersonOrganization savedPO = personOrganizationRepository.save(personOrganization);
    ChangeBroadcaster.firePushEvent(new PersonOrganizationChangedEvent(this, savedPO, EventAction.ADDED));
    return savedPO;
  }

  /**
   * Left organization.
   *
   * @param person the person
   * @param organization the organization
   * @return true, if successful
   */
  public boolean leftOrganization(Person person, Organization organization) {
    if (person == null || organization == null) {
      return false;
    }
    final Optional<PersonOrganization> personOrganizationOptional = person.getOrgList().stream()
      .filter(po -> po.getOrganization().getId().equals(organization.getId())).findFirst();
    if (personOrganizationOptional.isPresent()) {
      personOrganizationOptional.get().setAppRights(false);
      personOrganizationOptional.get().setExitDate(LocalDate.now());
      final PersonOrganization po = personOrganizationRepository.save(personOrganizationOptional.get());
      ChangeBroadcaster.firePushEvent(new PersonOrganizationChangedEvent(this, po, EventAction.MODIFIED));
      return true;
    }
    return false;
  }

  /**
   * Organization is parent.
   *
   * @param organization the organization
   * @return true, if successful
   */
  public boolean organizationIsParent(Organization organization) {
    return !organization.getChilds().isEmpty();
  }

  /**
   * Save or update organization.
   *
   * @param organization the organization
   * @return the organization
   */
  public Organization saveOrUpdateOrganization(Organization organization) {
    if (organization == null) {
      return null;
    }
    return ((OrganizationRepository) repository).save(organization);
  }

  /**
   * Gets the organization group members.
   *
   * @param groupType the group type
   * @param organizationId the organization id
   * @return the organization group members
   */
  public List<GroupMember> getOrganizationGroupMembers(GroupType groupType, Long organizationId) {
    if(groupType==null || organizationId ==null) {
      return new ArrayList<GroupMember>();
    }
    
    return groupMemberRepository.findByOrganizationIdAndGroupType(organizationId, groupType);
  }

  /**
   * Save group member.
   *
   * @param newMember the new member
   */
  public GroupMember saveGroupMember(GroupMember member) {
    if(member == null) {
      return null;
    }
   return groupMemberRepository.save(member);
    
  }
  
  /**
   * Delete group member.
   *
   * @param member the member
   */
  public void deleteGroupMember(GroupMember member) {
    if(member == null) {
      return;
    }
    groupMemberRepository.deleteById(member.getId());
  }

}
