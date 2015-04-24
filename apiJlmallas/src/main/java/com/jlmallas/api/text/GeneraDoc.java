/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.api.text;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author jorge-luis
 */
public class GeneraDoc {

    private XWPFDocument documento;

    public void crear() {
        documento = new XWPFDocument();
    }

    public void parrafo(String contenido, int aligment, int tamanioLetra, boolean bolder, int spacingAfter, int spacingBefore) {
        XWPFParagraph paragraph = this.documento.createParagraph();
        XWPFRun run = paragraph.createRun();
        paragraph.setSpacingAfter(spacingAfter);
        paragraph.setSpacingBefore(spacingBefore);
        run.setFontSize(tamanioLetra);
        run.setBold(bolder);
        if (aligment == 1) {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
        } else {
            if (aligment == 2) {
                paragraph.setAlignment(ParagraphAlignment.THAI_DISTRIBUTE);
            }
        }
        run.setText(contenido);
    }

    public void logo(InputStream is, int format, int width, int height) {
        try {
            int formato = 1;
            if (format == 1) {
                formato = documento.PICTURE_TYPE_JPEG;
            }
            XWPFParagraph p = documento.createParagraph();
            XWPFRun run = p.createRun();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void write(OutputStream out) {
        try {
            documento.write(out);
        } catch (Exception e) {
        }

    }

    public void tabla(String contenidos[][], int numeroFilas, int numeroColumnas, int alturaFila, int tamanioLetra, int aligment, boolean bolder) {
        XWPFTable table = this.documento.createTable(numeroFilas, numeroColumnas);
        List<XWPFTableRow> tableRows = table.getRows();
        ParagraphAlignment paragraphAlignment = ParagraphAlignment.CENTER;
        if (aligment == 1) {
            paragraphAlignment = ParagraphAlignment.CENTER;
        } else {
            if (aligment == 2) {
                paragraphAlignment = paragraphAlignment.THAI_DISTRIBUTE;
            }
        }
        int row = 1;
        for (int i = 0; i < numeroFilas; i++) {
            int j = 0;
            XWPFTableRow tableRow = tableRows.get(row);
            tableRow.setHeight(alturaFila);
            for (XWPFTableCell tableCell : tableRow.getTableCells()) {
                XWPFParagraph paragraph = tableCell.addParagraph();
                XWPFRun run = paragraph.createRun();
                run.setBold(bolder);
                run.setFontSize(tamanioLetra);
                paragraph.setAlignment(paragraphAlignment);
                run.setText(contenidos[i][j]);
                j++;
            }
            row++;
        }
    }

    public XWPFDocument getDocumento() {
        return documento;
    }

    public void setDocumento(XWPFDocument documento) {
        this.documento = documento;
    }

}
