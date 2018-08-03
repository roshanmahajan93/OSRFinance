package com.example.chetu.osrfinance.Service;

import android.os.Environment;
import android.util.Log;

import com.example.chetu.osrfinance.Model.CustomerFinance;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Vinayak on 7/27/2018.
 */

public class CustomerReportPDF {
    public ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();

    public Boolean write(String fname, ArrayList<CustomerFinance> list, String cardNoVal, String custNameVal, String ContactVal, String AmountVal, String DailyAmtVal, String AmtDateVal) {
        ln = list;
        Log.e("List_size", "---->" + ln.size());
        Document doc = new Document();
        PdfWriter docWriter = null;


        try {
            File fullPath = new File(Environment.getExternalStorageDirectory() + "/OSRFinanceFile");
            String fpath = fullPath.toString();

            //String fpath = "/sdcard/" + fname + ".pdf";
            File file = new File(fpath + "/PPR_" + fname + ".pdf");

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            } else {
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    file.delete();
                }
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            Font custFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
            Font custFontVal = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL);
            Font titleFont = new Font(Font.FontFamily.COURIER, 18, Font.BOLD, new BaseColor(255, 255, 255));

            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(255, 255, 255));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, new BaseColor(255, 68, 68));


            //file path
            String path = file.toString();
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));

            //document header attributes
            doc.addAuthor("OSRFinance");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("Roshan Mahajan");
            doc.addTitle("Customer Report");
            doc.setPageSize(PageSize.A4);

            //open document
            doc.open();


            Paragraph prHead = new Paragraph();
            prHead.setFont(titleFont);
            prHead.add("");

            PdfPTable myTable = new PdfPTable(1);
            myTable.setWidthPercentage(100.0f);

            PdfPCell myCell = new PdfPCell(new Phrase("Customer Finance Report\n", titleFont));
            //myCell.setBorder(Rectangle.);

            myCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            myCell.setVerticalAlignment(Element.ALIGN_CENTER);
            //set the cell column span in case you want to merge two or more cells
            myCell.setBorderColor(new BaseColor(255, 68, 68));
            myCell.setBackgroundColor(new BaseColor(255, 68, 68));
            //myCell.setMinimumHeight(10f);
            myCell.setFixedHeight(30f);
            myTable.addCell(myCell);

            prHead.add("\n\n");
            prHead.setAlignment(Element.ALIGN_CENTER);

            doc.add(myTable);
            doc.add(prHead);

            //create a paragraph
            Paragraph custPara = new Paragraph();
            custPara.add("");
            float[] custColumnWidths = {1f, 1.5f, 1.5f, 2f, 4f};
            PdfPTable CustomerTable = new PdfPTable(1);
            CustomerTable.setWidthPercentage(90f);
            //---------1
            PdfPCell custCell1 = new PdfPCell(new Phrase("Card No : " + cardNoVal, custFont));
            custCell1.setBorder(Rectangle.NO_BORDER);
            custCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            custCell1.setMinimumHeight(10f);
            CustomerTable.addCell(custCell1);
            //---------2
            PdfPCell custCell2 = new PdfPCell(new Phrase("Customer Name : " + custNameVal, custFont));
            custCell2.setBorder(Rectangle.NO_BORDER);
            custCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            custCell2.setMinimumHeight(10f);
            CustomerTable.addCell(custCell2);
            //---------3
            PdfPCell custCell3 = new PdfPCell(new Phrase("Contact No : " + ContactVal, custFont));
            custCell3.setBorder(Rectangle.NO_BORDER);
            custCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            custCell3.setMinimumHeight(10f);
            CustomerTable.addCell(custCell3);
            //---------4
            PdfPCell custCell4 = new PdfPCell(new Phrase("Amount : " + AmountVal + "             Daily Amount : " + DailyAmtVal, custFont));
            custCell4.setBorder(Rectangle.NO_BORDER);
            custCell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            custCell4.setMinimumHeight(10f);
            CustomerTable.addCell(custCell4);
            //---------5
            PdfPCell custCell5 = new PdfPCell(new Phrase("Amount Date : " + AmtDateVal, custFont));
            custCell5.setBorder(Rectangle.NO_BORDER);
            custCell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            custCell5.setMinimumHeight(10f);
            CustomerTable.addCell(custCell5);


            custPara.add(CustomerTable);
            custPara.add("\n");
            doc.add(custPara);

            //create a paragraph
            Paragraph paragraph = new Paragraph("");

            //specify column widths
            float[] columnWidths = {1f, 1.5f, 1.5f, 2f, 4f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            //insert column headings
            insertCell(table, "Sr.No.", Element.ALIGN_CENTER, 1, bfBold12, "1");
            insertCell(table, "Date", Element.ALIGN_CENTER, 1, bfBold12, "1");
            insertCell(table, "Amount", Element.ALIGN_CENTER, 1, bfBold12, "1");
            insertCell(table, "Fine/Penalty", Element.ALIGN_CENTER, 1, bfBold12, "1");
            insertCell(table, "Remark", Element.ALIGN_CENTER, 1, bfBold12, "1");
            table.setHeaderRows(1);

            CustomerFinance customerFinance;

            //just some data to fill
            for (int x = 0; x < ln.size(); x++) {
                customerFinance = ln.get(x);
                insertCell(table, "" + (x + 1), Element.ALIGN_CENTER, 1, bf12, "");
                insertCell(table, "" + customerFinance.FDate, Element.ALIGN_CENTER, 1, bf12, "");
                insertCell(table, "" + customerFinance.FAmount, Element.ALIGN_CENTER, 1, bf12, "");
                insertCell(table, "" + customerFinance.FineAndPenalty, Element.ALIGN_LEFT, 1, bf12, "");
                insertCell(table, "" + customerFinance.FRemark, Element.ALIGN_LEFT, 1, bf12, "");
            }

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);

            if (doc != null) {
                //close the document
                doc.close();
            }
            if (docWriter != null) {
                //close the writer
                docWriter.close();

            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font, String temp) {


        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setBorderColor(new BaseColor(255, 68, 68));
        if (temp.equalsIgnoreCase("1")) {
            cell.setBackgroundColor(new BaseColor(255, 68, 68));
        }

        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);

        }
        //add the call to the table
        table.addCell(cell);

    }

}
