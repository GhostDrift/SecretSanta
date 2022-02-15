package Server;

// -- Download JavaMail API from here: http://www.oracle.com/technetwork/java/javamail/index.html
// -- Download JavaBeans Activation Framework from here: http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#jaf-1.1.1-fcs-oth-JPR
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailUsingGMailSMTP extends Thread{
    private String to;
    private String subject;
    private String _message;
    private boolean go;

	// -- set the gmail host URL
	final static private String host = "smtp.gmail.com";

	// -- You must have a valid gmail username/password pair to use
	// gmail as a SMTP service
	static private String username = "<<your gmail username>>";
	static private String password = "<<your gmail password>>";

	public static void main(String[] args) throws ConfigNotInitializedException {
		Config.initializeConfig("ServerConfiguration.conf");
//		sendEmail("example@gmail.com", "Team MERJ", """
//				Dear user,
//
//				This is a test email to make sure that the email sender for the Client-Server project is working.
//
//				Regards,
//				Team MERJ""");
       new  SendEmailUsingGMailSMTP("Jrstojkovic123@gmail.com","Test Email", "Dear User,\n\n\nThis is a test Email.\n\n\nWith regards, Jessica Stojkovic").start();
	}

	//Constructor
    SendEmailUsingGMailSMTP(String to,String subject, String _message){
	    this.go = true;
	    this.to = to;
	    this.subject = subject;
	    this._message = _message;
	    this.start();

    }
    public void run(){
	    try{
	        System.out.println("Sending Email....");
	        while(go){
	            this.sendEmail(to,subject,_message);
            }
        } catch (ConfigNotInitializedException e) {
            e.printStackTrace();
        }
    }
	public void sendEmail(String to, String subject, String _message) throws ConfigNotInitializedException
	{
		username = Config.getEmailUsername();
		password = Config.getEmailPassword();

		// -- set up host properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// -- Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		// -- Set up the sender's email account information
		String from = username;//"TeamMERJ@gmail.com";

		try {
			// -- Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// -- Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// -- Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// -- Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(_message);

			// -- Send message
			// -- use either these three lines or...
			// Transport t = session.getTransport("smtp");
			// t.connect();
			// t.sendMessage(message, message.getAllRecipients());

			// -- .. this one (which ultimately calls sendMessage(...)
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		this.go = false;
	}
}
