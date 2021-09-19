/*
 * City City.java.
 * 
 */
package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * The Class City.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:33:52 PM Sep 19, 2021
 */
@Entity
@Table
@Data
public class City {
  
  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The name. */
  private String name;

  /** The municipality. */
  private String municipality;

  /** The county. */
  @ManyToOne
  private County county;
}
