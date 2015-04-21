/**	Tomcat8 server - Do prohlizece zadame pro web	:	http://localhost:8080/Java.Servlet15.Mail/ nebo	http://localhost:8080/Java.Servlet15.Mail/index.html **/
/*	NUTNE pridat javax.mail.jar z Program Files\JavaGlassFish\glassfish\modules jako externi knihovnu do vlastnosti projektu neboli do 
	classpath : Project - properties - Java Build path - libraries - add external *.jar-   (javax.mail.jar)
	NUTNE prida do WebContent/WEB-Inf/lib/javax.mail.jar	*/
package servlet.mail;
import java.io.*;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import mail.MailNastaveni;
@WebServlet("/OdeslaniPrilohaMailu")
public class OdeslaniPrilohaMailu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MailNastaveni mail;
    public OdeslaniPrilohaMailu() {
        super();	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Metoda Post");	
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		mail = new MailNastaveni(request,"sPrilohou");
		mail.vytvoreniMailuSPrilohou();
		try{
			Transport.send(mail.getMail());
			String title = "Posilani Mailu s prilohou ";
			String res = "Mail odeslan v poradku ....";
			String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
			out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n" +
	         "<body bgcolor=\"#f0f0f0\">\n" +
	         "<h1 align=\"center\">" + title + "</h1>\n" +
	         "<p align=\"center\">" + res + "</p>\n" +	"</body></html>");	}
		catch (MessagingException mex) {
	         mex.printStackTrace();		}	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Metoda GET");	}
}