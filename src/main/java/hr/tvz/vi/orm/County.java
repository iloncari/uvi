/*
 * County County.java.
 * 
 */
package hr.tvz.vi.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * The Class County.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:34:21 PM Sep 19, 2021
 */
@Data
@Entity
@Table
public class County {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The name. */
  private String name;
}
