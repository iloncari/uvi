package hr.tvz.vi.orm;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table
@EqualsAndHashCode(exclude = "report")
public class EventOrganization {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @JoinColumn
	  @OneToOne
	  private Organization organization;

	  @ManyToOne
	  @ToString.Exclude
	  @JoinColumn(name = "reportId")
	  private Report report;
	  
	  
	  private LocalDateTime alarmedDateTime;
	  
	  private LocalDateTime baseDepartureDateTime;
	  
	  private LocalDateTime fieldArrivedDateTime;
	  
	  private LocalDateTime workFinishedDateTime;
	  
	  private LocalDateTime baseReturnDateTime;

	  private LocalDateTime interventionFinishedDateTime;
	  
	  private LocalDateTime fireUnderSurveillanceDateTime;
	  
	  private LocalDateTime fireExtinguishedDateTime;
	  
	  private LocalDateTime fieldLocalizedDateTime;
	  
	  private LocalDateTime insuranceStartDateTime;
	  
	  private LocalDateTime insuranceEndDateTime;

}
