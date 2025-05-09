package test01;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainMail01
{

	public static void main(String[] args) throws Exception
	{
		String host = "smtp.office365.com";
		String port = "587";
		String username = "xxxxxxxx@xxxxxx.co.jp";
		String password = "password";
		String from = "xxxxxxxx@xxxxxxx.co.jp";
		String to = "xxxxxxxx@xxxxxxx.co.jp";
		String title = "テストメール";
		String body = "テストメールです。";

		// プロパティの設定
		Properties properties = new Properties();
        properties.setProperty( "mail.smtp.auth" , "true" );
        properties.setProperty( "mail.smtp.starttls.enable" , "true" );
        properties.setProperty( "mail.smtp.host" , host );
        properties.setProperty( "mail.smtp.port" , port );
        properties.setProperty( "mail.smtp.starttls.required" , "true" );
        properties.setProperty( "mail.smtp.ssl.protocols" , "TLSv1.2" );

		// 認証情報の作成
        Session session = Session.getInstance( properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

		Message message = new MimeMessage( session );
		message.setFrom( new InternetAddress( from ) );
		message.setRecipients( Message.RecipientType.TO , InternetAddress.parse( to ) );
		message.setSubject( title );
		message.setText( body );

		Transport.send( message );
	}

}
