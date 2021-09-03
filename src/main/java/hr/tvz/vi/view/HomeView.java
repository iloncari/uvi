/*
 * HomeView HomeView.java.
 *
 * Copyright (c) 2018 OptimIT d.o.o.. All rights reserved.
 */
package hr.tvz.vi.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VH2;
import org.vaadin.firitin.components.html.VH3;
import org.vaadin.firitin.components.html.VH4;
import org.vaadin.firitin.components.html.VH5;
import org.vaadin.firitin.components.html.VParagaph;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.Service;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.event.TaskChangeEvent;
import hr.tvz.vi.orm.Task;
import hr.tvz.vi.service.OrganizationService;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.EventSubscriber;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.SubscriberScope;
import hr.tvz.vi.util.Constants.TaskType;
import hr.tvz.vi.util.Constants.ThemeAttribute;

/**
 * The Class HomeView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 12:20:01 AM Aug 29, 2021
 */
@EventSubscriber(scope = SubscriberScope.PUSH)
@Route(value = Routes.HOME, layout = MainAppLayout.class)
public class HomeView extends VVerticalLayout implements HasDynamicTitle {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 3163549796670950551L;
   
  /** The my tasks count. */
  private VSpan myTasksCount = new VSpan();
  
  /** The group tasks count. */
  private VSpan groupTasksCount = new VSpan();
  
  /** The reports count. */
  private VSpan reportsCount = new VSpan();
  
  /** The members count. */
  private VSpan membersCount = new VSpan();
  
  
  /** The current user. */
  private final CurrentUser currentUser;
  
  /** The report service ref. */
  @Autowired
  private ServiceRef<ReportService> reportServiceRef;
  
  /** The organization service ref. */
  @Autowired
  private ServiceRef<OrganizationService> organizationServiceRef;
  
  private VHorizontalLayout boxes = new VHorizontalLayout();
  
  private VVerticalLayout graphLayout = new VVerticalLayout();
  
  /**
   * Instantiates a new home view.
   */
  public HomeView() {
    this.currentUser = Utils.getCurrentUser(UI.getCurrent());
    Utils.removeAllThemes(this);
    
    
    final VHorizontalLayout titleLayout = new VHorizontalLayout();
    Utils.removeAllThemes(titleLayout);
    titleLayout.getThemeList().set(ThemeAttribute.PAGE_TITLE, true);
    titleLayout.add(new VH2(getPageTitle()));
    
    add(titleLayout);
    
  }
  
  /**
   * On attach.
   *
   * @param attachEvent the attach event
   */
  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
  
    boxes.removeAll();
    boxes.add(initBox(myTasksCount, "tasksView.myTaskTab.label", reportServiceRef.get().getUserTaskNumber(currentUser.getActiveOrganization().getOrganization().getId(),
     currentUser.getPerson().getId()), Routes.TASKS, ThemeAttribute.GREEN, Map.of("tab", List.of("myTasks"))));
    boxes.add(initBox(groupTasksCount, "tasksView.groupTaskTab.label", reportServiceRef.get().getGroupTaskNumber(currentUser.getActiveOrganization().getOrganization().getId()), Routes.TASKS, ThemeAttribute.LIGHT_BLUE, Map.of("tab", List.of("groupTasks"))));
    boxes.add(initBox(reportsCount, "page.reports.title", reportServiceRef.get().getReportsNumber(currentUser.getActiveOrganization().getOrganization().getId()), Routes.REPORTS, ThemeAttribute.PURPLE, Map.of()));
    boxes.add(initBox(membersCount, "page.members.title", organizationServiceRef.get().getOrganizationMembersNumber(currentUser.getActiveOrganization().getOrganization()),Routes.MEMBERS, ThemeAttribute.AQUA, Map.of()));
    add(boxes);
    boxes.getThemeList().set(ThemeAttribute.WIDGET_CARDS, true);
    
    
    graphLayout.removeAll();
    graphLayout.getThemeList().set(ThemeAttribute.CARD, true);
    graphLayout.getThemeList().set(ThemeAttribute.CARD_FULL_BLOCK, true);
    graphLayout.getThemeList().set(ThemeAttribute.CARD_SPACING, true);

