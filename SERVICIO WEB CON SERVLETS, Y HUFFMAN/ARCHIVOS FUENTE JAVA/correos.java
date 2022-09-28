import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class correos {
    private String FROM, dirServidor, smtpPassword, text, rutaLlave;
    private Properties propiedades;
    private Session sesion;
    private Message mensaje;
    private Transport paqueteEmail;
    private Authenticator aut;
    private BodyPart msm, adjuntos;
    private MimeMultipart page;
    public correos(String FROM, String dirServidor, String smtpPassword, String nomLlave)
    {
        text = "<h1>Para recuperar contraseña descargue la llave publica que se genro</h1>\n";
        rutaLlave = "/home/brandon/Documentos/tarea8/web/apache-tomcat-10.0.22/webapps/webcrud/keys/";
        rutaLlave += nomLlave;
        msm = new MimeBodyPart();
        adjuntos = new MimeBodyPart();
        page = new MimeMultipart();
        try {
            msm.setContent(text, "text/html");
            adjuntos.setDataHandler(new DataHandler(new FileDataSource(rutaLlave)));
            adjuntos.setFileName("restorePassword.cer");
            page.addBodyPart(msm);
            page.addBodyPart(adjuntos);
        } catch (MessagingException e) { e.printStackTrace(); }
        this.FROM = FROM;
        this.dirServidor = dirServidor;
        this.smtpPassword = smtpPassword;
        propiedades = System.getProperties();
        agregaPropiedades();
        aut = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(dirServidor, smtpPassword);
            }
        }; 
        sesion = Session.getInstance(propiedades, aut);
        mensaje = new MimeMessage(sesion);
    }
    public void agregaPropiedades()
    {
        propiedades.put("mail.transport.protocol", "smtp");
        propiedades.put("mail.smtp.host", dirServidor);
        propiedades.put("mail.smtp.ssl.trust", dirServidor);
        propiedades.put("mail.smtp.username", FROM);
        propiedades.put("mail.smtp.password", smtpPassword);
        propiedades.put("mail.smtp.port", 587);
        propiedades.put("mail.smtp.ssl.enable", "false");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true"); 
        propiedades.put("mail.smtp.starttls.required", "true"); 
    }
    public void enviarEmail(String TO, String asunto, String content)
    {
        try {
            mensaje.setFrom(new InternetAddress(FROM.trim()));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(TO.trim()));
            mensaje.setSubject(asunto);
            mensaje.setContent(page);
            paqueteEmail = sesion.getTransport();
            paqueteEmail.connect(dirServidor, FROM, smtpPassword);
            paqueteEmail.sendMessage(mensaje, mensaje.getAllRecipients());
            paqueteEmail.close();
        } catch (AddressException e1) { e1.printStackTrace(); }
        catch (MessagingException e) { e.printStackTrace(); } 
    }
    public static void main(String[] args) {
        correos nuevoMail = new correos("estrem_@hotmail.com", "smtp.office365.com", "DAILEX9807", "llave.cert");
        nuevoMail.enviarEmail("estrem_@hotmail.com", "JAJAJAJJAJAJA", "Prueba de correo ");
    }
}
