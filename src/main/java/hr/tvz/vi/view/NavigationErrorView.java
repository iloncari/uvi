/*
 * NavigationErrorView NavigationErrorView.java.
 *
 */
package hr.tvz.vi.view;

import javax.servlet.http.HttpServletResponse;

import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;

/**
 * The Class NavigationErrorView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 3:21:05 PM Aug 13, 2021
 */
@CssImport("./styles/shared-styles.css")
public class NavigationErrorView extends VVerticalLayout implements HasErrorParameter<NotFoundException>, HasDynamicTitle {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 7715867164357519744L;

  /**
   * Instantiates a new navigation error view.
   */
  public NavigationErrorView() {
    setClassName(StyleConstants.FIRE_GRADIENT.getName());
    setAlignItems(Alignment.CENTER);
    setHeightFull();
    setJustifyContentMode(JustifyContentMode.CENTER);
    VVerticalLayout layout = new VVerticalLayout();
    Utils.removeAllThemes(layout);
    layout.getStyle().remove("width");
    layout.addClassName(StyleConstants.ERROR_CONTENT.getName());
    layout.add(new VH3(getTranslation("notFoundView.title")));
    Icon icon = VaadinIcon.SEARCH_MINUS.create();
    layout.add(icon);
    VButton redirect = new VButton(getTranslation("button.redirect")).withClickListener(e -> UI.getCurrent().navigate(HomeView.class));
    redirect.getElement().getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_BLUE);
    layout.add(new Paragraph(getTranslation("notFoundView.message")));
    layout.add(redirect);
    add(layout);
  }

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public String getPageTitle() {
    return getTranslation(Routes.getPageTitleKey(Routes.NAVIGATION_ERROR));
  }

  /**
   * Sets the error parameter.
   *
   * @param event the event
   * @param parameter the parameter
   * @return the int
   */
  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
    return HttpServletResponse.SC_NOT_FOUND;
  }

}
