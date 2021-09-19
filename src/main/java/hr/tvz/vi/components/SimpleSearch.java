/*
 * SimpleSearch SimpleSearch.java.
 * 
 */
package hr.tvz.vi.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.textfield.VTextField;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.QueryParameters;

/**
 * The Class SimpleSearch.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 6:28:52 PM Sep 19, 2021
 */
public class SimpleSearch extends VTextField {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -3093881432813564048L;
  
  /** The query params. */
  private Map<String, List<String>> queryParams;

  /** The parent view route. */
  private final String parentViewRoute;
  
 
  /**
   * Instantiates a new simple search.
   *
   * @param queryParams the query params
   * @param parentViewRoute the parent view route
   */
  public SimpleSearch(final  Map<String, List<String>> queryParams, final String parentViewRoute) {
    this.queryParams = new HashMap<String, List<String>>(queryParams);
    this.parentViewRoute = parentViewRoute;
  }

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  public void onAttach(final AttachEvent attachEvent) {
    setPlaceholder(getTranslation("simplesSearch.field.placeholder"));
    setClearButtonVisible(true);
    setWidthFull();
    if(queryParams.containsKey("simpleSearch")) {
      queryParams.get("simpleSearch").stream().findAny().ifPresent(value -> setValue(value));
    }

    // using ValueChangeMode.LAZY and timeout of 1sec allows user gap of 1 sec before simple search is fired. LAZY will update VTextField value every 1 sec.
    setValueChangeMode(ValueChangeMode.LAZY);
    setValueChangeTimeout(1000);
   
    addValueChangeListener(event -> {
      
      if(queryParams.containsKey("simpleSearch")) {
        queryParams.remove("simpleSearch");
      }
      if(StringUtils.isNotBlank(event.getValue())) {
        queryParams.put("simpleSearch", Arrays.asList(event.getValue()));
      }

      UI.getCurrent().navigate(parentViewRoute, new QueryParameters(queryParams));
      UI.getCurrent().getPage().reload();
    });

    // If user type text and press ENTER, key press change listener will be executed and it will
    // change ValueChangeMode to ON_BLUR. That allows VTextField to register new inserted value immediately. Before that, VtextField value was empty.
    addKeyPressListener(Key.ENTER, e -> {
      setValueChangeMode(ValueChangeMode.ON_BLUR);
      blur();
    });
  }
}

