/*
 * ReportsView ReportsView.java.
 * 
 */
package hr.tvz.vi.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.button.DeleteButton;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VAnchor;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextArea;
import org.vandeseer.easytable.TableDrawer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import de.codecamp.vaadin.serviceref.ServiceRef;
import hr.tvz.vi.auth.CurrentUser;
import hr.tvz.vi.event.ChangeBroadcaster;
import hr.tvz.vi.event.VechileChangedChangedEvent;
import hr.tvz.vi.orm.EventOrganization;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.Report;
import hr.tvz.vi.service.ReportService;
import hr.tvz.vi.util.Utils;
import hr.tvz.vi.util.Constants.EventAction;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.ReportStatus;
import hr.tvz.vi.util.Constants.Routes;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportsView.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 2:00:11 PM Aug 22, 2021
 */
@Slf4j
@Route(value = Routes.REPORTS, layout = MainAppLayout.class)
public class ReportsView extends AbstractGridView<Report>{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1372381528372139683L;
	
	/** The delete button. */
	private VButton deleteButton;
	
	/** The export anchor. */
	private VAnchor exportAnchor;
	
	
	/** The report service ref. */
	@Autowired
	private ServiceRef<ReportService> reportServiceRef;


	/**
   * Instantiates a new reports view.
   */
	public ReportsView() {
   super(true, true);
  }
	/**
	 * Gets the grid items.
	 *
	 * @return the grid items
	 */
	@Override
	public List<Report> getGridItems() {
		if(OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel())) {
			return reportServiceRef.get().getReports(getCurrentUser().getActiveOrganization().getOrganization(), getQueryParams());
		}else {
			return reportServiceRef.get().getOwningReports(getCurrentUser().getActiveOrganization().getOrganization(), getQueryParams());
		}
	}

	/**
	 * On attach.
	 *
	 * @param attachEvent the attach event
	 */
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		deleteButton.setVisible(!OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel()));
	}

	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {
		return getTranslation(Routes.getPageTitleKey(Routes.REPORTS));
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
	@SuppressWarnings("unchecked")
  @Override
	protected VHorizontalLayout initBellowButtonLayout() {
		final VHorizontalLayout buttonsLayout = new VHorizontalLayout();
	    //moze brisati samo kreator
	    deleteButton = new VButton(getTranslation("reportsView.button.delete.label")).withVisible(!OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganization().getOrganization().getLevel())).withEnabled(false);
	    deleteButton.addClickListener(e -> getGrid().getSelectedItems().forEach(repotForDelete -> {
	    	reportServiceRef.get().deleteReport(repotForDelete);
	    	((ListDataProvider<Report>)getGrid().getDataProvider()).getItems().remove(repotForDelete);	
	    	getGrid().getDataProvider().refreshAll();
	    	//event
	    }));
	    buttonsLayout.add(deleteButton);
	    
	    final VButton exportButton = new VButton(getTranslation("task.form.exportAsPdf.button"));
	    exportButton.addThemeName(ThemeAttribute.BUTTON_BLUE);

	    exportAnchor = new VAnchor();
	    exportAnchor.setTarget("_blank");
	    exportAnchor.add(exportButton);
	    

	    final Registration buttonListener = exportButton.addClickListener(e -> setPdfResource(exportAnchor, exportButton));

	    exportButton.onClick(() -> {
	      if (!exportAnchor.getHref().isBlank()) {
	        buttonListener.remove();
	      }
	    });

	    buttonsLayout.add(exportAnchor);


	    return buttonsLayout;
	}
	
	/**
   * Write line.
   *
   * @param text the text
   * @param fontsize the fontsize
   * @param font the font
   * @param contentStream the content stream
   */
	private float writeLine(String text, PDPageContentStream contentStream, float xposition, float yposition, int yoffset, int fontSize, PDFont font) {
    float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
    try {
      contentStream.beginText();
      contentStream.setFont(font, fontSize);
      yposition = yposition - yoffset - textHeight;
      contentStream.newLineAtOffset(xposition, yposition);
      contentStream.showText(text);
      contentStream.endText();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return yposition;
	 
	}

	 /**
   * Sets the pdf resource.
   *
   * @param exportAnchor the export anchor
   * @param exportButton the export button
   */
 	private void setPdfResource(final VAnchor exportAnchor, final VButton exportButton) {
 	    Report report = getGrid().getSelectedItems().stream().findFirst().orElse(null);
 	    String documentTitle = getTranslation("exportedReport.title", report.getIdentificationNumber());
 	    
 	   
	    final StreamResource streamResource = new StreamResource(exportAnchor.getTranslation("exportedReport.fileName", report.getIdentificationNumber(), LocalDateTime.now().toString()).replace("/",  "-"), () -> {
	      final PDDocument document = new PDDocument();
	      final PDPage page = new PDPage(PDRectangle.A4);
	      document.addPage(page);
	      
	      int fontSize;
	      float textWidth;
	      float textHeight;
	      String text;
	      final int topMargin = 20;
	      final int leftMargin = 50;
	      float yposition = 0;
	      Double yT = Double.valueOf(0);
	      try {
	        final InputStream is = getClass().getClassLoader().getResourceAsStream("LiberationSans-Regular.ttf");
	       final PDFont font = PDType0Font.load(document, is);
	        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
	        
	        text = "task.form.exportedFiles.title";
	        fontSize = 16;
	        textWidth = font.getStringWidth(text) / 1000 * fontSize;
	        textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
	        contentStream.beginText();
	        contentStream.setFont(font, fontSize);
	        yposition = page.getMediaBox().getHeight() - topMargin - textHeight;
	        contentStream.newLineAtOffset((page.getMediaBox().getWidth() - textWidth) / 2, page.getMediaBox().getHeight() - topMargin - textHeight);
	        contentStream.showText(text);
	        contentStream.endText();
	        
	        
	        
	        //alert
	        yposition = writeLine("Dojava", contentStream, 20, yposition, 40, 14, font);	        
	        yposition = writeLine("Vrijeme dojave: " + report.getEventDateTime(), contentStream, 20 + 10, yposition, 10, 12, font);
	        yposition = writeLine("Lokacija: " + report.getEventAddress().getCity().getCounty().getName(), contentStream, 20 + 10, yposition, 10, 12, font);
	        
	        //intervencija
	        yposition = writeLine("Intervencija", contentStream, 20, yposition, 20, 14, font);
	        yposition = writeLine("Vrsta intervencije: " + report.getEventType().getName(), contentStream, 20 + 10, yposition, 10, 12, font);
          
	        //SNAGE
	        yposition = writeLine("Snage na intervenciji", contentStream, 20, yposition, 20, 14, font);
	        PDFPosition yPosition = new PDFPosition();
	        yPosition.setPosition(yposition);
	        report.getEventOrganizationList().stream().forEach(eventOrg -> {
	          int counter = 1;
	          if(!OrganizationLevel.OPERATIONAL_LEVEL.equals(getCurrentUser().getActiveOrganizationObject().getLevel()) || eventOrg.getOrganization().getId().equals(getCurrentUser().getActiveOrganizationObject().getId())) {
	            yPosition.setPosition(writeLine(counter + ". " + eventOrg.getOrganization().getName(), contentStream, 20 + 10, yPosition.getPosition(), 15, 13, font));
	            
	            yPosition.setPosition(writeLine("Vremena postrojbe", contentStream, 20 + 10 + 10, yPosition.getPosition(), 10, 12, font));
	            yPosition.setPosition(writeLine("Izlatak: ", contentStream, 20 + 10 + 10 + 5, yPosition.getPosition(), 10, 11, font));
	            
	            yPosition.setPosition(writeLine("Vozila postrojbe", contentStream, 20 + 10 + 10, yPosition.getPosition(), 10, 12, font));
	            
	            yPosition.setPosition(writeLine("Vatrogasci", contentStream, 20 + 10 + 10, yPosition.getPosition(), 10, 12, font));
	          }else {
	            yPosition.setPosition(writeLine(counter + ". " + eventOrg.getOrganization().getName(), contentStream, 20 + 10, yPosition.getPosition(), 15, 13, font));
	          }
	          counter++;
	          
	         
            
	        });
          
	        
	        contentStream.close();
	        final ByteArrayOutputStream out = new ByteArrayOutputStream();
	        document.save(out);
	        document.close();

	        return new ByteArrayInputStream(out.toByteArray());
	      } catch (final IOException e) {
	        log.error("IOException while trying to generate pdf stream resource", e);
	        return null;
	      }

	    });
	   
	
	    exportAnchor.setHref(streamResource);
	    exportButton.clickInClient();
	  }

	/**
	 * Inits the grid.
	 */
	@Override
	protected void initGrid() {
		getGrid().removeAllColumns();
	    getGrid().setSelectionMode(SelectionMode.SINGLE);
	    getGrid().addSelectionListener(e ->{
	      deleteButton.setEnabled(!e.getFirstSelectedItem().isEmpty());
	      exportAnchor.setEnabled(!e.getFirstSelectedItem().isEmpty() && ReportStatus.APPROVED.equals(e.getFirstSelectedItem().get().getStatus()));
	    });

	    getGrid().addComponentColumn(report -> new RouterLink(report.getIdentificationNumber(), ReportView.class, report.getId().toString()))
	      .setHeader(getTranslation("reportsView.reportsGrid.identificationNumber"));
	    getGrid().addColumn(report -> report.getEventDateTime()).setHeader(getTranslation("reportsView.reportsGrid.eventDateTime"));
	    getGrid().addColumn(report -> getTranslation(report.getEventType().getEventTypeTranslationKey())).setHeader(getTranslation("reportsView.reportsGrid.eventType"));
	    getGrid().addColumn(report -> getTranslation(report.getStatus().getReportStatusTranslationKey())).setHeader(getTranslation("reportsView.reportsGrid.status"));
	    
	    getGrid().addItemClickListener(e -> {
	    	if(e.getClickCount() > 1) {
	    	  
	    		UI.getCurrent().navigate(ReportView.class, e.getItem().getId().toString());
	    	}
	    });
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
    return Routes.REPORTS;
  }
  
  
  private final class PDFPosition{
    @Setter
    @Getter
    private float position;
    
    public PDFPosition() {
      position = 0;
    }
  }



}
