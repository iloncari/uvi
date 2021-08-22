package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table
public class Address {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  @OneToOne
	  private City city;
	  
	  
	  private String street;
	  
	  private String streetNumber;
	  
	  public City getCity() {
		  if(city==null) {
			  city = new City();
		  }
		  return city;
	  }
	  
}
