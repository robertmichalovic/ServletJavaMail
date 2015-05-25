package mail;
import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.*;
public class MailNastaveni {
	private String prijemce;
	private String odesilatel;
	private String predmet;
	private String zprava;
	private String cestaSoubor; 
	private Properties props;
	private Session mailConnection;
	private Address odAdress,priAdress;
	private MimeMessage msg;
	public MimeMessage getMail() 						{ return msg; }
	public MailNastaveni(HttpServletRequest request, String cesta) {							//	konstruktor
		prijemce = request.getParameter("prijemce");
		odesilatel = request.getParameter("odesilatel");
		predmet = request.getParameter("predmet");
		zprava = request.getParameter("zprava");
		if ("sPrilohou".equals(cesta)) cestaSoubor = request.getParameter("cestaSoubor");
		else cestaSoubor = null;	}
	private void nastaveniServerMail(){
		props = new Properties( );
		props.put("mail.smtp.host", "smtp.seznam.cz");			//	konstanta(klic) a hodnota
		props.put("mail.smtp.user", "franta");
		props.put("mail.smtp.password", "aa123");
		props.put("mail.smtp.port", "25"); 
		props.put("mail.smtp.auth", "true");
		mailConnection = Session.getDefaultInstance(props,new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("franta", "aa123");	}	});	}
	private void nastaveniParamODKomu(){
		try {
			odAdress = new InternetAddress(odesilatel, "there");				//	od koho
			priAdress = new InternetAddress(prijemce);		}					//	komu
		catch (UnsupportedEncodingException e) {
			System.err.println("Nepodarilo se vytvorit objekt adresy odesilatele");
			e.printStackTrace();	} 
		catch (AddressException e) {
			System.err.println("Nepodarilo se vytvorit objekt adresy prijmence");
			e.printStackTrace();	}	}
	private void sestaveniMailuNoPriloha(){
		try {
			msg.setContent("<h1>"+zprava+"</h1>","text/html");					//	"text/html","text/plain"
			msg.setFrom(odAdress);
			msg.setRecipient(Message.RecipientType.TO, priAdress);
			msg.setSubject(predmet);			}								//	nastaveni predmetu zpravy
		catch (MessagingException e) {
			System.err.println("Nepodarilo se vyrobit objekt zpravy");
			e.printStackTrace();	}	}
	private void sestaveniMailuPriloha(){
		try {
			msg.setFrom(odAdress);
			msg.setRecipient(Message.RecipientType.TO, priAdress);
			msg.setSubject(predmet);											//	nastaveni predmetu zpravy
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("Toto je telo zpravy : "+this.zprava);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(cestaSoubor);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(zjistiJmenoSoubor(cestaSoubor));
			multipart.addBodyPart(messageBodyPart);
			msg.setContent(multipart);	}
		catch (MessagingException e) {
			System.err.println("Nepodarilo se vyrobit objekt zpravy");
			e.printStackTrace();	}	}
	private String zjistiJmenoSoubor(String txt){
		//System.out.println("TEST : "+txt);
		int i = txt.lastIndexOf('\\');
		String temp = txt.substring(++i);
		//System.out.println("TEST : "+txt+"\n"+temp);
		return temp;	}
	public void vytvoreniMailuBezPrilohy(){
		nastaveniServerMail();
		msg = new MimeMessage(mailConnection);
		nastaveniParamODKomu();
		sestaveniMailuNoPriloha();	}
	public void vytvoreniMailuSPrilohou(){
		nastaveniServerMail();
		msg = new MimeMessage(mailConnection);
		nastaveniParamODKomu();
		sestaveniMailuPriloha();	}
}