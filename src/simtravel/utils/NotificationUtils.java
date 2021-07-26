/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtravel.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author nursalim
 */
public class NotificationUtils {
    
    String from = "klp2pemvis@gmail.com";
    String host = "smtp.gmail.com";
    
    public void sentEmailPemesanan(Map data){
        
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("klp2pemvis@gmail.com", "kelompok");
            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String)data.get("to")));

            // Set Subject: header field
            message.setSubject("No. Pesanan "+data.get("noPemesanan").toString());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

                //File f =new File("D:/tmp/test_photo.PNG");

                attachmentPart.attachFile((File) data.get("attachment"));
                
                StringBuffer sb = new StringBuffer();
                sb.append("Dear All, terlampir data-data pesanan Anda\n");
                sb.append("Nama \t\t\t\t:"+data.get("nama")+"\n");
                sb.append("No. KTP \t\t\t:"+data.get("noKTP")+"\n");
                sb.append("No. Pemesanan \t\t:"+data.get("noPemesanan")+"\n");
                sb.append("Tgl. Pemesanan \t\t:"+data.get("tglPemesanan")+"\n");
                sb.append("Status Pembayaran \t:"+data.get("statusPembayaran")+"\n");
                sb.append("Nama Paket \t\t:"+data.get("namaPaket")+"\n");
                sb.append("Harga Paket \t\t:"+data.get("hargaPaket")+"\n"); 

                textPart.setText(sb.toString());
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void sentEmailUpdatePembayaran(Map data){
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("klp2pemvis@gmail.com", "kelompok");
            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String)data.get("to")));

            // Set Subject: header field
            message.setSubject("No. Pesanan "+data.get("noPemesanan").toString());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

                File f =new File("D:/tmp/test_photo.PNG");

                attachmentPart.attachFile(f);
                
                StringBuffer sb = new StringBuffer();
                sb.append("Dear All, terlampir data-data pesanan Anda\n");
                sb.append("Nama \t\t\t\t:"+data.get("nama")+"\n");
                sb.append("No. KTP \t\t\t:"+data.get("noKTP")+"\n");
                sb.append("No. Pemesanan \t\t:"+data.get("noPemesanan")+"\n");
                sb.append("Tgl. Pemesanan \t\t:"+data.get("tglPemesanan")+"\n");
                sb.append("Status Pembayaran \t:"+data.get("statusPembayaran")+"\n");
                sb.append("Nama Paket \t\t:"+data.get("namaPaket")+"\n");
                sb.append("Harga Paket \t\t:"+data.get("hargaPaket")+"\n");
                sb.append("Tanggal Berangkat \t\t:"+data.get("tglBerangkat")+"\n");
                sb.append("Tanggal Pulang \t\t:"+data.get("tglPulang")+"\n");
                sb.append("pimpinanRombongan \t\t:"+data.get("pimpinanRombongan")+"\n");
                
                textPart.setText(sb.toString());
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        NotificationUtils utils = new NotificationUtils();
        
        Map data = new HashMap();
        data.put("to", "nursalim.me@gmail.com");
        data.put("noPemesanan", "P210720RW1Y");
        data.put("tglPemesanan", "20/07/2021");
        data.put("nama", "Nursalim");
        data.put("noKTP", "332911111111");
        data.put("statusPembayaran", "Lunas");
        data.put("namaPaket", "Paket Umrah 1");
        
        utils.sentEmailPemesanan(data);
    }
}
