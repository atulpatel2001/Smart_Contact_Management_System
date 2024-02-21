package com.smart.contact.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.smart.contact.model.Contact;
import org.springframework.stereotype.Service;


import java.io.FileOutputStream;

import java.util.List;
@Service
public class PdfService{

    public String generatePDFForContact(String pdfName,List<Contact> contacts) {
        // Create a new document
        Document document = new Document();
        try {

            // Create a PDF writer
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfName));
            // Create a header event
            HeaderFooterEvent event = new HeaderFooterEvent();
            writer.setPageEvent(event);

            // Open the document for writing
            document.open();

            // Create a table with 3 columns
            PdfPTable table = new PdfPTable(6);

            // Add table headers
            table.addCell("Id");
            table.addCell("Name");
            table.addCell("NickName");
            table.addCell("Work");
            table.addCell("Email_Id");
            table.addCell("PhoneNumber");

            // Generate dynamic data for the table
            // You can replace this with your own logic to populate the table
            // ...

            // Add data rows to the table
            for (Contact contact:contacts) {
                table.addCell("CONTACT"+contact.getcId());
                table.addCell(contact.getName());
                table.addCell(contact.getNickName());
                table.addCell(contact.getWork());
                table.addCell(contact.getEmail());
                table.addCell(contact.getPhoneNumber());


            }
            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            // Get the PDF content as bytes
            return pdfName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
