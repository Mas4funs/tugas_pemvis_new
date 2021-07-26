/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel;

import java.sql.SQLException;
import simtravel.form.SplashScreen;

/**
 *
 * @author Nursalim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        new SplashScreen(50).showSplashAndExit();
    }
    
}
