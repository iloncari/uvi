/*
 * CustomBadge CustomBadge.java.
 * 
 */
package hr.tvz.vi.components;

import org.apache.commons.lang3.math.NumberUtils;

import com.vaadin.flow.component.html.Span;

import hr.tvz.vi.util.Constants.StyleConstants;

/**
 * The Class CustomBadge.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 4:50:42 PM Aug 29, 2021
 */
public class CustomBadge extends Span {


  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -5311776715471437319L;

  /**
   * Instantiates a new badge indicator.
   */
  public CustomBadge() {
    super(String.valueOf(NumberUtils.INTEGER_ZERO));
    addClassName(StyleConstants.NOTIFICATION_NUMBER.getName());
  }

  /**
   * Sets the count.
   *
   * @param count the new count
   */
  public void setCount(final int count) {
    setText(String.valueOf(count));
  }

  /**
   * Gets the count.
   *
   * @return the count
   */
  public int getCount() {
    return Integer.parseInt(getText());
  }

  /**
   * Clear count.
   */
  public void clearCount() {
    setText(String.valueOf(NumberUtils.INTEGER_ZERO));
  }

  /**
   * Increase.
   */
  public void increase() {
    setText(String.valueOf(Integer.parseInt(getText()) + 1));
  }

  /**
   * Decrease.
   */
  public void decrease() {
    final int oldValue = Integer.parseInt(getText());
    if (oldValue > NumberUtils.INTEGER_ZERO) {
      setText(String.valueOf(oldValue - 1));
    }
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.getElement().setAttribute("style", "");
    } else {
      this.getElement().setAttribute("style", "display:none");
    }
  }

}
