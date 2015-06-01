/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.poi;

import com.itextpdf.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
public class GeneraPdf {

    private Document document;
    List<Chunk> chunks = new ArrayList<>();

    public void crear(String page) {
        this.document = new Document(PageSize.getRectangle(page));
        this.chunks = new ArrayList<>();
    }

    public void addElemento(Element element) {
        try {
            document.add(element);
        } catch (Exception e) {
        }

    }

    public void open() {
        this.document.open();
    }

    public void close() {
        this.document.close();
    }

    public void printerWriter(ByteArrayOutputStream baos) {
        try {
            PdfWriter.getInstance(document, baos);
            document.setMargins(20f, 20f, 20f, 20f);
        } catch (Exception e) {
        }

    }

    public HeaderFooter header() {
        Phrase phrase = new Phrase();
        for (Chunk chunk : chunks) {
            phrase.add(chunk);
        }
        HeaderFooter header = new HeaderFooter(phrase, true);
        header.setBorder(Rectangle.NO_BORDER);
        return header;
    }

    public void footer(String contenido, String tipoLetra, int tamanioLetra, boolean bolder, int aligment) {
        int bold = 1;
        if (bolder) {
            bold = Font.BOLD;
        } else {
            bold = Font.NORMAL;
        }
        if (aligment == 1) {
            aligment = Element.ALIGN_CENTER;
        } else {
            if (aligment == 2) {
                aligment = Element.ALIGN_JUSTIFIED;
            }
        }
        Font font = FontFactory.getFont(tipoLetra, tamanioLetra, bold);
        HeaderFooter footer = new HeaderFooter(new Phrase(contenido, new Font(font)), true);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
    }

    public Paragraph parrafo(String texto, String tipoLetra, int tamanioLetra, float spacingAfter, float spacingBefore, boolean bolder, int aligment) {
        int bold = 1;
        if (bolder) {
            bold = Font.BOLD;
        } else {
            bold = Font.NORMAL;
        }
        Font font = FontFactory.getFont(tipoLetra, tamanioLetra, bold);
        Paragraph parrafo = new Paragraph(texto, font);
        parrafo.setSpacingAfter(spacingAfter);
        parrafo.setSpacingBefore(spacingBefore);
        if (aligment == 1) {
            aligment = Element.ALIGN_CENTER;
        } else {
            if (aligment == 2) {
                aligment = Element.ALIGN_JUSTIFIED;
            }
        }
        parrafo.setAlignment(aligment);
        return parrafo;
    }

    public PdfPTable tabla(List<String> titulos, List<String> contenidos, String tipoLetra, int tamanioLetraTitulos, int tamanioLetraContentenidos, boolean bolderTitulos, boolean bolderContenidos,
            int padding, int aligmentTitulos, int aligmentContentenidos, float spacingAfter, float spacingBefore) {
        PdfPTable tabla = new PdfPTable(titulos.size());
        Font fontParrafo = FontFactory.getFont(tipoLetra, tamanioLetraContentenidos);
        if (aligmentContentenidos == 1) {
            aligmentContentenidos = Element.ALIGN_CENTER;
        } else {
            if (aligmentContentenidos == 2) {
                aligmentContentenidos = Element.ALIGN_JUSTIFIED;
            }
        }

        tabla.setWidthPercentage(100f);
        tabla.setSpacingAfter(spacingAfter);
        tabla.setHorizontalAlignment(aligmentContentenidos);
        titulosTabla(tabla, titulos, tipoLetra, tamanioLetraTitulos, bolderTitulos, aligmentTitulos, padding);
        for (String contenido : contenidos) {
            PdfPCell cell = new PdfPCell();
            cell = new PdfPCell(new Phrase(contenido, fontParrafo));
            cell.setPadding(padding);
            cell.setHorizontalAlignment(aligmentContentenidos);
            tabla.addCell(cell);
        }
        return tabla;
    }

    public void titulosTabla(PdfPTable tabla, List<String> titulos, String tipoLetra, int tamanioLetra, boolean bolder, int aligment, int padding) {
        int bold = 1;
        if (bolder) {
            bold = Font.BOLD;
        } else {
            bold = Font.NORMAL;
        }
        Font fontHeader = FontFactory.getFont(tipoLetra, tamanioLetra, bold);
        if (aligment == 1) {
            aligment = Element.ALIGN_CENTER;
        } else {
            if (aligment == 2) {
                aligment = Element.ALIGN_JUSTIFIED;
            }
        }
        for (String titulo : titulos) {
            PdfPCell cell = new PdfPCell(new Phrase(titulo, fontHeader));
            cell.setPadding(padding);
            cell.setHorizontalAlignment(aligment);
            tabla.addCell(cell);
        }
    }

    public Image logoPath(String logo, float positionX, float positionY, int tamanio) {
        Image imageLogo = null;
        try {
            imageLogo = Image.getInstance(logo);
            imageLogo.scaleToFit(tamanio, tamanio);
            imageLogo.setAbsolutePosition(positionX, positionY);
        } catch (Exception e) {
        }
        return imageLogo;
    }

    public Image logoBytes(byte[] logo, float positionX, float positionY, int tamanio) {
        Image imageLogo = null;
        try {
            imageLogo = Image.getInstance(logo);
            imageLogo.scaleToFit(tamanio, tamanio);
            imageLogo.setAbsolutePosition(positionX, positionY);
        } catch (Exception e) {
        }
        return imageLogo;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

}
