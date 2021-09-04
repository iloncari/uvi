/*
 * VechilesView VechilesView.java.
 *
 */
package hr.tvz.vi.view;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.VechileChangedChangedEvent;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Vechile;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.VechileService;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Constants.ThemeAttribute;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.button.DeleteButton;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.dialog.VDialog;
import org.vaadin.firitin.components.html.VH5;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.textfield.VTextField;

import de.codecamp.vaadin.serviceref.ServiceRef;

/**
 * The Class VechilesView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 4:34:41 PM Aug 14, 2021
 */
@Route(value = Routes.VECHILES, layout = MainAppLayout.class)
@EventSubscriber(scope = SubscriberScope.PUSH)
public class VechilesView extends AbstractGridView<Vechile> implements HasDynamicTitle {

  /**
   * Instantiates a new vechiles view.
   */
  public VechilesView() {
    super(true, true);
  }

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -1667920498041105820L;

  /** The remove member button. */
  private DeleteButton deleteVechileButton;

  /** The transfer vechile button. */
  private VButton transferVechileButton;

  /** The organization service ref. */
  @Autowired
  private ServiceRef<OrganizationService> organizationServiceRef;

  /** The vechile service ref. */
  @Autowired
  private ServiceRef<VechileService> vechileServiceRef;

  /**
   * Gets the grid items.
   *
   * @return the grid items
   */
  @Override
  public List<Vechile> getGridItems() {
    return vechileServiceRef.get().getActiveByOrganization(getCurrentUser().getActiveOrganization().getOrganization().getId(), getQueryParams());
  }

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public String getPageTitle() {
    return getTranslation(Routes.getPageTitleKey(Routes.VECHILES));
  }
  /**
 	 * Vechile changed.
 	 *
 	 * @param changeEvent the change event
 	 */
  @SuppressWarnings("unchecked")
  @Subscribe
   public void vechileChanged(VechileChangedChangedEvent changeEvent) {
 	  if(getCurrentUser().getActiveOrganization().getOrganization().getId().equals(changeEvent.getVechile().getOrganization().getId())) {
 		  getUI().ifPresent(ui -> ui.access(() -> {
 		    if(EventAction.REMOVED.equals(changeEvent.getAction())) {
 		      ((ListDataProvider<Vechile>)getGrid().getDataProvider()).getItems().removeIf(vechile -> vechile.getId().equals(changeEvent.getVechile().getId()));
 		    }else if(EventAction.ADDED.equals(changeEvent.getAction())) {
 		     ((ListDataProvider<Vechile>)getGrid().getDataProvider()).getItems().add(changeEvent.getVechile());
 		    }
 	 		  getGrid().getDataProvider().refreshAll(); 
 		  }));
 	  }
   }
  /**
   * Gets the view title.
   *
   * @return the view title
   */
  @Override
  protected String getViewTitle() {
    return getPageTitle();
  }

  /**
   * Inits the bellow button layout.
   *
   * @return the v horizontal layout
   */
  @Override
  protected VHorizontalLayout initBellowButtonLayout() {
    final VHorizontalLayout buttonsLayout = new VHorizontalLayout();

    final VButton newVehcileButton = new VButton(getTranslation("vechilesView.button.newVechile.label"));
    newVehcileButton.addClickListener(e -> UI.getCurrent().navigate(NewVechileView.class));
    newVehcileButton.getThemeList().add(ThemeAttribute.BUTTON_BLUE);
    buttonsLayout.add(newVehcileButton);

    deleteVechileButton = new DeleteButton().withEnabled(false);
    deleteVechileButton.getElement().getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_RED);

    buttonsLayout.add(deleteVechileButton
      .withText(getTranslation("vechilesView.button.removeVechile.label"))
      .withConfirmText(getTranslation("button.confirm"))
      .withRejectText(getTranslation("button.close"))
      .withPromptText(getTranslation("areYouSure.label"))
      .withHeaderText(getTranslation("vechilesView.removeVechileDialog.title"))
      .withConfirmHandler(() -> {
        getGrid().getSelectedItems().stream().findFirst().ifPresent(vechile -> {
          vechileServiceRef.get().deleteVechileFromOrganization(vechile);
          ChangeBroadcaster.firePushEvent(new VechileChangedChangedEvent(this, vechile, EventAction.REMOVED));
        });
      }));

