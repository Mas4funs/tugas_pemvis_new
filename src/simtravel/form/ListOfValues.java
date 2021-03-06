/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import simtravel.utils.CurrencyUtils;
import simtravel.utils.DBUtils;

/**
 *
 * @author nursalim
 */
public class ListOfValues extends javax.swing.JDialog {

    /**
     * Creates new form ListOfValues
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public ListOfValues(java.awt.Frame parent, boolean modal, Map data) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        showTable(data);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ktKunciField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("List Of Values");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cancel-16.png"))); // NOI18N
        jButton1.setText("Tutup");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Kata Kunci         ");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simtravel/image/cari-16.png"))); // NOI18N
        jButton2.setText("Cari");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ktKunciField, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ktKunciField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int i = dataTable.getSelectedRow();
        
         if(i >= 0){  
        String noPemesanan = (String)dataTable.getValueAt(i, 0);
        
        Date tglPemesanan = null;
        try {
            tglPemesanan = new SimpleDateFormat("yyyy-MM-dd").parse((String)dataTable.getValueAt(i, 1));
        } catch (ParseException ex) {
            Logger.getLogger(ListOfValues.class.getName()).log(Level.SEVERE, null, ex);
        }
        String noKTP = (String)dataTable.getValueAt(i, 2);
        String namaPaket = (String)dataTable.getValueAt(i, 3);
        String tipePemesanan = (String)dataTable.getValueAt(i, 4);
        
        Map data = new HashMap();
        data.put("noPemesanan", noPemesanan);
        data.put("tglPemesanan", tglPemesanan);
        data.put("noKTP", noKTP);
        data.put("Nama", noKTP);
        data.put("namaPaket", namaPaket);
        data.put("tipePemesanan", tipePemesanan);
        dispose();
        System.out.println(data);
        new FrmStatusPembayaran(null, true, data).setVisible(true);
        
        }else{
             JOptionPane.showMessageDialog(null, "Anda belum memilih", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            String cancel = "cancel";
            Map data = new HashMap();
            data.put("Cancel",cancel);
            new FrmStatusPembayaran(null, true, data).setVisible(true);
         }
         dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int i = dataTable.getSelectedRow();
        
         if(i >= 0){  
        String noPemesanan = (String)dataTable.getValueAt(i, 0);
        
        Date tglPemesanan = null;
        try {
            tglPemesanan = new SimpleDateFormat("yyyy-MM-dd").parse((String)dataTable.getValueAt(i, 1));
        } catch (ParseException ex) {
            Logger.getLogger(ListOfValues.class.getName()).log(Level.SEVERE, null, ex);
        }
        String noKTP = (String)dataTable.getValueAt(i, 2);
        String namaPaket = (String)dataTable.getValueAt(i, 3);
        String tipePemesanan = (String)dataTable.getValueAt(i, 4);
        
        Map data = new HashMap();
        data.put("noPemesanan", noPemesanan);
        data.put("tglPemesanan", tglPemesanan);
        data.put("noKTP", noKTP);
        data.put("Nama", noKTP);
        data.put("namaPaket", namaPaket);
        data.put("tipePemesanan", tipePemesanan);
        dispose();
        System.out.println(data);
        new FrmStatusPembayaran(null, true, data).setVisible(true);
        
        }else{
             JOptionPane.showMessageDialog(null, "Anda belum memilih", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            String cancel = "cancel";
            Map data = new HashMap();
            data.put("Cancel",cancel);
            new FrmStatusPembayaran(null, true, data).setVisible(true);
         }
         dispose();
    }//GEN-LAST:event_formWindowClosing

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    public void showTable(Map data){
        String kataKunci = "";
        String kataKunciStr = ktKunciField.getText().trim();
        
        if(kataKunciStr == null || "".equals(kataKunciStr)){
            kataKunci = "%";
        }else{
            kataKunci = "%"+kataKunciStr+"%";
        }
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("No. Pemesanan"); 
        model.addColumn("Tanggal Pesanan");
        model.addColumn("No. KTP");
//        model.addColumn("Nama");
        model.addColumn("Nama Paket");
        model.addColumn("Tipe Pemesanan");
        dataTable.setModel(model);
        
        String sql = data.get("sql").toString();
        
        System.out.println("sql == "+sql);
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
//                String namaCustomer = getNamaCustomer(rs.getString("no_ktp"));
                
                model.addRow(new Object[]{ 
                    rs.getString("no_pemesanan"), 
                    rs.getString("tgl_pemesanan"),
                    rs.getString("no_ktp"),
//                    namaCustomer,
                    rs.getString("nama_paket"),
                    rs.getString("tipe_pemesanan"),
                    } 
                );
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Menampilkan Data", "Error", JOptionPane.ERROR_MESSAGE);
        }

        model.fireTableDataChanged();
        
//        dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
        dataTable.setEnabled(false);
        
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
    
    public String getNamaCustomer(String noKTP){
        String namaCustomer = "";
        
        String sql = "select * from tbL_customer where no_ktp = ?";
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, noKTP);
            rs = ps.executeQuery();
            
            while (rs.next()){
                namaCustomer = rs.getString("nama");
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Menampilkan Data", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return namaCustomer;
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
            java.util.logging.Logger.getLogger(ListOfValues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListOfValues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListOfValues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListOfValues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ListOfValues dialog = new ListOfValues(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField ktKunciField;
    // End of variables declaration//GEN-END:variables
}
