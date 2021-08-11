/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import simtravel.utils.DBUtils;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
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
public class FrmStatusPembayaran extends javax.swing.JDialog {

    /**
     * Creates new form FrmDaftar
     * Nambahin comment
     * Test 123456
     * Test 67889
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private String userId;
    private String action;
    private String tipePemesanan;
    private Date today;
    private java.sql.Date sqlDate;
    
    public FrmStatusPembayaran(java.awt.Frame parent, boolean modal, Map data) {
        super(parent, modal);
        con = new DBUtils().getKoneksi();
        userId = ""; //(String) data.get("userId");
        action = ""; //(String) data.get("action");

        initComponents();
        setLocationRelativeTo(null);
        struktur2();
        
        
        if(data != null){
            if(data.get("noPemesanan") != null){
            String noPemesanan = (String) data.get("noPemesanan");
            Date tglPemesanan = (Date) data.get("tglPemesanan");
            String namaPaket = (String) data.get("namaPaket");
            String noKTP = (String) data.get("noKTP");
            tipePemesanan = (String) data.get("tipePemesanan");
            
            noPemesananField.setText(noPemesanan);
            tglPesanan.setDate(tglPemesanan);
            namaPaketField.setText(namaPaket);
            noKTPField.setText(noKTP);
            
            noRegistrasiField.setText(new RegexUtils().generateNoRegistrasi());
            
            setDataCustomer(noKTP);
            setDataPaket(namaPaket);
            struktur3();
        }else{
            struktur();
        }
        
        today = new Date();       
        }
    }
    
    public void struktur(){
        tglPesanan.setEnabled(false);
        noRegistrasiField.setEnabled(false);
        emailField.setEnabled(false);
        namaLengkapField.setEnabled(false);
        noKTPField.setEnabled(false);
        namaPaketField.setEnabled(false);
        hargaField.setEnabled(false);
        tglBerangkat.setEnabled(false);
        tglPulang.setEnabled(false);
        pimpinanRombonganCB.setEnabled(false);
        pimpinanRombonganCB.setSelectedIndex(-1);
        btnSimpan.setEnabled(false);
    }
    
    public void struktur2(){
        noPemesananField.setEditable(false);
        tglPesanan.setEnabled(false);
        namaLengkapField.setEditable(false);
        noKTPField.setEditable(false);
        namaPaketField.setEditable(false);
        hargaField.setEditable(false);
        noRegistrasiField.setEditable(false);
        emailField.setEditable(false);
    }
    
    public void struktur3(){
        tglPesanan.setEnabled(false);
        noRegistrasiField.setEnabled(false);
        emailField.setEnabled(true);
        namaLengkapField.setEnabled(true);
        noKTPField.setEnabled(true);
        namaPaketField.setEnabled(true);;
        hargaField.setEnabled(true);
        tglBerangkat.setEnabled(true);
        tglPulang.setEnabled(true);
        pimpinanRombonganCB.setSelectedIndex(-1);
        btnSimpan.setEnabled(false);
    }
    
    public boolean validasiTambah(){
        // validate email format
        return true;
    }
    
    public boolean validasiUpdate(){
        return true;
    }
    
    public void setDataCustomer(String noKTP){
        String sql = "SELECT * FROM tbl_customer WHERE no_ktp = ?";
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, noKTP);
            rs = ps.executeQuery();
            
            while (rs.next()){
                namaLengkapField.setText(rs.getString("nama"));
                emailField.setText(rs.getString("email"));
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Menampilkan Data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setDataPaket(String namaPaket){
        String sql = "";
        
        if("Haji".equals(tipePemesanan)){
            sql = "SELECT * FROM tbl_paket_haji WHERE nama_paket = ?";
        }else{
            sql = "SELECT * FROM tbl_paket_umrah WHERE nama_paket = ?";
        }
              
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, namaPaket);
            rs = ps.executeQuery();
            
            while (rs.next()){
                hargaField.setText(new CurrencyUtils().formatRupiah(new BigDecimal(rs.getString("harga"))));
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Menampilkan Data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void updateRecord(){
        String sql = "UPDATE tbl_pemesanan SET no_registrasi = ?, pimpinan_rombongan = ?, tgl_berangkat = ?, tgl_pulang = ?, status_pembayaran = ? WHERE no_pemesanan = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, noRegistrasiField.getText());
            ps.setString(2, pimpinanRombonganCB.getSelectedItem().toString());
            ps.setDate(3, new java.sql.Date(tglBerangkat.getDate().getTime()));
            ps.setDate(4, new java.sql.Date(tglPulang.getDate().getTime()));
            ps.setString(5, "Lunas");
            ps.setString(6, noPemesananField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil di update", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    public void sentNotification(){
        Map data = new HashMap();
        data.put("to", emailField.getText());
        data.put("noPemesanan", noPemesananField.getText());
        data.put("tglPemesanan", tglPesanan.getDate());
        data.put("nama", namaLengkapField.getText());
        data.put("noKTP", noKTPField.getText());
        data.put("statusPembayaran", "Lunas");
        data.put("namaPaket", namaPaketField.getText());
        data.put("hargaPaket", hargaField.getText());
        data.put("tglBerangkat", tglBerangkat.getDate());//.getDateFormatString());
        data.put("tglPulang", tglPulang.getDate());
        data.put("pimpinanRombongan", pimpinanRombonganCB.getSelectedItem().toString());
        
        File is = new File(generateInvoiceKeberangkatan());
        data.put("attachment", is);
        
        new NotificationUtils().sentEmailUpdatePembayaran(data);
    }
    
     public void clear(){
        noPemesananField.setText("");
        noRegistrasiField.setText("");
        emailField.setText("");
        namaLengkapField.setText("");
        noKTPField.setText("");
        namaPaketField.setText("");
        hargaField.setText("");
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
        jLabel1 = new javax.swing.JLabel();
        namaLengkapField = new javax.swing.JTextField();
        tglPesanan = new com.toedter.calendar.JDateChooser();
        noRegistrasiField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tglBerangkat = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        tglPulang = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        pimpinanRombonganCB = new javax.swing.JComboBox<>();
        namaPaketField = new javax.swing.JTextField();
        noPemesananField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        noKTPField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        hargaField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistem Informasi Travel Umrah & Haji - PT. Ismata Nusantara Abadi");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        judulLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        judulLabel.setForeground(new java.awt.Color(255, 255, 255));
        judulLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/credit-card_32.png"))); // NOI18N
        judulLabel.setText(" Form Update Status Pembayaran  ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(judulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jLabel3.setText("No. Registrasi  ");

        jLabel1.setText("Nama Lengkap  ");

        jLabel4.setText("Paket ");

        jLabel5.setText("Tanggal Berangkat   ");

        tglBerangkat.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                tglBerangkatComponentAdded(evt);
            }
        });
        tglBerangkat.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tglBerangkatAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tglBerangkat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tglBerangkatFocusGained(evt);
            }
        });
        tglBerangkat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tglBerangkatMouseClicked(evt);
            }
        });
        tglBerangkat.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tglBerangkatPropertyChange(evt);
            }
        });
        tglBerangkat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglBerangkatKeyPressed(evt);
            }
        });

        jLabel6.setText("Tanggal Pulang ");

        jLabel7.setText("Pimpinan Rombongan   ");

        pimpinanRombonganCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ust A", "Ust B", "Ust C" }));
        pimpinanRombonganCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pimpinanRombonganCBActionPerformed(evt);
            }
        });

        noPemesananField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noPemesananFieldActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cari-16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("No. KTP ");

        jLabel9.setText("Harga : ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pimpinanRombonganCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(noPemesananField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                .addComponent(tglPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tglPulang, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(tglBerangkat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(namaLengkapField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(namaPaketField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hargaField)
                                    .addComponent(noKTPField)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(noRegistrasiField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emailField)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(jLabel2))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(noPemesananField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton2))))
                    .addComponent(tglPesanan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(noRegistrasiField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(namaLengkapField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(noKTPField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(namaPaketField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(hargaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(tglBerangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(tglPulang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(pimpinanRombonganCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(btnBatal))
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
                .addContainerGap(32, Short.MAX_VALUE))
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

        if(validasiUpdate()){
   
          if(tglBerangkat.getDate()== null){
                JOptionPane.showMessageDialog(null, "Tgl Berangkat tidak boleh kosong", "Informasi", JOptionPane.INFORMATION_MESSAGE);
          }else if(tglPulang.getDate()== null){
                JOptionPane.showMessageDialog(null, "Tgl Pulang tidak boleh kosong", "Informasi", JOptionPane.INFORMATION_MESSAGE);
          }else{
            int pilih = JOptionPane.showConfirmDialog(null, "Apakah Data yang Anda masukkan sudah benar?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
            if(pilih == JOptionPane.OK_OPTION){
                updateRecord();
                clear();
                sentNotification();     
            } 
        }
       }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
        
        String sql = "SELECT * FROM tbl_pemesanan WHERE no_registrasi is null";
        
        Map data = new HashMap();
        data.put("sql", sql);
        new ListOfValues(null, true, data).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void noPemesananFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noPemesananFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noPemesananFieldActionPerformed

    private void tglBerangkatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglBerangkatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglBerangkatKeyPressed

    private void tglBerangkatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tglBerangkatMouseClicked
      
    }//GEN-LAST:event_tglBerangkatMouseClicked

    private void tglBerangkatPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tglBerangkatPropertyChange
      
    }//GEN-LAST:event_tglBerangkatPropertyChange

    private void tglBerangkatComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_tglBerangkatComponentAdded

    }//GEN-LAST:event_tglBerangkatComponentAdded

    private void tglBerangkatAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tglBerangkatAncestorAdded

    }//GEN-LAST:event_tglBerangkatAncestorAdded

    private void pimpinanRombonganCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pimpinanRombonganCBActionPerformed
        if("".equals(pimpinanRombonganCB.getSelectedItem())){
            btnSimpan.setEnabled(false);
        }else{
            btnSimpan.setEnabled(true);
        }
    }//GEN-LAST:event_pimpinanRombonganCBActionPerformed

    private void tglBerangkatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tglBerangkatFocusGained

    }//GEN-LAST:event_tglBerangkatFocusGained

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus

    }//GEN-LAST:event_formWindowGainedFocus

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
            java.util.logging.Logger.getLogger(FrmStatusPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmStatusPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmStatusPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmStatusPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmStatusPembayaran dialog = new FrmStatusPembayaran(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JButton btnSimpan;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField hargaField;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel judulLabel;
    private javax.swing.JTextField namaLengkapField;
    private javax.swing.JTextField namaPaketField;
    private javax.swing.JTextField noKTPField;
    private javax.swing.JTextField noPemesananField;
    private javax.swing.JTextField noRegistrasiField;
    private javax.swing.JComboBox<String> pimpinanRombonganCB;
    private com.toedter.calendar.JDateChooser tglBerangkat;
    private com.toedter.calendar.JDateChooser tglPesanan;
    private com.toedter.calendar.JDateChooser tglPulang;
    // End of variables declaration//GEN-END:variables

    private void printInvoiceKeberangkatan() {
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
        
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_invoice_keberangkatan.pdf";
         try {
            File file = new File("src/simtravel/report/invoice_keberangkatan.jrxml");
            jasperDesign = JRXmlLoader.load(file);
            
            Map param = new HashMap();
            InputStream imgInputStream = new FileInputStream(new File("src/simtravel/image/logo.png"));
            param.put("logo", imgInputStream);
            param.put("p_nopemesanan", noPemesananField.getText());

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
    
    private String generateInvoiceKeberangkatan(){
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
        
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_invoice_keberangkatan.pdf";
         try {
            File file = new File("src/simtravel/report/invoice_keberangkatan.jrxml");
            jasperDesign = JRXmlLoader.load(file);
            
            Map param = new HashMap();
            InputStream imgInputStream = new FileInputStream(new File("src/simtravel/image/logo.png"));
            param.put("logo", imgInputStream);
            param.put("p_nopemesanan", noPemesananField.getText());

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
