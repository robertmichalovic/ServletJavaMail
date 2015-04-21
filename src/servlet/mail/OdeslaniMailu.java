/**	Tomcat8 server - Do prohlizece zadame pro web	:	http://localhost:8080/Java.Servlet15.Mail/ nebo	http://localhost:8080/Java.Servlet15.Mail/index.html **/
/*	NUTNE pridat javax.mail.jar z Program Files\JavaGlassFish\glassfish\modules jako externi knihovnu do vlastnosti projektu neboli do 
	classpath : Project - properties - Java Build path - libraries - add external *.jar-   (javax.mail.jar)
	NUTNE prida do WebContent/WEB-Inf/lib/javax.mail.jar
 */
package servlet.mail;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/OdeslaniMailu")		//	Error pokud bych to nastavil takto ( name ="/OdeslaniMailu")
public class OdeslaniMailu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String prijemce;
	private String odesilatel;
	private String zprava;
	private Session mailConnection;
	private Properties props;
	private Address odAdress,priAdress;
	private Message msg;
    public OdeslaniMailu() {
        super();	}
	private void zjistiPromenne(HttpServletRequest request) {
		prijemce = request.getParameter("prijemce");
		odesilatel = request.getParameter("odesilatel");
		zprava = request.getParameter("zprava");	}
	private void nastaveniServerMail(){
		props = new Properties( );
		props.put("mail.smtp.host", "smtp.seznam.cz");			//	konstanta(klic) a hodnota
		props.put("mail.smtp.user", "there");
		props.put("mail.smtp.password", "aa");
		props.put("mail.smtp.port", "25"); 
		props.put("mail.smtp.auth", "true");
		mailConnection = Session.getDefaultInstance(props,new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("there", "aa");	}	});	}
	private void sestaveniMailu(){
		try {
			odAdress = new InternetAddress(odesilatel, "there");				//	od koho
			priAdress = new InternetAddress(prijemce);		}					//	komu
		catch (UnsupportedEncodingException e) {
			System.err.println("Nepodarilo se vytvorit objekt adresy odesilatele");
			e.printStackTrace();	} 
		catch (AddressException e) {
			System.err.println("Nepodarilo se vytvorit objekt adresy prijmence");
			e.printStackTrace();	}
		try {
			msg.setContent(zprava,"text/plain");
			msg.setFrom(odAdress);
			msg.setRecipient(Message.RecipientType.TO, priAdress);
			msg.setSubject("Testovaci mail - predmet zpravy ze Servletu");			}			//	nastaveni predmetu zpravy
		catch (MessagingException e) {
			System.err.println("Nepodarilo se vyrobit objekt zpravy");
			e.printStackTrace();	}	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Metoda GET");
		zjistiPromenne(request);
		//System.out.println("Zde je odesilatel : "+odesilatel+"\nprijemce : "+prijemce+"\nZprava : "+zprava);	//	kontrola funkcnosti ... OK
		nastaveniServerMail();
		msg = new MimeMessage(mailConnection);
		sestaveniMailu();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try{
			Transport.send(msg);
			String title = "Send Email";
			String res = "Sent message successfully....";
			String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
			out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n" +
	         "<body bgcolor=\"#f0f0f0\">\n" +
	         "<h1 align=\"center\">" + title + "</h1>\n" +
	         "<p align=\"center\">" + res + "</p>\n" +	"</body></html>");	}
		catch (MessagingException mex) {
	         mex.printStackTrace();		}	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Metoda POST");	}
}

/*
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/OdeslaniMailu")		//	Error pokud bych to nastavil takto ( name ="/OdeslaniMailu")
public class OdeslaniMailu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String prijemce;
	private String odesilatel;
	private String zprava;
	private Session mailConnection;
	private Properties props;
	private Address odAdress,priAdress;
    public OdeslaniMailu() {
        super();	}
	private void zjistiPromenne(HttpServletRequest request) {
		prijemce = request.getParameter("prijemce");
		odesilatel = request.getParameter("odesilatel");
		zprava = request.getParameter("zprava");	}
	private void nastaveniMailu(){
		props = new Properties( );
		props.put("mail.smtp.host", "smtp.seznam.cz");			//	konstanta(klic) a hodnota
		props.put("mail.smtp.user", "there");
		props.put("mail.smtp.password", "aa");
		props.put("mail.smtp.port", "25"); 
		props.put("mail.smtp.auth", "true");
		mailConnection = Session.getDefaultInstance(props,new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("there", "aa");	}	});	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Metoda GET");
		zjistiPromenne(request);
		//System.out.println("Zde je odesilatel : "+odesilatel+"\nprijemce : "+prijemce+"\nZprava : "+zprava);	//	kontrola funkcnosti ... OK
		nastaveniMailu();
		Message msg = new MimeMessage(mailConnection);
		try {
			odAdress = new InternetAddress(odesilatel, "there");				//	od koho
			priAdress = new InternetAddress(prijemce);		}					//	komu
		catch (UnsupportedEncodingException e) {
			System.err.println("Nepodarilo se vytvorit objekt adresy odesilatele");
			e.printStackTrace();	} 
		catch (AddressException e) {
			System.err.println("Nepodarilo se vytvorit objekt adresy prijmence");
			e.printStackTrace();	}
		try {
			msg.setContent(zprava,"text/plain");
			msg.setFrom(odAdress);
			msg.setRecipient(Message.RecipientType.TO, priAdress);
			msg.setSubject("Testovaci mail - predmet zpravy ze Servletu");			}			//	nastaveni predmetu zpravy
		catch (MessagingException e) {
			System.err.println("Nepodarilo se vyrobit objekt zpravy");
			e.printStackTrace();	}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try{
			Transport.send(msg);
			String title = "Send Email";
			String res = "Sent message successfully....";
			String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
			out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n" +
	         "<body bgcolor=\"#f0f0f0\">\n" +
	         "<h1 align=\"center\">" + title + "</h1>\n" +
	         "<p align=\"center\">" + res + "</p>\n" +	"</body></html>");	}
		catch (MessagingException mex) {
	         mex.printStackTrace();	}	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Metoda POST");	}
}
*/