    transferVechileButton = new VButton(getTranslation("vechilesView.button.transferVechile")).withEnabled(false)
      .withClickListener(e -> showTransferVechileDialog());
    transferVechileButton.getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_GREEN);
    buttonsLayout.add(transferVechileButton);

    return buttonsLayout;
  }

  /**
   * Inits the grid.
   */
  @Override
  protected void initGrid() {
    getGrid().removeAllColumns();
    getGrid().setSelectionMode(SelectionMode.SINGLE);
    getGrid().addSelectionListener(e -> {
      deleteVechileButton.setEnabled(!e.getFirstSelectedItem().isEmpty());
      transferVechileButton.setEnabled(!e.getFirstSelectedItem().isEmpty());
    });

    getGrid().addComponentColumn(vechile -> new RouterLink(vechile.getVechileNumber(), VechileView.class, vechile.getId().toString()))
      .setHeader(getTranslation("vechilesView.vechilesGrid.vechileNumber"));
    getGrid().addColumn(vechile -> getTranslation(vechile.getType().getLabelKey())).setHeader(getTranslation("vechilesView.vechilesGrid.type"));
    getGrid().addColumn(vechile -> vechile.getMake().concat(StringUtils.isNotBlank(vechile.getMake()) ? " ".concat(vechile.getModel()) : ""))
      .setHeader(getTranslation("vechilesView.vechilesGrid.name"));
    getGrid().addColumn(vechile -> vechile.getLicencePlateNumber()).setHeader(getTranslation("vechilesView.vechilesGrid.licencePlateNumber"));
    getGrid().addColumn(vechile -> getTranslation(vechile.getCondition().getLabelKey())).setHeader(getTranslation("vechilesView.vechilesGrid.condition"));

    getGrid().addItemClickListener(e -> {
    	if(e.getClickCount() > 1) {
    		UI.getCurrent().navigate(VechileView.class, e.getItem().getId().toString());
    	}
    });
  }
  
 

  /**
   * Show transfer vechile dialog.
   */
  private void showTransferVechileDialog() {
    final VDialog transferDialog = new VDialog();
    final VVerticalLayout dialogLayout = new VVerticalLayout();
    final VH5 dialogTitle = new VH5(getTranslation("vechilesView.transferDialog.title"));
    dialogLayout.add(dialogTitle);
    final VTextField idField = new VTextField(getTranslation("vechilesView.transferDialog.identificationNumber"));
    idField.setValueChangeMode(ValueChangeMode.EAGER);

    final VButton transferButton = new VButton(getTranslation("vechilesView.transferDialog.button.transfer")).withEnabled(false)
      .withClickListener(transferEvent -> {
        final Optional<Organization> organizationOptional = organizationServiceRef.get().getOrganizationByIdentificationNumber(idField.getValue());
        if (organizationOptional.isEmpty()) {
          idField.setInvalid(true);
          idField.setErrorMessage(getTranslation("vechilesView.transferDialog.organizationNotExists.error"));
        } else {
          getGrid().getSelectedItems().stream().findFirst().ifPresent(vechile -> {
            ChangeBroadcaster.firePushEvent(new VechileChangedChangedEvent(this, vechile, EventAction.REMOVED));
            vechileServiceRef.get().transferVechile(vechile, organizationOptional.get());
          });
          transferDialog.close();
        }
      });

    idField.addValueChangeListener(changeEvent -> {
      transferButton.setEnabled(false);
      if (StringUtils.isBlank(changeEvent.getValue())) {
        changeEvent.getSource().setInvalid(true);
        changeEvent.getSource().setErrorMessage(getTranslation("vechilesView.transferDialog.identificationNumber.required.error"));
        return;
      }
      if (!changeEvent.getValue().matches("^[0-9]*$")) {
        changeEvent.getSource().setInvalid(true);
        changeEvent.getSource().setErrorMessage(getTranslation("membersView.newMembersDialog.identificationNumber.onlyNumbers.error"));
        return;
      }
      changeEvent.getSource().setInvalid(false);
      transferButton.setEnabled(true);
    });
    dialogLayout.add(dialogTitle, idField, transferButton);
    transferButton.getElement().getThemeList().add(ThemeAttribute.BUTTON_OUTLINE_GREEN);
    transferDialog.add(dialogLayout);
    transferDialog.open();
  }

  /**
   * Inits the above layout.
   *
   * @return the v horizontal layout
   */
  @Override
  protected VHorizontalLayout initAboveLayout() {
    return null;
  }

  /**
   * Gets the route.
   *
   * @return the route
   */
  @Override
  public String getRoute() {
    return Routes.VECHILES;
  }

}
