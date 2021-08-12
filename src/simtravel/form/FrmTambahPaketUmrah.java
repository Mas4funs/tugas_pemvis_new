/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import simtravel.utils.DBUtils;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import simtravel.utils.CurrencyUtils;

/**
 *
 * @author nursalim
 */
public class FrmTambahPaketUmrah extends javax.swing.JDialog {

    /**
     * Creates new form FrmDaftar
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private String userId;
    private String action;
    private Date today;
    private java.sql.Date sqlDate;
    
    public FrmTambahPaketUmrah(java.awt.Frame parent, boolean modal, Map data) {
        super(parent, modal);
        con = new DBUtils().getKoneksi();
        userId = ""; //(String) data.get("userId");
        action = (String) data.get("action");
        String namaPaket = (String) data.get("namaPaket");
        String hotel = (String) data.get("hotel");
        String maskapai = (String) data.get("maskapai");
        String transportasi = (String) data.get("transportasi");
        String fasilitas = (String) data.get("fasilitas");
        String harga_paket = (String) data.get("harga_paket");
        String harga_total = (String) data.get("harga_total");
        
        initComponents();
        displayHotelCB();
        displayMaskapaiCB();
        displayTransportCB();
        total1.setVisible(false);
        total2.setVisible(false);
        setLocationRelativeTo(null);
        
        if(action.equals("edit")){
            judulLabel.setText("Update Paket Umrah");
            namaField.setText(namaPaket);
            namaField.setBackground(Color.LIGHT_GRAY);
            namaField.setEditable(false);            
            hotelCB.setSelectedItem(hotel);
            maskapaiCB.setSelectedItem(maskapai);
            transportCB.setSelectedItem(transportasi);
            //transportasiField.setText(transportasi);
            fasilitasArea.setText(fasilitas);
            hargaField.setText(harga_paket);
            total2.setText(harga_total);
            jLabel10.setText(new CurrencyUtils().formatRupiah(new BigDecimal(""+harga_total)));
        }
        
        today = new Date();
        
    }
    
    public void displayHotelCB(){
        String sql = "SELECT * FROM tbl_hotel";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                hotelCB.addItem(rs.getString("nama"));   
                //hargaField1.setText(rs.getString("tarif"));;
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void displayMaskapaiCB(){
        String sql = "SELECT nama FROM tbl_maskapai";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                maskapaiCB.addItem(rs.getString("nama"));                
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void displayTransportCB(){
        String sql = "SELECT nama FROM tbl_transport";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                transportCB.addItem(rs.getString("nama"));                
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

     public void tampil2()
    {
        if("".equals(hargaField.getText())){
        jLabel10.setText("");
                }else{
        long getHarga = Integer.parseInt(hargaField.getText());
        long getHasil = Integer.parseInt(total1.getText());
        long total = getHarga + getHasil;
        jLabel10.setText(new CurrencyUtils().formatRupiah(new BigDecimal(""+total)));
        //jLabel10.setText(String.valueOf(total));
        total2.setText(String.valueOf(total));
        }
    }
     
     
     public void tampil_coba()
    {
        con = new DBUtils().getKoneksi();  
        try {
           
        String sql = "select tarif from tbl_hotel where nama='"+hotelCB.getSelectedItem()+"'";      // disini saya menampilkan NIM, anda dapat menampilkan
        ps = con.prepareStatement(sql);                                // yang anda ingin kan
        rs = ps.executeQuery();
        String trfhotel1 = null;
        while (rs.next()) {
            trfhotel1 = rs.getString(1);
        }
        long trfhotel = Integer.parseInt(trfhotel1);
		rs.close();


        String sql2 = "select tarif from tbl_maskapai where nama='"+maskapaiCB.getSelectedItem()+"'";      // disini saya menampilkan NIM, anda dapat menampilkan
        ps = con.prepareStatement(sql2);                                // yang anda ingin kan
        rs = ps.executeQuery();
        String trfmaskapai1 = null;
        while (rs.next()) {
            trfmaskapai1 = rs.getString(1);
        }
        long trfmaskapai = Integer.parseInt(trfmaskapai1);
		rs.close();

        String sql3 = "select tarif from tbl_transport where nama='"+transportCB.getSelectedItem()+"'";      // disini saya menampilkan NIM, anda dapat menampilkan
        ps = con.prepareStatement(sql3);                                // yang anda ingin kan
        rs = ps.executeQuery();
        String trftransport1 = null;
        while (rs.next()) {
            trftransport1 = rs.getString(1);
        }
        long trftransport = Integer.parseInt(trftransport1);
		rs.close();
  
        
        long testhasil = trfhotel + trfmaskapai + trftransport;
        //jLabel7.setText(""+testhasil);
        jLabel7.setText(new CurrencyUtils().formatRupiah(new BigDecimal(""+testhasil)));
        total1.setText(""+testhasil);
         
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }   
    }
     
     
     
     public void test3(java.awt.event.KeyEvent evt){
         char c = evt.getKeyChar();
        String name = hargaField.getText();
   if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           evt.consume();
        }
     }
    
    
    public boolean validasi(){
        if(namaField.getText() == null || namaField.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Nama Paket Tidak Boleh Kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;       
        }else if("-- Pilih Nama Hotel --".equals(hotelCB.getSelectedItem()) ){
            JOptionPane.showMessageDialog(null, "Pilih Nama Hotel Terlebih Dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if("-- Pilih Nama Maskapai --".equals(maskapaiCB.getSelectedItem()) ){
            JOptionPane.showMessageDialog(null, "Pilih Nama Maskapai Terlebih Dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if("-- Pilih Nama Transport --".equals(transportCB.getSelectedItem()) ){
            JOptionPane.showMessageDialog(null, "Pilih Nama Transport Terlebih Dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(jLabel7.getText() == null || jLabel7.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Jumlah Harga Tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
            return false;       
        }else if(hargaField.getText() == null || hargaField.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Harga Paket Tidak Boleh Kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return false;       
        }else if(total2.getText() == null || total2.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Total Harga Paket Tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
            return false; 
        }
        
        return true;
    }
    
    public void tambahRecord(){
        String sql = "INSERT INTO tbl_paket_umrah(nama_paket, hotel, maskapai, transportasi, fasilitas, harga_paket, harga_total) VALUES (?, ?, ?, ?, ?, ?, ?) ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, namaField.getText());
            ps.setString(2, hotelCB.getSelectedItem().toString());
            ps.setString(3, maskapaiCB.getSelectedItem().toString());
            ps.setString(4, transportCB.getSelectedItem().toString());
            ps.setString(5, fasilitasArea.getText());
            ps.setString(6, hargaField.getText());
            ps.setString(7, total2.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil di tambahkan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal di tambahkan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateRecord(){
        String sql = "UPDATE tbl_paket_umrah SET hotel = ?, maskapai = ?, transportasi = ?, fasilitas = ?, harga_paket = ?, harga_total = ? WHERE nama_paket = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, hotelCB.getSelectedItem().toString());
            ps.setString(2, maskapaiCB.getSelectedItem().toString());
            ps.setString(3, transportCB.getSelectedItem().toString());
            ps.setString(4, fasilitasArea.getText());
            ps.setString(5, hargaField.getText());
            ps.setString(6, total2.getText());
            ps.setString(7, namaField.getText());
            
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil di update", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal di update", "Error", JOptionPane.ERROR_MESSAGE);
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
        namaField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        hotelCB = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        maskapaiCB = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        hargaField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fasilitasArea = new javax.swing.JTextArea();
        transportCB = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        total1 = new javax.swing.JTextField();
        total2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistem Informasi Travel Umrah & Haji - PT. Ismata Nusantara Abadi");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        judulLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        judulLabel.setForeground(new java.awt.Color(255, 255, 255));
        judulLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/building-32.png"))); // NOI18N
        judulLabel.setText("Tambah Paket Umrah");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(judulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(judulLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Nama Paket ");

        jLabel1.setText("Transportasi ");

        jLabel6.setText("Hotel ");

        hotelCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Nama Hotel --" }));
        hotelCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                hotelCBItemStateChanged(evt);
            }
        });
        hotelCB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hotelCBMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hotelCBMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hotelCBMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hotelCBMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hotelCBMouseReleased(evt);
            }
        });
        hotelCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hotelCBActionPerformed(evt);
            }
        });

        jLabel4.setText("Maskapai    ");

        maskapaiCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Nama Maskapai --" }));
        maskapaiCB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maskapaiCBMouseClicked(evt);
            }
        });
        maskapaiCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskapaiCBActionPerformed(evt);
            }
        });

        jLabel3.setText("Harga Paket ");

        hargaField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hargaFieldFocusGained(evt);
            }
        });
        hargaField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                hargaFieldCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                hargaFieldInputMethodTextChanged(evt);
            }
        });
        hargaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaFieldActionPerformed(evt);
            }
        });
        hargaField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                hargaFieldPropertyChange(evt);
            }
        });
        hargaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hargaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hargaFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                hargaFieldKeyTyped(evt);
            }
        });

        jLabel5.setText("Fasilitas    ");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        fasilitasArea.setColumns(20);
        fasilitasArea.setRows(5);
        fasilitasArea.setTabSize(6);
        fasilitasArea.setAutoscrolls(false);
        jScrollPane1.setViewportView(fasilitasArea);

        transportCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Nama Transport --" }));
        transportCB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transportCBMouseClicked(evt);
            }
        });
        transportCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportCBActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Total Harga Paket  :");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 8, Short.MAX_VALUE)
                        .addComponent(jLabel9))
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jScrollPane1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hotelCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(maskapaiCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(transportCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(hargaField)
                                    .addComponent(namaField)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(namaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(hotelCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(maskapaiCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(transportCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(hargaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(46, 46, 46))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
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

        total1.setEditable(false);
        total1.setBorder(null);
        total1.setOpaque(false);
        total1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total1ActionPerformed(evt);
            }
        });

        total2.setEditable(false);
        total2.setBorder(null);
        total2.setOpaque(false);
        total2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(total1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(total2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(total1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void transportCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportCBActionPerformed
        if("-- Pilih Nama Hotel --".equals(hotelCB.getSelectedItem()) ){
                    System.out.print("Data Kosong");
                }else{;
                   tampil_coba();
                   tampil2();
        
                }
    }//GEN-LAST:event_transportCBActionPerformed

    private void maskapaiCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maskapaiCBActionPerformed
        if("-- Pilih Nama Maskapai --".equals(hotelCB.getSelectedItem()) ){
                    System.out.print("Data Kosong");
                }else{
                   tampil_coba();
                   tampil2();
        
                }
    }//GEN-LAST:event_maskapaiCBActionPerformed

    private void hotelCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hotelCBActionPerformed
        if("-- Pilih Nama Hotel --".equals(hotelCB.getSelectedItem()) ){
                    System.out.print("Data Kosong");
                }else{
                   tampil_coba();
                   tampil2();
        
                }
        
    }//GEN-LAST:event_hotelCBActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
      
    }//GEN-LAST:event_formWindowActivated

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        
    }//GEN-LAST:event_formWindowGainedFocus

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        
    }//GEN-LAST:event_formWindowStateChanged

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        
    }//GEN-LAST:event_formWindowIconified

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        
    }//GEN-LAST:event_formMouseClicked

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
     
    }//GEN-LAST:event_formFocusGained

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        
    }//GEN-LAST:event_formWindowDeiconified

    private void hargaFieldCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_hargaFieldCaretPositionChanged
       
    }//GEN-LAST:event_hargaFieldCaretPositionChanged

    private void hargaFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_hargaFieldInputMethodTextChanged
        
    }//GEN-LAST:event_hargaFieldInputMethodTextChanged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved

        
    }//GEN-LAST:event_formMouseMoved

    private void hargaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaFieldActionPerformed
    
    }//GEN-LAST:event_hargaFieldActionPerformed

    private void hargaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hargaFieldKeyPressed
      tampil_coba();
      tampil2();
      
        char c = evt.getKeyChar();
        String name = hargaField.getText();
   if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           evt.consume();
        }else if ( (!name.matches("[a-zA-Z]+"))&& (c != KeyEvent.VK_ENTER) ){
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           //evt.consume();
        }
    }//GEN-LAST:event_hargaFieldKeyPressed

  
    
    private void hargaFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hargaFieldKeyTyped
      tampil_coba();
      tampil2();
      
        char c = evt.getKeyChar();
        String name = hargaField.getText();
   if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           evt.consume();
        }else if ( (!name.matches("[a-zA-Z]+"))&& (c != KeyEvent.VK_ENTER) ){
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           //evt.consume();
        }
    }//GEN-LAST:event_hargaFieldKeyTyped

    private void hargaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hargaFieldKeyReleased
        tampil_coba();
      tampil2();
      
        char c = evt.getKeyChar();
        String name = hargaField.getText();
   if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           evt.consume();
        }else if ( (!name.matches("[a-zA-Z]+"))&& (c != KeyEvent.VK_ENTER) ){
      //JOptionPane.showMessageDialog(null, "masukan angka", "Informasi", JOptionPane.INFORMATION_MESSAGE);
           //evt.consume();
        }
    }//GEN-LAST:event_hargaFieldKeyReleased

    private void hargaFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hargaFieldFocusGained
       
    }//GEN-LAST:event_hargaFieldFocusGained

    private void hargaFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_hargaFieldPropertyChange
     
    }//GEN-LAST:event_hargaFieldPropertyChange

    private void total1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total1ActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(action.equals("tambah")){
            if(validasi()){
                tampil_coba();
                tampil2();
                int pilih = JOptionPane.showConfirmDialog(null, "Apakah Data yang Anda masukkan sudah benar?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
                if(pilih == JOptionPane.OK_OPTION){
                    tambahRecord();
                    dispose();
                    new FrmDaftarPaketUmrah(null, true).setVisible(true);
                }
            }
        }else{
            if(validasi()){
                tampil_coba();
                tampil2();
                int pilih = JOptionPane.showConfirmDialog(null, "Apakah Data yang Anda masukkan sudah benar?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
                if(pilih == JOptionPane.OK_OPTION){
                    updateRecord();
                    dispose();
                    new FrmDaftarPaketUmrah(null, true).setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void total2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total2ActionPerformed

    private void hotelCBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotelCBMouseClicked
        
    }//GEN-LAST:event_hotelCBMouseClicked

    private void maskapaiCBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maskapaiCBMouseClicked
        
    }//GEN-LAST:event_maskapaiCBMouseClicked

    private void transportCBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transportCBMouseClicked
        
    }//GEN-LAST:event_transportCBMouseClicked

    private void hotelCBMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotelCBMousePressed
        
    }//GEN-LAST:event_hotelCBMousePressed

    private void hotelCBMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotelCBMouseReleased
       
    }//GEN-LAST:event_hotelCBMouseReleased

    private void hotelCBMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotelCBMouseEntered
        
    }//GEN-LAST:event_hotelCBMouseEntered

    private void hotelCBMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hotelCBMouseExited
        
    }//GEN-LAST:event_hotelCBMouseExited

    private void hotelCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_hotelCBItemStateChanged
        
    }//GEN-LAST:event_hotelCBItemStateChanged

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
            java.util.logging.Logger.getLogger(FrmTambahPaketUmrah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmTambahPaketUmrah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmTambahPaketUmrah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmTambahPaketUmrah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                FrmTambahPaketUmrah dialog = new FrmTambahPaketUmrah(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JTextArea fasilitasArea;
    private javax.swing.JTextField hargaField;
    private javax.swing.JComboBox<String> hotelCB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel judulLabel;
    private javax.swing.JComboBox<String> maskapaiCB;
    private javax.swing.JTextField namaField;
    private javax.swing.JTextField total1;
    private javax.swing.JTextField total2;
    private javax.swing.JComboBox<String> transportCB;
    // End of variables declaration//GEN-END:variables
}
