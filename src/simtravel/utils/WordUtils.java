/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author Nursalim
 */
public class WordUtils {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File("createdocument.docx"));

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("Laporan Pengguna");
        run.setFontSize(20);
        run.setBold(true);
        
      //create table
      XWPFTable table = document.createTable();
		
      //create first row
      XWPFTableRow tableRowOne = table.getRow(0);      
      tableRowOne.getCell(0).setText("col one, row one");
      tableRowOne.addNewTableCell().setText("col two, row one");
      tableRowOne.addNewTableCell().setText("col three, row one");
      
		
      //create second row
      XWPFTableRow tableRowTwo = table.createRow();
      tableRowTwo.getCell(0).setText("col one, row two");
      tableRowTwo.getCell(1).setText("col two, row two");
      tableRowTwo.getCell(2).setText("col three, row two");
		
      //create third row
      XWPFTableRow tableRowThree = table.createRow();
      tableRowThree.getCell(0).setText("col one, row three");
      tableRowThree.getCell(1).setText("col two, row three");
      tableRowThree.getCell(2).setText("col three, row three");
        
        
        document.write(out);
        out.close();
        System.out.println("createdocument.docx written successully");
        
        

    }
}
