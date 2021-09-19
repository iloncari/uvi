/*
 * VechileService VechileService.java.
 *
 */
package hr.tvz.vi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.ServiceRepository;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.orm.VechileRepository;
import hr.tvz.vi.util.Constants.VechileCondition;
import hr.tvz.vi.util.Constants.VechileType;
import hr.tvz.vi.util.Utils;

/**
 * The Class VechileService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 7:22:17 PM Aug 14, 2021
 */
@Service
public class VechileService extends AbstractService<Vechile> {

  /** The service repository. */
  @Autowired
  ServiceRepository serviceRepository;

  /**
   * Delete vechile.
   *
   * @param vechile the vechile
   */
  public void deleteVechileFromOrganization(Vechile vechile) {
    if (vechile == null) {
      return;
    }
    vechile.setActive(false);
    repository.save(vechile);
  }

  /**
   * Gets the by id.
   *
   * @param vechileId the vechile id
   * @return the by id
   */
  public Optional<Vechile> getById(Long vechileId) {
    if (vechileId == null) {
      return Optional.empty();
    }
    return repository.findById(vechileId);
  }

  /**
   * Gets the by organization.
   *
   * @param organizationId the organization id
   * @return the by organization
   */
  public List<Vechile> getActiveByOrganization(Long organizationId) {
    if (organizationId == null) {
      return new ArrayList<>();
    }
    
    return ((VechileRepository) repository).findByOrganizationIdAndActiveTrue(organizationId);
  }
  
  /**
   * Gets the active by organization.
   *
   * @param organizationId the organization id
   * @param filter the filter
   * @return the active by organization
   */
  public List<Vechile> getActiveByOrganization(Long organizationId, Map<String, List<String>> filter) {
    if (organizationId == null) {
      return new ArrayList<>();
    }
    
    List<Vechile> vehicles = ((VechileRepository) repository).findByOrganizationIdAndActiveTrue(organizationId);
    
    if(filter.containsKey("simpleSearch")) {
      String searchValue = filter.get("simpleSearch").get(0);
      List<Vechile> filtered = vehicles.stream().filter(vehicle -> {
        AtomicBoolean passes = new AtomicBoolean(false);
        Utils.getSearchableFields(vehicle).forEach(fieldName -> {
          try {
            Object fieldValue =  Vechile.class.getDeclaredField(fieldName).get(vehicle);
            if(fieldValue instanceof String && !passes.get()) {
              String value = (String)fieldValue;
              passes.set(StringUtils.equalsIgnoreCase(value, searchValue));
            }
          } catch (IllegalAccessException | NoSuchFieldException e) {
          }
        });
        return passes.get();
      }).collect(Collectors.toList());
      vehicles.clear();
      vehicles.addAll(filtered);
    }
    
    
    filter.forEach((fieldKey, values) -> {
      if(fieldKey.equals("simpleSearch") || values.isEmpty()){
        return;
      } 
      List<Vechile> filtered = vehicles.stream().filter(vehicle -> {
      try {
         Object fieldValue =  Vechile.class.getDeclaredField(fieldKey).get(vehicle);
         if(fieldValue instanceof String) {
           String value = (String)fieldValue;
           return StringUtils.equalsIgnoreCase(value, values.get(0));
         }else if(fieldValue instanceof VechileCondition) {
           return values.stream().map(value -> VechileCondition.getVechileCondition(value)).filter(Objects::nonNull).anyMatch(gender -> gender.equals((VechileCondition)fieldValue));
         }else if(fieldValue instanceof VechileType) {
           return values.stream().map(value -> VechileType.getVechileType(value)).filter(Objects::nonNull).anyMatch(vechileType -> vechileType.equals((VechileType)fieldValue));
         }
      } catch (IllegalAccessException | NoSuchFieldException e) {
      }
       
       return true;
     }).collect(Collectors.toList());
      vehicles.clear();
      vehicles.addAll(filtered);
    });
    
    
    return vehicles;
  }

  /**
   * Save or update service record.
   *
   * @param service the service
   * @param vechile the vechile
   */
  public void saveOrUpdateServiceRecord(hr.tvz.vi.orm.Service service, Vechile vechile) {
    if (service == null || vechile == null) {
      return;
    }
    service.setServiceVechile(vechile);
    serviceRepository.save(service);
  }
  
  
  /**
   * Delete service record.
   *
   * @param service the service
   */
  public void deleteServiceRecord(hr.tvz.vi.orm.Service service) {
    if (service == null) {
      return;
    }
    serviceRepository.delete(service);
  }

  /**
   * Save or update vechile.
   *
   * @param vechile the vechile
   * @return the vechile
   */
  public Vechile saveOrUpdateVechile(Vechile vechile) {
    if (vechile == null) {
      return null;
    }
    return repository.save(vechile);
  }

  /**
   * Transfer vechile.
   *
   * @param vechile the vechile
   * @param destination the destination
   */
  public void transferVechile(Vechile vechile, Organization destination) {
    if (vechile == null || destination == null) {
      return;
    }
    vechile.setActive(false);
    repository.save(vechile);
    
    vechile.setOrganization(destination);
    vechile.setActive(true);
    vechile.setId(null);
    repository.save(vechile);
  }

}
