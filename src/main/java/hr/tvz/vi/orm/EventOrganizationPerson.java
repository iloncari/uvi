package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class EventOrganizationPerson {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private Long eventOrganizationId;
  
  @OneToOne
  private Vechile vechile;
  
  @OneToOne
  private Person person;
  
  long timeInSeconds;
}
