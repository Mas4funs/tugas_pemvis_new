/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.utils;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Febri Nahdatun
 */
public class DBUtils {
    Connection con;
    Statement stat;
    PreparedStatement ps;
    ResultSet rs;
    
    public Connection getKoneksi(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/db_travel", "root", "");
            stat = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tidak dapat melakukan koneksi database", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
        return con;
    }
    
    public boolean cekDataExist(String tableName, String columnName, String data, String message) throws SQLException{
        int cnt = 0;
        con = getKoneksi();
        String sql = "SELECT count(*) as cnt FROM "+tableName+" WHERE "+columnName+ " = ? ";
        ps = con.prepareStatement(sql);
        ps.setString(1, data);
        rs = ps.executeQuery();
        while(rs.next()){
            cnt = rs.getInt("cnt");
        }
        
        if(cnt > 0){
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
            
        }
        
        return true;
    }
   
    
    public Item getName(Map paramMap){
        con = getKoneksi();
        String tableName = (String) paramMap.get("tableName");
        String columnCode = (String) paramMap.get("columnCode");
        String columnName = (String) paramMap.get("columnName");
        String code = (String) paramMap.get("code");
        String name = "";
        String sql = "SELECT "+columnName+" as name FROM "+tableName+" WHERE "+columnCode+ " = ? ";
        
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, code);
            rs = ps.executeQuery();
            while(rs.next()){
                name = rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Item(code, name);
    }
    
    public void openFile(String fileName){
        Desktop desktop = Desktop.getDesktop();  
        try {
          File f = new File(fileName);
           desktop.open(f);  // opens application (MSWord) associated with .doc file
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }
    }
    
    public String getTotalPengguna(){
        String totalPengguna = "0";
        con = getKoneksi();
        String sql = "SELECT COUNT(*) AS jumlah_pengguna FROM tbl_user ";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                totalPengguna = rs.getString("jumlah_pengguna");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalPengguna;
    }
    
    public String getTotalJamaah(){
        String totalPengguna = "0";
        con = getKoneksi();
        String sql = "SELECT COUNT(*) AS jumlah_pengguna FROM tbl_customer ";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                totalPengguna = rs.getString("jumlah_pengguna");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalPengguna;
    }
    
    public String getTotalPemesanan(){
        String totalPengguna = "0";
        con = getKoneksi();
        String sql = "SELECT COUNT(*) AS jumlah_pengguna FROM tbl_pemesanan ";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                totalPengguna = rs.getString("jumlah_pengguna");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalPengguna;
    }
    
    public static void main(String[] args) throws SQLException{
        DBUtils util = new DBUtils();
        Map paramMap = new HashMap();
        paramMap.put("tableName", "tbl_jabatan");
        paramMap.put("columnCode", "kd_jabatan");
        paramMap.put("columnName", "nm_jabatan");
        paramMap.put("code", "ADMIN");
       
    }
    
    
}
