/*
 * DoubleToIntegerConverter DoubleToIntegerConverter.java.
 *
 */
package hr.tvz.vi.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * The Class DoubleToIntegerConverter.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 2:20:22 PM Aug 18, 2021
 */
public class DoubleToIntegerConverter implements Converter<Double, Integer> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 6424745157283920971L;

  /**
   * Convert to model.
   *
   * @param value the value
   * @param context the context
   * @return the result
   */
  @Override
  public Result<Integer> convertToModel(Double value, ValueContext context) {
    if (value == null) {
      return Result.ok(null);
    }
    return Result.ok(value.intValue());
  }

  /**
   * Convert to presentation.
   *
   * @param value the value
   * @param context the context
   * @return the double
   */
  @Override
  public Double convertToPresentation(Integer value, ValueContext context) {
    if (value == null || value == 0) {
      return null;
    }
    return Double.valueOf(value.intValue());
  }

}
