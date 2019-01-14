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

public class SmartWatchServlet extends HttpServlet 
{
	public HashMap<String, ProductCatalog> hm = null;
	ProductCatalog pc = null;
	ProductCatalog pc1 = null;
	public HashMap<String, ProductCatalog> hm1 = null;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String pid = request.getParameter("id");

		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getProductContentTemplate("smartWatch", pid);		
		util.getDefaultTemplate("footer.html");			
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{
			String productID = request.getParameter("productid");
			
			if(Servlet_UpdateCart(productID))
			{			
				WfCache.cartCount = WfCache.cartCount + 1;									
			}	
			response.sendRedirect("smartWatch");
		}		
		catch(Exception ex)
		{
			
		}
	}
	
	public boolean Servlet_UpdateCart(String productID)throws IOException , SQLException
	{
		hm = WfCache.isSmartWatchCatalogFetched();
		pc = hm.get(productID);
		
		hm1 = WfCache.showCart();
		
		if(hm1 != null)
		{
			if(hm1.containsKey(productID))
			{
				pc1 = hm1.get(productID);
				if(pc1 != null)
				{
					int qty = pc1.getQuantity() + 1;
					pc1.setQuantity(qty);
					WfCache.addToCart(pc1);
					return false;
				}
			}
			else
			{
				int qty = 1;
				pc.setQuantity(qty);
				WfCache.addToCart(pc);								
			}
		}
		else
		{
			int qty = 1;
			pc.setQuantity(qty);
			WfCache.addToCart(pc);			
		}
		return true;
	}
}