/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nursalim
 */
public class RegexUtils {

    String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
    String regexDigit = "\\d+";

    public boolean validateEmail(String emailAddress) {
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(emailAddress);

        return matcher.matches();
    }

    public boolean validateDigit(String numeric) {
        Pattern pattern = Pattern.compile(regexDigit);
        Matcher matcher = pattern.matcher(numeric);

        return matcher.matches();
    }

    public String generateNoPemesanan() {
        String pattern = "yyMMdd";
        DateFormat df = new SimpleDateFormat(pattern);
        Random r = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb = sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        return "P"+df.format(new java.util.Date())+sb.toString();
    }

    public String generateNoRegistrasi() {
        String pattern = "yyMMdd";
        DateFormat df = new SimpleDateFormat(pattern);
        Random r = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb = sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        return "R"+df.format(new java.util.Date())+sb.toString();
    }

    public static void main(String[] args) {
//        System.out.println(generateNoPemesanan());
    }

}
