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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FrmLaporanPemesanan extends javax.swing.JDialog {

    /**
     * Creates new form FrmDaftar
     */
   
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    JMenuItem menuItemAdd, menuItemRemove, menuItemUpdate;
    JPopupMenu popupMenu;
    
    public FrmLaporanPemesanan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        //showTable();
        
    }
    
    public void showTable(){
        
        String statusPembayaran = statusPembayaranCB.getSelectedItem().toString();
        
        if(statusPembayaran.equals("Semua")){
            statusPembayaran = "%%";
        }
        
        
        Date dateFrom = tglFrom.getDate();
        Date dateTo = tglTo.getDate();
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("No.");
        model.addColumn("No. Pemesanan"); 
        model.addColumn("Tanggal"); 
        model.addColumn("No.KTP");
        model.addColumn("Nama Paket");
        model.addColumn("Jenis Pembayaran");
        model.addColumn("Status Pembayaran");
        
        dataTable.setModel(model);
        
        String sql = "SELECT * FROM tbl_pemesanan WHERE DATE(tgl_pemesanan) BETWEEN ? AND ? AND status_pembayaran LIKE ?";
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dateFrom.getTime()));
            ps.setDate(2, new java.sql.Date(dateTo.getTime()));
            ps.setString(3, statusPembayaran);
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{cnt++, 
                    rs.getString("no_pemesanan"), 
                    rs.getString("tgl_pemesanan"),
                    rs.getString("no_ktp"),
                    rs.getString("nama_paket"),
                    rs.getString("jns_pembayaran"),
                    rs.getString("status_pembayaran"),
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
        
        String statusPembayaran = statusPembayaranCB.getSelectedItem().toString();
        if(statusPembayaran.equals("Semua")){
            statusPembayaran = "%%";
        }
        
        Date dateFrom = tglFrom.getDate();
        Date dateTo = tglTo.getDate();
        
        Object[] header = {"No", "No. Pemesanan", "Tgl Pemesanan", "No. KTP", "Nama Paket", "Jenis Pembayaran", "Status Pembayaran"};
        
        String sql = "SELECT * FROM tbl_pemesanan WHERE DATE(tgl_pemesanan) BETWEEN ? AND ? AND status_pembayaran LIKE ?";
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        List dataList = new ArrayList();
        
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dateFrom.getTime()));
            ps.setDate(2, new java.sql.Date(dateTo.getTime()));
            ps.setString(3, statusPembayaran);
            rs = ps.executeQuery();
            
            int i = 0;
            while (rs.next()){
                Map dataMap = new HashMap();
                dataMap.put("no", cnt++);
                dataMap.put("no_pemesanan", rs.getString("no_pemesanan"));
                dataMap.put("tgl_pemesanan", rs.getString("tgl_pemesanan"));
                dataMap.put("no_ktp", rs.getString("no_ktp"));
                dataMap.put("nama_paket", rs.getString("nama_paket"));
                dataMap.put("jns_pembayaran", rs.getString("jns_pembayaran"));
                dataMap.put("status_pembayaran", rs.getString("status_pembayaran"));
                
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
            cell.setCellValue((String)dataMap.get("no_pemesanan"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("tgl_pemesanan"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("no_ktp"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("nama_paket"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("jns_pembayaran"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("status_pembayaran"));
            
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
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_pemesanan.docx";
        
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        
        FileOutputStream out = new FileOutputStream(FILE_NAME);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText("Laporan Daftar Pemesanan");
        run.setFontSize(20);
        run.setBold(true);
        
        //create table
        XWPFTable table = document.createTable();
        table.setCellMargins(100, 100, 100, 100);
        

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);      
        tableRowOne.getCell(0).setText("No.");
        tableRowOne.addNewTableCell().setText("No. Pemesanan");
        tableRowOne.addNewTableCell().setText("Tgl Pemesanan");
        tableRowOne.addNewTableCell().setText("No. KTP");
        tableRowOne.addNewTableCell().setText("Nama Paket");
        tableRowOne.addNewTableCell().setText("Jenis Pembayaran");
        tableRowOne.addNewTableCell().setText("Status Pembayaran");
        
        String statusPembayaran = statusPembayaranCB.getSelectedItem().toString();
        
        if(statusPembayaran.equals("Semua")){
            statusPembayaran = "%%";
        }
        
        Date dateFrom = tglFrom.getDate();
        Date dateTo = tglTo.getDate();
        
        con = new DBUtils().getKoneksi();
        List dataList = new ArrayList();
        
        String sql = "SELECT * FROM tbl_pemesanan WHERE DATE(tgl_pemesanan) BETWEEN ? AND ? AND status_pembayaran LIKE ?";
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dateFrom.getTime()));
            ps.setDate(2, new java.sql.Date(dateTo.getTime()));
            ps.setString(3, statusPembayaran);
            rs = ps.executeQuery();
            
            while (rs.next()){
                Map dataMap = new HashMap();
                dataMap.put("no", cnt++);
                dataMap.put("no_pemesanan", rs.getString("no_pemesanan"));
                dataMap.put("tgl_pemesanan", rs.getString("tgl_pemesanan"));
                dataMap.put("no_ktp", rs.getString("no_ktp"));
                dataMap.put("nama_paket", rs.getString("nama_paket"));
                dataMap.put("jns_pembayaran", rs.getString("jns_pembayaran"));
                dataMap.put("status_pembayaran", rs.getString("status_pembayaran"));
                
                dataList.add(dataMap);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        for(int i = 0; i < dataList.size(); i++){
            Map dataMap = (Map) dataList.get(i);

            XWPFTableRow tableRowTwo = table.createRow();
            tableRowTwo.getCell(0).setText(dataMap.get("no").toString());
            tableRowTwo.getCell(1).setText((String) dataMap.get("no_pemesanan"));
            tableRowTwo.getCell(2).setText((String) dataMap.get("tgl_pemesanan"));
            tableRowTwo.getCell(3).setText((String) dataMap.get("no_ktp"));
            tableRowTwo.getCell(4).setText((String) dataMap.get("nama_paket"));
            tableRowTwo.getCell(5).setText((String) dataMap.get("jns_pembayaran"));
            tableRowTwo.getCell(6).setText((String) dataMap.get("status_pembayaran"));
            
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
        tglFrom = new com.toedter.calendar.JDateChooser();
        tglTo = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        statusPembayaranCB = new javax.swing.JComboBox<>();
        btnCari = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        exportPdf = new javax.swing.JButton();
        exportExcel = new javax.swing.JButton();
        exportWord = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
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
        jLabel1.setText("Laporan Pemesanan ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jLabel3.setText("Status Pembayaran ");

        statusPembayaranCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Belum Lunas", "Lunas" }));

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cari-16.png"))); // NOI18N
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

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
                    .addComponent(tglFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusPembayaranCB, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglTo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addGap(62, 62, 62))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tglFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(statusPembayaranCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        exportPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Adobe_Reader_PDF.png"))); // NOI18N
        exportPdf.setText("Unduh PDF");

        exportExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Excel.png"))); // NOI18N
        exportExcel.setText("Unduh Excel");
        exportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportExcelActionPerformed(evt);
            }
        });

        exportWord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Word.png"))); // NOI18N
        exportWord.setText("Unduh Word");
        exportWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportWordActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/profile-16.png"))); // NOI18N
        jButton4.setText("Detail Pesanan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportWord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportExcel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportPdf)
                .addGap(27, 27, 27))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exportExcel)
                        .addComponent(exportWord)
                        .addComponent(jButton4))
                    .addComponent(exportPdf))
                .addContainerGap())
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

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        Date dateFrom = tglFrom.getDate();
        Date dateTo = tglTo.getDate();
        
        if(dateFrom == null || dateTo == null){
            JOptionPane.showMessageDialog(null, "Silakan isi tanggal pemesanan", "Error", JOptionPane.ERROR_MESSAGE);
            
            return;
        }
        
        showTable();
    }//GEN-LAST:event_btnCariActionPerformed

    private void exportWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportWordActionPerformed
        try {
            generateWord();
        } catch (IOException ex) {
            Logger.getLogger(FrmLaporanPemesanan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_exportWordActionPerformed

    private void exportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportExcelActionPerformed
        generateExcel();
    }//GEN-LAST:event_exportExcelActionPerformed

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
            java.util.logging.Logger.getLogger(FrmLaporanPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLaporanPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLaporanPemesanan dialog = new FrmLaporanPemesanan(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCari;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton exportExcel;
    private javax.swing.JButton exportPdf;
    private javax.swing.JButton exportWord;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jumlahLabel;
    private javax.swing.JComboBox<String> statusPembayaranCB;
    private com.toedter.calendar.JDateChooser tglFrom;
    private com.toedter.calendar.JDateChooser tglTo;
    // End of variables declaration//GEN-END:variables


}
