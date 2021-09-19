/*
 * AccessDeniedView AccessDeniedView.java.
 *
 */
package hr.tvz.vi.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasErrorParameter;

import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

/**
 * The Class AccessDeniedView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 2:44:49 PM Aug 13, 2021
 */
@CssImport("./styles/shared-styles.css")
public class AccessDeniedView extends VVerticalLayout implements HasDynamicTitle, HasErrorParameter<AccessDeniedException> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -329895408910621488L;

  /**
   * Instantiates a new access denied view.
   */
  public AccessDeniedView() {
    setClassName(StyleConstants.FIRE_GRADIENT.getName());
    setAlignItems(Alignment.CENTER);
    setHeightFull();
    setJustifyContentMode(JustifyContentMode.CENTER);
   
    VVerticalLayout layout = new VVerticalLayout();
    Utils.removeAllThemes(layout);
    layout.getStyle().remove("width");
    layout.addClassName(StyleConstants.ERROR_CONTENT.getName());
    layout.add(new VH3(getTranslation("accessDeniedView.title")));
    Icon icon = VaadinIcon.LOCK.create();
    layout.add(icon);
    VButton redirect = new VButton(getTranslation("button.redirect")).withClickListener(e -> UI.getCurrent().navigate(HomeView.class));
    redirect.getElement().getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_BLUE);
    layout.add(new Paragraph(getTranslation("accessDeniedView.message")));
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
    return getTranslation(Routes.getPageTitleKey(Routes.ACCESS_DENIED));
  }

  /**
   * Sets the error parameter.
   *
   * @param event the event
   * @param parameter the parameter
   * @return the int
   */
  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter) {
    return HttpServletResponse.SC_FORBIDDEN;
  }

}
