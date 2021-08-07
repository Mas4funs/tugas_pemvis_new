/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import simtravel.test.*;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author nursalim
 */
public class GrafikLaporanJamaah extends javax.swing.JDialog {

    public GrafikLaporanJamaah(String title) throws ClassNotFoundException, SQLException {
        setContentPane(createDemoPanel());
    }

    private static PieDataset createDataset() throws ClassNotFoundException, SQLException {
//        DefaultPieDataset dataset = new DefaultPieDataset();
//        dataset.setValue("IPhone 5s", new Double(20));
//        dataset.setValue("SamSung Grand", new Double(20));
//        dataset.setValue("MotoG", new Double(40));
//        dataset.setValue("Nokia Lumia", new Double(10));
        
        Class.forName( "com.mysql.jdbc.Driver" );
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_travel", "root", "");
      
      Statement statement = connect.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT CASE jns_kelamin WHEN 'L' THEN 'Laki Laki' ELSE 'Perempuan' END AS jns_kelamin, COUNT(*) AS jumlah FROM tbl_customer GROUP BY jns_kelamin" );
      DefaultPieDataset dataset = new DefaultPieDataset( );
      
      while( resultSet.next( ) ) {
         dataset.setValue( 
         resultSet.getString( "jns_kelamin" ) ,
         Double.parseDouble( resultSet.getString( "jumlah" )));
      }
        return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Grafik Calon Jamaah", // chart title 
                dataset, // data    
                true, // include legend   
                true,
                false);

        return chart;
    }

    public static JPanel createDemoPanel() throws ClassNotFoundException, SQLException {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        GrafikLaporanJamaah demo = new GrafikLaporanJamaah("Grafik Calon Jamaah");
        demo.setSize(600, 400);
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
