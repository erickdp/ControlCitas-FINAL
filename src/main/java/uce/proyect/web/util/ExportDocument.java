package uce.proyect.web.util;

import com.itextpdf.text.Element;
import com.lowagie.text.*;
import java.io.*;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.export.PDFOptions;

/**
 *
 * @author Erick
 */
@Getter
@Setter
@Named
@RequestScoped
public class ExportDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    private PDFOptions pdfOptions;

    @PostConstruct
    public void init() {
        PersonalizarPDF();
    }
    
    public void PersonalizarPDF() {
        this.pdfOptions = new PDFOptions();
        pdfOptions.setFacetBgColor("#009999");
        pdfOptions.setFacetFontColor("#ffffff");
    }

   //Metodo para agregar logo al PDF -- Ver que elementos se les puede agregar
    public void preProcessPDF(Object documento) throws IOException, BadElementException, DocumentException {

        Document pdf = (Document) documento;
        pdf.setPageSize(PageSize.LEGAL.rotate());
        pdf.setMargins(-10.10f, -30.10f, 80.5f, 20.5f);

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();

        Image logoInicio = Image.getInstance(servletContext.getRealPath("") + File.separator
                + "resources" + File.separator + "img" + File.separator + "logo.png");
        Image logoFooter = Image.getInstance(servletContext.getRealPath("") + File.separator
                + "resources" + File.separator + "img" + File.separator + "logo.png");

//        Tamano del logo principal
        logoInicio.setAlignment(Image.TOP);
        logoInicio.scaleAbsoluteHeight(40);
        logoInicio.scaleAbsoluteWidth(40);
        logoInicio.scalePercent(100);

        if (!pdf.isOpen()) {
            pdf.open();
            pdf.add(Image.getInstance(logoInicio));
        }

//        Pone al logo en cada pagina
//        Si se quiere que desde la siguiente pagina se agrege la imagen al pie de pagina se debe de poner esto luego, sino aparece desde la primera
        logoFooter.setAlignment(Image.ALIGN_LEFT);
        logoFooter.scaleAbsoluteHeight(10);
        logoFooter.scaleAbsoluteWidth(10);
        logoFooter.scalePercent(30);
        Chunk chunk = new Chunk(logoFooter, 0, 0);
        HeaderFooter footer = new HeaderFooter(new Phrase(chunk), false);
        footer.setAlignment(Element.ALIGN_LEFT);
        footer.setBorder(Rectangle.NO_BORDER);
        pdf.setFooter(footer);
    }

}
