package com.smart.contact.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;


public class HeaderFooterEvent extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            // Create a table for the header
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell("Header - Contact Details");

            // Create a table for the footer
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            footerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            footerTable.addCell("Page " + writer.getPageNumber());

            // Add header and footer tables to the document
            document.add(headerTable);
            document.add(footerTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
