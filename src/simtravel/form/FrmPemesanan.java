/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import simtravel.utils.DBUtils;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import simtravel.utils.CurrencyUtils;
import simtravel.utils.EncryptionUtils;
import simtravel.utils.NotificationUtils;
import simtravel.utils.RegexUtils;

/**
 *
 * @author nursalim
 */
public class FrmPemesanan extends javax.swing.JDialog {

    /**
     * Creates new form FrmDaftar
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs, rs2;
    private String userId;
    private String action;
    private Date today;
    private java.sql.Date sqlDate;
    
    public FrmPemesanan(java.awt.Frame parent, boolean modal, Map data) {
        super(parent, modal);
        con = new DBUtils().getKoneksi();
        userId = ""; //(String) data.get("userId");
        action = "tambah"; //(String) data.get("action");
        initComponents();
        setLocationRelativeTo(null);
        struktur2();

        if(data != null){
            
            if(data.get("noKTP") != null){
                noKTPField.setText(data.get("noKTP").toString());
                namaLengkapField.setText(data.get("nama").toString());
                
                kodeField.setText(new RegexUtils().generateNoPemesanan());
                tglPesananField.setDate(new Date());
                emailField.setText(data.get("email").toString());
                struktur3();
            }else{
                struktur();
            }
            
        }
        
    }
    
    
    
    public void displayPaketHaji(){
        paketCB.removeAllItems();
        paketCB.addItem("Pilih Paket Haji");  
        String sql = "SELECT nama_paket FROM tbl_paket_haji";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                paketCB.addItem(rs.getString("nama_paket"));     
                displayDataPaketHaji(paketCB.getSelectedItem().toString());
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void displayPaketUmrah() throws SQLException{
        paketCB.removeAllItems();
        paketCB.addItem("Pilih Paket Umrah");   
        String sql = "SELECT nama_paket FROM tbl_paket_umrah";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                paketCB.addItem(rs.getString("nama_paket"));       
                displayDataPaketUmrah(paketCB.getSelectedItem().toString());
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    public void displayDataPaketUmrah(String namaPaket){
        hargaField.setText("");
        String sql = "SELECT * FROM tbl_paket_umrah WHERE nama_paket = ?";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, namaPaket);
            rs2 = ps.executeQuery();
            
            while (rs2.next()){
                hargaField.setText(new CurrencyUtils().formatRupiah(new BigDecimal(rs2.getString("harga_total"))));
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void displayDataPaketHaji(String namaPaket){
        hargaField.setText("");
        String sql = "SELECT * FROM tbl_paket_haji WHERE nama_paket = ?";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, namaPaket);
            rs2 = ps.executeQuery();
            
            while (rs2.next()){
                hargaField.setText(new CurrencyUtils().formatRupiah(new BigDecimal(rs2.getString("harga_total"))));
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public boolean validasiTambah(){
        if(kodeField.getText() == null || kodeField.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "ID Pengguna tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // validate email format
        
        return true;
    }
    
        public boolean validasiUpdate(){
        if(kodeField.getText() == null || kodeField.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "ID Pengguna tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    
    
    public void tambahRecord(){
        String Status = "";
               if("Tunai".equals(jnsPembayaranCB.getSelectedItem())){
                   Status = "Lunas";
               }else{
                   Status = "Belum Lunas";
               }
               
        String sql = "INSERT INTO tbl_pemesanan(no_pemesanan, no_ktp, nama_paket, jns_pembayaran, tgl_pemesanan, status_pembayaran, tipe_pemesanan, uang_dp, total_bayar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, kodeField.getText());
            ps.setString(2, noKTPField.getText());
            ps.setString(3, paketCB.getSelectedItem().toString());
            ps.setString(4, jnsPembayaranCB.getSelectedItem().toString());
            ps.setDate(5, new java.sql.Date(tglPesananField.getDate().getTime()));
            ps.setString(6, Status);
            ps.setString(7, rbHaji.isSelected()?"Haji":"Umrah");
            ps.setString(8, uangDPField.getText().equals("")?"0":uangDPField.getText());
            ps.setString(9, new CurrencyUtils().unFormatRupiah(hargaField.getText()));
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil di tambahkan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
    }
    
    public void sentNotification() throws FileNotFoundException{
        String Status = "";
               if("Tunai".equals(jnsPembayaranCB.getSelectedItem())){
                   Status = "Lunas";
               }else{
                   Status = "Belum Lunas";
               }
               
        Map data = new HashMap();
        data.put("to", emailField.getText());
        data.put("noPemesanan", kodeField.getText());
        data.put("tglPemesanan", tglPesananField.getDate());
        data.put("nama", namaLengkapField.getText());
        data.put("noKTP", noKTPField.getText());
        data.put("statusPembayaran", Status);
        data.put("jenisPembayaran", jnsPembayaranCB.getSelectedItem().toString());
        data.put("namaPaket", paketCB.getSelectedItem().toString());
        data.put("hargaPaket", hargaField.getText());
        
       File is = new File(generateInvoicePemesanan());
        data.put("attachment", is);
        
        new NotificationUtils().sentEmailPemesanan(data);
    }
    
    public void updateRecord(){
        String sql = "UPDATE tbl_user SET user_name = ? WHERE user_id = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(2, kodeField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil di update", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

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
        judulLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        kodeField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        namaLengkapField = new javax.swing.JTextField();
        tglPesananField = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        rbHaji = new javax.swing.JRadioButton();
        rbUmrah = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jnsPembayaranCB = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        hargaField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        uangDPField = new javax.swing.JTextField();
        noKTPField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        paketCB = new javax.swing.JComboBox<>();
        emailField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lamaAngsuranCB = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cicilanPerBulanField = new javax.swing.JTextField();
        btnHitung = new javax.swing.JButton();
        btnTableAngsuran = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        printInvoice = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistem Informasi Travel Umrah & Haji - PT. Ismata Nusantara Abadi");

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        judulLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        judulLabel.setForeground(new java.awt.Color(255, 255, 255));
        judulLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/shopping-cart.png"))); // NOI18N
        judulLabel.setText("Formulir Pemesanan ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(judulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(judulLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("No. Pemesanan");

        jLabel3.setText("No. KTP ");

        jLabel1.setText("Nama Lengkap  ");

        jLabel4.setText("Pilihan   ");

        rbHaji.setText("Haji");
        rbHaji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbHajiActionPerformed(evt);
            }
        });

        rbUmrah.setText("Umrah ");
        rbUmrah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbUmrahActionPerformed(evt);
            }
        });

        jLabel5.setText("Paket  ");

        jnsPembayaranCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai", "Kredit" }));
        jnsPembayaranCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnsPembayaranCBActionPerformed(evt);
            }
        });

        jLabel7.setText("Harga Paket ");

        hargaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                hargaFieldKeyTyped(evt);
            }
        });

        jLabel8.setText("Jns Pembayaran ");

        jLabel6.setText("Uang DP ");

        uangDPField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                uangDPFieldFocusGained(evt);
            }
        });
        uangDPField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                uangDPFieldPropertyChange(evt);
            }
        });
        uangDPField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                uangDPFieldKeyTyped(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cari-16.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        paketCB.setToolTipText("");
        paketCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paketCBActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel9.setText("minimal Rp. 1.000.000,-");

        jLabel10.setText("Lama Angsuran");

        lamaAngsuranCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Lama Angsuran --", "3 Bulan", "6 Bulan", "9 Bulan", "12 Bulan" }));
        lamaAngsuranCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lamaAngsuranCBActionPerformed(evt);
            }
        });

        jLabel11.setText("Cicilan Perbulan   ");

        btnHitung.setText("Hitung");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        btnTableAngsuran.setText("Tabel Angs");
        btnTableAngsuran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableAngsuranActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(74, 74, 74)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jnsPembayaranCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paketCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hargaField)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lamaAngsuranCB, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(uangDPField, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cicilanPerBulanField, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnHitung)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTableAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(namaLengkapField)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(noKTPField)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(rbHaji, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rbUmrah)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(kodeField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(tglPesananField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(49, 49, 49))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(kodeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(tglPesananField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(noKTPField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(namaLengkapField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbHaji)
                        .addComponent(rbUmrah)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(paketCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(hargaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jnsPembayaranCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(uangDPField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lamaAngsuranCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cicilanPerBulanField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHitung)
                    .addComponent(btnTableAngsuran))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/simpan-16.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cancel-16.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        printInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/printer-16.png"))); // NOI18N
        printInvoice.setText("Cetak Faktur");
        printInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvoiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(printInvoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnBatal)
                    .addComponent(printInvoice))
                .addGap(0, 9, Short.MAX_VALUE))
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
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(action.equals("tambah")){
            if(validasiTambah()){
                int pilih = JOptionPane.showConfirmDialog(null, "Apakah Data yang Anda masukkan sudah benar?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
                if(pilih == JOptionPane.OK_OPTION){
                    tambahRecord();
                    try {
                        sentNotification();
                        printInvoice.setEnabled(true);
                        //dispose();
                        //new FrmDaftarPengguna(null, true).setVisible(true);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(FrmPemesanan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }else{
            if(validasiUpdate()){
                int pilih = JOptionPane.showConfirmDialog(null, "Apakah Data yang Anda masukkan sudah benar?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
                if(pilih == JOptionPane.OK_OPTION){
                    updateRecord();
                    dispose();
                    new FrmDaftarPengguna(null, true).setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void rbHajiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbHajiActionPerformed
        rbHaji.setSelected(true);
        rbUmrah.setSelected(false);
        displayPaketHaji();
        paketCB.setEnabled(true);
        jnsPembayaranCB.setSelectedIndex(0);
        btnHitung.setEnabled(false);
        printInvoice.setEnabled(false);
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_rbHajiActionPerformed

    private void rbUmrahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbUmrahActionPerformed
        rbHaji.setSelected(false);
        rbUmrah.setSelected(true);
        paketCB.setEnabled(true);
        jnsPembayaranCB.setSelectedIndex(0);
        btnHitung.setEnabled(false);
        printInvoice.setEnabled(false);
        btnSimpan.setEnabled(false);
        try {
            displayPaketUmrah();
        } catch (SQLException ex) {
            Logger.getLogger(FrmPemesanan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbUmrahActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        dispose();
        
        String sql = "SELECT * FROM tbl_customer";
        
        Map data = new HashMap();
        data.put("sql", sql);
        new ListOfValuesCustomer(null, true, data).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void paketCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paketCBActionPerformed
        if(("Pilih Paket Haji".equals(paketCB.getSelectedItem())) || ("Pilih Paket Umrah".equals(paketCB.getSelectedItem()))){
           if("".equals(hargaField.getText())){
               btnSimpan.setEnabled(false);
           }
            hargaField.setEnabled(false);
            jnsPembayaranCB.setEnabled(false);
            printInvoice.setEnabled(false);
            btnSimpan.setEnabled(false);
            uangDPField.setEnabled(false);
            lamaAngsuranCB.setEnabled(false);
            jnsPembayaranCB.setSelectedIndex(0);
            lamaAngsuranCB.setSelectedIndex(0);

        }else{
            jnsPembayaranCB.setSelectedIndex(0);
           hargaField.setEnabled(true); 
           jnsPembayaranCB.setEnabled(true); 
           
        }
        
        if(rbHaji.isSelected()){
            if(paketCB.getSelectedItem() != null){
                displayDataPaketHaji(paketCB.getSelectedItem().toString());
                if("".equals(hargaField.getText())){
                    printInvoice.setEnabled(false);
               btnSimpan.setEnabled(false);

           }
           }
            
        }
        
        if(rbUmrah.isSelected()){
            if(paketCB.getSelectedItem() != null){
                displayDataPaketUmrah(paketCB.getSelectedItem().toString());;
                if("".equals(hargaField.getText())){
                    printInvoice.setEnabled(false);
               btnSimpan.setEnabled(false);
           }
            }
        }
    }//GEN-LAST:event_paketCBActionPerformed

    private void printInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceActionPerformed
        printInvoicePemesanan();
    }//GEN-LAST:event_printInvoiceActionPerformed

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
  
        if(lamaAngsuranCB.getSelectedItem().equals("-- Pilih Lama Angsuran --")){
            JOptionPane.showMessageDialog(null, "Silakan Pilih Lama Angsuran", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        calculateAngsuran();
        if("".equals(cicilanPerBulanField.getText())){
            btnTableAngsuran.setEnabled(false);
            btnSimpan.setEnabled(false);
            printInvoice.setEnabled(false);
        }else{
            btnTableAngsuran.setEnabled(true);
            btnSimpan.setEnabled(true);
        }
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnTableAngsuranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableAngsuranActionPerformed
        
        int lama = 0;
        
        String lamaAngsuran = lamaAngsuranCB.getSelectedItem().toString();
        if(lamaAngsuran.equals("3 Bulan")){
            lama = 3;
        }else if(lamaAngsuran.equals("6 Bulan")){
            lama = 6;
        }else if(lamaAngsuran.equals("9 Bulan")){
            lama = 9;
        }else if(lamaAngsuran.equals("12 Bulan")){
            lama = 12;
        }
        
        Map data = new HashMap();
        data.put("lama", lama);
        data.put("cicilanPerbulan", cicilanPerBulanField.getText());
        new FrmTableAngsuran(null, true, data).setVisible(true);
    }//GEN-LAST:event_btnTableAngsuranActionPerformed

    private void jnsPembayaranCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnsPembayaranCBActionPerformed
        btnHitung.setEnabled(false);
        if(jnsPembayaranCB.getSelectedItem().equals("Tunai")){
            uangDPField.setEnabled(false);
            cicilanPerBulanField.setEnabled(false);
            btnHitung.setEnabled(false);
            btnTableAngsuran.setEnabled(false);
            printInvoice.setEnabled(false);
            btnSimpan.setEnabled(true);
            jLabel9.setEnabled(false);
            
            uangDPField.setText("");
            lamaAngsuranCB.setSelectedIndex(0);
            cicilanPerBulanField.setText("");
            lamaAngsuranCB.setEnabled(false);
        }else{
            uangDPField.setEnabled(true);
            lamaAngsuranCB.setEnabled(true);
            cicilanPerBulanField.setEnabled(false);
            btnHitung.setEnabled(false);
            btnTableAngsuran.setEnabled(false);
            uangDPField.setText("0");
            printInvoice.setEnabled(false);
            btnSimpan.setEnabled(false);
            jLabel9.setEnabled(true);
        }
    }//GEN-LAST:event_jnsPembayaranCBActionPerformed

    private void uangDPFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_uangDPFieldFocusGained
        
    }//GEN-LAST:event_uangDPFieldFocusGained

    private void uangDPFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_uangDPFieldPropertyChange
        
    }//GEN-LAST:event_uangDPFieldPropertyChange

    private void uangDPFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_uangDPFieldKeyTyped
        char c = evt.getKeyChar();
   if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           evt.consume();
        }
    }//GEN-LAST:event_uangDPFieldKeyTyped

    private void hargaFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hargaFieldKeyTyped
       
    }//GEN-LAST:event_hargaFieldKeyTyped

    private void lamaAngsuranCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lamaAngsuranCBActionPerformed

        if(jnsPembayaranCB.getSelectedItem().equals("Kredit")){
            if(lamaAngsuranCB.getSelectedItem().equals("-- Pilih Lama Angsuran --")){
            btnHitung.setEnabled(false);
            btnTableAngsuran.setEnabled(false);
            cicilanPerBulanField.setText("");
            btnSimpan.setEnabled(false);
            }else{
              btnHitung.setEnabled(true);  
            }
           // btnHitung.setEnabled(false);
        }
        
  
   
    }//GEN-LAST:event_lamaAngsuranCBActionPerformed

    public void calculateAngsuran(){
        BigDecimal hargaPaket = new BigDecimal(new CurrencyUtils().unFormatRupiah(hargaField.getText()));
        BigDecimal uangDp = new BigDecimal(uangDPField.getText());
        
        String lamaAngsuran = lamaAngsuranCB.getSelectedItem().toString();
        BigDecimal lama = BigDecimal.ZERO;
        if(lamaAngsuran.equals("3 Bulan")){
            lama = new BigDecimal("3");
        }else if(lamaAngsuran.equals("6 Bulan")){
            lama = new BigDecimal("6");
        }else if(lamaAngsuran.equals("9 Bulan")){
            lama = new BigDecimal("9");
        }else if(lamaAngsuran.equals("12 Bulan")){
            lama = new BigDecimal("12");
        }
        
        System.out.println("Harga Paket : "+hargaPaket);
        System.out.println("Uang DP : "+uangDp);
        System.out.println("Lama Angsuran : "+lama);
        
        
        BigDecimal cicilanPerBulan = hargaPaket.subtract(uangDp);
        
        cicilanPerBulan = cicilanPerBulan.divide(lama, 0, RoundingMode.HALF_UP);
        
        System.out.println("Cicilan Perbulan : "+cicilanPerBulan);
        
        cicilanPerBulanField.setText(new CurrencyUtils().formatRupiah(new BigDecimal(cicilanPerBulan.toString())));
 
    }
    
    public void struktur(){
        
        noKTPField.setEnabled(false);
        tglPesananField.setEnabled(false);
        namaLengkapField.setEnabled(false);
        emailField.setEnabled(false);
        rbHaji.setEnabled(false);
        rbUmrah.setEnabled(false);
        paketCB.setEnabled(false);
        hargaField.setEnabled(false);
        jnsPembayaranCB.setEnabled(false);
        uangDPField.setEnabled(false);
        lamaAngsuranCB.setEnabled(false);
        cicilanPerBulanField.setEnabled(false);
        btnHitung.setEnabled(false);
        btnTableAngsuran.setEnabled(false);
        printInvoice.setEnabled(false);
        btnSimpan.setEnabled(false);
        jLabel9.setEnabled(false);
        
    }
    
    public void struktur2(){
        kodeField.setEditable(false);
        noKTPField.setEditable(false);
        tglPesananField.setEnabled(false);
        namaLengkapField.setEditable(false);
        hargaField.setEditable(false);
        emailField.setEditable(false);
        cicilanPerBulanField.setEditable(false);
        jLabel9.setEnabled(false);
    }
    
    public void struktur3(){
        paketCB.setEnabled(false);
        hargaField.setEnabled(false);
        jnsPembayaranCB.setEnabled(false);
        uangDPField.setEnabled(false);
        lamaAngsuranCB.setEnabled(false);
        cicilanPerBulanField.setEnabled(false);
        btnHitung.setEnabled(false);
        btnTableAngsuran.setEnabled(false);
        printInvoice.setEnabled(false);
        btnSimpan.setEnabled(false);
        jLabel9.setEnabled(false);
    }
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
            java.util.logging.Logger.getLogger(FrmPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                FrmPemesanan dialog = new FrmPemesanan(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTableAngsuran;
    private javax.swing.JTextField cicilanPerBulanField;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField hargaField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox<String> jnsPembayaranCB;
    private javax.swing.JLabel judulLabel;
    private javax.swing.JTextField kodeField;
    private javax.swing.JComboBox<String> lamaAngsuranCB;
    private javax.swing.JTextField namaLengkapField;
    private javax.swing.JTextField noKTPField;
    private javax.swing.JComboBox<String> paketCB;
    private javax.swing.JButton printInvoice;
    private javax.swing.JRadioButton rbHaji;
    private javax.swing.JRadioButton rbUmrah;
    private com.toedter.calendar.JDateChooser tglPesananField;
    private javax.swing.JTextField uangDPField;
    // End of variables declaration//GEN-END:variables

    private void printInvoicePemesanan() {
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        File dir = new File("C:/tmp/pdf/");
        if(!dir.exists()){
            try{
                dir.mkdirs();
            }catch(Exception iex){
                iex.printStackTrace();
            }
            
        }
        
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_invoice_pemesanan.pdf";
         try {
            File file = new File("src/simtravel/report/invoice_pemesanan.jrxml");
            jasperDesign = JRXmlLoader.load(file);
            
            Map param = new HashMap();
            InputStream imgInputStream = new FileInputStream(new File("src/simtravel/image/logo.png"));
            param.put("logo", imgInputStream);
            param.put("p_nopemesanan", kodeField.getText());

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
    
    private String generateInvoicePemesanan(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        File dir = new File("C:/tmp/pdf/");
        if(!dir.exists()){
            try{
                dir.mkdirs();
            }catch(Exception iex){
                iex.printStackTrace();
            }
            
        }
        
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_invoice_pemesanan.pdf";
         try {
            File file = new File("src/simtravel/report/invoice_pemesanan.jrxml");
            jasperDesign = JRXmlLoader.load(file);
            
            Map param = new HashMap();
            InputStream imgInputStream = new FileInputStream(new File("src/simtravel/image/logo.png"));
            param.put("logo", imgInputStream);
            param.put("p_nopemesanan", kodeField.getText());

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, new DBUtils().getKoneksi());

            //JasperViewer.viewReport(jasperPrint, false);
             JasperExportManager jem = new JasperExportManager();
             jem.exportReportToPdfFile(jasperPrint, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
         return FILE_NAME;
    }
}
