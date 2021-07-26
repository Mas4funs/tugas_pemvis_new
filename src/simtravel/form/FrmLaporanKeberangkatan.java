/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import simtravel.utils.DBUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author nursalim
 */
public class FrmLaporanKeberangkatan extends javax.swing.JDialog {

    /**
     * Creates new form FrmDaftar
     */
   
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    JMenuItem menuItemAdd, menuItemRemove, menuItemUpdate;
    JPopupMenu popupMenu;
    
    public FrmLaporanKeberangkatan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        showTable();
        
    }
    
    public void showTable(){
        String kataKunci = "";
        
        
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("No."); 
        model.addColumn("Nama Maskapai"); 
        model.addColumn("No. Pesawat");
        model.addColumn("Bandara");
        model.addColumn("Kelas");
        model.addColumn("Tarif");
        dataTable.setModel(model);
        
        String sql = "";
        
        if("Nama Maskapai".equals("")){
            sql = "SELECT * FROM tbl_maskapai WHERE nama LIKE ?";
        }else{
            sql = "SELECT * FROM tbl_maskapai WHERE no_pesawat LIKE ? ";
        }
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, kataKunci);
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{cnt++, 
                    rs.getString("nama"), 
                    rs.getString("no_pesawat"),
                    rs.getString("bandara"),
                    rs.getString("kelas"),
                    rs.getString("tarif")
                    } 
                );
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
     
        jumlahLabel.setText(String.valueOf(dataTable.getRowCount()));
        model.fireTableDataChanged();
        
        
        dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
        dataTable.setComponentPopupMenu(showPopupMenu());
        
        dataTable.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent event) {
                Point point = event.getPoint();
                int currentRow = dataTable.rowAtPoint(point);
                System.out.println("currentRow == "+currentRow);
                dataTable.setRowSelectionInterval(currentRow, currentRow);
            }
        });
    }
    
    public void hapusRecord(String kode){
        String sql = "DELETE FROM tbl_maskapai WHERE no_pesawat = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, kode);
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil di hapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal di hapus", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        showTable();
    }
    
    public JPopupMenu showPopupMenu(){
        popupMenu = new JPopupMenu();
        menuItemAdd = new JMenuItem("Tambah Data");
        menuItemAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/add_plus-16.png")));
        menuItemUpdate = new JMenuItem("Ubah Data");
        menuItemUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/edit-16.png")));
        menuItemRemove = new JMenuItem("Hapus Data");
        menuItemRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/hapus-16.png")));
 
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemUpdate);
        
        menuItemAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map data = new HashMap();
                data.put("action", "tambah");
                new FrmTambahMaskapai(null, true, data).setVisible(true);
            }
        });
        
        menuItemUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = dataTable.getSelectedRow();
                String namaHotel = (String)dataTable.getValueAt(i, 1);
                String lokasi = (String)dataTable.getValueAt(i, 2);


                Map data = new HashMap();
                data.put("action", "edit");
                data.put("namaHotel", namaHotel);
                data.put("lokasi", lokasi);
                new FrmTambahMaskapai(null, true, data).setVisible(true);
            }
        });
        
        menuItemRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dataTable.getSelectedRow() == -1){
                    JOptionPane.showMessageDialog(null, "Silakan pilih Data yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int i = dataTable.getSelectedRow();
                String kode = (String) dataTable.getValueAt(i, 1);
                System.out.println("kode == "+kode);

                int pilih = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
                if(pilih == JOptionPane.OK_OPTION){
                    hapusRecord(kode);
                }
            }
        });
        
        return popupMenu;
    }
    
    public void generatePdf(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
               
         try {
            URL url = getClass().getResource("/simtravel/report/rpt_maskapai.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, new DBUtils().getKoneksi());

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);          
            
            JDialog dialog = new JDialog(this);
            dialog.setContentPane(jasperViewer.getContentPane());
            dialog.setSize(jasperViewer.getSize());
            dialog.setTitle("Report Data Pengguna");
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
    
    public void generatePdfOld(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        File dir = new File("D:/tmp/");
        if(!dir.exists()){
            try{
                dir.mkdirs();
            }catch(Exception iex){
                iex.printStackTrace();
            }
            
        }
        
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_maskapai.pdf";
         try {
            File file = new File("src/simtravel/report/rpt_maskapai.jrxml");
            jasperDesign = JRXmlLoader.load(file);
            
            Map param = new HashMap();

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, new DBUtils().getKoneksi());

            //JasperViewer.viewReport(jasperPrint, false);
             JasperExportManager jem = new JasperExportManager();
             jem.exportReportToPdfFile(jasperPrint, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        new DBUtils().openFile(FILE_NAME);
    }
    
    public void generateExcel(){
        File dir = new File("D:/tmp/");
        if(!dir.exists()){
            try{
                dir.mkdirs();
            }catch(Exception iex){
                iex.printStackTrace();
            }
            
        }
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_maskapai.xlsx";
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data");
        
        Object[] header = {"No", "Nama Maskapai", "No. Pesawat", "Bandara", "Kelas", "Tarif"};
        
        
        String sql = "SELECT * FROM tbl_maskapai";
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        List dataList = new ArrayList();
        
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()){
                Map dataMap = new HashMap();
                dataMap.put("no", cnt++);
                dataMap.put("nama", rs.getString("nama"));
                dataMap.put("no_pesawat", rs.getString("no_pesawat"));
                dataMap.put("bandara", rs.getString("bandara"));
                dataMap.put("kelas", rs.getString("kelas"));
                dataMap.put("tarif", rs.getString("tarif"));
                
                dataList.add(dataMap);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        int rowNum = 0;
        System.out.println("Creating excel");
   
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        for (Object field : header) {
            Cell cell = row.createCell(colNum++);
            if (field instanceof String) {
                cell.setCellValue((String) field);
            } else if (field instanceof Integer) {
                cell.setCellValue((Integer) field);
            }
        }
        
        // looping data
        for(int i = 0; i < dataList.size(); i++){
            row = sheet.createRow(rowNum++);
            
            Map dataMap = (Map) dataList.get(i);
            
            colNum = 0;
                       
            Cell cell = row.createCell(colNum++);
            cell.setCellValue((Integer)dataMap.get("no"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("nama"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("no_pesawat"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("bandara"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("kelas"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("tarif"));
            
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
        
        new DBUtils().openFile(FILE_NAME);
    }
    
    public void generateWord() throws FileNotFoundException, IOException{
        File dir = new File("D:/tmp/");
        if(!dir.exists()){
            try{
                dir.mkdirs();
            }catch(Exception iex){
                iex.printStackTrace();
            }
            
        }
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_maskapai.docx";
        
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        
        FileOutputStream out = new FileOutputStream(FILE_NAME);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText("Laporan Daftar Maskapai");
        run.setFontSize(20);
        run.setBold(true);
        
        //create table
        XWPFTable table = document.createTable();
        table.setCellMargins(100, 100, 100, 100);
        

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);      
        tableRowOne.getCell(0).setText("No.");
        tableRowOne.addNewTableCell().setText("Nama Maskapai");
        tableRowOne.addNewTableCell().setText("No. Pesawat");
        tableRowOne.addNewTableCell().setText("Bandara");
        tableRowOne.addNewTableCell().setText("Kelas");
        tableRowOne.addNewTableCell().setText("Tarif");
        
        String sql = "SELECT * FROM tbl_maskapai";
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        List dataList = new ArrayList();
        
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()){
                Map dataMap = new HashMap();
                dataMap.put("no", cnt++);
                dataMap.put("nama", rs.getString("nama"));
                dataMap.put("no_pesawat", rs.getString("no_pesawat"));
                dataMap.put("bandara", rs.getString("bandara"));
                dataMap.put("kelas", rs.getString("kelas"));
                dataMap.put("tarif", rs.getString("tarif"));
                
                dataList.add(dataMap);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        for(int i = 0; i < dataList.size(); i++){
            Map dataMap = (Map) dataList.get(i);

            XWPFTableRow tableRowTwo = table.createRow();
            tableRowTwo.getCell(0).setText(dataMap.get("no").toString());
            tableRowTwo.getCell(1).setText((String) dataMap.get("nama"));
            tableRowTwo.getCell(2).setText((String) dataMap.get("no_pesawat"));
            tableRowTwo.getCell(3).setText((String) dataMap.get("bandara"));
            tableRowTwo.getCell(4).setText((String) dataMap.get("kelas"));
            tableRowTwo.getCell(4).setText((String) dataMap.get("tarif"));
            
        }
        document.write(out);
        System.out.println("Done");
        
        new DBUtils().openFile(FILE_NAME);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        expotXls = new javax.swing.JLabel();
        exportPdf = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jumlahLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistem Informasi Travel Umrah & Haji - PT. Ismata Nusantara Abadi");

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/report-32.png"))); // NOI18N
        jLabel1.setText("Laporan Keberangkatan/Kepulangan  ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Tanggal Pemesanan ");

        jLabel3.setText("Status ");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Belum Berangkat", "Berangkat", "Pulang" }));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cari-16.png"))); // NOI18N
        jButton1.setText("Cari");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(62, 62, 62))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Aksi ");

        expotXls.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Excel.png"))); // NOI18N
        expotXls.setText("Cetak Excel");
        expotXls.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expotXlsMouseClicked(evt);
            }
        });

        exportPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Adobe_Reader_PDF.png"))); // NOI18N
        exportPdf.setText("Cetak PDF");
        exportPdf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportPdfMouseClicked(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Word.png"))); // NOI18N
        jLabel6.setText("Cetak Word");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(expotXls)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportPdf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(exportPdf)
                    .addComponent(jLabel4)
                    .addComponent(expotXls))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(dataTable);

        jLabel5.setText("Jumlah Data ");

        jumlahLabel.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jumlahLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(31, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(33, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(148, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jumlahLabel))
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(43, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportPdfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdfMouseClicked
        generatePdf();
    }//GEN-LAST:event_exportPdfMouseClicked

    private void expotXlsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expotXlsMouseClicked
        generateExcel();
    }//GEN-LAST:event_expotXlsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanKeberangkatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanKeberangkatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanKeberangkatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanKeberangkatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLaporanKeberangkatan dialog = new FrmLaporanKeberangkatan(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable dataTable;
    private javax.swing.JLabel exportPdf;
    private javax.swing.JLabel expotXls;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jumlahLabel;
    // End of variables declaration//GEN-END:variables


}
