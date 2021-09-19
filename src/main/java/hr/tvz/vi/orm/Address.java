/*
 * Address Address.java.
 * 
 */
package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * The Class Address.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:32:01 PM Sep 19, 2021
 */
@Entity
@Data
@Table
public class Address {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	  
	/** The city. */
	@OneToOne
  private City city;
	   
	/** The street. */
	private String street;
	  
	/** The street number. */
	private String streetNumber;
	  
	/**
   * Gets the city.
   *
   * @return the city
   */
	public City getCity() {
	 if(city==null) {
		city = new City();
	 }
	 return city;
	}
}
