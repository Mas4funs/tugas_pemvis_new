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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import simtravel.utils.CurrencyUtils;

/**
 *
 * @author nursalim
 */
public class FrmDaftarPaketHaji extends javax.swing.JDialog {

    /**
     * Creates new form FrmDaftar
     */
   
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    JMenuItem menuItemAdd, menuItemRemove, menuItemUpdate;
    JPopupMenu popupMenu;
    
    public FrmDaftarPaketHaji(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        showTable();
        
    }
    
    public void showTable(){
        String kataKunci = "";
        String kataKunciStr = ktKunciField.getText().trim();
        
        if(kataKunciStr == null || "".equals(kataKunciStr)){
            kataKunci = "%";
        }else{
            kataKunci = "%"+kataKunciStr+"%";
        }
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("No."); 
        model.addColumn("Nama Paket"); 
        model.addColumn("Hotel");
        model.addColumn("Maskapai");
        model.addColumn("Transportasi");
        model.addColumn("Fasilitas");
        model.addColumn("Harga");
        dataTable.setModel(model);
        
        String sql = "";
        
        if("Semua".equals(cbPengguna.getSelectedItem())){
            sql = "SELECT * FROM tbl_paket_haji WHERE nama_paket LIKE ? OR hotel LIKE ? OR maskapai LIKE ? OR transportasi LIKE ? OR harga LIKE ?";
        }else if("Nama Paket".equals(cbPengguna.getSelectedItem())){
            sql = "SELECT * FROM tbl_paket_haji WHERE nama_paket LIKE ? ";
        }else if("Hotel".equals(cbPengguna.getSelectedItem())){
            sql = "SELECT * FROM tbl_paket_haji WHERE hotel LIKE ? ";
        }else if("Maskapai".equals(cbPengguna.getSelectedItem())){
            sql = "SELECT * FROM tbl_paket_haji WHERE maskapai LIKE ? ";
        }else if("Harga".equals(cbPengguna.getSelectedItem())){
            sql = "SELECT * FROM tbl_paket_haji WHERE harga LIKE ? ";
        }
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            
            if("Semua".equals(cbPengguna.getSelectedItem())){
                ps.setString(1, kataKunci);
                ps.setString(2, kataKunci);
                ps.setString(3, kataKunci);
                ps.setString(4, kataKunci);
                ps.setString(5, kataKunci);
            }else{
                ps.setString(1, kataKunci);
            }
                        
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{cnt++, 
                    rs.getString("nama_paket"), 
                    rs.getString("hotel"),
                    rs.getString("maskapai"),
                    rs.getString("transportasi"),                    
                    rs.getString("fasilitas"),
                    new CurrencyUtils().formatRupiah(new BigDecimal(rs.getString("harga"))),
                    } 
                );
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Menampilkan Data", "Error", JOptionPane.ERROR_MESSAGE);
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
        String sql = "DELETE FROM tbl_paket_haji WHERE nama_paket = ? ";
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
        popupMenu.add(menuItemUpdate);
        popupMenu.add(menuItemRemove);
        
        menuItemAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map data = new HashMap();
                data.put("action", "tambah");
                new FrmTambahPaketHajiNew(null, true, data).setVisible(true);
            }
        });
        
        menuItemUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = dataTable.getSelectedRow();
                String namaPaket = (String)dataTable.getValueAt(i, 1);
                String hotel = (String)dataTable.getValueAt(i, 2);
                String maskapai = (String)dataTable.getValueAt(i, 3);
                String transportasi = (String)dataTable.getValueAt(i, 4);
                String fasilitas = (String)dataTable.getValueAt(i, 5);
                String harga = (String)dataTable.getValueAt(i, 6);


                Map data = new HashMap();
                data.put("action", "edit");
                data.put("namaPaket", namaPaket);
                data.put("hotel", hotel);
                data.put("maskapai", maskapai);
                data.put("transportasi", transportasi);
                data.put("fasilitas", fasilitas);
                data.put("harga", new CurrencyUtils().unFormatRupiah(harga));
                new FrmTambahPaketHajiNew(null, true, data).setVisible(true);
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
            URL url = getClass().getResource("/simtravel/report/rpt_paket_haji.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, new DBUtils().getKoneksi());

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);          
            
            JDialog dialog = new JDialog(this);
            dialog.setContentPane(jasperViewer.getContentPane());
            dialog.setSize(jasperViewer.getSize());
            dialog.setTitle("Laporan Daftar Paket Haji");
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
        
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_paket_haji.pdf";
         try {
            File file = new File("src/simtravel/report/rpt_paket_haji.jrxml");
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
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_paket_haji.xlsx";
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data");
        
        Object[] header = {"No", "Nama Paket", "Hotel", "Maskapai", "Transportasi", "Fasilitas", "Harga"};
        
        
        String sql = "SELECT * FROM tbl_paket_haji";
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
                dataMap.put("nama_paket", rs.getString("nama_paket"));
                dataMap.put("hotel", rs.getString("hotel"));
                dataMap.put("maskapai", rs.getString("maskapai"));
                dataMap.put("transportasi", rs.getString("transportasi"));
                dataMap.put("fasilitas", rs.getString("fasilitas"));
                dataMap.put("harga", new CurrencyUtils().formatRupiah(new BigDecimal(rs.getString("harga"))));                
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
            cell.setCellValue((String)dataMap.get("nama_paket"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("hotel"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("maskapai"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("transportasi"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("fasilitas"));
            cell = row.createCell(colNum++);
            cell.setCellValue((String)dataMap.get("harga"));
            
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
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_paket_haji.docx";
        
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        
        FileOutputStream out = new FileOutputStream(FILE_NAME);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText("Laporan Daftar Paket Haji");
        run.setFontSize(20);
        run.setBold(true);
        
        //create table
        XWPFTable table = document.createTable();
        table.setCellMargins(100, 100, 100, 100);
                
        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);      
        tableRowOne.getCell(0).setText("No.");
        tableRowOne.addNewTableCell().setText("Nama Paket");
        tableRowOne.addNewTableCell().setText("Hotel");
        tableRowOne.addNewTableCell().setText("Maskapai");
        tableRowOne.addNewTableCell().setText("Maskapai");
        tableRowOne.addNewTableCell().setText("Fasilitas");
        tableRowOne.addNewTableCell().setText("Harga");
        
        String sql = "SELECT * FROM tbl_paket_haji";
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
                dataMap.put("nama_paket", rs.getString("nama_paket"));
                dataMap.put("hotel", rs.getString("hotel"));
                dataMap.put("maskapai", rs.getString("maskapai"));
                dataMap.put("transportasi", rs.getString("transportasi"));
                dataMap.put("fasilitas", rs.getString("fasilitas"));
                dataMap.put("harga", new CurrencyUtils().formatRupiah(new BigDecimal(rs.getString("harga"))));
                
                dataList.add(dataMap);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        for(int i = 0; i < dataList.size(); i++){
            Map dataMap = (Map) dataList.get(i);

            XWPFTableRow tableRowTwo = table.createRow();
            tableRowTwo.getCell(0).setText(dataMap.get("no").toString());
            tableRowTwo.getCell(1).setText((String) dataMap.get("nama_paket"));
            tableRowTwo.getCell(2).setText((String) dataMap.get("hotel"));
            tableRowTwo.getCell(3).setText((String) dataMap.get("maskapai"));
            tableRowTwo.getCell(4).setText((String) dataMap.get("transportasi"));
            tableRowTwo.getCell(5).setText((String) dataMap.get("fasilitas"));
            tableRowTwo.getCell(6).setText((String) dataMap.get("harga"));
            
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
        jLabel3 = new javax.swing.JLabel();
        cbPengguna = new javax.swing.JComboBox<>();
        ktKunciField = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tambahBtn = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        expotXls = new javax.swing.JLabel();
        exportPdf = new javax.swing.JLabel();
        exportWord = new javax.swing.JLabel();
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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/umrah-32.png"))); // NOI18N
        jLabel1.setText("Daftar Paket Haji");

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

        jLabel2.setText("Cari Berdasarkan ");

        jLabel3.setText("Kata Kunci ");

        cbPengguna.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Nama Paket", "Hotel", "Maskapai", "Harga" }));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbPengguna, 0, 151, Short.MAX_VALUE)
                    .addComponent(ktKunciField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCari)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ktKunciField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Export ");

        tambahBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/add_plus-16.png"))); // NOI18N
        tambahBtn.setText("Tambah");
        tambahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahBtnActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/edit-16.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/hapus-16.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        expotXls.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Excel.png"))); // NOI18N
        expotXls.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expotXlsMouseClicked(evt);
            }
        });

        exportPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Adobe_Reader_PDF.png"))); // NOI18N
        exportPdf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportPdfMouseClicked(evt);
            }
        });

        exportWord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/Word.png"))); // NOI18N
        exportWord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportWordMouseClicked(evt);
            }
        });

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
                .addComponent(exportWord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tambahBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exportWord)
                    .addComponent(exportPdf)
                    .addComponent(jLabel4)
                    .addComponent(expotXls)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tambahBtn)
                        .addComponent(btnEdit)
                        .addComponent(btnHapus)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(453, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(21, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(23, Short.MAX_VALUE)))
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
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
                .addGap(29, 29, 29)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silakan pilih Data yang akan diupdate", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int i = dataTable.getSelectedRow();
        String namaPaket = (String)dataTable.getValueAt(i, 1);
        String hotel = (String)dataTable.getValueAt(i, 2);
        String maskapai = (String)dataTable.getValueAt(i, 3);
        String transportasi = (String)dataTable.getValueAt(i, 4);
        String fasilitas = (String)dataTable.getValueAt(i, 5);
        String harga = (String)dataTable.getValueAt(i, 6);

        Map data = new HashMap();
        data.put("action", "edit");
        data.put("namaPaket", namaPaket);
        data.put("hotel", hotel);
        data.put("maskapai", maskapai);
        data.put("transportasi", transportasi);
        data.put("fasilitas", fasilitas);
        data.put("harga", new CurrencyUtils().unFormatRupiah(harga));
                
        new FrmTambahPaketHajiNew(null, true, data).setVisible(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tambahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahBtnActionPerformed
        // TODO add your handling code here:
        dispose();
        Map data = new HashMap();
        data.put("action", "tambah");
        new FrmTambahPaketHajiNew(null, true, data).setVisible(true);
    }//GEN-LAST:event_tambahBtnActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        showTable();
    }//GEN-LAST:event_btnCariActionPerformed

    private void exportPdfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdfMouseClicked
        generatePdfOld();
    }//GEN-LAST:event_exportPdfMouseClicked

    private void expotXlsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expotXlsMouseClicked
        generateExcel();
    }//GEN-LAST:event_expotXlsMouseClicked

    private void exportWordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportWordMouseClicked
        try {
            generateWord();
        } catch (IOException ex) {
            Logger.getLogger(FrmDaftarPaketHaji.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_exportWordMouseClicked

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
            java.util.logging.Logger.getLogger(FrmDaftarPaketHaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmDaftarPaketHaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmDaftarPaketHaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmDaftarPaketHaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmDaftarPaketHaji dialog = new FrmDaftarPaketHaji(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JComboBox<String> cbPengguna;
    private javax.swing.JTable dataTable;
    private javax.swing.JLabel exportPdf;
    private javax.swing.JLabel exportWord;
    private javax.swing.JLabel expotXls;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jumlahLabel;
    private javax.swing.JTextField ktKunciField;
    private javax.swing.JButton tambahBtn;
    // End of variables declaration//GEN-END:variables


}
