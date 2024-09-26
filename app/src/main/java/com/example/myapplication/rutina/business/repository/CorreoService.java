package com.example.myapplication.rutina.business.repository;


import android.util.Log;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

public class CorreoService {

    // Método para enviar correo con archivo adjunto
    public static void enviarCorreoConPDF(final String destinatario, final String asunto, final String cuerpo, final String rutaArchivoPDF) {
        new Thread(() -> {
            try {
                // Configurar propiedades del servidor SMTP
                Properties propiedades = new Properties();
                propiedades.put("mail.smtp.host", "smtp.gmail.com");
                propiedades.put("mail.smtp.port", "587");
                propiedades.put("mail.smtp.auth", "true");
                propiedades.put("mail.smtp.starttls.enable", "true");

                // Autenticación
                Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("samuel.inge.profe@gmail.com", "cswjgjubqzzeqgng");
                    }
                });

                // Crear el mensaje de correo
                Message mensaje = new MimeMessage(session);
                mensaje.setFrom(new InternetAddress("samuel.inge.profe@gmail.com"));
                mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                mensaje.setSubject(asunto);

                // Crear cuerpo del mensaje
                MimeBodyPart cuerpoMensaje = new MimeBodyPart();
                cuerpoMensaje.setText(cuerpo);

                // Adjuntar el archivo PDF
                MimeBodyPart adjunto = new MimeBodyPart();
                DataSource fuenteAdjunto = new FileDataSource(rutaArchivoPDF);
                adjunto.setDataHandler(new DataHandler(fuenteAdjunto));
                adjunto.setFileName(fuenteAdjunto.getName());

                // Crear multipart para el mensaje y el adjunto
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(cuerpoMensaje);
                multipart.addBodyPart(adjunto);

                // Establecer el contenido del mensaje
                mensaje.setContent(multipart);

                // Enviar el correo
                Transport.send(mensaje);
                Log.i("Correo", "Correo enviado con éxito.");

            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("Correo", "Error al enviar el correo: " + e.getMessage());
            }
        }).start();
    }
}