    graphLayout.getThemeList().remove(ThemeAttribute.PADDING);
    graphLayout.getThemeList().remove(ThemeAttribute.SPACING);
    VH3 chartTitle = new VH3(getTranslation("homeView.chart.label"));
    graphLayout.add(chartTitle);
    Map<ReportStatus, Double> reportsCountMap = new HashMap<>();
    reportServiceRef.get().getReports(currentUser.getActiveOrganizationObject()).forEach(report -> {
      if(reportsCountMap.containsKey(report.getStatus())) {
        reportsCountMap.put(report.getStatus(), reportsCountMap.get(report.getStatus()) + 1D);
      }else {
        reportsCountMap.put(report.getStatus(), 1D);
      }
    });

    String[] colors = {"#D26600", "#0078c0", "#95d76e"};
    final ApexCharts chart = ApexChartsBuilder.get()
      .withChart(ChartBuilder.get().withType(Type.donut).withBackground("#ffffff").withFontFamily("Open Sans, sans-serif").withForeColor("#707070")
        .withHeight("300px").build())
      .withLegend(LegendBuilder.get().withPosition(Position.left).build())
      .withTitle(TitleSubtitleBuilder.get().withText(getTranslation("homeView.chart.reports")).build())
      .withResponsive(ResponsiveBuilder.get().withBreakpoint(991.0)
        .withOptions(OptionsBuilder.get().withLegend(LegendBuilder.get().withPosition(Position.bottom).build()).build()).build())
      .withStroke(StrokeBuilder.get().withWidth(0.0).build())
      .withColors(colors)
      .withSeries(reportsCountMap.values().toArray(Double[]::new))
      .withLabels(reportsCountMap.keySet().stream().map(rs -> getTranslation("reportStatus."+rs.getName()+".label")).toArray(String[]::new))
      .build();
    graphLayout.add(chart);
    add(graphLayout);
  }

  /**
   * Init box.
   *
   * @param box the box
   * @param titleKey the title key
   * @param count the count
   * @param view the view
   * @return the v div
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private VVerticalLayout initBox(VSpan countSpan, String titleKey, Long count, String route, String boxTheme, Map<String, List<String>> queryParams) {
    VVerticalLayout box = new VVerticalLayout();
      box.add(new VH4(getTranslation(titleKey)).withClassName(StyleConstants.WIDGET_TITLE.getName()));
      countSpan.setText(count.toString());
      countSpan.setClassName(StyleConstants.WIDGET_NUMBER.getName());
      box.add(countSpan);
      box.addClickListener(e ->UI.getCurrent().navigate(route, new QueryParameters(queryParams)));
      box.getElement().getThemeList().set(ThemeAttribute.WIDGET_CARD, true);
      box.getElement().getThemeList().set(boxTheme, true);
      return box;
  }
  
  /**
   * Task changed.
   *
   * @param taskEvent the task event
   */
  @Subscribe
  public void taskChanged(TaskChangeEvent taskEvent) {
    if(currentUser.getActiveOrganizationObject().getId().equals(taskEvent.getTask().getOrganizationAssignee().getId())) {
      getUI().ifPresent(ui -> ui.access(() -> {
          if(taskEvent.getTask().getAssignee()==null) {
            myTasksCount.setText(reportServiceRef.get().getUserTaskNumber(currentUser.getActiveOrganization().getOrganization().getId(),
              currentUser.getPerson().getId()).toString());
          }else {
            groupTasksCount.setText(reportServiceRef.get().getGroupTaskNumber(currentUser.getActiveOrganization().getOrganization().getId()).toString());
          }
          
          if(EventAction.ADDED.equals(taskEvent.getAction()) && TaskType.PREPARATION_TASK.equals(taskEvent.getTask().getType())) {
            //new report and task(s)
            reportsCount.setText(reportServiceRef.get().getReportsNumber(currentUser.getActiveOrganization().getOrganization().getId()).toString());
          }
       }));
    }
  }

  /**
   * Gets the page title.
   *
   * @return the page title
   */
  @Override
  public String getPageTitle() {
    return getTranslation(Routes.getPageTitleKey(Routes.HOME));
  }

}
