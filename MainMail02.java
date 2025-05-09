package test01;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainMail02
{

	public static void main(String[] args) throws Exception
	{
        // メール情報の設定
        String smtpServer = "smtp.office365.com";
        int smtpPort = 587;
        String username = "xxxxxxx@xxxxxx.co.jp";
        String password = "xxxxxxxxx";
        String from = "xxxxxxxx@xxxxxx.co.jp";
        String to = "xxxxxxxx@xxxxxxxxx.co.jp";
        String subject = "Test Email";
        String body = "This is a test email sent from Java program.";

        // プロパティの設定
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpServer);
        properties.put("mail.smtp.port", String.valueOf(smtpPort));
        properties.put( "mail.smtp.starttls.required" , "true" );
        properties.put( "mail.smtp.ssl.protocols" , "TLSv1.2" );

        // 認証情報の作成
        PasswordAuthentication pwdauth = new PasswordAuthentication( username , password );
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return( pwdauth );
            }
        };
        Session session = Session.getInstance( properties, auth );

        // メールの作成
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject( subject );
        message.setText(body);

        // メール送信
        Transport.send(message);

        System.out.println("Email sent successfully!");

	}

}
