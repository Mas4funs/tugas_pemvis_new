/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.utils;
import java.util.Calendar;

/**
 *
 * @author Febri Nahdatun
 */
public class DateUtils {
    
    public String today(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int date = Calendar.getInstance().get(Calendar.DATE);
        
        int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return getWeek(dow)+", "+String.valueOf(date)+" "+getMonth(month)+" "+String.valueOf(year);
    }
    
    public String getWeek(int week){
        String hari = "";
        switch (week){
            case 1 :
                hari = "Minggu";
                break;
            case 2 :
                hari = "Senin";
                break;
                
            case 3 :
                hari = "Selasa";
                break;
                
            case 4 :
                hari = "Rabu";
                break;
                
            case 5 :
                hari = "Kamis";
                break;
                
            case 6 :
                hari = "Jumat";
                break;
                
            case 7 :
                hari = "Sabtu";
                break;    
                
        }
        
        return hari;
    }
    
    public String getMonth(int week){
        String hari = "";
        switch (week){
            case 1 :
                hari = "Januari";
                break;
            case 2 :
                hari = "Februari";
                break;
                
            case 3 :
                hari = "Maret";
                break;
                
            case 4 :
                hari = "April";
                break;
                
            case 5 :
                hari = "Mei";
                break;
                
            case 6 :
                hari = "Juni";
                break;
                
            case 7 :
                hari = "Juli";
                break;
                
            case 8 :
                hari = "Agustus";
                break;
                
            case 9 :
                hari = "September";
                break;
                
            case 10 :
                hari = "Oktober";
                break;    
            
            case 11 :
                hari = "November";
                break;
            
            case 12 :
                hari = "Desember";
                break;    
        }
        
        return hari;
    }
    
}
