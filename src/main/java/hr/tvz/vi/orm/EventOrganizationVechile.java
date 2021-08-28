package hr.tvz.vi.orm;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table
public class EventOrganizationVechile {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private LocalDateTime baseDepartureDateTime;
  
  private LocalDateTime fieldArrivedDateTime;
  
  private LocalDateTime fieldDepartureDateTime;
  
  private LocalDateTime baseArrivedDateTime;
  
  private int distance;
  
  private int peopleNumber;
  
  private Long eventOrganizationId;
  

  

  
  @OneToOne
  private Vechile vechile;

}
