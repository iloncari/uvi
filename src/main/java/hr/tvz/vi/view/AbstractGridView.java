/*
 * AbstractGridView AbstractGridView.java.
 *
 */
package hr.tvz.vi.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;

import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.components.AdvancedSearch;
import hr.tvz.vi.components.SimpleSearch;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AbstractGridView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @param <T> the generic type
 * @since 6:10:55 PM Aug 14, 2021
 */
@EventSubscriber(scope = SubscriberScope.PUSH)
public abstract class AbstractGridView<T> extends VVerticalLayout implements HasDynamicTitle, HasUrlParameter<String> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8662193361768278434L;

  /** The grid. */
  @Getter
  private final VGrid<T> grid = new VGrid<>();

  /** The current user. */
  @Getter
  private final CurrentUser currentUser = Utils.getCurrentUser(UI.getCurrent());
  
  /** The simple search. */
  private final boolean simpleSearch;
  
  /** The advanced search. */
  private final boolean advancedSearch;
  
  /** The query params. */
  @Getter
  private Map<String, List<String>> queryParams;

  /**
   * Instantiates a new abstract grid view.
   *
   * @param simpleSearch the simple search
   * @param advancedSearch the advanced search
   * @param queryParams the query params
   */
  public AbstractGridView(boolean simpleSearch, boolean advancedSearch) {
    this.simpleSearch = simpleSearch;
    this.advancedSearch = advancedSearch;
    add(new VH3(getViewTitle()));
  }
  
  /**
   * Sets the query params.
   *
   * @param params the params
   */
  protected void setQueryParams(Map<String, List<String>> params) {
    this.queryParams = new HashMap<String, List<String>>(params);
  }
  
  
  /**
   * Sets the parameter.
   *
   * @param event the event
   * @param parameter the parameter
   */
  @Override
  public void setParameter(final BeforeEvent event, @OptionalParameter final String parameter) {
    final Location location = event.getLocation();
    final QueryParameters queryParameters = location
      .getQueryParameters();

    this.queryParams = new HashMap<String, List<String>>(queryParameters.getParameters());
    
    
    
    
  }

  /**
   * Gets the grid items.
   *
   * @return the grid items
   */
  public abstract List<T> getGridItems();
  
  /**
   * Gets the route.
   *
   * @return the route
   */
  public abstract String getRoute();

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public abstract String getPageTitle();

  /**
   * Gets the view title.
   *
   * @return the view title
   */
  protected abstract String getViewTitle();

  /**
   * Inits the bellow button layout.
   *
   * @return the v horizontal layout
   */
  protected abstract VHorizontalLayout initBellowButtonLayout();
  
  /**
   * Inits the above button layout.
   *
   * @return the v horizontal layout
   */
  protected abstract VHorizontalLayout initAboveLayout();

  /**
   * Inits the grid layout.
   */
  protected abstract void initGrid();

  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    ChangeBroadcaster.registerToPushEvents(this);
    removeAll();
    if(simpleSearch || advancedSearch) {
      VHorizontalLayout searchLayout = new VHorizontalLayout();
      if(simpleSearch) {
        searchLayout.add(new SimpleSearch(queryParams, getRoute()));
      }
      if(advancedSearch) {
        searchLayout.add(new AdvancedSearch<T>(queryParams, getRoute()));
      }
      add(searchLayout);
      add(buildTagsLayout());
    }
    
    VHorizontalLayout aboveGrid = initAboveLayout();
    if(aboveGrid != null) {
      add(aboveGrid);
    }
    initGrid();
    add(grid);
    grid.setItems(getGridItems());
    VHorizontalLayout buttons = initBellowButtonLayout();
    if(buttons != null) {
      add(buttons);
    }
  }

  /**
   * Builds the tags layout.
   *
   * @return the v horizontal layout
   */
  private VHorizontalLayout buildTagsLayout() {
    VHorizontalLayout tagsLayout = new VHorizontalLayout();
    queryParams.forEach((key, values) -> {
      values.forEach(value -> {
        VSpan tag = new VSpan(key + ":" + value);
        tag.addClickListener(e -> {
          tagsLayout.remove(tag);
          List<String> newValues = new ArrayList<>(queryParams.get(key));
          newValues.remove(value);
          queryParams.replace(key,  newValues);
          if(newValues.isEmpty()) {
            queryParams.remove(key);
          }
          UI.getCurrent().navigate(getRoute(), new QueryParameters(queryParams));
          UI.getCurrent().getPage().reload();
        });
        tagsLayout.add(tag);
      });
    });
    return tagsLayout;
  }


  /**
   * On detach.
   *
   * @param detachEvent the detach event
   */
  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
    ChangeBroadcaster.unregisterFromPushEvents(this);
  }

  /**
   * Sets the grid items.
   */
  protected void setGridItems() {
    grid.setItems(getGridItems());
    grid.getDataProvider().refreshAll();
  }

}
