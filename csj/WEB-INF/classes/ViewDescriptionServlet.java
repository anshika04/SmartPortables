import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class ViewDescriptionServlet extends HttpServlet 
{
	public HashMap<String, ProductCatalog> hm = null;
	ProductCatalog pc = null;
	ProductCatalog pc1 = null;
	public HashMap<String, ProductCatalog> hm1 = null;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{
	
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		String productID = request.getParameter("productid1");
		System.out.println("hfw*************" + productID);
		String type = null;
		if(productID.toLowerCase().startsWith("lp")){
			type = "laptop";
		}
		if(productID.toLowerCase().startsWith("ph")){
			type = "phone";
		}
		if(productID.toLowerCase().startsWith("sp")){
			type = "speaker";
		}
		if(productID.toLowerCase().startsWith("sm")){
			type = "smartWatch";
		}
		if(productID.toLowerCase().startsWith("he")){
			type = "headphone";
		}
		if(productID.toLowerCase().startsWith("ex")){
			type = "externalStorage";
		}

		if(productID.toLowerCase().startsWith("ac")){
			type = "accessory";
		}
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getProductDescription(type, productID);		
		util.getDefaultTemplate("footer.html");	

		}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}		
	}
}