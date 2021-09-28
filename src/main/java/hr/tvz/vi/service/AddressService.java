/*
 * AddressService AddressService.java.
 * 
 */
package hr.tvz.vi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.vi.orm.Address;
import hr.tvz.vi.orm.AddressRepository;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.CityRepository;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.CountyRepository;

/**
 * The Class AddressService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:22:31 PM Aug 21, 2021
 */
@Service
public class AddressService extends AbstractService<Address> {
	
	/** The county repository. */
	@Autowired
	private CountyRepository countyRepository;
	
	/** The city repository. */
	@Autowired
	private CityRepository cityRepository;
	
	/**
	 * Gets the all counties.
	 *
	 * @return the all counties
	 */
	public List<County> getAllCounties(){
		return countyRepository.findAll();
	}
	
	
	/**
	 * Save or update address.
	 *
	 * @param address the address
	 */
	public void saveOrUpdateAddress(Address address) {
		if(address==null) {
			return;
		}
		if(address.getCity()!=null){
		  cityRepository.save(address.getCity());
		}
		
		((AddressRepository)repository).save(address);
	}
	
	/**
	 * Gets the cities.
	 *
	 * @param county the county
	 * @return the cities
	 */
	public List<City> getCities(County county){
		if(county == null) {
			return new ArrayList<City>();
		}
		return cityRepository.findByCounty(county);
	}
	
	/**
   * Gets the all cities.
   *
   * @return the all cities
   */
	public List<City> getAllCities(){

    return cityRepository.findAll();
  }

}
