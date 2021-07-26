/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

/**
 *
 * @author Nursalim
 */
public class SplashScreen extends JWindow {
    private int duration;
    private JProgressBar progress;
    private ImageSplash panel;
    private JPanel p_bar;

    public SplashScreen(int d){
        duration = d;
        setSize(500, 300);
        setLocationRelativeTo(null);
    }
    
    public void showSplash(){
        panel = new ImageSplash();
        
        p_bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,40));
        p_bar.setOpaque(false);

        progress = new JProgressBar(0, 100);
        progress.setString("Please wait...");
        progress.setStringPainted(true);
        progress.setPreferredSize(new Dimension(getWidth()-10, 15));
        progress.setForeground(Color.red);
        progress.setVisible(true);
        
        
        p_bar.add(progress);
      

        panel.add(p_bar, "South");        
        getContentPane().add(panel, "Center");

        setVisible(true);
        
        for (int i = 0; i < 100; i++) {
            try {
                progress.setValue(i);
                Thread.sleep(duration); 
            } catch (Exception e) {
                e.getMessage();
            }
        }
        setVisible(false);
    }

    public void showSplashAndExit() throws SQLException{
        showSplash();
        dispose();
        new FrmLogin(null, true).setVisible(true);
    }
    
    public static void main(String []args){
        //SplashScreen obj = new SplashScreen(100);
        //obj.showSplashAndExit();
    }
}
