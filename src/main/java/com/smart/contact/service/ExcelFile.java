package com.smart.contact.service;

import com.smart.contact.model.Contact;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelFile {

    Logger logger = LoggerFactory.getLogger(ExcelFile.class);
    public static String[] HEADERS = {
            "Contact_Id", "Contact_Name", "Conatct_NickName",
            "Contact_Work", "Contact_Email", "Conatct_PhoneNumber", "User_Name"};
    public static String SHEET_NAME = "contacts";

    public Workbook dataToExcel(List<Contact> list) throws IOException {
        //create work
        Workbook workbook = new XSSFWorkbook();


        try {

            //create sheet
            Sheet sheet = workbook.createSheet(SHEET_NAME);


            //create Row : Header Raw
            Row row = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }

            //Value raw

            int rowIndex = 1;
            for (Contact contact : list) {
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                dataRow.createCell(0).setCellValue("CONTACT" + contact.getcId());
                dataRow.createCell(1).setCellValue(contact.getName());
                dataRow.createCell(2).setCellValue(contact.getNickName());
                dataRow.createCell(3).setCellValue(contact.getWork());
                dataRow.createCell(4).setCellValue(contact.getEmail());
                dataRow.createCell(5).setCellValue(contact.getPhoneNumber());
                dataRow.createCell(6).setCellValue(contact.getUser().getName());
            }


            logger.info("Downloaded");
            return workbook;


        } catch (Exception e) {
            e.printStackTrace();
            logger.info("fail to input data to excel");
            return null;
        }

    }

}
